package ru.twistofthemyth.imperialportals.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.bukkit.Axis;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import ru.twistofthemyth.imperialportals.ImperialPortals;
import ru.twistofthemyth.imperialportals.helpers.Constants;
import ru.twistofthemyth.imperialportals.portal.Portal;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class JsonDataManager implements DataManager {
    private static final String ID_PROPERTY = "id";
    private static final String AXIS_PROPERTY = "axis";
    private static final String LINK_PROPERTY = "link";
    private static final String CREATOR_ID_PROPERTY = "creatorId";
    private static final String WORLD_PROPERTY = "world";
    private static final String FROM_PROPERTY = "from";
    private static final String TO_PROPERTY = "to";
    private static final String X_PROPERTY = "X";
    private static final String Y_PROPERTY = "Y";
    private static final String Z_PROPERTY = "Z";

    private final File unlinkedPortalsFile = new File(ImperialPortals.getInstance().getDataFolder().getPath() +
            Constants.UNLINKED_PORTALS_FILE);
    private final File linkedPortalsFile = new File(ImperialPortals.getInstance().getDataFolder().getPath() +
            Constants.LINKED_PORTALS_FILE);

    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    @Override
    public @NotNull Map<UUID, Portal> getLinkedData() {
        return getData(true);
    }

    @Override
    public @NotNull Map<UUID, Portal> getUnlinkedData() {
        return getData(false);
    }

    @Override
    public void setLinkedData(@NotNull Map<UUID, Portal> portals) {
        setData(portals, true);
    }

    @Override
    public void setUnlinkedData(@NotNull Map<UUID, Portal> portals) {
        setData(portals, false);
    }

    @NotNull
    private JsonObject serialize(@NotNull Portal portal) {
        JsonObject jsonPortal = new JsonObject();
        jsonPortal.addProperty(ID_PROPERTY, portal.getId().toString());
        jsonPortal.addProperty(AXIS_PROPERTY, portal.getAxis().toString());
        jsonPortal.addProperty(LINK_PROPERTY, String.valueOf(portal.getLink()));
        jsonPortal.addProperty(CREATOR_ID_PROPERTY, String.valueOf(portal.getCreatorId()));
        jsonPortal.addProperty(WORLD_PROPERTY, portal.getWorld());
        jsonPortal.add(FROM_PROPERTY, vectorToJson(portal.getFrom()));
        jsonPortal.add(TO_PROPERTY, vectorToJson(portal.getTo()));
        return jsonPortal;
    }

    @NotNull
    private Portal deserialize(@NotNull JsonObject jsonPortal) {
        UUID id = UUID.fromString(jsonPortal.get(ID_PROPERTY).getAsString());
        Axis axis = Axis.valueOf(jsonPortal.get(AXIS_PROPERTY).getAsString());
        String rawLingId = jsonPortal.get(LINK_PROPERTY).getAsString();
        UUID linkId = "null".equals(rawLingId) ? null : UUID.fromString(rawLingId);
        UUID creatorId = UUID.fromString(jsonPortal.get(CREATOR_ID_PROPERTY).getAsString());
        String world = jsonPortal.get(WORLD_PROPERTY).getAsString();
        JsonObject fromJson = jsonPortal.getAsJsonObject(FROM_PROPERTY);
        JsonObject toJson = jsonPortal.getAsJsonObject(TO_PROPERTY);
        Vector from = new Vector(fromJson.get(X_PROPERTY).getAsInt(),
                fromJson.get(Y_PROPERTY).getAsInt(),
                fromJson.get(Z_PROPERTY).getAsInt());
        Vector to = new Vector(toJson.get(X_PROPERTY).getAsInt(),
                toJson.get(Y_PROPERTY).getAsInt(),
                toJson.get(Z_PROPERTY).getAsInt());
        return new Portal(id, axis, creatorId, linkId, world, from, to);
    }

    private @NotNull JsonArray serializeMap(@NotNull Map<UUID, Portal> portals) {
        JsonArray jsonArray = new JsonArray();
        portals.keySet().forEach(key -> jsonArray.add(serialize(portals.get(key))));
        return jsonArray;
    }

    private @NotNull Map<UUID, Portal> deserializeMap(@NotNull JsonArray jsonArray, boolean linked) {
        Map<UUID, Portal> portalMap = new HashMap<>();
        jsonArray.forEach(e -> {
            JsonObject jsonObject = e.getAsJsonObject();
            UUID key = linked ? UUID.fromString(jsonObject.get(ID_PROPERTY).getAsString()) :
                    UUID.fromString(jsonObject.get(CREATOR_ID_PROPERTY).getAsString());
            portalMap.put(key, deserialize(jsonObject));
        });
        return portalMap;
    }

    private @NotNull JsonObject vectorToJson(@NotNull Vector vector) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(X_PROPERTY, vector.getBlockX());
        jsonObject.addProperty(Y_PROPERTY, vector.getBlockY());
        jsonObject.addProperty(Z_PROPERTY, vector.getBlockZ());
        return jsonObject;
    }

    private synchronized @NotNull Map<UUID, Portal> getData(boolean linked) {
        File jsonFile = linked ? linkedPortalsFile : unlinkedPortalsFile;
        try (FileReader fr = new FileReader(jsonFile)) {
            JsonArray jsonArray = gson.fromJson(fr, JsonArray.class);
            return deserializeMap(jsonArray, linked);
        } catch (IOException e) {
            return new HashMap<>();
        }
    }

    private synchronized void setData(@NotNull Map<UUID, Portal> portals, boolean linked) {
        File jsonFile = linked ? linkedPortalsFile : unlinkedPortalsFile;
        try (FileWriter fw = new FileWriter(jsonFile)) {
            fw.write(serializeMap(portals).toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
