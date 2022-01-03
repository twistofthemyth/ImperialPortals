package ru.twistofthemyth.imperialportals;

import lombok.Getter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import ru.twistofthemyth.imperialportals.helpers.Constants;
import ru.twistofthemyth.imperialportals.helpers.DebugLogger;
import ru.twistofthemyth.imperialportals.portal.PortalManager;
import ru.twistofthemyth.imperialportals.portal.PortalManagerImpl;

public final class ImperialPortals extends JavaPlugin {

    private static ImperialPortals instance;

    @Getter
    @NotNull
    private PortalManager portalManager;

    @Getter
    @NotNull
    private DebugLogger debugLogger;

    @Override
    public void onEnable() {
        setInstance(this);
        this.debugLogger = new DebugLogger(getConfig());
        this.portalManager = new PortalManagerImpl();
        this.createDataFolder();
        this.createDefaultConfig();
        this.portalManager.setMeta();
        this.getServer().getPluginManager().registerEvents(new PortalListener(), this);
    }

    @Override
    public void onDisable() {
    }

    @NotNull
    public static synchronized ImperialPortals getInstance() {
        return instance;
    }

    private void createDefaultConfig() {
        FileConfiguration config = getConfig();
        config.addDefault(Constants.CFG_DEBUG_MODE, false);
        config.options().copyDefaults(true);
        saveConfig();
    }

    private void createDataFolder() {
        if (!getDataFolder().exists()) {
            getDataFolder().mkdir();
        }
    }

    private static synchronized void setInstance(ImperialPortals plugin) {
        instance = plugin;
    }
}
