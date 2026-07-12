package cc.xypp.damage_number.api.decoration;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class ItemDecoration implements INumberDecoration {
    public static final Identifier ID = Identifier.fromNamespaceAndPath("damage_number", "item_icon");
    private final ItemStack item;

    public ItemDecoration(RegistryFriendlyByteBuf registryFriendlyByteBuf) {
        this.item = ItemStack.OPTIONAL_STREAM_CODEC.decode(registryFriendlyByteBuf);
    }

    public ItemDecoration(ItemStack item) {
        this.item = item;
    }

    public ItemStack getItem() {
        return item;
    }


    @Override
    public @NotNull Identifier getId() {
        return ID;
    }

    @Override
    public void writeToNetwork(RegistryFriendlyByteBuf buf) {
        ItemStack.OPTIONAL_STREAM_CODEC.encode(buf, item);
    }
}
