package me.beaubaer.mentalism.capabilities.focus.modifiers;

import me.beaubaer.mentalism.capabilities.focus.modifiers.abstractmodifiers.FocusModifier;
import me.beaubaer.mentalism.capabilities.focus.Focus;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;

public class FocusLevel extends FocusModifier
{
    public static final String UNIVERSAL_FOCUSLEVEL = "mentalism.focuslevel.universal";

    private float level;

    public FocusLevel(Focus parent, CompoundTag tag)
    {
        super(parent, tag);
    }

    public FocusLevel(Focus parent, float level, String ID)
    {
        super(parent, 3, ID);
        this.level = level;
    }

    public void setLevel(float level)
    {
        this.level = level;
    }

    public float getLevel()
    {
        return this.level;
    }

    @Override
    public float apply(float initialValue)
    {
        return initialValue * level;
    }

    @Override
    public void saveNBTData(ListTag nbt)
    {
        CompoundTag thisData = new CompoundTag();
        thisData.putString("type", this.getClass().getCanonicalName());
        thisData.putFloat("level", level);
        thisData.putInt("priority", priority);
        nbt.add(thisData);
    }

    @Override
    public void loadNBTData(CompoundTag nbt)
    {
        level = nbt.getFloat("level");
        priority = nbt.getInt("priority");
    }

    @Override
    public boolean shouldCopy()
    {
        return true;
    }

    @Override
    public boolean shouldSave()
    {
        return true;
    }
}
