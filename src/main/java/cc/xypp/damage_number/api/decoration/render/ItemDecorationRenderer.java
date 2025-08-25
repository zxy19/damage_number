package cc.xypp.damage_number.api.decoration.render;

import cc.xypp.damage_number.api.decoration.ItemDecoration;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.BakedModel;

public class ItemDecorationRenderer implements INumberDecorationRenderer<ItemDecoration> {
    @Override
    public void render(PoseStack poseStack, ItemDecoration decoration, float pt) {
        Minecraft.getInstance().getTextureManager().getTexture(TextureAtlas.LOCATION_BLOCKS).setFilter(false, false);
        RenderSystem.setShaderTexture(0, TextureAtlas.LOCATION_BLOCKS);
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        poseStack.translate(8, 8, 100 + Minecraft.getInstance().getItemRenderer().blitOffset);
        poseStack.scale(1.0F, -1.0F, 1.0F);
        poseStack.scale(16.0F, 16.0F, 16.0F);
        BakedModel model = Minecraft.getInstance().getItemRenderer().getModel(decoration.getItem(),
                Minecraft.getInstance().level,
                Minecraft.getInstance().player,
                0);
        boolean flag = model.usesBlockLight();
        if (!flag)
            Lighting.setupForFlatItems();
        Minecraft.getInstance().getItemRenderer()
                .render(
                        decoration.getItem(),
                        ItemTransforms.TransformType.GUI,
                        false,
                        poseStack,
                        Minecraft.getInstance().renderBuffers().bufferSource(),
                        LightTexture.FULL_BRIGHT,
                        OverlayTexture.NO_OVERLAY,
                        model
                );
        Minecraft.getInstance().renderBuffers().bufferSource().endBatch();
        RenderSystem.enableDepthTest();
        if (!flag)
            Lighting.setupFor3DItems();
    }
}
