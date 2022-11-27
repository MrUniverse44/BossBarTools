package me.blueslime.bossbar.legacy;

import com.cryptomorin.xseries.ReflectionUtils;
import net.minecraft.server.v1_8_R3.EntityWither;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_8_R3.PacketPlayOutSpawnEntityLiving;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.entity.Player;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@SuppressWarnings("unused")
public class PluginBossBar {

    public static final ConcurrentHashMap<UUID, EntityWither> WITHER_MAP = new ConcurrentHashMap<>();
    private static boolean errorReported = false;

    public static void sendBossBar(Player player, String message) {
        sendBossBar(player, message, 100);
    }

    public static void sendBossBar(Player player, String message, float percentage) {
        if (errorReported) {
            return;
        }
        try {
            if (percentage <= 0) {
                percentage = (float) 0.001;
            }

            CraftWorld world = (CraftWorld) player.getLocation().getWorld();

            EntityWither wither = WITHER_MAP.computeIfAbsent(
                    player.getUniqueId(),
                    e -> new EntityWither(world.getHandle())
            );

            float health = wither.getMaxHealth();

            float life = (percentage * health);

            Location location = obtainWitherLocation(
                    player.getLocation()
            );

            wither.setCustomName(
                    message
            );

            wither.setHealth(
                    life
            );

            wither.setInvisible(true);

            wither.setLocation(
                    location.getX(),
                    location.getY(),
                    location.getZ(),
                    0,
                    0
            );

            PacketPlayOutSpawnEntityLiving packet = new PacketPlayOutSpawnEntityLiving(
                    wither
            );


            ReflectionUtils.sendPacket(
                    player,
                    packet
            );
        } catch (Exception ignored) {
            errorReported = true;
        }
    }

    public static void removeBossBar(Player player) {
        if (errorReported) {
            return;
        }
        if (WITHER_MAP.containsKey(player.getUniqueId())) {
            EntityWither wither = WITHER_MAP.remove(
                    player.getUniqueId()
            );

            try {
                int id = wither.getId();

                PacketPlayOutEntityDestroy packet = new PacketPlayOutEntityDestroy(id);

                ReflectionUtils.sendPacket(
                        player,
                        packet
                );
            } catch (Exception ignored) { }
        }
    }

    private static Location obtainWitherLocation(Location location) {
        return location.add(
                location.getDirection().multiply(60)
        );
    }

}
