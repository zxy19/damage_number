package cc.xypp.damage_number.server;

import cc.xypp.damage_number.Config;
import cc.xypp.damage_number.DamageTypeConfig;
import cc.xypp.damage_number.api.DamageEmitEvent;
import cc.xypp.damage_number.api.decoration.INumberDecoration;
import cc.xypp.damage_number.data.DamageRecord;
import cc.xypp.damage_number.data.DamageTextFmt;
import cc.xypp.damage_number.network.Network;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.neoforged.neoforge.common.NeoForge;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DamageProcessor {
    static Map<String, Float> userDamage = new HashMap<>();
    static Map<String, Long> keepUntil = new HashMap<>();
    static Map<String, Integer> damageCount = new HashMap<>();

    public static void process(ServerPlayer serverPlayer, DamageSource source, float newDamage) {
        long defaultColor = DamageTypeConfig.getColorForDamageType(source);
        List<INumberDecoration> decorations = DamageTypeConfig.getDecorationsForDamageType(source);
        DamageTextFmt textFormat = DamageTypeConfig.getTextFmtForDamageType(source);
        String uid = serverPlayer.getUUID().toString();

        process(serverPlayer, source, newDamage, uid, (int) defaultColor, decorations, textFormat);
    }

    public static void process(ServerPlayer serverPlayer, DamageSource source, float newDamage, String uid, int defaultColor, List<INumberDecoration> decorations, DamageTextFmt textFormat) {
        DamageEmitEvent event = new DamageEmitEvent(
                newDamage + userDamage.getOrDefault(uid, 0.0f),
                newDamage,
                damageCount.getOrDefault(uid, 0) + 1,
                defaultColor,
                decorations,
                textFormat,
                source,
                serverPlayer
        );

        NeoForge.EVENT_BUS.post(event);

        damageCount.put(uid, event.getCombo());
        userDamage.put(uid, event.getAmount());
        keepUntil.put(uid, new Date().getTime() + Config.clearTime);
        sendDamageEmit(serverPlayer,
                userDamage.get(uid),
                damageCount.get(uid),
                new DamageRecord(
                        event.getInstant(),
                        event.getColor(),
                        event.getFormat(),
                        event.getDecorations()
                )
        );
    }

    public static void sendDamageEmit(ServerPlayer serverPlayer,
                                      float amount,
                                      int combo,
                                      DamageRecord data
    ) {
        Network.send(serverPlayer, "emit", amount, combo, data);
    }

    public static void sendDamageClear(ServerPlayer serverPlayer) {
        if (!keepUntil.containsKey(serverPlayer.getUUID().toString())) return;
        keepUntil.put(serverPlayer.getUUID().toString(), new Date().getTime());
    }

    public static void sendDamageTotal(ServerPlayer serverPlayer, float amount, int combo) {
        Network.send(serverPlayer, "total", amount, combo, DamageRecord.EMPTY);
    }

    public static void checkPlayerDamageTick(ServerPlayer serverPlayer) {
        String uuid = serverPlayer.getUUID().toString();
        if (keepUntil.containsKey(uuid)) {
            if (keepUntil.get(uuid) < new Date().getTime()) {
                sendDamageTotal(serverPlayer, userDamage.get(uuid), damageCount.get(uuid));
                keepUntil.remove(uuid);
                damageCount.remove(uuid);
                userDamage.remove(uuid);
            }
        }
    }

    public static float getAmount(ServerPlayer serverPlayer) {
        return userDamage.getOrDefault(serverPlayer.getUUID().toString(), 0.0f);
    }

    public static float getCombo(ServerPlayer serverPlayer) {
        return damageCount.getOrDefault(serverPlayer.getUUID().toString(), 0);
    }
}
