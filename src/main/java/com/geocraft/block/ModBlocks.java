package com.geocraft.block;

import com.geocraft.Geocraft;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.cauldron.CauldronBehavior;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;


public class ModBlocks {

    public static final Block WINE_CAULDRON = registerBlock(
            "wine_cauldron",
            new WineCauldronBlock(
                    AbstractBlock.Settings.copy(Blocks.WATER_CAULDRON)
                            .registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of(Geocraft.MOD_ID, "wine_cauldron"))),
                    CauldronBehavior.createMap("wine")
            )
    );
    public static final Block KHACHAPURI = registerBlock(
            "khachapuri",
            new KhachapuriBlock(
                    AbstractBlock.Settings.create()
                            .strength(0.5f)
                            .sounds(net.minecraft.sound.BlockSoundGroup.WOOL)
                            .nonOpaque()
                            .pistonBehavior(net.minecraft.block.piston.PistonBehavior.DESTROY)
                            .registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of(Geocraft.MOD_ID, "khachapuri")))
            )
    );

    private static Block registerBlock(String name, Block block) {
        registerBlockItem(name, block);
        return Registry.register(
                Registries.BLOCK,
                Identifier.of(Geocraft.MOD_ID, name),
                block
        );
    }

    private static void registerBlockItem(String name, Block block) {
        Registry.register(
                Registries.ITEM,
                Identifier.of(Geocraft.MOD_ID, name),
                new BlockItem(block, new Item.Settings()
                        .registryKey(RegistryKey.of(RegistryKeys.ITEM, Identifier.of(Geocraft.MOD_ID, name))))
        );
    }

    public static void registerModBlocks() {
        Geocraft.LOGGER.info("Registering Mod Blocks for " + Geocraft.MOD_ID);

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.FUNCTIONAL).register(entries -> {
            entries.add(WINE_CAULDRON);
        });
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.FOOD_AND_DRINK).register(entries -> {
            entries.add(KHACHAPURI);
        });
    }
}