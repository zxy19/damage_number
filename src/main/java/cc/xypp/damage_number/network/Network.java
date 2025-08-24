package cc.xypp.damage_number.network;

import cc.xypp.damage_number.data.DamageRecord;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

public class Network {
    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            new ResourceLocation("damage_number", "damaged"),
            () -> PROTOCOL_VERSION,
            (v) -> true,
            (v) -> true
    );

    public static void send(ServerPlayer player, String type, float amount, int combo, DamageRecord data) {
        INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), new DamagePackage(type, amount, combo, data));
    }

}
