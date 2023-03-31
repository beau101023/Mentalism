package me.beaubaer.mentalism.spells.strategies.activations;

import me.beaubaer.mentalism.capabilities.focus.FocusProvider;
import me.beaubaer.mentalism.capabilities.focus.modifiers.Distraction;
import me.beaubaer.mentalism.spells.strategies.interfaces.ActivationStrategy;
import net.minecraft.world.entity.player.Player;

public class DistractFocus implements ActivationStrategy
{
    private float amount;
    private float duration;

    public DistractFocus(float amount, float duration)
    {
        this.amount = amount;
        this.duration = duration;
    }

    public void activate(Player p)
    {
        p.getCapability(FocusProvider.FOCUS).ifPresent(f ->
        {
            if(f.hasModifier(Distraction.SPELL_DISTRACTION))
            {
                Distraction d = f.getModifier(Distraction.SPELL_DISTRACTION, Distraction.class);
                d.setAmount(d.getAmount()+amount);
                d.setDuration(d.getDuration()+duration);
            }
            else
                f.putModifier(new Distraction(f, Distraction.SPELL_DISTRACTION_PRIORITY, amount, duration, Distraction.SPELL_DISTRACTION));
        });
    }
}
