package me.beaubaer.mentalism.spells.strategies.activations;

import me.beaubaer.mentalism.capabilities.focus.FocusProvider;
import me.beaubaer.mentalism.spells.strategies.interfaces.ActivationStrategy;
import net.minecraft.world.entity.player.Player;

public class InterruptFocus implements ActivationStrategy
{
    @Override
    public void activate(Player p)
    {
        p.getCapability(FocusProvider.FOCUS).ifPresent(f ->
        {
            f.setFocusing(false);
        });
    }
}
