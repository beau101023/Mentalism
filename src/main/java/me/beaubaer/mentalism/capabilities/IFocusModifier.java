package me.beaubaer.mentalism.capabilities;

public interface IFocusModifier
{
    Focus parent = null;

    public abstract float apply(float initialValue);

    public abstract boolean isAfterFocusLevel();
}
