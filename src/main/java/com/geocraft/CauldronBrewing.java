package com.geocraft;

import com.geocraft.item.ModItems;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.LeveledCauldronBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;

public class CauldronBrewing {
    // Custom property to track if cauldron has wine (we'll use a simple registry)
    private static final java.util.Map<BlockPos, Boolean> WINE_CAULDRONS = new java.util.HashMap<>();

    public static void register() {
        UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> {
            BlockPos pos = hitResult.getBlockPos();
            BlockState state = world.getBlockState(pos);
            ItemStack handStack = player.getStackInHand(hand);

            // Check if it's a water cauldron
            if (state.getBlock() == Blocks.WATER_CAULDRON) {
                int level = state.get(LeveledCauldronBlock.LEVEL);

                // Step 1: Click with grape to turn water into wine
                if (handStack.isOf(ModItems.GRAPE)) {
                    if (!world.isClient) {
                        // Mark this cauldron as containing wine
                        WINE_CAULDRONS.put(pos, true);

                        // Consume the grape
                        if (!player.isCreative()) {
                            handStack.decrement(1);
                        }

                        // Play sound
                        world.playSound(null, pos, SoundEvents.ITEM_BOTTLE_FILL, SoundCategory.BLOCKS, 1.0f, 1.0f);
                        player.incrementStat(Stats.USE_CAULDRON);
                    }
                    return ActionResult.SUCCESS;
                }

                // Step 2: Click with yanwi on wine cauldron to get yanwi_savse
                if (handStack.isOf(ModItems.YANWI) && WINE_CAULDRONS.getOrDefault(pos, false)) {
                    if (!world.isClient) {
                        // Give yanwi_savse
                        if (!player.isCreative()) {
                            handStack.decrement(1);
                        }
                        player.giveItemStack(new ItemStack(ModItems.YANWISAVSE));

                        // Decrease water level
                        if (level > 1) {
                            world.setBlockState(pos, state.with(LeveledCauldronBlock.LEVEL, level - 1));
                        } else {
                            // Empty the cauldron
                            world.setBlockState(pos, Blocks.CAULDRON.getDefaultState());
                            WINE_CAULDRONS.remove(pos);
                        }

                        // Play sound
                        world.playSound(null, pos, SoundEvents.ITEM_BOTTLE_FILL, SoundCategory.BLOCKS, 1.0f, 1.0f);
                        player.incrementStat(Stats.USE_CAULDRON);
                    }
                    return ActionResult.SUCCESS;
                }
            }

            return ActionResult.PASS;
        });
    }
}