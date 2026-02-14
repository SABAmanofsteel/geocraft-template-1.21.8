package com.geocraft.item;

import com.geocraft.Geocraft;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;


public class ModItems {
    public  static final Item KHINKALI = registerItem("khinkali", new Item(new Item.Settings()));

    private  static  Item registerItem(String name,Item item){
        return Registry.register(Registries.ITEM, Identifier.of(Geocraft.MOD_ID,name),item);
    }

    public static void registerModItems(){
        Geocraft.LOGGER.info("Registering Mod Items for "+Geocraft.MOD_ID);

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register(fabricItemGroupEntries -> {
            fabricItemGroupEntries.add(khinkali);
        });
    }
}
