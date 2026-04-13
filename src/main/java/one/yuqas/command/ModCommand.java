package one.yuqas.command;

import one.yuqas.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ModCommand implements CommandExecutor, TabCompleter {
    private final Main plugin;

    public ModCommand(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
            plugin.getConfigUtil().reload();
            sender.sendMessage("§x§2§A§E§5§2§7§lu§x§5§3§F§5§5§1§lM§x§8§3§F§F§8§1§lo§x§6§3§F§9§6§0§ld§x§4§5§F§0§4§2§lD§x§2§A§E§5§2§7§le§x§2§A§E§5§2§7§lt§x§2§A§E§5§2§7§le§x§2§A§E§5§2§7§lc§x§2§A§E§5§2§7§lt" +
                    " §8| §fThe plugin was successfully reloaded.");
            return true;
        }

        sender.sendMessage("§x§2§A§E§5§2§7§lu§x§5§3§F§5§5§1§lM§x§8§3§F§F§8§1§lo§x§6§3§F§9§6§0§ld§x§4§5§F§0§4§2§lD§x§2§A§E§5§2§7§le§x§2§A§E§5§2§7§lt§x§2§A§E§5§2§7§le§x§2§A§E§5§2§7§lc§x§2§A§E§5§2§7§lt" +
                " §8| §fUsage: /umd reload");
        return true;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        List<String> list = new ArrayList<>();

        if (args.length == 1) {
            list.add("reload");
        }

        return list;
    }
}