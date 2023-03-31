package me.beaubaer.mentalism.capabilities.focus;

public interface IFocus
{
    void setFocusing(boolean focusing);

    boolean getFocusing();

    float getFocusPower();

    // should be called every player tick
    void update();
}
