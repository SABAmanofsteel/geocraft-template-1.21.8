package com.geocraft.item;

import com.geocraft.Geocraft;
import com.geocraft.block.ModBlocks;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ConsumableComponent;
import net.minecraft.component.type.ConsumableComponents;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.consume.ApplyEffectsConsumeEffect;
import net.minecraft.item.consume.ConsumeEffect;
import net.minecraft.item.equipment.ArmorMaterial;
import net.minecraft.item.equipment.EquipmentAsset;
import net.minecraft.item.equipment.EquipmentAssetKeys;
import net.minecraft.item.equipment.EquipmentType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import net.minecraft.component.type.FoodComponent;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.EquipmentSlot;
import com.github.crimsondawn45.fabricshieldlib.lib.object.FabricShieldItem;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ToolMaterial;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;



public class ModItems {

    public static final int BASE_DURABILITY = 15;

    public static final RegistryKey<EquipmentAsset> GUIDITE_ARMOR_MATERIAL_KEY = RegistryKey.of(EquipmentAssetKeys.REGISTRY_KEY, Identifier.of(Geocraft.MOD_ID, "guidite"));

    public static final TagKey<Item> REPAIRS_GUIDITE_ARMOR = TagKey.of(Registries.ITEM.getKey(), Identifier.of(Geocraft.MOD_ID, "repairs_guidite_armor"));

    public static final ArmorMaterial INSTANCE = new ArmorMaterial(
            BASE_DURABILITY,
            Map.of(
                    EquipmentType.HELMET, 12,
                    EquipmentType.CHESTPLATE, 8,
                    EquipmentType.LEGGINGS, 6,
                    EquipmentType.BOOTS, 3
            ),
            30,
            SoundEvents.ITEM_ARMOR_EQUIP_IRON,
            0.0F,
            0.0F,
            REPAIRS_GUIDITE_ARMOR,
            GUIDITE_ARMOR_MATERIAL_KEY
    );


    public static final Item SVANURI_QUDI = registerItem(
            "svanuri_qudi",
            new Item(new Item.Settings().registryKey(RegistryKey.of(
                            RegistryKeys.ITEM,
                            Identifier.of(Geocraft.MOD_ID, "svanuri_qudi")
                    ))
                    .armor(INSTANCE, EquipmentType.HELMET)
                    .maxDamage(EquipmentType.HELMET.getMaxDamage(BASE_DURABILITY))
            )
    );
    public static final Item SVANURI_PHARI = registerItem(
            "svanuri_phari",
            new FabricShieldItem(
                    new Item.Settings()
                            .registryKey(RegistryKey.of(RegistryKeys.ITEM, Identifier.of(Geocraft.MOD_ID, "svanuri_phari")))
                            .maxDamage(336)
                            .maxCount(1),
                    20,          // cooldown ticks
                    9,           // enchantability
                    Items.GOLD_INGOT  // repair item
            )
    );
    public static final Item CLUB = registerItem(
            "club",
            new ClubItem(
                    ToolMaterial.WOOD,        // Change to STONE, IRON, DIAMOND, etc. as needed
                    7.0f,                     // attack damage bonus (axe is 6-9 depending on material)
                    -3.2f,                    // attack speed (axe is -3.1f, slightly slow)
                    new Item.Settings()
                            .registryKey(RegistryKey.of(RegistryKeys.ITEM, Identifier.of(Geocraft.MOD_ID, "club")))
                            .maxDamage(ToolMaterial.WOOD.durability()) // or pick a number like 59
            )
    );


    public static final Item YANWI = registerItem(
            "yanwi",
            new Item(new Item.Settings()
                    .registryKey(RegistryKey.of(
                            RegistryKeys.ITEM,
                            Identifier.of(Geocraft.MOD_ID, "yanwi")
                    ))
                    .maxCount(1) // Make non-stackable
            )
    );

    public static final Item YANWISAVSE = createFoodItem(
            "yanwi_savse",
            2,
            0.3f,
            true,
            List.of(
                    new StatusEffectInstance(StatusEffects.STRENGTH, 1200, 0),
                    new StatusEffectInstance(StatusEffects.REGENERATION, 600, 0),
                    new StatusEffectInstance(StatusEffects.SLOWNESS, 1200, 1),
                    new StatusEffectInstance(StatusEffects.NAUSEA, 1200, 1)
            ),
            true, // drinking animation
            YANWI, // Return YANWI after consumption
            1
    );

    public static final Item GRAPE = createFoodItem(
            "grape",
            2,
            0.3f,
            false,
            null,
            false, // eating animation
            null, // No return item
            64
    );

    public static final Item KHINKALI = createFoodItem(
            "khinkali",
            10,
            15f,
            false,
            null,
            false, // eating animation
            null, // No return item
            64
    );


    private static Item createFoodItem(
            String name,
            int nutrition,
            float saturation,
            boolean alwaysEdible,
            List<StatusEffectInstance> effects,
            boolean useDrinkAnimation,
            Item returnItem,
            int maxstackcount
    ) {
        FoodComponent.Builder foodBuilder = new FoodComponent.Builder()
                .nutrition(nutrition)
                .saturationModifier(saturation);

        if (alwaysEdible) {
            foodBuilder.alwaysEdible();
        }

        FoodComponent food = foodBuilder.build();

        Item.Settings settings = new Item.Settings()
                .maxCount(maxstackcount); // Make all food items non-stackable by default

        // Start with base consumable component based on animation type
        ConsumableComponent.Builder consumableBuilder;

        if (useDrinkAnimation) {
            consumableBuilder = ConsumableComponents.drink();
        } else {
            consumableBuilder = ConsumableComponents.food();
        }

        // Add all effect consume effects
        if (effects != null && !effects.isEmpty()) {
            for (StatusEffectInstance effect : effects) {
                consumableBuilder.consumeEffect(new ApplyEffectsConsumeEffect(effect, 1.0f));
            }
        }

        ConsumableComponent consumable = consumableBuilder.build();
        settings.food(food, consumable);

        settings.registryKey(RegistryKey.of(
                RegistryKeys.ITEM,
                Identifier.of(Geocraft.MOD_ID, name)
        ));

        // Create the item with the return item parameter
        return registerItem(name, new ReturnableItem(settings, returnItem));
    }

    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, Identifier.of(Geocraft.MOD_ID, name), item);
    }

    public static void registerModItems() {
        Geocraft.LOGGER.info("Registering Mod Items for " + Geocraft.MOD_ID);

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register(fabricItemGroupEntries -> {
            fabricItemGroupEntries.add(KHINKALI);
            fabricItemGroupEntries.add(GRAPE);
            fabricItemGroupEntries.add(YANWI);
            fabricItemGroupEntries.add(YANWISAVSE);
            fabricItemGroupEntries.add(SVANURI_QUDI);
            fabricItemGroupEntries.add(SVANURI_PHARI);
        });
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register(entries -> {
            entries.add(CLUB);
        });
    }

    // Custom item class that can return any item after consumption
    private static class ReturnableItem extends Item {
        private final Item returnItem;

        public ReturnableItem(Settings settings, Item returnItem) {
            super(settings);
            this.returnItem = returnItem;
        }

        @Override
        public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
            // Call super to apply food and effects
            ItemStack result = super.finishUsing(stack, world, user);

            // Give the return item if specified (not null)
            if (!world.isClient && user instanceof PlayerEntity player && returnItem != null) {
                player.getInventory().offerOrDrop(new ItemStack(returnItem));
            }

            return result;
        }
    }
}