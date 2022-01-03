package ru.twistofthemyth.imperialportals.data;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.twistofthemyth.imperialportals.ImperialPortals;
import ru.twistofthemyth.imperialportals.portal.Portal;

import java.util.UUID;

public class PortalMeta implements MetadataValue {

    public static final String KEY = "portalId";

    @NotNull
    private final UUID portalId;

    public PortalMeta(@NotNull UUID portalId) {
        this.portalId = portalId;
    }

    public static boolean setPortalMeta(Portal portal, boolean force) {
        Vector from = portal.getFrom();
        Vector to = portal.getTo();
        World world = ImperialPortals.getInstance().getServer().getWorld(portal.getWorld());

        if (!force) {
            for (int x = from.getBlockX(); x <= to.getBlockX(); x++) {
                for (int z = from.getBlockZ(); z <= to.getBlockZ(); z++) {
                    for (int y = from.getBlockY(); y <= to.getBlockY(); y++) {
                        if (new Location(world, x, y, z).getBlock().getState().hasMetadata(KEY)) {
                            return false;
                        }
                    }
                }
            }
        }

        for (int x = from.getBlockX(); x <= to.getBlockX(); x++) {
            for (int z = from.getBlockZ(); z <= to.getBlockZ(); z++) {
                for (int y = from.getBlockY(); y <= to.getBlockY(); y++) {
                    new Location(world, x, y, z).getBlock().getState().setMetadata(KEY, new PortalMeta(portal.getId()));
                }
            }
        }
        return true;
    }

    @Override
    public @Nullable Object value() {
        return portalId;
    }

    @Override
    public int asInt() {
        return 0;
    }

    @Override
    public float asFloat() {
        return 0;
    }

    @Override
    public double asDouble() {
        return 0;
    }

    @Override
    public long asLong() {
        return 0;
    }

    @Override
    public short asShort() {
        return 0;
    }

    @Override
    public byte asByte() {
        return 0;
    }

    @Override
    public boolean asBoolean() {
        return false;
    }

    @Override
    public @NotNull String asString() {
        return portalId.toString();
    }

    @Override
    public @Nullable Plugin getOwningPlugin() {
        return ImperialPortals.getInstance();
    }

    @Override
    public void invalidate() {

    }
}
