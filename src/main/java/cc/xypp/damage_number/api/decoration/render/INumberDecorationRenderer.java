package cc.xypp.damage_number.api.decoration.render;

import cc.xypp.damage_number.api.decoration.INumberDecoration;
import net.minecraft.client.gui.GuiGraphicsExtractor;

public interface INumberDecorationRenderer<T extends INumberDecoration> {
    void render(GuiGraphicsExtractor guiGraphics, T decoration, float pt);
}