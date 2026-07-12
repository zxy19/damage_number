package cc.xypp.damage_number.api.decoration;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.NotNull;

public class IconDecoration implements INumberDecoration {
    public static final Identifier ID = Identifier.fromNamespaceAndPath("damage_number", "texture_icon");


    @Override
    public @NotNull Identifier getId() {
        return ID;
    }

    private final Identifier icon;
    private final int u, v, width, height, imageWidth, imageHeight;

    public IconDecoration(Identifier icon, int u, int v, int width, int height, int imageWidth, int imageHeight) {
        this.icon = icon;
        this.u = u;
        this.v = v;
        this.width = width;
        this.height = height;
        this.imageWidth = imageWidth;
        this.imageHeight = imageHeight;
    }

    public IconDecoration(RegistryFriendlyByteBuf buf) {
        this.icon = buf.readIdentifier();
        this.u = buf.readInt();
        this.v = buf.readInt();
        this.width = buf.readInt();
        this.height = buf.readInt();
        this.imageWidth = buf.readInt();
        this.imageHeight = buf.readInt();
    }

    public Identifier getIcon() {
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
    public void writeToNetwork(RegistryFriendlyByteBuf buf) {
        buf.writeIdentifier(icon);
        buf.writeInt(u);
        buf.writeInt(v);
        buf.writeInt(width);
        buf.writeInt(height);
        buf.writeInt(imageWidth);
        buf.writeInt(imageHeight);
    }
}
