package com.geocraft;

import com.geocraft.block.ModBlocks;
import com.geocraft.item.ModItems;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.LeveledCauldronBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;

public class CauldronBrewing {

    public static void register() {
        UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> {
            BlockPos pos = hitResult.getBlockPos();
            BlockState state = world.getBlockState(pos);
            ItemStack handStack = player.getStackInHand(hand);

            // Step 1: Click water cauldron with grape to turn it into wine cauldron
            if (state.getBlock() == Blocks.WATER_CAULDRON) {
                if (handStack.isOf(ModItems.GRAPE)) {
                    if (!world.isClient) {
                        int waterLevel = state.get(LeveledCauldronBlock.LEVEL);

                        // Change the block to wine cauldron and preserve the level
                        world.setBlockState(pos, ModBlocks.WINE_CAULDRON.getDefaultState()
                                .with(LeveledCauldronBlock.LEVEL, waterLevel));

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
            }

            // Step 2: Click wine cauldron with yanwi to get yanwi_savse
            if (state.getBlock() == ModBlocks.WINE_CAULDRON) {
                if (handStack.isOf(ModItems.YANWI)) {
                    if (!world.isClient) {
                        int wineLevel = state.get(LeveledCauldronBlock.LEVEL);

                        // Consume yanwi
                        handStack.decrement(1);

                        // Give yanwi_savse
                        ItemStack result = new ItemStack(ModItems.YANWISAVSE);
                        if (!player.getInventory().insertStack(result)) {
                            player.dropItem(result, false);
                        }

                        // Decrease level or empty the cauldron
                        if (wineLevel > 1) {
                            world.setBlockState(pos, state.with(LeveledCauldronBlock.LEVEL, wineLevel - 1));
                        } else {
                            world.setBlockState(pos, Blocks.CAULDRON.getDefaultState());
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