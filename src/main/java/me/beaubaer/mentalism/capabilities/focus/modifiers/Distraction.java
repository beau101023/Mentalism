package me.beaubaer.mentalism.capabilities.focus.modifiers;

import me.beaubaer.mentalism.capabilities.focus.modifiers.abstractmodifiers.DecayingFocusModifier;
import me.beaubaer.mentalism.capabilities.focus.Focus;

public class Distraction extends DecayingFocusModifier
{
    public static final String SPELL_DISTRACTION = "mentalism.distraction.spell";
    public static final int SPELL_DISTRACTION_PRIORITY = 3;

    public static final String SOUND_DISTRACTION = "mentalism.distraction.sound";

    public Distraction(Focus parent, int priority, float amount, float decayTime, String ID)
    {
        super(parent, priority, amount, decayTime, ID);
    }

    @Override
    public float apply(float initialValue)
    {
        return Math.max(initialValue - amount, 0.0f);
    }
}
