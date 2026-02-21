package com.geocraft.mixin;

import com.geocraft.item.ModItems;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class SvanuriPhariMixin {

    @Inject(method = "getDamageBlockedAmount", at = @At("RETURN"), cancellable = true)
    private void halveDamageAndReflect(ServerWorld world, DamageSource source, float amount, CallbackInfoReturnable<Float> cir) {
        LivingEntity self = (LivingEntity)(Object)this;
        if (!self.getActiveItem().isOf(ModItems.SVANURI_PHARI)) return;

        // Block only 50%
        float blocked = cir.getReturnValue() * 0.5f;
        cir.setReturnValue(blocked);

        // Reflect the other 50% back to the attacker
        LivingEntity attacker = null;

        if (source.getAttacker() instanceof LivingEntity livingAttacker) {
            attacker = livingAttacker;
        } else if (source.getSource() instanceof LivingEntity livingSource) {
            // Catches projectiles — source is the arrow/projectile, attacker is who shot it
            attacker = livingSource;
        }

        if (attacker != null) {
            float reflectAmount = amount * 0.5f;
            // Use thorns damage type so it bypasses armor and works on all mobs
            attacker.damage(
                    world,
                    world.getDamageSources().thorns(self),
                    reflectAmount
            );
        }
    }
}