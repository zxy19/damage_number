package cc.xypp.damage_number.api.decoration;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class ItemDecoration implements INumberDecoration {
    public static final ResourceLocation ID = new ResourceLocation("damage_number", "item_icon");
    private final ItemStack item;

    public ItemDecoration(FriendlyByteBuf registryFriendlyByteBuf) {
        this.item = ItemStack.of(registryFriendlyByteBuf.readNbt());
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
    public void writeToNetwork(FriendlyByteBuf buf) {
        buf.writeNbt(item.save(new CompoundTag()));
    }
}
