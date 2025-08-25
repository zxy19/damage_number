package cc.xypp.damage_number.mixin;

import cc.xypp.damage_number.Config;
import cc.xypp.damage_number.server.DamageProcessor;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public class EntityDamageMixin {
    @Inject(method = "actuallyHurt", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;getHealth()F"), require = 0)
    public void actuallyHurt(DamageSource p_21240_, float p_21241_, CallbackInfo ci, @Local(name = "f1") float f1) {
        if (!Config.useMixinDamageGetter) return;
        if (p_21240_.getEntity() instanceof ServerPlayer serverPlayer)
            DamageProcessor.process(serverPlayer, p_21240_, f1);
    }
}
