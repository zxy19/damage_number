package cc.xypp.damage_number.server;

import cc.xypp.damage_number.DamageNumber;
import cc.xypp.damage_number.DamageTypeConfig;
import cc.xypp.damage_number.network.Network;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.level.LevelEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.registries.RegisterEvent;
import org.jetbrains.annotations.Nullable;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ServerEvent {
    static Map<String, Float> userDamage = new HashMap<>();
    static Map<String, Long> keepUntil = new HashMap<>();
    static Map<String, Integer> damageCount = new HashMap<>();

    @EventBusSubscriber(modid = DamageNumber.MODID, bus = EventBusSubscriber.Bus.GAME)
    public static class FORGE {
        @SubscribeEvent
        public static void onPlayerTick(PlayerTickEvent.Pre event) {
            if (!(event.getEntity() instanceof ServerPlayer)) return;
            String uuid = event.getEntity().getUUID().toString();
            if (keepUntil.containsKey(uuid)) {
                if (keepUntil.get(uuid) < new Date().getTime()) {
                    Network.send((ServerPlayer) event.getEntity(), "total",
                            userDamage.get(uuid),
                            damageCount.get(uuid),
                            0.0f,
                            0);
                    keepUntil.remove(uuid);
                    damageCount.remove(uuid);
                    userDamage.remove(uuid);
                }
            }
        }

        @SubscribeEvent(priority = EventPriority.LOWEST)
        public static void onDamagePost(LivingDamageEvent.Post event) {
            Entity entity = event.getSource().getEntity();
            if (entity != null && entity.getType() == net.minecraft.world.entity.EntityType.PLAYER) {
                String uid = entity.getUUID().toString();
                damageCount.put(uid, damageCount.getOrDefault(uid, 0) + 1);
                userDamage.put(uid, event.getNewDamage() + userDamage.getOrDefault(uid, 0.0f));
                keepUntil.put(uid, new Date().getTime() + 3000);
                Network.send((ServerPlayer) entity,
                        "emit",
                        userDamage.get(uid),
                        damageCount.get(uid),
                        event.getNewDamage(),
                        DamageTypeConfig.getColorForDamageType(event.getSource())
                );
            }
        }

        @SubscribeEvent
        static void LevelLoaded(LevelEvent.Load event) {
            if (event.getLevel().isClientSide()) return;
            Registry<DamageType> registry = event.getLevel().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE);
            for (ResourceLocation typeKey : registry.keySet()) {
                DamageType type = registry.get(typeKey);
                registry.holders().forEach(holder -> {
                    holder.tags().forEach(tag -> {
                        DamageTypeConfig.addTagGroup(tag.location().toString());
                    });
                });
                DamageTypeConfig.addGroup(type.msgId());
            }
            DamageTypeConfig.register();
        }
    }
}
