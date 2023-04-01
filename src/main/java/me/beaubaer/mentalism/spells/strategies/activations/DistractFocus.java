package me.beaubaer.mentalism.spells.strategies.activations;

import me.beaubaer.mentalism.capabilities.focus.FocusProvider;
import me.beaubaer.mentalism.capabilities.focus.modifiers.Distraction;
import me.beaubaer.mentalism.spells.strategies.interfaces.ActivationStrategy;
import net.minecraft.world.entity.player.Player;

public class DistractFocus implements ActivationStrategy
{
    Distraction spellDistraction = Distraction.SPELL_DISTRACTION;

    public DistractFocus()
    {

    }

    public DistractFocus(Distraction specialDistraction)
    {
        spellDistraction = specialDistraction;
    }

    public void activate(Player p)
    {
        p.getCapability(FocusProvider.FOCUS).ifPresent(f ->
        {
                f.putModifier(spellDistraction);
        });
    }
}
