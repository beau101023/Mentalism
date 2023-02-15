package me.beaubaer.mentalism.capabilities.modifiers;

import me.beaubaer.mentalism.capabilities.IFocusModifier;

public class FocusLevel implements IFocusModifier
{

    @Override
    public float apply(float initialValue) {
        return 0;
    }

    @Override
    public boolean isAfterFocusLevel() {
        return false;
    }
}
