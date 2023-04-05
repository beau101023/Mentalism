package me.beaubaer.mentalism.spells.strategies.conditions;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

import java.util.List;

public class PlayerLookingAtPickableCondition
{
    private final List<Block> targetBlocks;
    private final List<EntityType> targetEntities;
    private boolean hitsAllEntities = false;
    private boolean hitsAllBlocks = false;
    private float checkDistance = 128f;

    public PlayerLookingAtPickableCondition(List<Block> targetBlocks, List<EntityType> targetEntities, boolean hitsAllEntities, boolean hitsAllBlocks, float checkDistance)
    {
        this.targetBlocks = targetBlocks;
        this.targetEntities = targetEntities;
        this.hitsAllEntities = hitsAllEntities;
        this.hitsAllBlocks = hitsAllBlocks;
        this.checkDistance = checkDistance;
    }

    public boolean check(Player p)
    {
        HitResult res = p.pick(checkDistance, 0, false);

        if(res.getType() == HitResult.Type.MISS)
        {
            return false;
        }

        if(res instanceof BlockHitResult)
        {
            BlockHitResult blockRes = (BlockHitResult) res;
            if(targetBlocks == null)
            {
                return hitsAllBlocks;
            }
            else
            {
                return targetBlocks.stream().anyMatch(b -> p.level.getBlockState(blockRes.getBlockPos()).is(b));
            }
        }
        if(res instanceof EntityHitResult)
        {
            if(targetEntities == null) return hitsAllEntities;
            else return targetEntities.contains(((EntityHitResult) res).getEntity().getType());
        }

        return false;
    }
}
