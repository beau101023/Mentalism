package me.beaubaer.mentalism.spells.strategies.activations;

import me.beaubaer.mentalism.capabilities.focus.FocusProvider;
import net.minecraft.world.entity.player.Player;

public class InterruptFocus
{
    public static void activate(Player p)
    {
        p.getCapability(FocusProvider.FOCUS).ifPresent(f ->
        {
            f.setFocusing(false);
        });
    }
}
