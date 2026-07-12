package cc.xypp.damage_number.api.decoration.render;

import cc.xypp.damage_number.api.decoration.ItemDecoration;
import net.minecraft.client.gui.GuiGraphicsExtractor;

public class ItemDecorationRenderer implements INumberDecorationRenderer<ItemDecoration> {
    @Override
    public void render(GuiGraphicsExtractor guiGraphics, ItemDecoration decoration, float pt) {
        guiGraphics.item(decoration.getItem(), 0, 0);
    }
}
