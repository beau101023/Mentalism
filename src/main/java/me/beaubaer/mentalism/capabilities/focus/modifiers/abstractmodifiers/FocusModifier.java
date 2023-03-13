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

    protected CompoundTag saveCoreNBTData()
    {
        CompoundTag thisData = new CompoundTag();
        thisData.putString("type", this.getClass().getCanonicalName());
        thisData.putInt("priority", priority);
        thisData.putString("ID", ID);
        return thisData;
    }

    public void loadNBTData(CompoundTag nbt) {}

    protected void loadCoreNBTData(CompoundTag nbt)
    {
        this.priority = nbt.getInt("priority");
        this.ID = nbt.getString("ID");
    }

    public boolean shouldCopy() { return false; }

    public boolean shouldSave() { return false; }
}
