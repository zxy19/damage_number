package cc.xypp.damage_number;


import cc.xypp.damage_number.utils.Colors;
import com.electronwill.nightconfig.core.CommentedConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import com.electronwill.nightconfig.toml.TomlFormat;
import com.electronwill.nightconfig.toml.TomlParser;
import com.electronwill.nightconfig.toml.TomlWriter;
import net.minecraft.core.Holder;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.common.Tags;

import java.io.FileReader;
import java.io.FileWriter;
import java.nio.file.Path;
import java.util.*;


@EventBusSubscriber(modid = DamageNumber.MODID, bus = EventBusSubscriber.Bus.MOD)
public class DamageTypeConfig {
    static CommentedConfig config;
    public static Map<String, Integer> typePriority = new HashMap<>();
    public static Map<String, Integer> tagPriority = new HashMap<>();
    public static Map<String, String> typeStyle = new HashMap<>();
    public static Map<String, String> tagStyle = new HashMap<>();
    public static Set<String> typeList = new HashSet<>();
    public static Set<String> tagList = new HashSet<>();
    public static boolean randomColorForTag = false;
    public static boolean randomColorForType = false;
    public static String lastMatching = "null";
    public static void addGroup(String id) {
        typeList.add(id);
    }

    public static void addTagGroup(String id) {
        tagList.add(id);
    }

    public static void register() {
        load();
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

        for (String id : typeList) {
            if (!config.contains("type." + id + ".color")) {
                if (randomColorForType)
                    config.set("type." + id + ".color", String.valueOf(Colors.randomColor()));
                else
                    config.set("type." + id + ".color", "[NO-VALUE]");

                config.setComment(
                        "type." + id + ".color",
                        "Color of the damage number. [NO-VALUE] to use default color."
                );
                change = true;
            }
            if (!config.contains("type." + id + ".priority")) {
                config.add("type." + id + ".priority", 0);
                change = true;
            }

            typeStyle.put(id, config.get("type." + id + ".color"));
            typePriority.put(id, config.getIntOrElse("type." + id + ".priority", 0));
        }

        for (String id : tagList) {
            if (!config.contains("tag." + id + ".color")) {
                if (randomColorForTag)
                    config.set("tag." + id + ".color", String.valueOf(Colors.randomColor()));
                else
                    config.set("tag." + id + ".color", "[NO-VALUE]");

                config.setComment(
                        "tag." + id + ".color",
                        "Color of the damage number. [NO-VALUE] to use default color."
                );
                change = true;
            }
            if (!config.contains("tag." + id + ".priority")) {
                if (id.equals("minecraft:is_player_attack"))
                    config.set("tag." + id + ".priority", -1);
                else
                    config.set("tag." + id + ".priority", 0);
                change = true;
            }

            tagStyle.put(id, config.get("tag." + id + ".color"));
            tagPriority.put(id, config.getIntOrElse("tag." + id + ".priority", 0));
        }


        if (change) save();
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
        if (typePriority.containsKey(damageSource.type().msgId())) {
            if (
                    typePriority.get(damageSource.type().msgId()) >= maxPriority
                            &&
                            !typeStyle.get(damageSource.type().msgId()).equals("[NO-VALUE]")
            ) {
                maxPriority = typePriority.get(damageSource.type().msgId());
                color = Colors.toColor(typeStyle.get(damageSource.type().msgId()));
                matching = "ID:"+damageSource.type().msgId();
            }
        }

        for (TagKey<DamageType> tag : damageSource.typeHolder().tags().toList()) {
            if (tagPriority.containsKey(tag.location().toString())) {
                if (damageSource.is(tag)) {
                    if (
                            tagPriority.get(tag.location().toString()) >= maxPriority
                                    &&
                                    !tagStyle.get(tag.location().toString()).equals("[NO-VALUE]")
                    ) {
                        maxPriority = tagPriority.get(tag.location().toString());
                        color = Colors.toColor(tagStyle.get(tag.location().toString()));
                        matching = "TAG:"+tag.location().toString();
                    }
                }
            }
        }
        lastMatching = matching;
        return color;
    }

    @SubscribeEvent
    static void onReload(final ModConfigEvent event) {
        load();
    }
}
