package cc.xypp.damage_number;

import cc.xypp.damage_number.client.ClientEvent;
import cc.xypp.damage_number.network.DamagePackage;
import cc.xypp.damage_number.server.ServerEvent;
import com.mojang.logging.LogUtils;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;
import cc.xypp.damage_number.network.Network;
// The value here should match an entry in the META-INF/mods.toml file
@Mod(DamageNumber.MODID)
public class DamageNumber
{
    public static final String MODID = "damage_number";
    public static final Path CONFIG_BASE_PATH = FMLPaths.CONFIGDIR.get();
    private static final Logger LOGGER = LogUtils.getLogger();
    public DamageNumber() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.register(this);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }
    @SubscribeEvent
    void FMLCommonSetupEvent(FMLCommonSetupEvent event) {
        Network.INSTANCE.registerMessage(0, DamagePackage.class, DamagePackage::toBytes, DamagePackage::new, null);
    }
}
