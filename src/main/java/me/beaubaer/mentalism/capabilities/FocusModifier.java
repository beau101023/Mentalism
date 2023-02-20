package me.beaubaer.mentalism.capabilities;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;

public abstract class FocusModifier
{
    public int priority;

    protected Focus parent;

    public FocusModifier(Focus parent, CompoundTag tag)
    {
        this.parent = parent;
        this.loadNBTData(tag);
    }

    public FocusModifier(Focus parent, int priority)
    {
        this.parent = parent;
        this.priority = priority;
    }

    public abstract float apply(float initialValue);

    public abstract void saveNBTData(ListTag nbt);

    public abstract void loadNBTData(CompoundTag nbt);

    public abstract boolean shouldCopy();

    public abstract boolean shouldSave();
}
