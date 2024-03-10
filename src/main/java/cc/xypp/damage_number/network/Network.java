package cc.xypp.damage_number.network;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.logging.Logger;

public class Network {
    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            new ResourceLocation("damage_number", "damaged"),
            () -> PROTOCOL_VERSION,
            (v)->true,
            (v)->true
    );
}
