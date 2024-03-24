package cc.xypp.damage_number.server;

import cc.xypp.damage_number.DamageNumber;
import cc.xypp.damage_number.network.DamagePackage;
import cc.xypp.damage_number.network.Network;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLDedicatedServerSetupEvent;
import net.minecraftforge.network.PacketDistributor;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ServerEvent {
    static Map<String, Float> userDamage = new HashMap<>();
    static Map<String, Long> keepUntil = new HashMap<>();
    static Map<String, Integer> damageCount = new HashMap<>();
    @Mod.EventBusSubscriber(modid = DamageNumber.MODID,bus = Mod.EventBusSubscriber.Bus.FORGE)
    public static class FORGE {
        @SubscribeEvent
        public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
            if (event.side != LogicalSide.SERVER) return;
            if (event.phase == TickEvent.Phase.START) {
                String uuid = event.player.getUUID().toString();
                if (keepUntil.containsKey(uuid)) {
                    if (keepUntil.get(uuid) < new Date().getTime()) {
                        Network.INSTANCE.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer) event.player),
                                new DamagePackage("total",
                                        userDamage.get(uuid),
                                        damageCount.get(uuid),
                                        0.0f,
                                        -1));
                        keepUntil.remove(uuid);
                        damageCount.remove(uuid);
                        userDamage.remove(uuid);
                    }
                }
            }
        }

        @SubscribeEvent(priority = EventPriority.LOWEST)
        public static void onDamagePost(LivingDamageEvent event) {
            Entity entity = event.getSource().getEntity();
            if (entity != null && entity.getType() == net.minecraft.world.entity.EntityType.PLAYER) {
                String uid = entity.getUUID().toString();
                damageCount.put(uid, damageCount.getOrDefault(uid, 0) + 1);
                userDamage.put(uid, event.getAmount() + userDamage.getOrDefault(uid, 0.0f));
                keepUntil.put(uid, new Date().getTime() + 3000);
                int shield = -1;
                if(ModList.get().isLoaded("battery_shield")){
                    cc.xypp.battery_shield.api.IDamageSourceA sourceA = (cc.xypp.battery_shield.api.IDamageSourceA)event.getSource();
                    if(sourceA.isByBatteryShield())
                        shield= sourceA.getShieldDamageType().ordinal();
                }
                Network.INSTANCE.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer) entity),
                        new DamagePackage("emit",
                                userDamage.get(uid),
                                damageCount.get(uid),
                                event.getAmount(),shield));
            }
        }
    }
}
