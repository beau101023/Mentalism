package me.beaubaer.mentalism.capabilities;

public interface IFocusModifier
{
    boolean afterFocusLevel = false;

    FocusCapability parent = null;

    public abstract float apply(float initialValue);

    public abstract boolean isAfterFocusLevel();
}
