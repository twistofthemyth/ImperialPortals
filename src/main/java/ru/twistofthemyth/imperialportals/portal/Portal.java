package ru.twistofthemyth.imperialportals.portal;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Axis;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class Portal {
    @NotNull
    @Getter
    private final UUID id;

    @NotNull
    @Getter
    private final Axis axis;

    @Nullable
    @Getter
    @Setter
    private UUID link;

    @NotNull
    @Getter
    private final UUID creatorId;

    @NotNull
    @Getter
    private final String world;

    @NotNull
    @Getter
    private final Vector from;

    @NotNull
    @Getter
    private final Vector to;

    public Portal(@NotNull UUID id, @NotNull Axis axis, @NotNull UUID creatorId, @Nullable UUID link,
                  @NotNull String world, @NotNull Vector from, @NotNull Vector to) {
        this.id = id;
        this.axis = axis;
        this.creatorId = creatorId;
        this.link = link;
        this.world = world;
        this.from = from;
        this.to = to;
    }

    public Portal(@NotNull Axis axis, @NotNull UUID creatorId, @NotNull String world, @NotNull Vector from, @NotNull Vector to) {
        id = UUID.randomUUID();
        this.axis = axis;
        this.creatorId = creatorId;
        this.world = world;
        this.from = from;
        this.to = to;
    }

    @Override
    public String toString() {
        return String.format("ID: %s\nAxis: %s\nLink: %s\nCreatorId: %s\nWorld: %s\nFrom: %s\nTo:%s",
                id, axis, link, creatorId, world, from, to);
    }
}
