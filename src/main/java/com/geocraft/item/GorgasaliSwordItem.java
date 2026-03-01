package com.geocraft.item;

import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ToolMaterial;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;

import java.util.List;

public class GorgasaliSwordItem extends Item {

    // ============================================
    // TUNE THESE VALUES:
    private static final float DAMAGE = 10.0f;         // 1.0f = half a heart
    private static final double KNOCKBACK_XZ = 1.5;   // horizontal force
    private static final double KNOCKBACK_Y = 0.5;    // vertical force
    private static final double RADIUS = 2.0;         // radius in blocks
    private static final int COOLDOWN_TICKS = 100;    // 20 ticks = 1 second
    // ============================================

    public GorgasaliSwordItem(ToolMaterial material, float attackDamage, float attackSpeed, Item.Settings settings) {
        super(settings
                .attributeModifiers(createAttributeModifiers(material, attackDamage, attackSpeed))
        );
    }

    @Override
    public ActionResult use(World world, PlayerEntity user, Hand hand) {
        if (!world.isClient && world instanceof ServerWorld serverWorld) {

            Box searchBox = user.getBoundingBox().expand(RADIUS);
            List<LivingEntity> nearbyEntities = world.getEntitiesByClass(
                    LivingEntity.class,
                    searchBox,
                    entity -> entity != user
            );

            for (LivingEntity entity : nearbyEntities) {
                // Knockback direction away from player
                double dx = entity.getX() - user.getX();
                double dz = entity.getZ() - user.getZ();
                double length = Math.sqrt(dx * dx + dz * dz);
                if (length > 0) {
                    dx /= length;
                    dz /= length;
                }
                entity.setVelocity(dx * KNOCKBACK_XZ, KNOCKBACK_Y, dz * KNOCKBACK_XZ);
                entity.velocityModified = true;

                // Damage attributed to the player
                entity.damage(serverWorld, serverWorld.getDamageSources().playerAttack(user), DAMAGE);
            }

            // Spawn explosion particles at the player's position
            serverWorld.spawnParticles(
                    ParticleTypes.EXPLOSION_EMITTER,  // big explosion cloud
                    user.getX(), user.getY(), user.getZ(),
                    1,    // count
                    0, 0, 0,  // offset x/y/z
                    0     // speed
            );

            // TNT explosion sound
            world.playSound(null, user.getBlockPos(),
                    SoundEvents.ENTITY_GENERIC_EXPLODE.value(),
                    SoundCategory.PLAYERS, 1.0f, 1.0f);

            // Apply cooldown
            user.getItemCooldownManager().set(user.getStackInHand(hand), COOLDOWN_TICKS);
        }

        return ActionResult.SUCCESS;
    }

    private static AttributeModifiersComponent createAttributeModifiers(
            ToolMaterial material, float attackDamage, float attackSpeed) {
        return AttributeModifiersComponent.builder()
                .add(
                        EntityAttributes.ATTACK_DAMAGE,
                        new EntityAttributeModifier(
                                Identifier.ofVanilla("base_attack_damage"),
                                attackDamage + material.attackDamageBonus(),
                                EntityAttributeModifier.Operation.ADD_VALUE
                        ),
                        AttributeModifierSlot.MAINHAND
                )
                .add(
                        EntityAttributes.ATTACK_SPEED,
                        new EntityAttributeModifier(
                                Identifier.ofVanilla("base_attack_speed"),
                                attackSpeed,
                                EntityAttributeModifier.Operation.ADD_VALUE
                        ),
                        AttributeModifierSlot.MAINHAND
                )
                .build();
    }
}