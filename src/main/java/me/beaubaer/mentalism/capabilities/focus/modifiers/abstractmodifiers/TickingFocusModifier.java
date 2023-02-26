package me.beaubaer.mentalism.capabilities.focus.modifiers.abstractmodifiers;

import me.beaubaer.mentalism.capabilities.focus.Focus;

public abstract class TickingFocusModifier extends FocusModifier
{
    public TickingFocusModifier(Focus parent, int priority, String ID)
    {
        super(parent, priority, ID);
    }

    public abstract void update();
}