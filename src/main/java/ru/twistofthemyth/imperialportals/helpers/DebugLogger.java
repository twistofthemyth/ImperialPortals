package ru.twistofthemyth.imperialportals.helpers;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.logging.Logger;

public class DebugLogger {
    private final Logger logger = Logger.getLogger("ImperialPortals");
    private final boolean enable;

    public DebugLogger(FileConfiguration config) {
        config.get("debug-mode");
        Boolean debugMode = (Boolean) config.get(Constants.CFG_DEBUG_MODE);
        enable = debugMode != null && debugMode;
    }

    public void log(String message) {
        if (enable) {
            logger.info(message);
        }
    }

    public void send(Player player, String message) {
        if (enable) {
            player.sendMessage(message);
        }
    }
}
