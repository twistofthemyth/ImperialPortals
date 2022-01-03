package ru.twistofthemyth.imperialportals.helpers;

import org.bukkit.Location;
import org.bukkit.Material;

import java.util.Arrays;

public class Utils {
    private Utils() {
        throw new IllegalAccessError();
    }

    public static boolean checkBlocksIs(Material material, Location... locations) {
        long validCount = Arrays.stream(locations)
                .filter(e -> e.getBlock().getBlockData().getMaterial().equals(material))
                .count();

        return (validCount == locations.length);
    }

    public static Location addLocation(Location location, int x, int y, int z) {
        return location.clone().add(x, y, z);
    }
}
