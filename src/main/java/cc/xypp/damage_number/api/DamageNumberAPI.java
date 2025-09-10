package cc.xypp.damage_number.api;

import cc.xypp.damage_number.api.decoration.INumberDecoration;
import cc.xypp.damage_number.data.DamageTextFmt;
import cc.xypp.damage_number.server.DamageProcessor;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class DamageNumberAPI {
    public static DamageNumberAPI INSTANCE = new DamageNumberAPI();

    public void clearDamageNumberInNextTick(ServerPlayer player) {
        DamageProcessor.sendDamageClear(player);
    }

    public void emitDamageNumber(
            ServerPlayer serverPlayer,
            DamageSource source,
            float newDamage,
            String uid,
            int defaultColor,
            List<INumberDecoration> decorations,
            DamageTextFmt textFormat
    ) {
        DamageProcessor.process(
                serverPlayer,
                source,
                newDamage,
                uid,
                defaultColor,
                decorations,
                textFormat
        );
    }

    public static void process(ServerPlayer serverPlayer,
                               DamageSource source,
                               float newDamage,
                               @Nullable Long defaultColor,
                               @Nullable List<INumberDecoration> decorations,
                               @Nullable DamageTextFmt textFormat) {
        DamageProcessor.process(serverPlayer, source, newDamage, defaultColor, decorations, textFormat);
    }

    public void emitDamageNumber(
            ServerPlayer serverPlayer,
            DamageSource source,
            float newDamage
    ) {
        DamageProcessor.process(serverPlayer, source, newDamage);
    }

    public float getTotalAmount(ServerPlayer serverPlayer) {
        return DamageProcessor.getAmount(serverPlayer);
    }

    public float getTotalCombo(ServerPlayer serverPlayer) {
        return DamageProcessor.getCombo(serverPlayer);
    }
}
