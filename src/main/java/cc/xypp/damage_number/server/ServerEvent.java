package cc.xypp.damage_number.server;

import cc.xypp.damage_number.Config;
import cc.xypp.damage_number.DamageNumber;
import cc.xypp.damage_number.DamageTypeConfig;
import cc.xypp.damage_number.network.Network;
import net.minecraft.commands.Commands;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.level.LevelEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

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
                keepUntil.put(uid, new Date().getTime() + Config.clearTime);
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

        @SubscribeEvent
        public static void onRegisterCommand(RegisterCommandsEvent event) {
            event.getDispatcher().register(
                    Commands.literal("damage_number")
                            .then(Commands.literal("clear")
                                    .executes(t -> {
                                        ServerPlayer player = t.getSource().getPlayerOrException();
                                        if (keepUntil.containsKey(player.getUUID().toString())) {
                                            keepUntil.put(player.getUUID().toString(), new Date().getTime());
                                            return 0;
                                        }
                                        return 1;
                                    }))
            );
        }
    }
}
