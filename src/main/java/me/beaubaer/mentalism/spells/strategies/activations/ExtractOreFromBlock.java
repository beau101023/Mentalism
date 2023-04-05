package me.beaubaer.mentalism.spells.strategies.activations;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

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
        if (blockState.is(Blocks.GOLD_ORE) || blockState.is(Blocks.IRON_ORE) || blockState.is(Blocks.COAL_ORE) || blockState.is(Blocks.DIAMOND_ORE) || blockState.is(Blocks.EMERALD_ORE) || blockState.is(Blocks.LAPIS_ORE) || blockState.is(Blocks.REDSTONE_ORE))
        {
            level.setBlockAndUpdate(hit.getBlockPos(), Blocks.STONE.defaultBlockState());
        }

        // if the block is a nether ore, replace with netherrack
        else if (blockState.is(Blocks.NETHER_GOLD_ORE) || blockState.is(Blocks.NETHER_QUARTZ_ORE) || blockState.is(Blocks.ANCIENT_DEBRIS))
        {
            level.setBlockAndUpdate(hit.getBlockPos(), Blocks.NETHERRACK.defaultBlockState());
        }

        // if the block is a deepslate ore, replace with deepslate
        else if (blockState.is(Blocks.DEEPSLATE_GOLD_ORE) || blockState.is(Blocks.DEEPSLATE_IRON_ORE) || blockState.is(Blocks.DEEPSLATE_COAL_ORE) || blockState.is(Blocks.DEEPSLATE_DIAMOND_ORE) || blockState.is(Blocks.DEEPSLATE_EMERALD_ORE) || blockState.is(Blocks.DEEPSLATE_LAPIS_ORE) || blockState.is(Blocks.DEEPSLATE_REDSTONE_ORE))
        {
            level.setBlockAndUpdate(hit.getBlockPos(), Blocks.DEEPSLATE.defaultBlockState());
        }
    }
}
