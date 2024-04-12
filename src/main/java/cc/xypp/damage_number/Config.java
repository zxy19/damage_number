package cc.xypp.damage_number;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static cc.xypp.damage_number.utils.Colors.toColor;


@Mod.EventBusSubscriber(modid = DamageNumber.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Config
{
    public static class RankOptionItem{
        public String title;
        public long color;
        public long amount;
        public RankOptionItem(String title, long color, long amount){
            this.title = title;
            this.color = color;
            this.amount = amount;
        }
    }
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

    private static final ForgeConfigSpec.BooleanValue DAMAGE_RANK_USE =  BUILDER
            .comment("Use damage ranked style")
            .translation("config.damage_number.damage_rank.use")
            .define("damage_rank.enable", false);
    private static final ForgeConfigSpec.BooleanValue DAMAGE_RANK_COLOR_NUMBER =  BUILDER
            .comment("Color the damage number")
            .translation("config.damage_number.damage_rank.color_number")
            .define("damage_rank.color.number", true);
    private static final ForgeConfigSpec.BooleanValue DAMAGE_RANK_COLOR_TITLE =  BUILDER
            .comment("Color the title")
            .translation("config.damage_number.damage_rank.color_title")
            .define("damage_rank.color.title", true);
    private static final ForgeConfigSpec.BooleanValue DAMAGE_RANK_COLOR_COMBO =  BUILDER
            .comment("Color the title")
            .translation("config.damage_number.damage_rank.color_combo")
            .define("damage_rank.color.combo", true);
    private static final ForgeConfigSpec.ConfigValue<List<String>> DAMAGE_RANK_OPT = BUILDER
            .comment("Use format like \"<damage amount>|<rank name>|<render color>\"")
            .translation("config.damage_number.damage_rank.option")
            .define("damage_rank.option",
                    List.of("0|D|#ffffff", "20|C|#e6fffb", "50|B|#bae7ff", "100|A|#bae637", "200|S|#fff566", "400|SS|#ffbb96", "800|SSS|#ffa39e")
                    ,Config::validateColorTitle);


    private static final ForgeConfigSpec.BooleanValue COMBO_RANK_USE =  BUILDER
            .comment("Use combo ranked style")
            .translation("config.damage_number.damage_rank.use")
            .define("combo_rank.enable", false);
    private static final ForgeConfigSpec.BooleanValue COMBO_RANK_COLOR_NUMBER =  BUILDER
            .comment("Color the damage number")
            .translation("config.damage_number.damage_rank.color_number")
            .define("combo_rank.color.number", true);
    private static final ForgeConfigSpec.BooleanValue COMBO_RANK_COLOR_TITLE =  BUILDER
            .comment("Color the title")
            .translation("config.damage_number.damage_rank.color_title")
            .define("combo_rank.color.title", true);
    private static final ForgeConfigSpec.BooleanValue COMBO_RANK_COLOR_COMBO =  BUILDER
            .comment("Color the combo")
            .translation("config.damage_number.damage_rank.color_combo")
            .define("combo_rank.color.combo", true);
    private static final ForgeConfigSpec.ConfigValue<List<String>> COMBO_RANK_OPT = BUILDER
            .comment("Use format like \"<combo>|<rank name>|<render color>\"")
            .translation("config.damage_number.damage_rank.option")
            .define("combo_rank.option",
                    List.of("0|D|#ffffff", "5|C|#e6fffb", "10|B|#bae7ff", "20|A|#bae637", "40|S|#fff566", "80|SS|#ffbb96", "160|SSS|#ffa39e")
                    ,Config::validateColorTitle);

    static final ForgeConfigSpec SPEC = BUILDER.build();

    public static boolean showDamage;
    //Damage Number Options
    public static int numberX;
    public static int numberY;
    public static float numberScale;
    public static boolean numberShow;

    //Title Options
    public static int titleX;
    public static int titleY;
    public static float titleScale;
    public static boolean titleShow;

    //Combo Options
    public static int comboX;
    public static int comboY;
    public static float comboScale;
    public static boolean comboShow;

    //Damage List Options
    public static int damageListX;
    public static int damageListY;
    public static float damageListScale;
    public static boolean damageListShow;

    //Damage Rank Options
    public static List<RankOptionItem> damageRankList = new ArrayList<>();
    public static boolean damageRankEnabled;
    public static boolean damageRankColorDamageNumber;
    public static boolean damageRankColorTitle;
    public static boolean damageRankColorCombo;

    //Combo Rank Options
    public static List<RankOptionItem> comboRankList = new ArrayList<>();
    public static boolean comboRankEnabled;
    public static boolean comboRankColorDamageNumber;
    public static boolean comboRankColorTitle;
    public static boolean comboRankColorCombo;


    @SuppressWarnings("unchecked")
    private static boolean validateColorTitle(final Object obj)
    {
        if(! (obj instanceof List))
            return false;
        List<String> list = (List<String>) obj;
        for(String item : list){
            if(item.split("\\|").length<2)
                return false;
        }
        return true;
    }

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
        damageRankEnabled = DAMAGE_RANK_USE.get();
        damageRankList = DAMAGE_RANK_OPT.get().stream().map(item->{
            String[] items = item.split("\\|");
            return new RankOptionItem(items[1], toColor(items[2]), Long.parseLong(items[0]));
        }).collect(Collectors.toList());
        damageRankColorDamageNumber = DAMAGE_RANK_COLOR_NUMBER.get();
        damageRankColorTitle = DAMAGE_RANK_COLOR_TITLE.get();
        damageRankColorCombo = DAMAGE_RANK_COLOR_COMBO.get();
        comboRankEnabled = COMBO_RANK_USE.get();
        comboRankList = COMBO_RANK_OPT.get().stream().map(item->{
            String[] items = item.split("\\|");
            return new RankOptionItem(items[1], toColor(items[2]), Long.parseLong(items[0]));
        }).collect(Collectors.toList());
        comboRankColorDamageNumber = COMBO_RANK_COLOR_NUMBER.get();
        comboRankColorTitle = COMBO_RANK_COLOR_TITLE.get();
        comboRankColorCombo = COMBO_RANK_COLOR_COMBO.get();

    }
}
