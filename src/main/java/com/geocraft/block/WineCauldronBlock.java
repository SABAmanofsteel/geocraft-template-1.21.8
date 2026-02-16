package com.geocraft.block;

import net.minecraft.block.BlockState;
import net.minecraft.block.LeveledCauldronBlock;
import net.minecraft.block.cauldron.CauldronBehavior;
import net.minecraft.world.biome.Biome;

public class WineCauldronBlock extends LeveledCauldronBlock {

    public WineCauldronBlock(Settings settings, CauldronBehavior.CauldronBehaviorMap behaviorMap) {
        super(Biome.Precipitation.NONE, behaviorMap, settings);
        this.setDefaultState(this.stateManager.getDefaultState().with(LEVEL, 3));
    }

    @Override
    protected double getFluidHeight(BlockState state) {
        return (6.0 + state.get(LEVEL) * 3.0) / 16.0;
    }

    @Override
    public boolean isFull(BlockState state) {
        return state.get(LEVEL) == 3;
    }
}