package me.beaubaer.mentalism.capabilities;

public abstract class TickingFocusModifier extends FocusModifier
{
    public TickingFocusModifier(Focus parent, int priority)
    {
        super(parent, priority);
    }

    public abstract void update();
}