package me.beaubaer.mentalism.spells.strategies.conditions;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

import java.util.List;
import java.util.function.Predicate;

public class MouseOnPickableInMeleeRangeCondition
{
    public static boolean check(Player p, Predicate<HitResult> predicate)
    {
        HitResult res = Minecraft.getInstance().hitResult;

        if(res != null && res.getType() != HitResult.Type.MISS)
        {
            return predicate.test(res);
        }

        return false;
    }
}
