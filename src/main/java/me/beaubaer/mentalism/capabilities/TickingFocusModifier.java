package me.beaubaer.mentalism.capabilities;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;

public abstract class TickingFocusModifier extends FocusModifier
{
    public TickingFocusModifier(Focus parent, int priority)
    {
        super(parent, priority);
    }

    public abstract void update();
}
