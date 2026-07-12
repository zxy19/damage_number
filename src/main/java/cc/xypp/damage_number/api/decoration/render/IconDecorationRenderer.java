package cc.xypp.damage_number.api.decoration.render;

import cc.xypp.damage_number.api.decoration.IconDecoration;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;

public class IconDecorationRenderer implements INumberDecorationRenderer<IconDecoration> {
    @Override
    public void render(GuiGraphicsExtractor guiGraphics, IconDecoration decoration, float pt) {
        guiGraphics.pose().scale(16f / decoration.getWidth(), 16f / decoration.getHeight());
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, decoration.getIcon(),
                0, 0,
                decoration.getU(), decoration.getV(),
                decoration.getWidth(), decoration.getHeight(),
                decoration.getImageWidth(), decoration.getImageHeight()
        );
    }
}
