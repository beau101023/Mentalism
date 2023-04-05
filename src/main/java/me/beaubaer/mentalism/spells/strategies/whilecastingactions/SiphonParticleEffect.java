package me.beaubaer.mentalism.spells.strategies.whilecastingactions;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class SiphonParticleEffect
{
    public static void activate(ServerPlayer p)
    {
        // get block the player is looking at
        HitResult res = p.pick(128f, 0, false);

        if(res.getType() == HitResult.Type.BLOCK)
        {
            BlockPos blockPos = ((BlockHitResult) res).getBlockPos();
            BlockState blockState = p.level.getBlockState(blockPos);

            // vector from the block to the player
            Vec3 vec = new Vec3(p.getX() - blockPos.getX(), p.getEyeY() - blockPos.getY(), p.getZ() - blockPos.getZ());
            vec = vec.normalize();

            // spawn particles moving towards the player
            ((ServerLevel) p.level).sendParticles(new BlockParticleOption(ParticleTypes.BLOCK, blockState).setPos(blockPos), blockPos.getX()+0.5D, blockPos.getY()+0.5D, blockPos.getZ()+0.5D, 2, vec.x, vec.y, vec.z, 0.01D);
        }
    }
}
