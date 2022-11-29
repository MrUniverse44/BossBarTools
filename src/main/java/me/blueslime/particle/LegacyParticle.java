package me.blueslime.particle;

import net.minecraft.server.v1_8_R3.EnumParticle;
import net.minecraft.server.v1_8_R3.PacketPlayOutWorldParticles;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.Locale;

public class LegacyParticle {
    public static void send(Player player, String particle, float x, float y, float z, int amount) {
        send(player, particle, x, y, z, new int[] { amount });
    }
    public static void send(Player player, String particle, float x, float y, float z, int[] amount) {
        EnumParticle enumParticle = EnumParticle.valueOf(
                particle.toUpperCase(Locale.ENGLISH)
        );

        PacketPlayOutWorldParticles particlePacket = new PacketPlayOutWorldParticles(
                enumParticle,
                true,
                x,
                y,
                z,
                0.0F,
                0.0F,
                0.0F,
                0.0F,
                0,
                amount
        );

        ((CraftPlayer)player).getHandle().playerConnection.sendPacket(
                particlePacket
        );
    }
}
