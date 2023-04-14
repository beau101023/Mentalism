package me.beaubaer.mentalism.capabilities.focus.modifiers.abstractmodifiers;

import me.beaubaer.mentalism.capabilities.focus.Focus;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraftforge.registries.ForgeRegistryEntry;

public abstract class FocusModifier extends ForgeRegistryEntry<FocusModifier> implements Cloneable
{
    public short priority;

    protected String ID;
    protected Focus parent;

    public FocusModifier(CompoundTag tag)
    {
        this.loadNBTData(tag);
    }

    public FocusModifier(short priority, String ID)
    {
        this.priority = priority;
        this.ID = ID;
    }

    public void intitializeParent(Focus parent)
    {
        this.parent = parent;
    }

    public abstract float apply(float initialValue);

    public String getID()
    {
        return this.ID;
    }

    public void saveNBTData(ListTag nbt) {}

    protected CompoundTag saveCoreNBTData()
    {
        CompoundTag thisData = new CompoundTag();
        thisData.putString("type", this.getClass().getCanonicalName());
        thisData.putShort("priority", priority);
        thisData.putString("ID", ID);
        return thisData;
    }

    public void loadNBTData(CompoundTag nbt) {}

    protected void loadCoreNBTData(CompoundTag nbt)
    {
        this.priority = nbt.getShort("priority");
        this.ID = nbt.getString("ID");
    }

    public boolean shouldCopy() { return false; }

    public boolean shouldSave() { return false; }

    public <T extends FocusModifier> T copy()
    {
        try
        {
            return (T) this.clone();
        }
        catch (CloneNotSupportedException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    // Comparison performed by ID only rather than by reference
    public boolean equals(Object other)
    {
        if (other instanceof FocusModifier otherModifier)
        {
            return this.ID.equals(otherModifier.ID) && this.parent.equals(otherModifier.parent);
        }
        return false;
    }
}
