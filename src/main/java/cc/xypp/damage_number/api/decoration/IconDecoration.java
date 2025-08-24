package cc.xypp.damage_number.api.decoration;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class IconDecoration implements INumberDecoration {
    public static final ResourceLocation ID = new ResourceLocation("damage_number", "texture_icon");


    @Override
    public @NotNull ResourceLocation getId() {
        return ID;
    }

    private final ResourceLocation icon;
    private final int u, v, width, height, imageWidth, imageHeight;

    public IconDecoration(ResourceLocation icon, int u, int v, int width, int height, int imageWidth, int imageHeight) {
        this.icon = icon;
        this.u = u;
        this.v = v;
        this.width = width;
        this.height = height;
        this.imageWidth = imageWidth;
        this.imageHeight = imageHeight;
    }

    public IconDecoration(FriendlyByteBuf buf) {
        this.icon = buf.readResourceLocation();
        this.u = buf.readInt();
        this.v = buf.readInt();
        this.width = buf.readInt();
        this.height = buf.readInt();
        this.imageWidth = buf.readInt();
        this.imageHeight = buf.readInt();
    }

    public ResourceLocation getIcon() {
        return icon;
    }
    public int getU() {
        return u;
    }
    public int getV() {
        return v;
    }
    public int getWidth() {
        return width;
    }
    public int getHeight() {
        return height;
    }
    public int getImageWidth() {
        return imageWidth;
    }
    public int getImageHeight() {
        return imageHeight;
    }

    @Override
    public void writeToNetwork(FriendlyByteBuf buf) {
        buf.writeResourceLocation(icon);
        buf.writeInt(u);
        buf.writeInt(v);
        buf.writeInt(width);
        buf.writeInt(height);
        buf.writeInt(imageWidth);
        buf.writeInt(imageHeight);
    }
}
