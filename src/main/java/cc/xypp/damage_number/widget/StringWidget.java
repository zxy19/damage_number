package cc.xypp.damage_number.widget;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

public class StringWidget extends AbstractWidget {

    private Font font;

    public StringWidget(int p_93629_, int p_93630_, int p_93631_, int p_93632_, Component p_93633_) {
        super(p_93629_, p_93630_, p_93631_, p_93632_, p_93633_);
    }

    public StringWidget(Component msg, Font font) {
        super(0, 0, 0, 0, msg);
        this.font = font;
        this.width = font.width(msg);
    }

    @Override
    public void render(@NotNull PoseStack p_93657_, int p_93658_, int p_93659_, float p_93660_) {
//        super.render(p_93657_, p_93658_, p_93659_, p_93660_);
        if (this.visible)
            this.font.draw(p_93657_, this.getMessage().getString(), this.x, this.y, 0xffffff);
    }

    @Override
    public int getWidth() {
        return this.font.width(this.getMessage());
    }

    @Override
    public int getHeight() {
        return this.font.lineHeight;
    }

    @Override
    public void updateNarration(NarrationElementOutput p_169152_) {
    }
}
