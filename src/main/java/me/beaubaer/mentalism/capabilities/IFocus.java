package me.beaubaer.mentalism.capabilities;

public interface IFocus
{
    public void setFocusing(boolean focusing);

    public boolean getFocusing();

    public float getFocusPower();

    public float getFocusLevel();

    public void setFocusLevel(float focusLevel);

    // should be called every player tick
    public void updateFocus();
}
