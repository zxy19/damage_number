package cc.xypp.damage_number.client;

import cc.xypp.damage_number.DamageNumber;
import cc.xypp.damage_number.data.DamageListItem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.resources.ResourceLocation;
import cc.xypp.damage_number.Config;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;
import net.minecraftforge.fml.ModList;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Date;

public class DamageRender implements IGuiOverlay {
    private long shakeDiff = 0;
    private long confirmTime = 0;

    private int valTransform(int origin, int maxv) {
        if (origin < 0) {
            return origin + maxv;
        } else {
            return origin;
        }
    }


    private String i18n(String s, Object... args) {
        return I18n.get(String.valueOf(new ResourceLocation(DamageNumber.MODID, s)), args);
    }


    @Override
    public void render(ForgeGui gui, GuiGraphics guiGraphics, float partialTick, int screenWidth, int screenHeight) {
        this.render(guiGraphics, gui.getFont(), partialTick, screenWidth, screenHeight,false);
    }

    public void render(GuiGraphics guiGraphics, Font font, float partialTick, int screenWidth, int screenHeight,boolean alwaysShow){
        if (!Config.showDamage) return;
        if (!Data.show && !alwaysShow) return;
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
            guiGraphics.pose().pushPose();
            guiGraphics.pose().scale(scale, scale, scale);
            guiGraphics.setColor(1,1,1,Config.titleOpacity);
            int x = valTransform(Config.titleX, screenWidth);
            int y = valTransform(Config.titleY, screenHeight);
            x = (int) (x / scale);
            y = (int) (y / scale);
            guiGraphics.drawString(font, titleContent, x, y, (int)titleColor);
            guiGraphics.pose().popPose();
        }//TITLE Render
        if(Config.comboShow){//COMBO Render
            float scale = (float) Config.comboScale;
            guiGraphics.pose().pushPose();
            guiGraphics.pose().scale(scale, scale, scale);
            guiGraphics.setColor(1,1,1,Config.comboOpacity);
            int x = valTransform(Config.comboX, screenWidth);
            int y = valTransform(Config.comboY, screenHeight);
            x = (int) (x / scale);
            y = (int) (y / scale);
            guiGraphics.drawString(font, i18n("combo.content",  String.valueOf(Data.combo)), x, y, (int)comboColor);
            guiGraphics.pose().popPose();
        }//COMBO Render
        if(Config.damageListShow){//List Render
            float scale = (float) Config.damageListScale;
            guiGraphics.pose().pushPose();
            guiGraphics.pose().scale(scale, scale, scale);
            guiGraphics.setColor(1,1,1,Config.damageListOpacity);
            int x = valTransform(Config.damageListX, screenWidth);
            int y = valTransform(Config.damageListY, screenHeight);
            int lh = font.lineHeight;
            x = (int) (x / scale);
            y = (int) (y / scale);
            lh = (int) (lh / scale);
            long currentTime = new Date().getTime();
            while (Data.latest.size()>0 && Data.latest.get(0).getRight() < currentTime - 2000) {
                Data.latest.remove(0);
            }

            for (Pair<DamageListItem, Long> pair : Data.latest) {
                guiGraphics.drawString(font, i18n("damage_list.content",String.format("%.1f",pair.getLeft().amount)), x, y, (int) ((pair.getRight().getLeft()) | ((int) (Config.damageListOpacity * 255) << 24)));
                y += lh;
            }

            guiGraphics.pose().popPose();
        }//List Render
        if(Config.numberShow){//Number Render
            float scale = (float) Config.numberScale;
            guiGraphics.pose().pushPose();
            guiGraphics.pose().scale(scale, scale, scale);
            guiGraphics.setColor(1,1,1,Config.numberOpacity);
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
            guiGraphics.drawString(
                    font,
                    i18n("number.content",String.format("%.1f",Data.amount)),
                    x,
                    y,
                    Data.confirm ? 0xf9a825 : (int)damageColor);
            guiGraphics.pose().popPose();
        }//Number Render
        guiGraphics.setColor(1,1,1,1);
    }
}
