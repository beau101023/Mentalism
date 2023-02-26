package me.beaubaer.mentalism.capabilities.focus.modifiers;

import me.beaubaer.mentalism.capabilities.focus.modifiers.abstractmodifiers.DecayingFocusModifier;
import me.beaubaer.mentalism.capabilities.focus.Focus;

public class AntiDistraction extends DecayingFocusModifier
{
    public static final String BELL_ANTIDISTRACTION = "mentalism.antidistraction.bell";

    public AntiDistraction(Focus parent, int priority, float amount, float decayTime, String ID)
    {
        super(parent, priority, amount, decayTime, ID);
    }

    public float apply(float initialValue)
    {
        if(parent.getFocusing())
            return initialValue + amount;
        else return initialValue;
    }
}