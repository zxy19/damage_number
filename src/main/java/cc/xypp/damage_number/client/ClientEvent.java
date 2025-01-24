package cc.xypp.damage_number.client;

import cc.xypp.damage_number.Config;
import cc.xypp.damage_number.DamageNumber;
import cc.xypp.damage_number.network.DamagePackage;
import cc.xypp.damage_number.network.Network;
import cc.xypp.damage_number.screen.ConfigScreen;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import org.apache.commons.lang3.tuple.MutablePair;
import org.lwjgl.glfw.GLFW;

import java.util.Date;
import java.util.Objects;

public class ClientEvent {
    public static final Lazy<KeyMapping> KEY_MAPPING_LAZY = Lazy.of(() -> new KeyMapping(
            "key.damage_number.show_config",
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_UNKNOWN,
            "key.categories.misc"
    ));
    @Mod.EventBusSubscriber(modid = DamageNumber.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class MC {
        @SubscribeEvent
        public static void onRegisterKeyMapping(RegisterKeyMappingsEvent event) {
            event.register(KEY_MAPPING_LAZY.get());
        }
        @SubscribeEvent
        public static void FMLClientSetupEvent(FMLClientSetupEvent event) {
            Network.INSTANCE.registerMessage(0, DamagePackage.class, DamagePackage::toBytes, DamagePackage::new,
                    (msg, context) -> {
                        if (Objects.equals(msg.type, "emit")) {
                            Data.amount = msg.amount;
                            Data.shakes = 4;
                            Data.combo = msg.combo;
                            Data.latest.add(new MutablePair<>(msg.instant, new Date().getTime()));
                            while (Data.latest.size() != 0 && Data.latest.size() > Config.damageListMaxSize) {
                                Data.latest.remove(0);
                            }
                            Data.confirm = false;
                            Data.show = true;
                        } else if (Objects.equals(msg.type, "total")) {
                            Data.amount = msg.amount;
                            Data.confirm = true;
                            Data.show = true;
                        }
                        context.get().setPacketHandled(true);
                    });
        }

        @SubscribeEvent
        public static void ReloadConfigEvent(ModConfigEvent.Reloading event) {

        }
        @SubscribeEvent
        public static void RegisterGuiOverlaysEvent(RegisterGuiOverlaysEvent event) {
            DamageRender damageRender = new DamageRender();
            event.registerAboveAll("damage_number", damageRender);
        }

    }

    @Mod.EventBusSubscriber(modid = DamageNumber.MODID,value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
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
