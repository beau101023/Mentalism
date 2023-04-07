package me.beaubaer.mentalism.spells.strategies.conditions;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.HitResult;

import java.util.function.Predicate;

public class MouseOnPickableInMeleeRangeCondition
{
    public static boolean check(Player p, Predicate<HitResult> predicate)
    {
        double hitDist = p.getReachDistance();
        HitResult res = p.pick(hitDist, 0.0f, false);

        if(res.getType() != HitResult.Type.MISS)
        {
            return predicate.test(res);
        }

        return false;
    }
}
