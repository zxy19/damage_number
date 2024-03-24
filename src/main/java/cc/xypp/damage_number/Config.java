package cc.xypp.damage_number;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.registries.ForgeRegistries;

// An example config class. This is not required, but it's a good idea to have one to keep your config organized.
// Demonstrates how to use Forge's config APIs
@Mod.EventBusSubscriber(modid = DamageNumber.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Config
{
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    private static final ForgeConfigSpec.BooleanValue SHOW_DAMAGE = BUILDER
            .translation("config.damage_number.show")
            .define("show", true);

    private static final ForgeConfigSpec.ConfigValue<Integer> NUMBER_X =  BUILDER
            .translation("config.damage_number.number.x")
            .define("number.x", -200);
    private static final ForgeConfigSpec.ConfigValue<Integer> NUMBER_Y =  BUILDER
            .translation("config.damage_number.number.y")
            .define("number.y", 100);
    private static final ForgeConfigSpec.ConfigValue<Float> NUMBER_SCALE =  BUILDER
            .translation("config.damage_number.number.scale")
            .define("number.scale", 2.5f);
    private static final ForgeConfigSpec.BooleanValue NUMBER_SHOW =  BUILDER
            .translation("config.damage_number.number.show")
            .define("number.show", true);

    private static final ForgeConfigSpec.ConfigValue<Integer> TITLE_X =  BUILDER
            .translation("config.damage_number.title.x")
            .define("title.x", -200);
    private static final ForgeConfigSpec.ConfigValue<Integer> TITLE_Y =  BUILDER
            .translation("config.damage_number.title.y")
            .define("title.y", 75);
    private static final ForgeConfigSpec.ConfigValue<Float> TITLE_SCALE =  BUILDER
            .translation("config.damage_number.title.scale")
            .define("title.scale", 1.8f);
    private static final ForgeConfigSpec.BooleanValue TITLE_SHOW =  BUILDER
            .translation("config.damage_number.title.show")
            .define("title.show", true);

    private static final ForgeConfigSpec.ConfigValue<Integer> COMBO_X =  BUILDER
            .translation("config.damage_number.combo.x")
            .define("combo.x", -210);
    private static final ForgeConfigSpec.ConfigValue<Integer> COMBO_Y =  BUILDER
            .translation("config.damage_number.combo.y")
            .define("combo.y", 125);
    private static final ForgeConfigSpec.ConfigValue<Float>  COMBO_SCALE = BUILDER
            .translation("config.damage_number.combo.scale")
            .define("combo.scale", 1.2f);
    private static final ForgeConfigSpec.BooleanValue COMBO_SHOW =  BUILDER
            .translation("config.damage_number.combo.show")
            .define("combo.show", true);

    private static final ForgeConfigSpec.ConfigValue<Integer> DAMAGE_LIST_X =  BUILDER
            .translation("config.damage_number.damage_list.x")
            .define("damage_list.x", -120);
    private static final ForgeConfigSpec.ConfigValue<Integer> DAMAGE_LIST_Y =  BUILDER
            .translation("config.damage_number.damage_list.y")
            .define("damage_list.y", 100);
    private static final ForgeConfigSpec.ConfigValue<Float>  DAMAGE_LIST_SCALE = BUILDER
            .translation("config.damage_number.damage_list.scale")
            .define("damage_list.scale", 0.8f);
    private static final ForgeConfigSpec.BooleanValue DAMAGE_LIST_SHOW =  BUILDER
            .translation("config.damage_number.damage_list.show")
            .define("damage_list.show", true);

    static final ForgeConfigSpec SPEC = BUILDER.build();

    public static boolean showDamage;
    public static int numberX;
    public static int numberY;
    public static float numberScale;
    public static boolean numberShow;
    public static int titleX;
    public static int titleY;
    public static float titleScale;
    public static boolean titleShow;
    public static int comboX;
    public static int comboY;
    public static float comboScale;
    public static boolean comboShow;
    public static int damageListX;
    public static int damageListY;
    public static float damageListScale;
    public static boolean damageListShow;

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event)
    {
        showDamage = SHOW_DAMAGE.get();
        numberX = NUMBER_X.get();
        numberY = NUMBER_Y.get();
        numberScale = NUMBER_SCALE.get();
        numberShow = NUMBER_SHOW.get();
        titleX = TITLE_X.get();
        titleY = TITLE_Y.get();
        titleScale = TITLE_SCALE.get();
        titleShow = TITLE_SHOW.get();
        comboX = COMBO_X.get();
        comboY = COMBO_Y.get();
        comboScale = COMBO_SCALE.get();
        comboShow = COMBO_SHOW.get();
        damageListX = DAMAGE_LIST_X.get();
        damageListY = DAMAGE_LIST_Y.get();
        damageListScale = DAMAGE_LIST_SCALE.get();
        damageListShow = DAMAGE_LIST_SHOW.get();
    }
}
