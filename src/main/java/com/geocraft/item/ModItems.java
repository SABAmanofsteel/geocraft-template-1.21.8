package com.geocraft.item;

import com.geocraft.Geocraft;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.component.type.ConsumableComponent;
import net.minecraft.component.type.ConsumableComponents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.consume.ApplyEffectsConsumeEffect;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.component.type.FoodComponent;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;


public class ModItems {

    public static final Item GRAPE = createFoodItem(
            "grape",
            2,
            0.3f,
            true,
            new StatusEffectInstance(StatusEffects.STRENGTH, 1200, 0)
    );

    public static final Item KHINKALI = createFoodItem(
            "khinkali",
            8,
            0.8f,
            false,
            null // no special effect
    );


    private static Item createFoodItem(
            String name,
            int nutrition,
            float saturation,
            boolean alwaysEdible,
            StatusEffectInstance effect // can be null
    ) {
        FoodComponent.Builder foodBuilder = new FoodComponent.Builder()
                .nutrition(nutrition)
                .saturationModifier(saturation);

        if (alwaysEdible) {
            foodBuilder.alwaysEdible();
        }

        FoodComponent food = foodBuilder.build();

        Item.Settings settings = new Item.Settings();

        if (effect != null) {
            ConsumableComponent consumable = ConsumableComponents.food()
                    .consumeEffect(new ApplyEffectsConsumeEffect(effect, 1.0f))
                    .build();

            settings.food(food, consumable);
        } else {
            settings.food(food);
        }

        settings.registryKey(RegistryKey.of(
                RegistryKeys.ITEM,
                Identifier.of(Geocraft.MOD_ID, name)
        ));

        return registerItem(name, new Item(settings));
    }


    private  static  Item registerItem(String name,Item item){
        return Registry.register(Registries.ITEM, Identifier.of(Geocraft.MOD_ID,name),item);
    }

    public static void registerModItems(){
        Geocraft.LOGGER.info("Registering Mod Items for "+Geocraft.MOD_ID);

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register(fabricItemGroupEntries -> {
            fabricItemGroupEntries.add(KHINKALI);
            fabricItemGroupEntries.add(GRAPE);
        });
    }
}
