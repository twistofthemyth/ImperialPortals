package ru.twistofthemyth.imperialportals.portal;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.data.Orientable;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.twistofthemyth.imperialportals.ImperialPortals;
import ru.twistofthemyth.imperialportals.data.DataManager;
import ru.twistofthemyth.imperialportals.data.JsonDataManager;
import ru.twistofthemyth.imperialportals.data.PortalMeta;
import ru.twistofthemyth.imperialportals.helpers.Constants;
import ru.twistofthemyth.imperialportals.helpers.DebugLogger;
import ru.twistofthemyth.imperialportals.helpers.Utils;

import java.util.Map;
import java.util.UUID;

public class PortalManagerImpl implements PortalManager {

    private final DebugLogger logger = ImperialPortals.getInstance().getDebugLogger();
    private final DataManager dataManager = new JsonDataManager();

    @Override
    public void create(@NotNull Portal portal) {
        logger.log("[XZ PM] Creating portal\n" + portal);
        Map<UUID, Portal> unlinkedPortals = dataManager.getUnlinkedData();
        Map<UUID, Portal> linkedPortals = dataManager.getLinkedData();
        UUID creatorId = portal.getCreatorId();
        if (unlinkedPortals.containsKey(creatorId)) {
            Portal unlinkedPortal = unlinkedPortals.get(creatorId);
            logger.log("[XZ PM] Link portal " + portal.getId() + " to " + unlinkedPortal.getId());

            unlinkedPortal.setLink(portal.getId());
            portal.setLink(unlinkedPortal.getId());

            linkedPortals.put(portal.getId(), portal);
            linkedPortals.put(unlinkedPortal.getId(), unlinkedPortal);

            unlinkedPortals.remove(creatorId);

            setPortalBlocks(portal);
            setPortalBlocks(unlinkedPortal);
        } else {
            logger.log("[XZ PM] Set portal " + portal.getId() + " as unlinked");
            unlinkedPortals.put(creatorId, portal);
        }

        dataManager.setUnlinkedData(unlinkedPortals);
        dataManager.setLinkedData(linkedPortals);
    }

    @Override
    public void destroy(@NotNull UUID portalId) {
        Map<UUID, Portal> linkedPortalMap = dataManager.getLinkedData();
        Map<UUID, Portal> unlinkedPortalMap = dataManager.getUnlinkedData();

        Portal unlinkedPortal = unlinkedPortalMap.get(portalId);
        Portal linkedPortal1 = linkedPortalMap.get(portalId);
        if (unlinkedPortal != null) {
            unlinkedPortalMap.remove(unlinkedPortal.getId());
            removePortalBlocks(unlinkedPortal);
        }
        if (linkedPortal1 != null) {
            Portal linkedPortal2 = linkedPortalMap.get(linkedPortal1.getLink());
            linkedPortalMap.remove(linkedPortal1.getId());
            linkedPortalMap.remove(linkedPortal2.getId());
            removePortalBlocks(linkedPortal1);
            removePortalBlocks(linkedPortal2);
        }

        dataManager.setLinkedData(linkedPortalMap);
        dataManager.setUnlinkedData(unlinkedPortalMap);
    }

    @Override
    public @Nullable Location teleport(@NotNull UUID fromPortalId) {
        UUID link = dataManager.getLinkedData().get(fromPortalId).getLink();
        if (link != null) {
            Portal linkPortal = dataManager.getLinkedData().get(link);
            World world = ImperialPortals.getInstance().getServer().getWorld(linkPortal.getWorld());
            int x = (linkPortal.getFrom().getBlockX() + linkPortal.getTo().getBlockX()) / 2;
            int y = linkPortal.getFrom().getBlockY() + 1;
            int z = (linkPortal.getFrom().getBlockZ() + linkPortal.getTo().getBlockZ()) / 2;
            return new Location(world, x, y, z);
        }
        return null;
    }

    @Override
    public void setMeta() {
        setMeta(true);
        setMeta(false);
    }

    private void setMeta(boolean linked) {
        Map<UUID, Portal> portalMap = linked ? dataManager.getLinkedData() : dataManager.getUnlinkedData();
        portalMap.keySet().forEach(k -> PortalMeta.setPortalMeta(portalMap.get(k), true));
    }

    private void setPortalBlocks(@NotNull Portal portal) {
        Vector from = portal.getFrom();
        Vector to = portal.getTo();
        World world = ImperialPortals.getInstance().getServer().getWorld(portal.getWorld());

        for (int x = from.getBlockX(); x <= to.getBlockX(); x++) {
            for (int z = from.getBlockZ(); z <= to.getBlockZ(); z++) {
                for (int y = from.getBlockY(); y <= to.getBlockY(); y++) {
                    Block block = new Location(world, x, y, z).getBlock();
                    if (!Utils.checkBlocksIs(Constants.RAW_PORTAL_FRAME, block.getLocation())) {
                        block.setType(Constants.PORTAL_BLOCK);
                        block.getState().setMetadata(PortalMeta.KEY, new PortalMeta(portal.getId()));
                        Orientable blockData = ((Orientable) block.getBlockData());
                        blockData.setAxis(portal.getAxis());
                        block.setBlockData(blockData);
                    }
                }
            }
        }
    }

    private void removePortalBlocks(@NotNull Portal portal) {
        Vector from = portal.getFrom();
        Vector to = portal.getTo();
        World world = ImperialPortals.getInstance().getServer().getWorld(portal.getWorld());

        for (int x = from.getBlockX(); x <= to.getBlockX(); x++) {
            for (int z = from.getBlockZ(); z <= to.getBlockZ(); z++) {
                for (int y = from.getBlockY(); y <= to.getBlockY(); y++) {
                    Block block = new Location(world, x, y, z).getBlock();
                    if (!Utils.checkBlocksIs(Constants.RAW_PORTAL_FRAME, block.getLocation())) {
                        block.setType(Material.AIR);
                    }
                    block.getState().removeMetadata(PortalMeta.KEY, ImperialPortals.getInstance());
                }
            }
        }
    }
}
