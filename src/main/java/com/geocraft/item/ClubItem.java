package com.geocraft.item;

import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.Item;
import net.minecraft.item.ToolMaterial;
import net.minecraft.util.Identifier;

public class ClubItem extends Item {

    public ClubItem(ToolMaterial material, float attackDamage, float attackSpeed, Item.Settings settings) {
        super(settings
                .attributeModifiers(createAttributeModifiers(material, attackDamage, attackSpeed))
        );
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