package cc.xypp.damage_number;

import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.neoforged.fml.loading.FMLPaths;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.level.LevelEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.registries.RegisterEvent;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import cc.xypp.damage_number.network.Network;

import java.nio.file.Path;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(DamageNumber.MODID)
public class DamageNumber
{

    public static final String MODID = "damage_number";
    public static final Path CONFIG_BASE_PATH = FMLPaths.CONFIGDIR.get();
    private static final Logger LOGGER = LogUtils.getLogger();
    public DamageNumber() {
        ModLoadingContext.get().getActiveContainer().registerConfig(ModConfig.Type.COMMON, Config.SPEC);
        ModLoadingContext.get().getActiveContainer().getEventBus().register(this);
    }
    @SubscribeEvent
    void FMLCommonSetupEvent(RegisterPayloadHandlersEvent event) {
        Network.register(event);
    }


}
