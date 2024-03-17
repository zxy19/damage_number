package cc.xypp.damage_number.client;

import cc.xypp.damage_number.DamageNumber;
import cc.xypp.damage_number.data.DamageListItem;
import cc.xypp.damage_number.network.DamagePackage;
import cc.xypp.damage_number.network.Network;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.gui.OverlayRegistry;
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
                            DamageListItem listItem = new DamageListItem(msg.instant, msg.shield);
                            Data.latest.add(new MutablePair<>(listItem, new Date().getTime()));
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
}
