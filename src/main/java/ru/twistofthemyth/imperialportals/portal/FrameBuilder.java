package ru.twistofthemyth.imperialportals.portal;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface FrameBuilder {

    @Nullable
    Portal build(@NotNull Location location, @NotNull Player player);
}
