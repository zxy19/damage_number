package cc.xypp.damage_number.api.decoration.render;

import cc.xypp.damage_number.api.decoration.INumberDecoration;
import com.mojang.blaze3d.vertex.PoseStack;

public interface INumberDecorationRenderer<T extends INumberDecoration> {
    void render(PoseStack poseStack, T decoration, float pt);
}