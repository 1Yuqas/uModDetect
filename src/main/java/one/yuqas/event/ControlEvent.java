package one.yuqas.event;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import one.yuqas.Main;
import one.yuqas.util.ConfigUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.sign.Side;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class ControlEvent implements Listener {

    private final Map<UUID, ScannerRequest> pendingRequests = new ConcurrentHashMap<>();


    private final ConfigUtil configUtil;

    public ControlEvent(JavaPlugin plugin, ConfigUtil configUtil) {
        this.configUtil = configUtil;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();

        if (player.isOp()) return;

        Main.getWrappedScheduler().runTaskLaterAtEntity(player, () -> {
            if (!player.isOnline()) return;

            Location signLoc = player.getLocation().clone();
            signLoc.setY(Math.max(signLoc.getWorld().getMinHeight(), signLoc.getY() - 3));

            Main.getWrappedScheduler().runTaskAtLocation(signLoc, () -> {
                Block block = signLoc.getBlock();
                BlockData oldData = block.getBlockData();
                BlockState oldState = block.getState();

                Block blockBelow = block.getRelative(0, -1, 0);
                if (blockBelow.getY() <= block.getWorld().getMinHeight()) {
                    return;
                }

                block.setType(Material.DARK_OAK_SIGN, false);

                if (!(block.getState() instanceof Sign sign)) return;

                var back = sign.getSide(Side.BACK);
                var builder = Component.text();

                Collection<ConfigUtil.ModConfig> mods = configUtil.getMods();
                for (ConfigUtil.ModConfig mod : mods) {
                    builder.append(Component.text(" T: ")).append(Component.translatable(mod.key()));
                }

                back.line(0, builder.build());
                sign.update(false, false);
//                sign.setAllowedEditorUniqueId(player.getUniqueId());

                pendingRequests.put(player.getUniqueId(), new ScannerRequest(mods, oldData, signLoc, oldState));

                Main.getWrappedScheduler().runTaskLaterAtEntity(player, () -> {
                    if (player.isOnline()) {
                        player.openSign(sign, Side.BACK);
                        player.closeInventory();
                    } else {
                        cleanUp(player.getUniqueId());
                    }
                }, 3L);
            });
        }, 40L);
        if (configUtil == null) return;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onSignChange(SignChangeEvent event) {
        Player player = event.getPlayer();
        ScannerRequest request = pendingRequests.remove(player.getUniqueId());

        if (request == null) return;

        event.setCancelled(true);

        Component comp = event.line(0);
        if (comp != null) {
            String text = PlainTextComponentSerializer.plainText().serialize(comp);

            for (ConfigUtil.ModConfig mod : request.modConfigs()) {
                for (String key : mod.contains()) {
                    if (text.contains(key)) {
                        Main.getWrappedScheduler().runTaskAtEntity(player, () -> {
                            configUtil.executeCommand(mod, player.getName());
                            configUtil.broadcastToStaff(mod, player.getName());
                        });
                        break;
                    }
                }
            }
        }
        restoreBlock(request);
    }
    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        cleanUp(event.getPlayer().getUniqueId());
    }

    private void cleanUp(UUID uuid) {
        ScannerRequest request = pendingRequests.remove(uuid);
        if (request != null) {
            Main.getWrappedScheduler().runTaskAtLocation(request.signLoc(), () -> restoreBlock(request));
        }
    }

    private void restoreBlock(ScannerRequest req) {
        BlockState oldState = req.oldState();

        if (oldState != null) {
            oldState.update(true, false);
        }
    }


    private record ScannerRequest(
            Collection<ConfigUtil.ModConfig> modConfigs,
            BlockData oldBlockData,
            Location signLoc,
            BlockState oldState
    ) {}
}