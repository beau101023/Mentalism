package me.beaubaer.mentalism.spells.strategies.activations;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.common.Tags;

public class ExtractOreFromBlock
{
    public static void activate(ServerPlayer p)
    {
        BlockHitResult hit = (BlockHitResult) p.pick(128D, 0.0F, false);
        if (hit.getType() == BlockHitResult.Type.MISS) {
            return;
        }

        ServerLevel level = p.getLevel();

        BlockPos blockPos = hit.getBlockPos();

        BlockState blockState = level.getBlockState(blockPos);

        // make the block drop its item without being destroyed
        Block b = blockState.getBlock();
        p.awardStat(Stats.BLOCK_MINED.get(b));
        Block.dropResources(blockState, level, blockPos, null, p, p.getMainHandItem());

        // spawn block broken particles
        level.levelEvent(2001, blockPos, Block.getId(blockState));


        // if the block is a normal ore, replace with stone
        if (blockState.is(Tags.Blocks.ORES_IN_GROUND_STONE))
        {
            level.setBlockAndUpdate(hit.getBlockPos(), Blocks.STONE.defaultBlockState());
        }

        // if the block is a nether ore, replace with netherrack
        else if (blockState.is(Tags.Blocks.ORES_IN_GROUND_NETHERRACK))
        {
            level.setBlockAndUpdate(hit.getBlockPos(), Blocks.NETHERRACK.defaultBlockState());
        }

        // if the block is a deepslate ore, replace with deepslate
        else if (blockState.is(Tags.Blocks.ORES_IN_GROUND_DEEPSLATE))
        {
            level.setBlockAndUpdate(hit.getBlockPos(), Blocks.DEEPSLATE.defaultBlockState());
        }
    }
}
