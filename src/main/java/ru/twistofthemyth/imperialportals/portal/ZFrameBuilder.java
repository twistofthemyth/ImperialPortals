package ru.twistofthemyth.imperialportals.portal;

import org.bukkit.Axis;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import ru.twistofthemyth.imperialportals.ImperialPortals;
import ru.twistofthemyth.imperialportals.helpers.Constants;
import ru.twistofthemyth.imperialportals.helpers.DebugLogger;

import static ru.twistofthemyth.imperialportals.helpers.Utils.addLocation;
import static ru.twistofthemyth.imperialportals.helpers.Utils.checkBlocksIs;

public class ZFrameBuilder implements FrameBuilder {

    private final DebugLogger logger = ImperialPortals.getInstance().getDebugLogger();

    @Override
    public Portal build(@NotNull Location location, @NotNull Player player) {
        boolean stopLeftSearchFlag = false;
        boolean stopRightSearchFlag = false;
        int leftBorderShift = 0;
        int rightBorderShift = 0;
        int height = 0;

        logger.log("[ZFrame Builder] Foundation check...");
        for (int i = 1; i < Constants.MAX_FRAME_WIDTH; i++) {
            if (!stopRightSearchFlag && checkBlocksIs(Constants.RAW_PORTAL_FRAME, addLocation(location, 0, 0, i))) {
                rightBorderShift = i;
            } else {
                stopRightSearchFlag = true;
            }

            if (!stopLeftSearchFlag && checkBlocksIs(Constants.RAW_PORTAL_FRAME, addLocation(location, 0, 0, -i))) {
                leftBorderShift = -i;

            } else {
                stopLeftSearchFlag = true;
            }

            if (stopLeftSearchFlag && stopRightSearchFlag) {
                break;
            }
        }
        logger.log("[ZFrame Builder] Foundation check done");

        int width = Math.abs(leftBorderShift) + Math.abs(rightBorderShift) + 1;

        if (width > Constants.MAX_FRAME_WIDTH || width < Constants.MIN_FRAME_WIDTH) {
            return null;
        }

        logger.log("[ZFrame Builder] Wall check...");
        for (int i = 0; i < Constants.MAX_FRAME_HEIGHT; i++) {

            if (checkBlocksIs(Constants.RAW_PORTAL_FRAME, addLocation(location, 0, i, leftBorderShift),
                    addLocation(location, 0, i, rightBorderShift))) {
                height = i + 1;
            } else {
                break;
            }
        }
        logger.log("[ZFrame Builder] Wall check done");

        if (height > Constants.MAX_FRAME_HEIGHT || height < Constants.MIN_FRAME_HEIGHT) {
            return null;
        }

        logger.log("[ZFrame Builder] Roof check...");
        for (int i = leftBorderShift; i <= rightBorderShift; i++) {
            if (!checkBlocksIs(Constants.RAW_PORTAL_FRAME, addLocation(location, 0, height - 1, i))) {
                return null;
            }
        }
        logger.log("[ZFrame Builder] Roof check done");

        Vector from = new Vector(location.getX(), location.getY(), location.getZ() + leftBorderShift);
        Vector to = new Vector(location.getX(), location.getY() + height - 1, location.getZ() + rightBorderShift);

        logger.log("[ZFrame Builder] Height: " + height);
        logger.log("[ZFrame Builder] Foundation: " + width);
        logger.log("[ZFrame Builder] Left border shift: " + leftBorderShift);
        logger.log("[ZFrame Builder] Right border shift: " + rightBorderShift);

        return new Portal(Axis.Z, player.getUniqueId(), location.getWorld().getName(), from, to);
    }
}
