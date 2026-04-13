package one.yuqas.util;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public class ConfigUtil {

    private final JavaPlugin plugin;
    private final Map<String, ModConfig> modMap = new HashMap<>();


    public ConfigUtil(JavaPlugin plugin) {
        this.plugin = plugin;

        load();
    }

    public void load() {
        modMap.clear();
        ConfigurationSection modsSection = plugin.getConfig().getConfigurationSection("mods");
        if (modsSection == null) return;

        for (String modName : modsSection.getKeys(false)) {
            ConfigurationSection section = modsSection.getConfigurationSection(modName);
            if (section == null) continue;

            String key = section.getString("key", "");
            List<String> contains = section.getStringList("contains");
            String command = section.getString("command", "");
            String message = section.getString("message", "");
            String coloredMessage = ChatColor.translateAlternateColorCodes('&', message);

            modMap.put(modName, new ModConfig(modName, key, contains, command, coloredMessage));
        }
    }

    public void reload() {
        plugin.reloadConfig();
        load();
    }


    public void executeCommand(ModConfig mod, String playerName) {
        if (mod.command() == null || mod.command().isEmpty()) return;
        String cmd = mod.command().replace("%player%", playerName);
        Bukkit.getGlobalRegionScheduler().run(plugin, task -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd));
    }

    public void broadcastToStaff(ModConfig mod, String playerName) {
        if (mod.message() == null || mod.message().isEmpty()) return;
        String msg = mod.message().replace("%player%", playerName);
        for (Player online : Bukkit.getOnlinePlayers()) {
            if (online.isOp() || online.hasPermission("umd.message")) online.sendMessage(msg);
        }
    }

    public Collection<ModConfig> getMods() { return modMap.values(); }


    public record ModConfig(String name, String key, List<String> contains, String command, String message) {}
}