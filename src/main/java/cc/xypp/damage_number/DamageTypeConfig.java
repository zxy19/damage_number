package cc.xypp.damage_number;


import cc.xypp.damage_number.api.decoration.INumberDecoration;
import cc.xypp.damage_number.api.decoration.IconDecoration;
import cc.xypp.damage_number.api.decoration.ItemDecoration;
import cc.xypp.damage_number.data.DamageTextFmt;
import cc.xypp.damage_number.utils.Colors;
import com.electronwill.nightconfig.core.CommentedConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import com.electronwill.nightconfig.toml.TomlFormat;
import com.electronwill.nightconfig.toml.TomlParser;
import com.electronwill.nightconfig.toml.TomlWriter;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.registries.ForgeRegistries;
import oshi.util.tuples.Pair;

import java.io.FileReader;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Predicate;


@Mod.EventBusSubscriber(modid = DamageNumber.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DamageTypeConfig {
    record StyleRecord(String color, ResourceLocation itemStack, ResourceLocation icon, String prependKey,
                       String appendKey, String formatKey) {
    }

    private static Map<String, Predicate<DamageSource>> TagLikePredicate = Map.of(
            "isDamageHelmet", DamageSource::isDamageHelmet,
            "isBypassArmor", DamageSource::isBypassArmor,
            "isDamageFire", DamageSource::isFire,
            "isDamageMagic", DamageSource::isMagic,
            "isDamageProjectile", DamageSource::isProjectile,
            "isDamageExplosion", DamageSource::isExplosion,
            "isFall", DamageSource::isFall,
            "isBypassInvul", DamageSource::isBypassInvul
    );
    private static List<String> builtInMsgIds = List.of(
            "inFire",
            "lightningBolt",
            "onFire",
            "lava",
            "hotFloor",
            "inWall",
            "cramming",
            "drown",
            "starve",
            "cactus",
            "flyIntoWall",
            "outOfWorld",
            "generic",
            "magic",
            "wither",
            "anvil",
            "fallingBlock",
            "dragonBreath",
            "dryout",
            "sweetBerryBush",
            "freeze",
            "fallingStalactite",
            "stalagmite"
    );

    static CommentedConfig config;
    static Map<String, Integer> typePriority = new HashMap<>();
    static Map<String, Integer> tagPriority = new HashMap<>();
    static Map<String, StyleRecord> typeStyle = new HashMap<>();
    static Map<String, StyleRecord> tagStyle = new HashMap<>();
    static Set<String> typeList = new HashSet<>();
    static Set<String> tagList = new HashSet<>();
    static boolean randomColorForTag = false;
    static boolean randomColorForType = false;
    public static String lastMatching = "null";

    public static void register() {
        load();
    }

    private static void collectKeys(Set<String> keys, String currentPath, CommentedConfig currentConfig) {
        if (currentConfig.contains("priority")) {
            keys.add(currentPath);
        }
        for (CommentedConfig.Entry key : currentConfig.entrySet()) {
            if (key.getValue() instanceof CommentedConfig c) {
                collectKeys(keys, (currentPath.isEmpty() ? "" : (currentPath + ".")) + key.getKey(), c);
            }
        }
    }

    public static void load() {
        Path configPath = DamageNumber.CONFIG_BASE_PATH.resolve("damage_number_type.toml");
        TomlParser parser = new TomlParser();

        if (configPath.toFile().exists())
            try {
                config = parser.parse(new FileReader(configPath.toFile()));
            } catch (Exception e) {
                config = TomlFormat.newConfig();
            }
        else
            config = TomlFormat.newConfig();

        boolean change = false;

        if (!config.contains("sys.randomColorForTag")) {
            config.set("sys.randomColorForTag", false);
            change = true;
        }
        randomColorForTag = config.getOrElse("sys.randomColorForTag", false);

        if (!config.contains("sys.randomColorForType")) {
            config.set("sys.randomColorForType", false);
            change = true;
        }
        randomColorForType = config.getOrElse("sys.randomColorForType", false);

        typeList = new HashSet<>();
        collectKeys(typeList, "", config.get("type"));
        typeList.addAll(builtInMsgIds);
        tagList = TagLikePredicate.keySet();

        for (String id : typeList) {
            change |= loadFor(typeStyle, typePriority, "type", id);
        }

        for (String id : tagList) {
            change |= loadFor(tagStyle, tagPriority, "tag", id);
        }

        if (change) save();
    }

    private static boolean loadFor(Map<String, StyleRecord> target, Map<String, Integer> priority, String category, String id) {
        String prefix = category + "." + id;
        boolean change = false;
        if (!config.contains(prefix + ".color")) {
            if (randomColorForTag)
                config.set(prefix + ".color", String.valueOf(Colors.randomColor()));
            else
                config.set(prefix + ".color", "[NO-VALUE]");

            config.setComment(
                    prefix + ".color",
                    "Color of the damage number. [NO-VALUE] to use default color."
            );
            change = true;
        }
        if (!config.contains(prefix + ".priority")) {
            if (id.equals("minecraft:is_player_attack"))
                config.set(prefix + ".priority", -1);
            else
                config.set(prefix + ".priority", 0);
            change = true;
        }
        if (!config.contains(prefix + ".icon.tex")) {
            config.set(prefix + ".icon.tex", "");
            change = true;
        }
        if (!config.contains(prefix + ".icon.item")) {
            config.set(prefix + ".icon.item", "");
            change = true;
        }
        if (!config.contains(prefix + ".format.prepend")) {
            config.set(prefix + ".format.prepend", "");
            change = true;
        }
        if (!config.contains(prefix + ".format.append")) {
            config.set(prefix + ".format.append", "");
            change = true;
        }
        if (!config.contains(prefix + ".format.format")) {
            config.set(prefix + ".format.format", "");
            change = true;
        }


        String color = config.get(prefix + ".color");
        String iconTex = config.get(prefix + ".icon.tex");
        String iconItem = config.get(prefix + ".icon.item");
        String iconPrepend = config.get(prefix + ".format.prepend");
        String iconAppend = config.get(prefix + ".format.append");
        String iconFormat = config.get(prefix + ".format.format");


        target.put(id, new StyleRecord(
                color,
                iconItem.isEmpty() ? null : ResourceLocation.tryParse(iconItem),
                iconTex.isEmpty() ? null : ResourceLocation.tryParse(iconTex),
                iconPrepend,
                iconAppend,
                iconFormat
        ));
        priority.put(id, config.getIntOrElse(prefix + ".priority", 0));
        return change;
    }

    public static void save() {
        Path configPath = DamageNumber.CONFIG_BASE_PATH.resolve("damage_number_type.toml");
        TomlWriter writer = new TomlWriter();

        try {
            if (!configPath.toFile().exists())
                configPath.toFile().createNewFile();
            writer.write(config.unmodifiable(), configPath, WritingMode.REPLACE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static long getColorForDamageType(DamageSource damageSource) {
        if (!Config.damageListColorDamageType) return 0xffffff;
        long maxPriority = -0x7fffffff;
        long color = 0xffffff;
        String matching = "null";
        if (typePriority.containsKey(damageSource.getMsgId())) {
            if (
                    typePriority.get(damageSource.getMsgId()) >= maxPriority
                            &&
                            !typeStyle.get(damageSource.getMsgId()).color().equals("[NO-VALUE]")
            ) {
                maxPriority = typePriority.get(damageSource.getMsgId());
                color = Colors.toColor(typeStyle.get(damageSource.getMsgId()).color());
                matching = "ID:" + damageSource.getMsgId();
            }
        }

        for (Map.Entry<String, Predicate<DamageSource>> tag : TagLikePredicate.entrySet()) {
            if (tagPriority.containsKey(tag.getKey())) {
                if (tag.getValue().test(damageSource)) {
                    if (
                            tagPriority.get(tag.getKey()) >= maxPriority
                                    &&
                                    !tagStyle.get(tag.getKey()).color().equals("[NO-VALUE]")
                    ) {
                        maxPriority = tagPriority.get(tag.getKey());
                        color = Colors.toColor(tagStyle.get(tag.getKey()).color());
                        matching = "TAG:" + tag.getKey();
                    }
                }
            }
        }
        lastMatching = matching;
        return color;
    }

    public static DamageTextFmt getTextFmtForDamageType(DamageSource damageSource) {
        DamageTextFmt textFmt = DamageTextFmt.getDefault();
        List<Pair<Integer, Pair<Integer, String>>> textFormats = new ArrayList<>();
        if (typePriority.containsKey(damageSource.getMsgId())) {
            addTextFormat(damageSource.getMsgId(), textFormats, typeStyle, typePriority);
        }

        for (Map.Entry<String, Predicate<DamageSource>> tag : TagLikePredicate.entrySet()) {
            if (tagPriority.containsKey(tag.getKey())) {
                if (tag.getValue().test(damageSource)) {
                    addTextFormat(tag.getKey(), textFormats, tagStyle, tagPriority);
                }
            }
        }
        textFormats.stream()
                .sorted(Comparator.comparingInt(Pair::getA))
                .forEach(pair -> {
                    switch (pair.getB().getA()) {
                        case 0 -> textFmt.setFormatKey(pair.getB().getB());
                        case 1 -> textFmt.append(Component.translatable(pair.getB().getB()));
                        case 2 -> textFmt.prepend(Component.translatable(pair.getB().getB()));
                    }
                });
        return textFmt;
    }

    private static void addTextFormat(String id, List<Pair<Integer, Pair<Integer, String>>> textFormats, Map<String, StyleRecord> tagStyle, Map<String, Integer> tagPriority) {
        if (!tagStyle.get(id).formatKey().isBlank()) {
            textFormats.add(new Pair<>(
                    tagPriority.get(id),
                    new Pair<>(
                            0,
                            tagStyle.get(id).formatKey()
                    )
            ));
        }
        if (!tagStyle.get(id).appendKey().isBlank()) {
            textFormats.add(new Pair<>(
                    tagPriority.get(id),
                    new Pair<>(
                            1,
                            tagStyle.get(id).appendKey()
                    )
            ));
        }
        if (!tagStyle.get(id).prependKey().isBlank()) {
            textFormats.add(new Pair<>(
                    tagPriority.get(id),
                    new Pair<>(
                            2,
                            tagStyle.get(id).prependKey()
                    )
            ));
        }
    }

    public static List<INumberDecoration> getDecorationsForDamageType(DamageSource damageSource) {
        List<Pair<Integer, INumberDecoration>> decorations = new ArrayList<>();
        if (typePriority.containsKey(damageSource.getMsgId())) {
            addDecoration(damageSource.getMsgId(), decorations, typeStyle, typePriority);
        }
        for (Map.Entry<String, Predicate<DamageSource>> tag : TagLikePredicate.entrySet()) {
            if (tagPriority.containsKey(tag.getKey())) {
                if (tag.getValue().test(damageSource)) {
                    addDecoration(tag.getKey(), decorations, tagStyle, tagPriority);
                }
            }
        }
        return decorations.stream().sorted(Comparator.comparingInt(Pair::getA)).map(Pair::getB).toList();
    }

    private static void addDecoration(String id, List<Pair<Integer, INumberDecoration>> decorations, Map<String, StyleRecord> styleRecordMap, Map<String, Integer> priorityMap) {
        if (!styleRecordMap.containsKey(id))
            return;
        if (styleRecordMap.get(id).icon() != null) {
            decorations.add(new Pair<>(
                    priorityMap.get(id),
                    new IconDecoration(styleRecordMap.get(id).icon(), 0, 0, 64, 64, 64, 64)
            ));
        }
        if (styleRecordMap.get(id).itemStack() != null) {
            Optional<Item> item = Optional.ofNullable(ForgeRegistries.ITEMS.getValue(styleRecordMap.get(id).itemStack()));
            item.ifPresent(value -> decorations.add(new Pair<>(
                    priorityMap.get(id),
                    new ItemDecoration(value.getDefaultInstance())
            )));
        }
    }

    @SubscribeEvent
    static void onReload(final ModConfigEvent event) {
        load();
    }
}
