package cc.xypp.damage_number;


import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static cc.xypp.damage_number.utils.Colors.toColor;


@EventBusSubscriber(modid = DamageNumber.MODID, bus = EventBusSubscriber.Bus.MOD)
public class Config {

    public static class RankOptionItem {
        public String title;
        public long color;
        public long amount;

        public RankOptionItem(String title, long color, long amount) {
            this.title = title;
            this.color = color;
            this.amount = amount;
        }
    }

    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    private static final ModConfigSpec.BooleanValue SHOW_DAMAGE = BUILDER
            .translation("config.damage_number.show")
            .define("show", true);
    private static final ModConfigSpec.BooleanValue NO_SHAKE = BUILDER
            .translation("config.damage_number.no_shake")
            .define("no_shake", false);
    private static final ModConfigSpec.IntValue CLEAR_TIME = BUILDER
            .translation("config.damage_number.clear_time")
            .comment("How long will the damage accumulation being cleared. This option is decided by server.")
            .defineInRange("clear_time", 3000, 0, Integer.MAX_VALUE);

    private static final ModConfigSpec.ConfigValue<Integer> NUMBER_X = BUILDER
            .translation("config.damage_number.number.x")
            .define("number.x", -200);
    private static final ModConfigSpec.ConfigValue<Integer> NUMBER_Y = BUILDER
            .translation("config.damage_number.number.y")
            .define("number.y", 100);
    private static final ModConfigSpec.DoubleValue NUMBER_SCALE = BUILDER
            .translation("config.damage_number.number.scale")
            .defineInRange("number.scale", 2.5, 0.0, 10.0);
    private static final ModConfigSpec.DoubleValue NUMBER_OPACITY = BUILDER
            .translation("config.damage_number.number.opacity")
            .defineInRange("number.opacity", 1.0, 0.0, 1.0);
    private static final ModConfigSpec.BooleanValue NUMBER_SHOW = BUILDER
            .translation("config.damage_number.number.show")
            .define("number.show", true);

    private static final ModConfigSpec.ConfigValue<Integer> TITLE_X = BUILDER
            .translation("config.damage_number.title.x")
            .define("title.x", -200);
    private static final ModConfigSpec.ConfigValue<Integer> TITLE_Y = BUILDER
            .translation("config.damage_number.title.y")
            .define("title.y", 75);
    private static final ModConfigSpec.DoubleValue TITLE_SCALE = BUILDER
            .translation("config.damage_number.title.scale")
            .defineInRange("title.scale", 1.8, 0.0, 10.0);
    private static final ModConfigSpec.DoubleValue TITLE_OPACITY = BUILDER
            .translation("config.damage_number.title.opacity")
            .defineInRange("title.opacity", 1.0, 0.0, 1.0);
    private static final ModConfigSpec.BooleanValue TITLE_SHOW = BUILDER
            .translation("config.damage_number.title.show")
            .define("title.show", true);

    private static final ModConfigSpec.ConfigValue<Integer> COMBO_X = BUILDER
            .translation("config.damage_number.combo.x")
            .define("combo.x", -210);
    private static final ModConfigSpec.ConfigValue<Integer> COMBO_Y = BUILDER
            .translation("config.damage_number.combo.y")
            .define("combo.y", 125);
    private static final ModConfigSpec.DoubleValue COMBO_OPACITY = BUILDER
            .translation("config.damage_number.combo.opacity")
            .defineInRange("combo.opacity", 1.0, 0.0, 1.0);
    private static final ModConfigSpec.DoubleValue COMBO_SCALE = BUILDER
            .translation("config.damage_number.combo.scale")
            .defineInRange("combo.scale", 1.2, 0.0, 10.0);
    private static final ModConfigSpec.BooleanValue COMBO_SHOW = BUILDER
            .translation("config.damage_number.combo.show")
            .define("combo.show", true);

    private static final ModConfigSpec.ConfigValue<Integer> DAMAGE_LIST_X = BUILDER
            .translation("config.damage_number.damage_list.x")
            .define("damage_list.x", -120);
    private static final ModConfigSpec.ConfigValue<Integer> DAMAGE_LIST_Y = BUILDER
            .translation("config.damage_number.damage_list.y")
            .define("damage_list.y", 100);
    private static final ModConfigSpec.DoubleValue DAMAGE_LIST_OPACITY = BUILDER
            .translation("config.damage_number.damage_list.opacity")
            .defineInRange("damage_list.opacity", 1.0, 0.0, 1.0);
    private static final ModConfigSpec.DoubleValue DAMAGE_LIST_SCALE = BUILDER
            .translation("config.damage_number.damage_list.scale")
            .defineInRange("damage_list.scale", 0.8, 0.0, 10.0);
    private static final ModConfigSpec.ConfigValue<Integer> DAMAGE_LIST_MAX_SIZE = BUILDER
            .translation("config.damage_number.damage_list.max_size")
            .define("damage_list.max_size", 100);
    private static final ModConfigSpec.BooleanValue DAMAGE_LIST_SHOW = BUILDER
            .translation("config.damage_number.damage_list.show")
            .define("damage_list.show", true);
    private static final ModConfigSpec.BooleanValue DAMAGE_LIST_COLOR_TYPE = BUILDER
            .translation("config.damage_number.damage_list.color_type")
            .comment("Color the damage number by damage type. Configure in damage_number_type.toml")
            .define("damage_list.color_type", true);
    private static final ModConfigSpec.IntValue DAMAGE_LIST_CLEAR_TIME = BUILDER
            .translation("config.damage_number.damage_list.clear_time")
            .comment("How long will number in list disappears")
            .defineInRange("damage_list.clear_time", 2000, 0, Integer.MAX_VALUE);

    private static final ModConfigSpec.BooleanValue DAMAGE_RANK_USE = BUILDER
            .comment("Use damage ranked color")
            .translation("config.damage_number.damage_rank.use")
            .define("damage_rank.enable", false);
    private static final ModConfigSpec.BooleanValue DAMAGE_RANK_COLOR_NUMBER = BUILDER
            .comment("Color the damage number")
            .translation("config.damage_number.damage_rank.color_number")
            .define("damage_rank.color.number", true);
    private static final ModConfigSpec.BooleanValue DAMAGE_RANK_COLOR_TITLE = BUILDER
            .comment("Color the title")
            .translation("config.damage_number.damage_rank.color_title")
            .define("damage_rank.color.title", true);
    private static final ModConfigSpec.BooleanValue DAMAGE_RANK_COLOR_COMBO = BUILDER
            .comment("Color the title")
            .translation("config.damage_number.damage_rank.color_combo")
            .define("damage_rank.color.combo", true);
    private static final ModConfigSpec.ConfigValue<List<String>> DAMAGE_RANK_OPT = BUILDER
            .comment("Use format like \"<damage amount>|<rank name>|<render color>\"")
            .translation("config.damage_number.damage_rank.option")
            .define("damage_rank.option",
                    List.of("0|D|#ffffff", "20|C|#e6fffb", "50|B|#bae7ff", "100|A|#bae637", "200|S|#fff566", "400|SS|#ffbb96", "800|SSS|#ffa39e")
                    , Config::validateColorTitle);


    private static final ModConfigSpec.BooleanValue COMBO_RANK_USE = BUILDER
            .comment("Use combo ranked color")
            .translation("config.damage_number.damage_rank.use")
            .define("combo_rank.enable", false);
    private static final ModConfigSpec.BooleanValue COMBO_RANK_COLOR_NUMBER = BUILDER
            .comment("Color the damage number")
            .translation("config.damage_number.damage_rank.color_number")
            .define("combo_rank.color.number", true);
    private static final ModConfigSpec.BooleanValue COMBO_RANK_COLOR_TITLE = BUILDER
            .comment("Color the title")
            .translation("config.damage_number.damage_rank.color_title")
            .define("combo_rank.color.title", true);
    private static final ModConfigSpec.BooleanValue COMBO_RANK_COLOR_COMBO = BUILDER
            .comment("Color the combo")
            .translation("config.damage_number.damage_rank.color_combo")
            .define("combo_rank.color.combo", true);
    private static final ModConfigSpec.ConfigValue<List<String>> COMBO_RANK_OPT = BUILDER
            .comment("Use format like \"<combo>|<rank name>|<render color>\"")
            .translation("config.damage_number.damage_rank.option")
            .define("combo_rank.option",
                    List.of("0|D|#ffffff", "5|C|#e6fffb", "10|B|#bae7ff", "20|A|#bae637", "40|S|#fff566", "80|SS|#ffbb96", "160|SSS|#ffa39e")
                    , Config::validateColorTitle);

    static final ModConfigSpec SPEC = BUILDER.build();

    //misc
    public static boolean showDamage;
    public static boolean noShake;
    public static int clearTime;
    //Damage Number Options
    public static int numberX;
    public static int numberY;
    public static float numberScale;
    public static float numberOpacity;
    public static boolean numberShow;

    //Title Options
    public static int titleX;
    public static int titleY;
    public static float titleScale;
    public static float titleOpacity;
    public static boolean titleShow;

    //Combo Options
    public static int comboX;
    public static int comboY;
    public static float comboScale;
    public static float comboOpacity;
    public static boolean comboShow;

    //Damage List Options
    public static int damageListX;
    public static int damageListY;
    public static float damageListScale;
    public static float damageListOpacity;
    public static boolean damageListShow;
    public static boolean damageListColorDamageType;
    public static int damageListMaxSize;
    public static int damageListClearTime;

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
    private static boolean validateColorTitle(final Object obj) {
        if (!(obj instanceof List))
            return false;
        List<String> list = (List<String>) obj;
        for (String item : list) {
            if (item.split("\\|").length < 2)
                return false;
        }
        return true;
    }

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event) {
        showDamage = SHOW_DAMAGE.get();
        noShake = NO_SHAKE.get();
        clearTime = CLEAR_TIME.get();
        numberX = NUMBER_X.get();
        numberY = NUMBER_Y.get();
        numberScale = (float) (double) NUMBER_SCALE.get();
        numberOpacity = (float) (double) NUMBER_OPACITY.get();
        numberShow = NUMBER_SHOW.get();
        titleX = TITLE_X.get();
        titleY = TITLE_Y.get();
        titleScale = (float) (double) TITLE_SCALE.get();
        titleOpacity = (float) (double) TITLE_OPACITY.get();
        titleShow = TITLE_SHOW.get();
        comboX = COMBO_X.get();
        comboY = COMBO_Y.get();
        comboScale = (float) (double) COMBO_SCALE.get();
        comboOpacity = (float) (double) COMBO_OPACITY.get();
        comboShow = COMBO_SHOW.get();
        damageListX = DAMAGE_LIST_X.get();
        damageListY = DAMAGE_LIST_Y.get();
        damageListScale = (float) (double) DAMAGE_LIST_SCALE.get();
        damageListOpacity = (float) (double) DAMAGE_LIST_OPACITY.get();
        damageListMaxSize = DAMAGE_LIST_MAX_SIZE.get();
        damageListShow = DAMAGE_LIST_SHOW.get();
        damageListColorDamageType = DAMAGE_LIST_COLOR_TYPE.get();
        damageListClearTime = DAMAGE_LIST_CLEAR_TIME.get();
        damageRankEnabled = DAMAGE_RANK_USE.get();
        damageRankList = DAMAGE_RANK_OPT.get().stream().map(item -> {
            String[] items = item.split("\\|");
            return new RankOptionItem(items[1], toColor(items[2]), Long.parseLong(items[0]));
        }).collect(Collectors.toList());
        damageRankColorDamageNumber = DAMAGE_RANK_COLOR_NUMBER.get();
        damageRankColorTitle = DAMAGE_RANK_COLOR_TITLE.get();
        damageRankColorCombo = DAMAGE_RANK_COLOR_COMBO.get();
        comboRankEnabled = COMBO_RANK_USE.get();
        comboRankList = COMBO_RANK_OPT.get().stream().map(item -> {
            String[] items = item.split("\\|");
            return new RankOptionItem(items[1], toColor(items[2]), Long.parseLong(items[0]));
        }).collect(Collectors.toList());
        comboRankColorDamageNumber = COMBO_RANK_COLOR_NUMBER.get();
        comboRankColorTitle = COMBO_RANK_COLOR_TITLE.get();
        comboRankColorCombo = COMBO_RANK_COLOR_COMBO.get();
    }

    public static void save() {
        SHOW_DAMAGE.set(showDamage);
        NO_SHAKE.set(noShake);
        CLEAR_TIME.set(clearTime);
        NUMBER_X.set(numberX);
        NUMBER_Y.set(numberY);
        NUMBER_SCALE.set((double) numberScale);
        NUMBER_OPACITY.set((double) numberOpacity);
        NUMBER_SHOW.set(numberShow);
        TITLE_X.set(titleX);
        TITLE_Y.set(titleY);
        TITLE_SCALE.set((double) titleScale);
        TITLE_OPACITY.set((double) titleOpacity);
        TITLE_SHOW.set(titleShow);
        COMBO_X.set(comboX);
        COMBO_Y.set(comboY);
        COMBO_SCALE.set((double) comboScale);
        COMBO_OPACITY.set((double) comboOpacity);
        COMBO_SHOW.set(comboShow);
        DAMAGE_LIST_X.set(damageListX);
        DAMAGE_LIST_Y.set(damageListY);
        DAMAGE_LIST_SCALE.set((double) damageListScale);
        DAMAGE_LIST_OPACITY.set((double) damageListOpacity);
        DAMAGE_LIST_MAX_SIZE.set(damageListMaxSize);
        DAMAGE_LIST_COLOR_TYPE.set(damageListColorDamageType);
        DAMAGE_LIST_SHOW.set(damageListShow);
        DAMAGE_LIST_CLEAR_TIME.set(damageListClearTime);
        DAMAGE_RANK_USE.set(damageRankEnabled);
        DAMAGE_RANK_OPT.set(damageRankList.stream().map(item -> item.amount + "|" + item.title + "|" + String.valueOf(item.color)).collect(Collectors.toList()));
        DAMAGE_RANK_COLOR_NUMBER.set(damageRankColorDamageNumber);
        DAMAGE_RANK_COLOR_TITLE.set(damageRankColorTitle);
        DAMAGE_RANK_COLOR_COMBO.set(damageRankColorCombo);
        COMBO_RANK_USE.set(comboRankEnabled);
        COMBO_RANK_OPT.set(comboRankList.stream().map(item -> item.amount + "|" + item.title + "|" + String.valueOf(item.color)).collect(Collectors.toList()));
        COMBO_RANK_COLOR_NUMBER.set(comboRankColorDamageNumber);
        COMBO_RANK_COLOR_TITLE.set(comboRankColorTitle);
        COMBO_RANK_COLOR_COMBO.set(comboRankColorCombo);
        SPEC.save();
    }
}
