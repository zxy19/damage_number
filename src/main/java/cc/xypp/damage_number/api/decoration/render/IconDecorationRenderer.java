package cc.xypp.damage_number.api.decoration.render;

import cc.xypp.damage_number.api.decoration.IconDecoration;
import net.minecraft.client.gui.GuiGraphics;

public class IconDecorationRenderer implements INumberDecorationRenderer<IconDecoration> {
    @Override
    public void render(GuiGraphics guiGraphics, IconDecoration decoration, float pt) {
        guiGraphics.pose().scale(16f / decoration.getWidth(), 16f / decoration.getHeight(), 1);
        guiGraphics.blit(decoration.getIcon(),
                0, 0,
                decoration.getU(), decoration.getV(),
                decoration.getWidth(), decoration.getHeight(),
                decoration.getImageWidth(), decoration.getImageHeight()
        );
    }
}
