package me.beaubaer.mentalism.spells.strategies.conditions;

import me.beaubaer.mentalism.spells.strategies.interfaces.ConditionCheckStrategy;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

import java.util.List;

public class PlayerLookingAtPickableCondition implements ConditionCheckStrategy
{
    private final List<Block> targetBlocks;
    private final List<EntityType> targetEntities;
    private boolean hitsAllEntities = false;
    private boolean hitsAllBlocks = false;
    private float checkDistance = -1f;

    public PlayerLookingAtPickableCondition(List<Block> targetBlocks, List<EntityType> targetEntities, boolean hitsAllEntities, boolean hitsAllBlocks, float checkDistance)
    {
        this.targetBlocks = targetBlocks;
        this.targetEntities = targetEntities;
        this.hitsAllEntities = hitsAllEntities;
        this.hitsAllBlocks = hitsAllBlocks;
        this.checkDistance = checkDistance;
    }

    @Override
    public boolean check(Player p)
    {
        HitResult res = p.pick(checkDistance, 0, false);

        if(res == null || res.getType() == HitResult.Type.MISS)
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
                boolean isTargetBlock = targetBlocks.stream().anyMatch(b -> p.level.getBlockState(blockRes.getBlockPos()).is(b));
                return isTargetBlock;
            }
        }
        if(res instanceof EntityHitResult)
        {
            if(targetEntities == null)
            {
                return hitsAllEntities;
            }
            else if(targetEntities.contains(((EntityHitResult) res).getEntity().getType()))
            {
                return true;
            }
        }

        return false;
    }
}
