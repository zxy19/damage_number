package cc.xypp.damage_number.client;

import cc.xypp.damage_number.DamageNumber;
import cc.xypp.damage_number.data.DamageListItem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
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
        if (!Config.showDamage) return;
        if (!Data.show) return;
        {//TITLE Render
            float scale = (float) Config.titleScale;
            guiGraphics.pose().pushPose();
            guiGraphics.pose().scale(scale, scale, scale);
            int x = valTransform(Config.titleX, screenWidth);
            int y = valTransform(Config.titleY, screenHeight);
            x = (int) (x / scale);
            y = (int) (y / scale);
            guiGraphics.drawString(gui.getFont(), i18n("title.content"), x, y, 0xFFFFFF);
            guiGraphics.pose().popPose();
        }//TITLE Render
        {//COMBO Render
            float scale = (float) Config.comboScale;
            guiGraphics.pose().pushPose();
            guiGraphics.pose().scale(scale, scale, scale);
            int x = valTransform(Config.comboX, screenWidth);
            int y = valTransform(Config.comboY, screenHeight);
            x = (int) (x / scale);
            y = (int) (y / scale);
            guiGraphics.drawString(gui.getFont(), i18n("combo.content",  String.valueOf(Data.combo)), x, y, 0xFFFFFF);
            guiGraphics.pose().popPose();
        }//COMBO Render
        {//List Render
            float scale = (float) Config.damageListScale;
            guiGraphics.pose().pushPose();
            guiGraphics.pose().scale(scale, scale, scale);
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
            for (Pair<DamageListItem, Long> pair : Data.latest) {
                if(pair.getLeft().shield!=-1){
                    cc.xypp.battery_shield.data.DamageNumberType damageNumberType = cc.xypp.battery_shield.data.DamageNumberType.values()[pair.getLeft().shield];
                    guiGraphics.drawString(gui.getFont(), i18n("damage_list.content",String.format("%.1f",pair.getLeft().amount)), x+20, y, cc.xypp.battery_shield.utils.ShieldUtil.getColor(damageNumberType));
                    cc.xypp.battery_shield.utils.ShieldUtil.getIconByType(cc.xypp.battery_shield.data.DamageNumberType.values()[pair.getLeft().shield]).blit(guiGraphics,x,y);
                }else{
                    guiGraphics.drawString(gui.getFont(), i18n("damage_list.content",String.format("%.1f",pair.getLeft().amount)), x, y, 0xffffffff);
                }
                y += lh;
            }

            guiGraphics.pose().popPose();
        }//List Render
        {//Number Render
            float scale = (float) Config.numberScale;
            guiGraphics.pose().pushPose();
            guiGraphics.pose().scale(scale, scale, scale);
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
                    gui.getFont(),
                    i18n("number.content",String.format("%.1f",Data.amount)),
                    x,
                    y,
                    Data.confirm ? 0xf9a825 : 0xFFFFFF);
            guiGraphics.pose().popPose();
        }//Number Render
    }
}
