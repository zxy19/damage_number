package cc.xypp.damage_number.client;

import cc.xypp.damage_number.DamageNumber;
import cc.xypp.damage_number.network.DamagePackage;
import cc.xypp.damage_number.network.Network;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import org.apache.commons.lang3.tuple.MutablePair;

import java.util.Date;
import java.util.Objects;

public class ClientEvent {
    @Mod.EventBusSubscriber(modid = DamageNumber.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class MC {
        @SubscribeEvent
        public static void FMLClientSetupEvent(FMLClientSetupEvent event) {
            Network.INSTANCE.registerMessage(0, DamagePackage.class, DamagePackage::toBytes, DamagePackage::new,
                    (msg, context) -> {
                        if (Objects.equals(msg.type, "emit")) {
                            Data.amount = msg.amount;
                            Data.shakes = 4;
                            Data.combo = msg.combo;
                            Data.latest.add(new MutablePair<>(msg.instant, new Date().getTime()));
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
}
