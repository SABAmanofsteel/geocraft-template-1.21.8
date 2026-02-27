package com.geocraft.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class KhachapuriBlock extends Block {

    public static final IntProperty BITES = IntProperty.of("bites", 0, 7);

    // 3px tall, inset from edges to approximate a circle
    private static final VoxelShape SHAPE = Block.createCuboidShape(1, 0, 1, 15, 3, 15);

    public KhachapuriBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState().with(BITES, 0));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(BITES);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }

    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos,
                                 PlayerEntity player, BlockHitResult hit) {
        if (world.isClient) return ActionResult.SUCCESS;

        if (!player.canConsume(false)) {
            return ActionResult.PASS;
        }

        // Feed the player (adjust nutrition/saturation as you like)
        player.getHungerManager().add(4, 0.6f);

        int bites = state.get(BITES);

        if (bites >= 7) {
            // Last slice eaten — remove the block
            world.removeBlock(pos, false);
        } else {
            world.setBlockState(pos, state.with(BITES, bites + 1));
        }

        return ActionResult.SUCCESS;
    }

    @Override
    public boolean canPlaceAt(BlockState state, net.minecraft.world.WorldView world, BlockPos pos) {
        // Must have a solid surface below, just like cake
        return world.getBlockState(pos.down()).isSolid();
    }
}