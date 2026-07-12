package cc.xypp.damage_number.api.decoration;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.NotNull;

public interface INumberDecoration {
    @FunctionalInterface
    public interface INumberDecorationFromNetwork<T extends INumberDecoration> {
        T build(RegistryFriendlyByteBuf buf);
    }

    @NotNull Identifier getId();

    void writeToNetwork(RegistryFriendlyByteBuf buf);
}
