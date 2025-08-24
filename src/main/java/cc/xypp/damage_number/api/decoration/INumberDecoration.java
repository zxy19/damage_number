package cc.xypp.damage_number.api.decoration;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public interface INumberDecoration {
    @FunctionalInterface
    public interface INumberDecorationFromNetwork<T extends INumberDecoration> {
        T build(RegistryFriendlyByteBuf buf);
    }

    @NotNull ResourceLocation getId();

    void writeToNetwork(RegistryFriendlyByteBuf buf);
}
