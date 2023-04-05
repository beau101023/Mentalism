package me.beaubaer.mentalism.spells.strategies.activations;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class ExplodeAtCursor
{
    public static void activate(Player p)
    {
        Level l = p.getLevel();

        HitResult res = p.pick(128D, 0f, false);

        if(res == null || res.getType() == HitResult.Type.MISS)
            return;

        Vec3 expLoc = res.getLocation();
        l.explode(p, expLoc.x, expLoc.y, expLoc.z, 3f, Explosion.BlockInteraction.BREAK);
    }
}