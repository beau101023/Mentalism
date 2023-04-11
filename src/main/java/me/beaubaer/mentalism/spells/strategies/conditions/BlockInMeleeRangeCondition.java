package me.beaubaer.mentalism.spells.strategies.conditions;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

import java.util.List;

public class BlockInMeleeRangeCondition
{
    // version of test that returns true if the player is looking at any block
    public static boolean test(ServerPlayer player)
    {
        double reach = player.getReachDistance();
        HitResult res = player.pick(reach, 0.0f, false);

        return res.getType() == HitResult.Type.BLOCK;
    }

    public static boolean test(ServerPlayer player, List<TagKey<Block>> tags)
    {
        double reach = player.getReachDistance();
        HitResult res = player.pick(reach, 0.0f, false);

        if (res.getType() == HitResult.Type.BLOCK)
        {
            BlockState block = player.level.getBlockState(((BlockHitResult) res).getBlockPos());
            return tags.stream().anyMatch(tag -> block.is(tag));
        }
        return false;
    }
}
