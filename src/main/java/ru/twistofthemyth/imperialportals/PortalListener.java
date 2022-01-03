package ru.twistofthemyth.imperialportals;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityPortalEnterEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.twistofthemyth.imperialportals.data.PortalMeta;
import ru.twistofthemyth.imperialportals.helpers.Constants;
import ru.twistofthemyth.imperialportals.helpers.DebugLogger;
import ru.twistofthemyth.imperialportals.helpers.Effects;
import ru.twistofthemyth.imperialportals.helpers.Messages;
import ru.twistofthemyth.imperialportals.portal.*;

import java.util.UUID;

import static org.bukkit.event.player.PlayerTeleportEvent.TeleportCause.NETHER_PORTAL;
import static ru.twistofthemyth.imperialportals.helpers.Utils.addLocation;
import static ru.twistofthemyth.imperialportals.helpers.Utils.checkBlocksIs;

public class PortalListener implements Listener {

    private final DebugLogger logger = ImperialPortals.getInstance().getDebugLogger();
    private final PortalManager portalManager = ImperialPortals.getInstance().getPortalManager();

    @EventHandler
    public void onActivatePortal(PlayerInteractEvent event) {
        try {
            if (event.getClickedBlock().getType().equals(Constants.RAW_PORTAL_FRAME)) {
                Player player = event.getPlayer();
                ItemStack activatedItem = event.getItem();
                if (!activatedItem.getType().equals(Constants.ACTIVATE_ITEM)) {
                    logger.send(player, ChatColor.RED + "[PortalListener] Inappropriate item to activate a portal");
                    return;
                }
                if (activatedItem.getAmount() < Constants.ACTIVATE_ITEM_AMOUNT) {
                    logger.send(player, ChatColor.RED + "[PortalListener] Not enough items to activate the portal");
                    return;
                }
                Portal portal = buildXZFrame(event.getClickedBlock().getLocation(), player);
                if (portal != null) {
                    logger.send(player, ChatColor.GREEN + "[PortalListener] Portal frame integrity check success");
                    if (PortalMeta.setPortalMeta(portal, false)) {
                        logger.send(player, ChatColor.GREEN + "[PortalListener] Portal meta check success");
                        boolean linked = portalManager.create(portal);
                        activatedItem.setAmount(activatedItem.getAmount() - Constants.ACTIVATE_ITEM_AMOUNT);
                        Effects.playCreateEffect(event.getClickedBlock().getLocation());
                        if (linked) {
                            Messages.linkPortal(player);
                        } else {
                            Messages.activatePortal(player);
                        }
                    } else {
                        logger.send(player, ChatColor.RED + "[PortalListener] Portal meta check fail");
                    }
                } else {
                    logger.send(player, ChatColor.RED + "[PortalListener] Portal frame integrity check fail");
                }
            }
        } catch (NullPointerException ignore) {
        }
    }

    @EventHandler
    public void onTeleport(PlayerTeleportEvent event) {
        if (NETHER_PORTAL.equals(event.getCause())) {
            Block coreBlock = event.getFrom().getBlock();
            Block minusXBlock = addLocation(event.getFrom(), -1, 0, 0).getBlock();
            Block plusXBlock = addLocation(event.getFrom(), 1, 0, 0).getBlock();
            Block minusZBlock = addLocation(event.getFrom(), 0, 0, -1).getBlock();
            Block plusZBlock = addLocation(event.getFrom(), 0, 0, 1).getBlock();

            Block teleportBlock = null;
            if (coreBlock.getState().hasMetadata(PortalMeta.KEY)) {
                teleportBlock = coreBlock;
            } else if (minusXBlock.getState().hasMetadata(PortalMeta.KEY)) {
                teleportBlock = minusXBlock;
            } else if (plusXBlock.getState().hasMetadata(PortalMeta.KEY)) {
                teleportBlock = plusXBlock;
            } else if (minusZBlock.getState().hasMetadata(PortalMeta.KEY)) {
                teleportBlock = minusZBlock;
            } else if (plusZBlock.getState().hasMetadata(PortalMeta.KEY)) {
                teleportBlock = plusZBlock;
            }

            if (teleportBlock != null) {
                Location to = portalManager.teleport(UUID.fromString(teleportBlock.getMetadata(PortalMeta.KEY).get(0).asString()));
                if (to != null) {
                    event.setTo(to);
                    Effects.playTeleportEffect(to, event.getFrom());
                } else {
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onEntityTeleport(EntityPortalEnterEvent event) {
        if (!EntityType.PLAYER.equals(event.getEntity().getType())) {
            Block coreBlock = event.getLocation().getBlock();
            Block minusXBlock = addLocation(event.getLocation(), -1, 0, 0).getBlock();
            Block plusXBlock = addLocation(event.getLocation(), 1, 0, 0).getBlock();
            Block minusZBlock = addLocation(event.getLocation(), 0, 0, -1).getBlock();
            Block plusZBlock = addLocation(event.getLocation(), 0, 0, 1).getBlock();

            Block teleportBlock = null;
            if (coreBlock.getState().hasMetadata(PortalMeta.KEY)) {
                teleportBlock = coreBlock;
            } else if (minusXBlock.getState().hasMetadata(PortalMeta.KEY)) {
                teleportBlock = minusXBlock;
            } else if (plusXBlock.getState().hasMetadata(PortalMeta.KEY)) {
                teleportBlock = plusXBlock;
            } else if (minusZBlock.getState().hasMetadata(PortalMeta.KEY)) {
                teleportBlock = minusZBlock;
            } else if (plusZBlock.getState().hasMetadata(PortalMeta.KEY)) {
                teleportBlock = plusZBlock;
            }

            if (teleportBlock != null) {
                Location to = portalManager.teleport(UUID.fromString(teleportBlock.getMetadata(PortalMeta.KEY).get(0).asString()));
                if (to != null) {
                    event.getEntity().teleportAsync(addLocation(to, 2, 0, 0));
                    Effects.playTeleportEffect(to, event.getLocation());
                }
            }
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (event.getBlock().getState().hasMetadata(PortalMeta.KEY)) {
            portalManager.destroy(UUID.fromString(
                    event.getBlock().getState().getMetadata(PortalMeta.KEY).get(0).asString()));
            Effects.playDestroyEffect(event.getPlayer().getLocation());
            Messages.destroyPortal(event.getPlayer());
        }
    }

    @Nullable
    private Portal buildXZFrame(@NotNull Location location, @NotNull Player player) {
        FrameBuilder portalFrame;
        if (checkBlocksIs(Constants.RAW_PORTAL_FRAME, location, addLocation(location, 1, 0, 0),
                addLocation(location, -1, 0, 0))) {
            portalFrame = new XFrameBuilder();
        } else if (checkBlocksIs(Constants.RAW_PORTAL_FRAME, location, addLocation(location, 0, 0, 1),
                addLocation(location, 0, 0, -1))) {
            portalFrame = new ZFrameBuilder();
        } else {
            return null;
        }
        return portalFrame.build(location, player);
    }
}
