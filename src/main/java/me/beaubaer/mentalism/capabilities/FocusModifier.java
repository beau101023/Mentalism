package me.beaubaer.mentalism.capabilities;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;

public abstract class FocusModifier
{
    Focus parent;

    public FocusModifier(Focus parent)
    {
        this.parent = parent;
    }

    public abstract float apply(float initialValue);

    public abstract void saveNBTData(ListTag nbt);

    public abstract void loadNBTData(CompoundTag nbt);

    public abstract boolean shouldCopy();

    public abstract boolean shouldSave();
}
