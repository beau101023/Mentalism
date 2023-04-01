package me.beaubaer.mentalism.capabilities.focus.modifiers.abstractmodifiers;

public abstract class TickingFocusModifier extends FocusModifier
{
    public TickingFocusModifier(short priority, String ID)
    {
        super(priority, ID);
    }

    public abstract void update();
}