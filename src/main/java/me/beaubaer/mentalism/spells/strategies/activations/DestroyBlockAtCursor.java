package me.beaubaer.mentalism.spells.strategies.activations;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class DestroyBlockAtCursor
{
    public static void activate(Player p)
    {
        BlockHitResult hit = (BlockHitResult) p.pick(128.0D, 0.0F, false);
        if (hit.getType() == BlockHitResult.Type.MISS) {
            return;
        }

        Level level = p.getCommandSenderWorld();
        BlockState blockState = level.getBlockState(hit.getBlockPos());

        if (!level.isEmptyBlock(hit.getBlockPos())) {
            p.getMainHandItem().mineBlock(level, blockState, hit.getBlockPos(), p);
            level.destroyBlock(hit.getBlockPos(), true);
        }
    }
}
