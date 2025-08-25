package cc.xypp.damage_number.client;

import cc.xypp.damage_number.Config;
import cc.xypp.damage_number.DamageNumber;
import cc.xypp.damage_number.DamageTypeConfig;
import cc.xypp.damage_number.network.DamagePackage;
import cc.xypp.damage_number.network.Network;
import cc.xypp.damage_number.screen.ConfigScreen;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.ClientRegistry;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.gui.OverlayRegistry;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import org.apache.commons.lang3.tuple.MutablePair;
import org.lwjgl.glfw.GLFW;
import oshi.util.tuples.Pair;

import java.util.Date;
import java.util.Objects;

public class ClientEvent {
    public static final KeyMapping KEY_MAPPING_LAZY = new KeyMapping(
            "key.damage_number.show_config",
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_UNKNOWN,
            "key.categories.misc"
    );
    @Mod.EventBusSubscriber(modid = DamageNumber.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class MC {
        @SubscribeEvent
        public static void FMLClientSetupEvent(FMLClientSetupEvent event) {
            ClientRegistry.registerKeyBinding(KEY_MAPPING_LAZY);
            Network.INSTANCE.registerMessage(0, DamagePackage.class, DamagePackage::toBytes, DamagePackage::new,
                    (msg, context) -> {
                        if (Objects.equals(msg.type, "emit")) {
                            Data.amount = msg.amount;
                            Data.shakes = 4;
                            Data.combo = msg.combo;
                            Data.latest.add(new Pair<>(new Date().getTime(), msg.data));
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
            DamageRender damageRender = new DamageRender();
            OverlayRegistry.registerOverlayTop("DamageNumber", damageRender);
            OverlayRegistry.enableOverlay(damageRender,true);
        }
    }

    @Mod.EventBusSubscriber(modid = DamageNumber.MODID,value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
    public static class FM {
        @SubscribeEvent
        public static void keyInput(InputEvent event) {
            while (KEY_MAPPING_LAZY.consumeClick()) {
                Minecraft.getInstance().setScreen(new ConfigScreen());
            }
        }

//        @SubscribeEvent//TODO
//        public static void debug(CustomizeGuiOverlayEvent.DebugText event) {
//            if (Config.damageListShow && Minecraft.getInstance().setting)
//                event.getLeft().add("Last Damage Number Color Type:" + DamageTypeConfig.lastMatching);
//        }
    }
}
