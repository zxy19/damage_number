package cc.xypp.damage_number.api.decoration;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

import java.util.Map;

public class DecorationSerializer {
    public static Map<ResourceLocation, INumberDecoration.INumberDecorationFromNetwork<?>> types = Map.of(
            IconDecoration.ID, IconDecoration::new,
            ItemDecoration.ID, ItemDecoration::new
    );

    public static INumberDecoration fromNetwork(RegistryFriendlyByteBuf buf) {
        var decoration = types.get(buf.readResourceLocation());
        if (decoration == null) return null;
        return decoration.build(buf);
    }

    public static void toNetwork(RegistryFriendlyByteBuf buf, INumberDecoration decoration) {
        buf.writeResourceLocation(decoration.getId());
        decoration.writeToNetwork(buf);
    }
}
