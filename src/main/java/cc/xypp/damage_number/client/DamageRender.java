package cc.xypp.damage_number.client;

import cc.xypp.damage_number.DamageNumber;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.gui.ForgeIngameGui;
import net.minecraftforge.client.gui.IIngameOverlay;
import cc.xypp.damage_number.Config;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Date;

public class DamageRender implements IIngameOverlay {
    private long shakeDiff = 0;
    private long confirmTime = 0;

    private int valTransform(int origin, int maxv) {
        if (origin < 0) {
            return origin + maxv;
        } else {
            return origin;
        }
    }

    @Override
    public void render(ForgeIngameGui gui, PoseStack poseStack, float partialTick, int screenWidth, int screenHeight) {
        if (!Config.showDamage) return;
        if (!Data.show) return;
        String titleContent = i18n("title.content");
        long titleColor = 0xFFFFFF;
        long damageColor = 0xFFFFFF;
        long comboColor = 0xFFFFFF;

        {//RANK OPT
            long RankColor = 0xFFFFFF;
            if (Config.damageRankEnabled) {
                long maxRankData = -1;
                for (Config.RankOptionItem item : Config.damageRankList) {
                    if (item.amount <= Data.amount && item.amount > maxRankData) {
                        maxRankData = item.amount;
                        titleContent = item.title;
                        RankColor = item.color;
                    }
                }
                if(Config.damageRankColorDamageNumber){
                    damageColor = RankColor;
                }
                if(Config.damageRankColorTitle){
                    titleColor = RankColor;
                }
                if(Config.damageRankColorCombo){
                    comboColor = RankColor;
                }
            }
            if (Config.comboRankEnabled) {
                long maxRankData = -1;
                for (Config.RankOptionItem item : Config.comboRankList) {
                    if (item.amount <= Data.combo && item.amount > maxRankData) {
                        maxRankData = item.amount;
                        titleContent = item.title;
                        RankColor = item.color;
                    }
                }
                if(Config.comboRankColorDamageNumber){
                    damageColor = RankColor;
                }
                if(Config.comboRankColorTitle){
                    titleColor = RankColor;
                }
                if(Config.comboRankColorCombo){
                    comboColor = RankColor;
                }
            }
        }//RANK OPT

        if(Config.titleShow){//TITLE Render
            float scale = (float) Config.titleScale;
            poseStack.pushPose();
            poseStack.scale(scale, scale, scale);
            int x = valTransform(Config.titleX, screenWidth);
            int y = valTransform(Config.titleY, screenHeight);
            x = (int) (x / scale);
            y = (int) (y / scale);
            GuiComponent.drawString(poseStack, gui.getFont(), titleContent, x, y, ((int) titleColor)|((int)(Config.titleOpacity*255)<<24));
            poseStack.popPose();
        }//TITLE Render
        if(Config.comboShow){//COMBO Render
            float scale = (float) Config.comboScale;
            poseStack.pushPose();
            poseStack.scale(scale, scale, scale);
            int x = valTransform(Config.comboX, screenWidth);
            int y = valTransform(Config.comboY, screenHeight);
            x = (int) (x / scale);
            y = (int) (y / scale);
            GuiComponent.drawString(poseStack, gui.getFont(), i18n("combo.content",  String.valueOf(Data.combo)), x, y, ((int)comboColor)|((int)(Config.comboOpacity*255)<<24));
            poseStack.popPose();
        }//COMBO Render
        if(Config.damageListShow){//List Render
            float scale = (float) Config.damageListScale;
            poseStack.pushPose();
            poseStack.scale(scale, scale, scale);
            int x = valTransform(Config.damageListX, screenWidth);
            int y = valTransform(Config.damageListY, screenHeight);
            int lh = gui.getFont().lineHeight;
            x = (int) (x / scale);
            y = (int) (y / scale);
            lh = (int) (lh / scale);
            long currentTime = new Date().getTime();
            while (Data.latest.size()>0 && Data.latest.get(0).getRight() < currentTime - 2000) {
                Data.latest.remove(0);
            }
            for (Pair<Float, Long> pair : Data.latest) {
                GuiComponent.drawString(poseStack, gui.getFont(), i18n("damage_list.content",String.format("%.1f",pair.getLeft())), x, y, 0xFFFFFF|((int)(Config.damageListOpacity*255)<<24));
                y += lh;
            }

            poseStack.popPose();
        }//List Render
        if(Config.numberShow){//Number Render
            float scale = (float) Config.numberScale;
            poseStack.pushPose();
            poseStack.scale(scale, scale, scale);
            int x = valTransform(Config.numberX, screenWidth);
            int y = valTransform(Config.numberY, screenHeight);
            x = (int) (x / scale);
            y = (int) (y / scale);
            if (Data.confirm) {
                if (confirmTime == 0) {
                    confirmTime = new Date().getTime();
                }
                if (confirmTime != 0 && new Date().getTime() - confirmTime > 1500) {
                    confirmTime = 0;
                    Data.confirm = false;
                    Data.show = false;
                }
            }

            if (Data.shakes > 0) {
                long time = new Date().getTime();
                if (shakeDiff == 0 || time - shakeDiff > 100) {
                    shakeDiff = time;
                    Data.shakes--;
                }
                if (Data.shakes % 2 == 0) {
                    x += 1;
                    y += 1;
                } else {
                    x -= 1;
                    y -= 1;
                }
            }
            GuiComponent.drawString(poseStack,
                    gui.getFont(),
                    i18n("number.content",String.format("%.1f",Data.amount)),
                    x,
                    y,
                    (Data.confirm ? 0xf9a825 : (int)damageColor)|((int)(Config.numberOpacity*255)<<24));
            poseStack.popPose();
        }//Number Render

    }

    private String i18n(String s, Object... args) {
        return I18n.get(String.valueOf(new ResourceLocation(DamageNumber.MODID, s)), args);
    }


}
