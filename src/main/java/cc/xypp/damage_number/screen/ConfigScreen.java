package cc.xypp.damage_number.screen;

import cc.xypp.damage_number.Config;
import cc.xypp.damage_number.DamageNumber;
import cc.xypp.damage_number.client.DamageRender;
import cc.xypp.damage_number.client.Data;
import cc.xypp.damage_number.widget.GridLayout;
import cc.xypp.damage_number.widget.StringWidget;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.components.*;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.glfw.GLFW;

import javax.swing.*;
import java.util.Date;
@OnlyIn(Dist.CLIENT)
public class ConfigScreen extends Screen {
    private static final int BUTTON_WIDTH = 200;
    private static final int BUTTON_HEIGHT = 20;
    private static final int DONE_BUTTON_TOP_OFFSET = 20;
    private static final int CONFIG_PANE_WIDTH = 140;
    private static final int TITLE_HEIGHT = 40;

    private final DamageRender render;
    private GridLayout layout;
    private CycleButton<Component> selectKey;

    EditBox xEditBox;
    EditBox yEditBox;
    EditBox opacityEditBox;
    EditBox scaleEditBox;
    String currentKey = "";
    EditBox maxSizeEditBox;
    StringWidget maxSizeEditBoxLabel;

    public ConfigScreen() {
        // Use the super class' constructor to set the screen's title
        super(new TranslatableComponent("damage_number:config.title"));
        render = new DamageRender();
    }

    @Override
    protected void init() {
        super.init();
        this.addRenderableWidget(
                new Button((this.width - BUTTON_WIDTH) / 2,
                        this.height - DONE_BUTTON_TOP_OFFSET,
                        BUTTON_WIDTH,
                        BUTTON_HEIGHT,
                        new TranslatableComponent("gui.done"),
                        button -> this.onClose())
        );
        this.layout = new GridLayout(CONFIG_PANE_WIDTH, this.height - DONE_BUTTON_TOP_OFFSET);
        GridLayout.RowHelper row = this.layout.createRowHelper(2);
        this.addRenderableWidget(row.addChild(new StringWidget(new TranslatableComponent("damage_number:config.x"), this.font)));
        xEditBox = this.addRenderableWidget(row.addChild(new EditBox(this.font, 0, 0, CONFIG_PANE_WIDTH, BUTTON_HEIGHT, new TextComponent("x"))));
        this.addRenderableWidget(row.addChild(new StringWidget(new TranslatableComponent("damage_number:config.y"), this.font)));
        yEditBox = this.addRenderableWidget(row.addChild(new EditBox(this.font, 0, 0, CONFIG_PANE_WIDTH, BUTTON_HEIGHT, new TextComponent("y"))));
        this.addRenderableWidget(row.addChild(new StringWidget(new TranslatableComponent("damage_number:config.opacity"), this.font)));
        opacityEditBox = this.addRenderableWidget(row.addChild(new EditBox(this.font, 0, 0, CONFIG_PANE_WIDTH, BUTTON_HEIGHT, new TextComponent("opacity"))));
        this.addRenderableWidget(row.addChild(new StringWidget(new TranslatableComponent("damage_number:config.scale"), this.font)));
        scaleEditBox = this.addRenderableWidget(row.addChild(new EditBox(this.font, 0, 0, CONFIG_PANE_WIDTH, BUTTON_HEIGHT, new TextComponent("scale"))));
        this.addRenderableWidget(row.addChild(new StringWidget(new TextComponent(""), this.font)));
        selectKey = this.addRenderableWidget(row.addChild(
                new CycleButton.Builder<Component>(this::changeKey)
                        .withValues(
                                new TextComponent("number"),
                                new TextComponent("title"),
                                new TextComponent("combo"),
                                new TextComponent("list")
                        )
                        .create(0, 0, CONFIG_PANE_WIDTH, BUTTON_HEIGHT, new TranslatableComponent("damage_number:config.select_key"))
        ));
        selectKey.setValue(new TextComponent("number"));
        maxSizeEditBoxLabel = this.addRenderableWidget(row.addChild(new StringWidget(new TranslatableComponent("damage_number:config.max_size"), this.font)));
        maxSizeEditBox = this.addRenderableWidget(row.addChild(new EditBox(this.font, 0, 0, CONFIG_PANE_WIDTH, BUTTON_HEIGHT, new TextComponent("scale"))));
        maxSizeEditBox.visible = false;
        maxSizeEditBoxLabel.visible = false;
        this.layout.setX(0);
        this.layout.setY(TITLE_HEIGHT);
        this.layout.arrangeElements();
    }

    private Component changeKey(Component component) {
        return component;
    }

    @Override
    public void tick() {
        super.tick();
        if (!currentKey.equals(selectKey.getValue().getString())) {
            fillValues(selectKey.getValue().getString());
            currentKey = selectKey.getValue().getString();
        } else {
            readValues(currentKey);
        }
        long currentTime = new Date().getTime();
        while (Data.latest.size() < Config.damageListMaxSize) {
            Data.latest.add(new MutablePair<>(233.0f, currentTime));
        }
        while (Data.latest.size() > Config.damageListMaxSize) {
            Data.latest.remove(0);
        }
        Data.latest.forEach((pair) -> pair.getRight().setValue(currentTime));
    }


    @Override
    public void render(@NotNull PoseStack poseStack, int pMouseX, int pMouseY, float pPartialTick) {
        this.renderBackground(poseStack);
        GuiComponent.drawString(poseStack,this.font,
                I18n.get("damage_number:config.title"),
                this.width / 2 - this.font.width(I18n.get("damage_number:config.title")) / 2,
                10,
                0xFFFFFF);
        super.render(poseStack, pMouseX, pMouseY, pPartialTick);
        render.render(poseStack, this.font, pPartialTick, this.width, this.height, true);
    }

    @Override
    public void resize(Minecraft p_96575_, int p_96576_, int p_96577_) {
        super.resize(p_96575_, p_96576_, p_96577_);
        this.layout.arrangeElements();
    }


    public void fillValues(String key) {
        switch (key) {
            case "number" -> {
                xEditBox.setValue(String.valueOf(Config.numberX));
                yEditBox.setValue(String.valueOf(Config.numberY));
                opacityEditBox.setValue(String.valueOf(Config.numberOpacity));
                scaleEditBox.setValue(String.valueOf(Config.numberScale));
            }
            case "title" -> {
                xEditBox.setValue(String.valueOf(Config.titleX));
                yEditBox.setValue(String.valueOf(Config.titleY));
                opacityEditBox.setValue(String.valueOf(Config.titleOpacity));
                scaleEditBox.setValue(String.valueOf(Config.titleScale));
            }
            case "combo" -> {
                xEditBox.setValue(String.valueOf(Config.comboX));
                yEditBox.setValue(String.valueOf(Config.comboY));
                opacityEditBox.setValue(String.valueOf(Config.comboOpacity));
                scaleEditBox.setValue(String.valueOf(Config.comboScale));
            }
            case "list" -> {
                xEditBox.setValue(String.valueOf(Config.damageListX));
                yEditBox.setValue(String.valueOf(Config.damageListY));
                opacityEditBox.setValue(String.valueOf(Config.damageListOpacity));
                scaleEditBox.setValue(String.valueOf(Config.damageListScale));
                maxSizeEditBox.setValue(String.valueOf(Config.damageListMaxSize));
            }
            default -> {
            }
        }
        maxSizeEditBox.visible = key.equals("list");
        maxSizeEditBoxLabel.visible = key.equals("list");
    }

    private void readValues(String currentKey) {
        try {
            switch (currentKey) {
                case "number" -> {
                    Config.numberX = Integer.parseInt(xEditBox.getValue());
                    Config.numberY = Integer.parseInt(yEditBox.getValue());
                    Config.numberOpacity = Float.parseFloat(opacityEditBox.getValue());
                    Config.numberScale = Float.parseFloat(scaleEditBox.getValue());
                }
                case "title" -> {
                    Config.titleX = Integer.parseInt(xEditBox.getValue());
                    Config.titleY = Integer.parseInt(yEditBox.getValue());
                    Config.titleOpacity = Float.parseFloat(opacityEditBox.getValue());
                    Config.titleScale = Float.parseFloat(scaleEditBox.getValue());
                }
                case "combo" -> {
                    Config.comboX = Integer.parseInt(xEditBox.getValue());
                    Config.comboY = Integer.parseInt(yEditBox.getValue());
                    Config.comboOpacity = Float.parseFloat(opacityEditBox.getValue());
                    Config.comboScale = Float.parseFloat(scaleEditBox.getValue());
                }
                case "list" -> {
                    Config.damageListX = Integer.parseInt(xEditBox.getValue());
                    Config.damageListY = Integer.parseInt(yEditBox.getValue());
                    Config.damageListOpacity = Float.parseFloat(opacityEditBox.getValue());
                    Config.damageListScale = Float.parseFloat(scaleEditBox.getValue());
                    Config.damageListMaxSize = Integer.parseInt(maxSizeEditBox.getValue());
                }
                default -> {
                }
            }
        } catch (NumberFormatException ignored) {
        }
    }

    @Override
    public void onClose() {
        Config.save();
        super.onClose();
    }


    @Override
    public boolean keyPressed(int pKeyCode, int pScanCode, int pModifiers) {
        int vd = 0, hd = 0;
        if (pKeyCode == GLFW.GLFW_KEY_UP) vd = 1;
        if (pKeyCode == GLFW.GLFW_KEY_DOWN) vd = -1;
        if (pKeyCode == GLFW.GLFW_KEY_LEFT) hd = -1;
        if (pKeyCode == GLFW.GLFW_KEY_RIGHT) hd = 1;
        float oval_f;
        int oval_i;
        if (pModifiers == GLFW.GLFW_MOD_CONTROL) {
            if (vd != 0) {
                try {
                    oval_f = Float.parseFloat(scaleEditBox.getValue());
                } catch (NumberFormatException ignored) {
                    oval_f = 1.0f;
                }
                scaleEditBox.setValue(String.format("%.2f", oval_f + 0.1f * vd));
                return true;
            }

            if (hd != 0) {
                try {
                    oval_f = Float.parseFloat(opacityEditBox.getValue());
                } catch (NumberFormatException ignored) {
                    oval_f = 1.0f;
                }
                opacityEditBox.setValue(String.format("%.2f", oval_f + 0.1f * hd));
                return true;
            }
        } else {
            if (vd != 0) {
                try {
                    oval_i = Integer.parseInt(yEditBox.getValue());
                } catch (NumberFormatException ignored) {
                    oval_i = 0;
                }
                yEditBox.setValue(String.valueOf(oval_i - vd));
                return true;
            }
            if (hd != 0) {
                try {
                    oval_i = Integer.parseInt(xEditBox.getValue());
                } catch (NumberFormatException ignored) {
                    oval_i = 0;
                }
                xEditBox.setValue(String.valueOf(oval_i + hd));
                return true;
            }
        }
        return super.keyPressed(pKeyCode, pScanCode, pModifiers);
    }
}
