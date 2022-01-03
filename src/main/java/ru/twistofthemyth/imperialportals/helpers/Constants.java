package ru.twistofthemyth.imperialportals.helpers;

import org.bukkit.Material;

public class Constants {
    private Constants() {
        throw new IllegalAccessError();
    }

    public static final Material RAW_PORTAL_FRAME = Material.GOLD_BLOCK;
    public static final Material PORTAL_BLOCK = Material.NETHER_PORTAL;
    public static final Material ACTIVATE_ITEM = Material.DIAMOND;
    public static final int MAX_FRAME_WIDTH = 20;
    public static final int MIN_FRAME_WIDTH = 4;
    public static final int MAX_FRAME_HEIGHT = 20;
    public static final int MIN_FRAME_HEIGHT = 5;
    public static final String UNLINKED_PORTALS_FILE = "/Unlinked.json";
    public static final String LINKED_PORTALS_FILE = "/Linked.json";

    public static final String CFG_DEBUG_MODE = "debug-mode";
}
