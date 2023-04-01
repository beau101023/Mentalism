package me.beaubaer.mentalism.capabilities.focus.modifiers;

import me.beaubaer.mentalism.capabilities.focus.ModifierPriority;
import me.beaubaer.mentalism.capabilities.focus.modifiers.abstractmodifiers.DecayingFocusModifier;

public class Distraction extends DecayingFocusModifier
{
    public static final Distraction SPELL_DISTRACTION = new Distraction(ModifierPriority.AFTER_LEVEL, 0.5f, 10.0f, "mentalism.distraction.spell");
    public static final Distraction SOUND_DISTRACTION = new Distraction(ModifierPriority.AFTER_LEVEL, 1.0f, 1.0f,"mentalism.distraction.sound");

    public Distraction(short priority, float amount, float decayTime, String ID)
    {
        super(priority, amount, decayTime, ID);
    }

    @Override
    public float apply(float initialValue)
    {
        return Math.max(initialValue - amount, 0.0f);
    }
}
