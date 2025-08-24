package cc.xypp.damage_number.api.decoration.render;

import cc.xypp.damage_number.api.decoration.INumberDecoration;
import net.minecraft.client.gui.GuiGraphics;

public interface INumberDecorationRenderer<T extends INumberDecoration> {
    void render(GuiGraphics guiGraphics, T decoration, float pt);
}