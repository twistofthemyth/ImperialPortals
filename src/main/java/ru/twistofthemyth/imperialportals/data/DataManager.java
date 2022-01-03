package ru.twistofthemyth.imperialportals.data;

import org.jetbrains.annotations.NotNull;
import ru.twistofthemyth.imperialportals.portal.Portal;

import java.util.Map;
import java.util.UUID;

public interface DataManager {
    @NotNull
    Map<UUID, Portal> getLinkedData();

    @NotNull
    Map<UUID, Portal> getUnlinkedData();

    void setLinkedData(@NotNull Map<UUID, Portal> portalMap);

    void setUnlinkedData(@NotNull Map<UUID, Portal> portalMap);
}
