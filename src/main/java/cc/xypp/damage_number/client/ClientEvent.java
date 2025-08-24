package cc.xypp.damage_number.client;

import cc.xypp.damage_number.Config;
import cc.xypp.damage_number.DamageNumber;
import cc.xypp.damage_number.DamageTypeConfig;
import cc.xypp.damage_number.screen.ConfigScreen;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.DebugScreenOverlay;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.client.event.*;
import net.neoforged.neoforge.common.util.Lazy;
import org.lwjgl.glfw.GLFW;

public class ClientEvent {
    public static final Lazy<KeyMapping> KEY_MAPPING_LAZY = Lazy.of(() -> new KeyMapping(
            "key.damage_number.show_config",
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_UNKNOWN,
            "key.categories.misc"
    ));

    @EventBusSubscriber(modid = DamageNumber.MODID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
    public static class MC {
        @SubscribeEvent
        public static void onRegisterKeyMapping(RegisterKeyMappingsEvent event) {
            event.register(KEY_MAPPING_LAZY.get());
        }

        @SubscribeEvent
        public static void ReloadConfigEvent(ModConfigEvent.Reloading event) {

        }

        @SubscribeEvent
        public static void RegisterGuiOverlaysEvent(RegisterGuiLayersEvent event) {
            DamageRender damageRender = new DamageRender();
            event.registerAboveAll(ResourceLocation.fromNamespaceAndPath(DamageNumber.MODID, "damage_number"), damageRender);
        }
    }

    @EventBusSubscriber(modid = DamageNumber.MODID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.GAME)
    public static class FM {
        @SubscribeEvent
        public static void keyInput(InputEvent.Key event) {
            while (KEY_MAPPING_LAZY.get().consumeClick()) {
                Minecraft.getInstance().setScreen(new ConfigScreen());
            }
        }

        @SubscribeEvent
        public static void debug(CustomizeGuiOverlayEvent.DebugText event) {
            if (Config.damageListShow)
                event.getLeft().add("Last Damage Number Color Type:" + DamageTypeConfig.lastMatching);
        }
    }
}
