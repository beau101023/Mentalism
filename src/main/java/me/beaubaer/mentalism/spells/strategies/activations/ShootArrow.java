package me.beaubaer.mentalism.spells.strategies.activations;

import me.beaubaer.mentalism.spells.strategies.interfaces.ActivationStrategy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Arrow;

public class ShootArrow implements ActivationStrategy
{
    public void activate(Player p)
    {
        Arrow a = new Arrow(p.level, p);

        a.shootFromRotation(p, p.getXRot(), p.getYRot(), 0.0f, 4.0f, 1.0f);
        a.setCritArrow(true);

        p.level.addFreshEntity(a);
    }
}
