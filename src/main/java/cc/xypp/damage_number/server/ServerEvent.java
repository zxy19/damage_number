package cc.xypp.damage_number.server;

import cc.xypp.damage_number.DamageNumber;
import cc.xypp.damage_number.DamageTypeConfig;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

public class ServerEvent {
    @Mod.EventBusSubscriber(modid = DamageNumber.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
    public static class FORGE {
        @SubscribeEvent
        public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
            if (!(event.player instanceof ServerPlayer serverPlayer)) return;
            DamageProcessor.checkPlayerDamageTick(serverPlayer);
        }

        @SubscribeEvent(priority = EventPriority.LOWEST)
        public static void onDamagePost(LivingDamageEvent event) {
            Entity entity = event.getSource().getEntity();
            if (entity instanceof ServerPlayer serverPlayer) {
                DamageProcessor.process(serverPlayer, event.getSource(), event.getAmount());
            }
        }

        @SubscribeEvent
        static void LevelLoaded(PlayerEvent.PlayerLoggedInEvent event) {
            if (event.getPlayer().level.isClientSide()) return;
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
