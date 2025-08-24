package cc.xypp.damage_number.api.decoration;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class ItemDecoration implements INumberDecoration {
    public static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath("damage_number", "item_icon");
    private final ItemStack item;

    public ItemDecoration(RegistryFriendlyByteBuf registryFriendlyByteBuf) {
        this.item = ItemStack.parseOptional(registryFriendlyByteBuf.registryAccess(), registryFriendlyByteBuf.readNbt());
    }

    public ItemDecoration(ItemStack item) {
        this.item = item;
    }

    public ItemStack getItem() {
        return item;
    }


    @Override
    public @NotNull ResourceLocation getId() {
        return ID;
    }

    @Override
    public void writeToNetwork(RegistryFriendlyByteBuf buf) {
        buf.writeNbt(item.saveOptional(buf.registryAccess()));
    }
}
