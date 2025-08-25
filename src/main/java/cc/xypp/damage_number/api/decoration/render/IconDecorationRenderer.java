package cc.xypp.damage_number.api.decoration.render;

import cc.xypp.damage_number.api.decoration.IconDecoration;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiComponent;

public class IconDecorationRenderer implements INumberDecorationRenderer<IconDecoration> {
    @Override
    public void render(PoseStack poseStack, IconDecoration decoration, float pt) {
        poseStack.scale(16f / decoration.getWidth(), 16f / decoration.getHeight(), 1);
        RenderSystem.setShaderTexture(0, decoration.getIcon());
        GuiComponent.blit(
                poseStack,
                0, 0,
                decoration.getU(), decoration.getV(),
                decoration.getWidth(), decoration.getHeight(),
                decoration.getImageWidth(), decoration.getImageHeight()
        );
    }
}
