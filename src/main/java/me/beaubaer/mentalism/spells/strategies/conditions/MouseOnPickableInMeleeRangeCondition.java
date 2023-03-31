package me.beaubaer.mentalism.spells.strategies.conditions;

import me.beaubaer.mentalism.spells.strategies.interfaces.ConditionCheckStrategy;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

import java.util.List;

public class MouseOnPickableInMeleeRangeCondition implements ConditionCheckStrategy
{
    private List<Block> targetBlocks;
    private List<EntityType> targetEntities;
    private boolean hitsAllEntities = false;
    private boolean hitsAllBlocks = false;
    public MouseOnPickableInMeleeRangeCondition(List<Block> targetBlocks, List<EntityType> targetEntities, boolean hitsAllEntities, boolean hitsAllBlocks)
    {
        this.targetBlocks = targetBlocks;
        this.targetEntities = targetEntities;
        this.hitsAllEntities = hitsAllEntities;
        this.hitsAllBlocks = hitsAllBlocks;
    }

    @Override
    public boolean check(Player p)
    {
        HitResult res = Minecraft.getInstance().hitResult;

        if(res != null && res.getType() != HitResult.Type.MISS)
        {
            if(res.getType() == HitResult.Type.ENTITY)
            {
                if(hitsAllEntities)
                    return true;

                if(targetEntities == null)
                    return false;
                else
                    return targetEntities.contains(((EntityHitResult) res).getEntity().getType());
            }
            else if(res.getType() == HitResult.Type.BLOCK)
            {
                if(hitsAllBlocks)
                    return true;

                if(targetBlocks == null)
                    return false;
                else
                    return targetBlocks.stream().anyMatch(b -> p.level.getBlockState(((BlockHitResult) res).getBlockPos()).is(b));
            }
        }

        return false;
    }
}
