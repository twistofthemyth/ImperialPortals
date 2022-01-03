package ru.twistofthemyth.imperialportals.helpers;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;

public class Effects {
    private Effects() {
        throw new IllegalAccessError();
    }

    public static void playTeleportEffect(Location to, Location from) {
        to.getWorld().playSound(to, Sound.ENTITY_FOX_TELEPORT, 10, 0);
        to.getWorld().spawnParticle(Particle.DRAGON_BREATH, to, 100, 1, 1, 1, 0.1);
        from.getWorld().playSound(to, Sound.ENTITY_FOX_TELEPORT, 10, 0);
        from.getWorld().spawnParticle(Particle.DRAGON_BREATH, from, 100, 1, 1, 1, 0.1);
    }

    public static void playCreateEffect(Location location) {
        location.getWorld().playSound(location, Sound.AMBIENT_CAVE, 15, 0);
        location.getWorld().spawnParticle(Particle.LAVA, location, 100, 1, 1, 1, 0.3);
    }

    public static void playDestroyEffect(Location location) {
        location.getWorld().playSound(location, Sound.ENTITY_WITHER_SPAWN, 10, 0);
    }
}
