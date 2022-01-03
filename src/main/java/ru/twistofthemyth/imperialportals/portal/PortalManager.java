package ru.twistofthemyth.imperialportals.portal;

import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public interface PortalManager {

    boolean create(@NotNull Portal location);

    void destroy(@NotNull UUID portalId);

    @Nullable
    Location teleport(@NotNull UUID fromPortalId);

    void setMeta();
}
