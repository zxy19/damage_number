package cc.xypp.damage_number.client;

import cc.xypp.damage_number.Config;
import cc.xypp.damage_number.DamageNumber;
import cc.xypp.damage_number.DamageTypeConfig;
import cc.xypp.damage_number.screen.ConfigScreen;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.Identifier;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.gui.GuiLayer;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;
import net.neoforged.neoforge.client.settings.KeyConflictContext;
import net.neoforged.neoforge.client.settings.KeyModifier;
import org.lwjgl.glfw.GLFW;

public class ClientEvent {
    public static final KeyMapping.Category CATEGORY = new KeyMapping.Category(
            Identifier.fromNamespaceAndPath(DamageNumber.MODID, "main")
    );

    public static final KeyMapping KEY_MAPPING = new KeyMapping(
            "key.damage_number.show_config",
            KeyConflictContext.IN_GAME,
            KeyModifier.NONE,
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_UNKNOWN,
            CATEGORY
    );

    @EventBusSubscriber(modid = DamageNumber.MODID, value = Dist.CLIENT)
    public static class MC {
        @SubscribeEvent
        public static void onRegisterKeyMapping(RegisterKeyMappingsEvent event) {
            event.registerCategory(CATEGORY);
            event.register(KEY_MAPPING);
        }

        @SubscribeEvent
        public static void ReloadConfigEvent(ModConfigEvent.Reloading event) {

        }

        @SubscribeEvent
        public static void RegisterGuiOverlaysEvent(RegisterGuiLayersEvent event) {
            DamageRender damageRender = new DamageRender();
            event.registerAbove(VanillaGuiLayers.HOTBAR, Identifier.fromNamespaceAndPath(DamageNumber.MODID, "damage_number"), damageRender);
        }
    }

    @EventBusSubscriber(modid = DamageNumber.MODID, value = Dist.CLIENT)
    public static class FM {
        @SubscribeEvent
        public static void keyInput(InputEvent.Key event) {
            while (KEY_MAPPING.consumeClick()) {
                Minecraft.getInstance().setScreen(new ConfigScreen());
            }
        }
    }
}
