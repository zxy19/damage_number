package cc.xypp.damage_number.api.decoration;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public interface INumberDecoration {
    @FunctionalInterface
    public interface INumberDecorationFromNetwork<T extends INumberDecoration> {
        T build(FriendlyByteBuf buf);
    }

    @NotNull ResourceLocation getId();

    void writeToNetwork(FriendlyByteBuf buf);
}
