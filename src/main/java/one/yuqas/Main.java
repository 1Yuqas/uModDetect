package one.yuqas;

import one.yuqas.command.ModCommand;
import one.yuqas.event.ControlEvent;
import one.yuqas.util.ConfigUtil;

import me.nahu.scheduler.wrapper.FoliaWrappedJavaPlugin;
import me.nahu.scheduler.wrapper.WrappedScheduler;

public final class Main extends FoliaWrappedJavaPlugin {
    private static Main instance;
    private ControlEvent controlEvent;
    private ConfigUtil configUtil;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();

        ModCommand cmd = new ModCommand(this);
        getCommand("uMd").setExecutor(cmd);
        getCommand("uMd").setTabCompleter(cmd);

        this.configUtil = new ConfigUtil(this);
        this.controlEvent = new ControlEvent(this, configUtil);
    }

    @Override
    public void onDisable() {}

    public static Main getInstance() {
        return instance;
    }

    public static WrappedScheduler getWrappedScheduler() {
        return instance.getScheduler();
    }

    public ControlEvent getControlEvent() {
        return controlEvent;
    }
    public ConfigUtil getConfigUtil() {
        return configUtil;
    }
}