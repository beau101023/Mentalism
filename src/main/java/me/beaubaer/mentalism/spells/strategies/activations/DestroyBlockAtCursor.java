package me.beaubaer.mentalism.spells.strategies.activations;

import me.beaubaer.mentalism.spells.strategies.interfaces.ActivationStrategy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class DestroyBlockAtCursor implements ActivationStrategy
{
    public void activate(Player p)
    {
        BlockHitResult hit = (BlockHitResult) p.pick(5.0D, 0.0F, false);
        if (hit == null) {
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
