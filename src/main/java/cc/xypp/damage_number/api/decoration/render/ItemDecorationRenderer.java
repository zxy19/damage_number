package cc.xypp.damage_number.api.decoration.render;

import cc.xypp.damage_number.api.decoration.ItemDecoration;
import net.minecraft.client.gui.GuiGraphics;

public class ItemDecorationRenderer implements INumberDecorationRenderer<ItemDecoration> {
    @Override
    public void render(GuiGraphics guiGraphics, ItemDecoration decoration, float pt) {
        guiGraphics.renderItem(decoration.getItem(), 0, 0);
    }
}
