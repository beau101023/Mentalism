package me.beaubaer.mentalism.capabilities.focus.modifiers.abstractmodifiers;

import me.beaubaer.mentalism.capabilities.focus.Focus;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;

public abstract class FocusModifier
{
    public int priority;

    public String ID;
    protected Focus parent;

    public FocusModifier(Focus parent, CompoundTag tag)
    {
        this.parent = parent;
        this.loadNBTData(tag);
    }

    public FocusModifier(Focus parent, int priority, String ID)
    {
        this.parent = parent;
        this.priority = priority;
        this.ID = ID;
    }

    public abstract float apply(float initialValue);

    public void saveNBTData(ListTag nbt) {}

    public void loadNBTData(CompoundTag nbt) {}

    public boolean shouldCopy() { return false; }

    public boolean shouldSave() { return false; }
}
