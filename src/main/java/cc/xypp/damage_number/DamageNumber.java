package cc.xypp.damage_number;

import com.mojang.logging.LogUtils;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import org.slf4j.Logger;
import cc.xypp.damage_number.network.Network;
// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(DamageNumber.MODID)
public class DamageNumber
{
    public static final String MODID = "damage_number";
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
