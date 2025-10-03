package cc.xypp.damage_number.server;

import cc.xypp.damage_number.Config;
import cc.xypp.damage_number.DamageNumber;
import cc.xypp.damage_number.DamageTypeConfig;
import cc.xypp.damage_number.network.DamagePackage;
import cc.xypp.damage_number.network.Network;
import net.minecraft.commands.Commands;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLDedicatedServerSetupEvent;

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
            if (Config.useMixinDamageGetter) return;
            Entity entity = event.getSource().getEntity();
            if (entity instanceof ServerPlayer serverPlayer) {
                DamageProcessor.process(serverPlayer, event.getSource(), event.getAmount());
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
                                        DamageProcessor.sendDamageClear(player);
                                        return 1;
                                    }))
            );
        }
    }

    @Mod.EventBusSubscriber(modid = DamageNumber.MODID, value = Dist.DEDICATED_SERVER, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class MC {
        @SubscribeEvent
        public static void FMLServerSetupEvent(FMLDedicatedServerSetupEvent event) {
            Network.INSTANCE.registerMessage(0, DamagePackage.class, DamagePackage::toBytes, DamagePackage::new, null);
        }
    }
}
