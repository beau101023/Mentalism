package me.beaubaer.mentalism.capabilities.focus.modifiers;

import me.beaubaer.mentalism.capabilities.focus.ModifierPriority;
import me.beaubaer.mentalism.capabilities.focus.modifiers.abstractmodifiers.DecayingFocusModifier;

public class AntiDistraction extends DecayingFocusModifier
{
    public static final AntiDistraction BELL_ANTIDISTRACTION = new AntiDistraction(ModifierPriority.AFTER_LEVEL, 0.5f, 10, "mentalism.antidistraction.bell");

    public AntiDistraction(short priority, float amount, float decayTime, String ID)
    {
        super(priority, amount, decayTime, ID);
    }

    public float apply(float initialValue)
    {
        if(parent.getFocusing())
            return initialValue + amount;
        else return initialValue;
    }
}