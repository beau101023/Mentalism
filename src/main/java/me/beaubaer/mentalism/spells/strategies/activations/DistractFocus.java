package me.beaubaer.mentalism.spells.strategies.activations;

import me.beaubaer.mentalism.capabilities.focus.FocusProvider;
import me.beaubaer.mentalism.capabilities.focus.modifiers.Distraction;
import net.minecraft.world.entity.player.Player;

public class DistractFocus
{
    public static void activate(Player p, Distraction d)
    {
        p.getCapability(FocusProvider.FOCUS).ifPresent(f ->
        {
                f.putModifier(d);
        });
    }

    public static void activate(Player p)
    {
        p.getCapability(FocusProvider.FOCUS).ifPresent(f ->
        {
            f.putModifier(Distraction.SPELL_DISTRACTION);
        });
    }


}
