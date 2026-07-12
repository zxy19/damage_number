package cc.xypp.damage_number.server;

import cc.xypp.damage_number.Config;
import cc.xypp.damage_number.DamageNumber;
import cc.xypp.damage_number.DamageTypeConfig;
import net.minecraft.commands.Commands;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
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

public class ServerEvent {

    @EventBusSubscriber(modid = DamageNumber.MODID)
    public static class FORGE {
        @SubscribeEvent
        public static void onPlayerTick(PlayerTickEvent.Pre event) {
            if (!(event.getEntity() instanceof ServerPlayer serverPlayer)) return;
            DamageProcessor.checkPlayerDamageTick(serverPlayer);
        }

        @SubscribeEvent(priority = EventPriority.LOWEST)
        public static void onDamagePost(LivingDamageEvent.Post event) {
            if (Config.useMixinDamageGetter) return;
            Entity entity = event.getSource().getEntity();
            if (entity instanceof ServerPlayer serverPlayer) {
                DamageProcessor.process(serverPlayer, event.getSource(), event.getNewDamage());

            }
        }

        @SubscribeEvent
        static void LevelLoaded(LevelEvent.Load event) {
            if (event.getLevel().isClientSide()) return;
            Registry<DamageType> registry = event.getLevel().registryAccess().lookupOrThrow(Registries.DAMAGE_TYPE);
            registry.listElements().forEach(holder -> {
                holder.tags().forEach(tag -> {
                    DamageTypeConfig.addTagGroup(tag.location().toString());
                });
                DamageTypeConfig.addGroup(holder.value().msgId());
            });
            DamageTypeConfig.register();
        }

        @SubscribeEvent
        public static void onRegisterCommand(RegisterCommandsEvent event) {
            event.getDispatcher().register(
                    Commands.literal("damage_number")
                            .then(Commands.literal("clear")
                                    .executes(t -> {
                                        ServerPlayer player = t.getSource().getPlayerOrException();
                                        DamageProcessor.sendDamageClear(player);
                                        return 1;
                                    }))
            );
        }
    }
}
