package me.beaubaer.mentalism.capabilities.focus.modifiers;

import me.beaubaer.mentalism.capabilities.focus.ModifierPriority;
import me.beaubaer.mentalism.capabilities.focus.modifiers.abstractmodifiers.FocusModifier;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;

public class FocusLevel extends FocusModifier
{
    public static final FocusLevel DEFAULT_FOCUSLEVEL = new FocusLevel(ModifierPriority.LEVEL, 1.0f, "focuslevel.default");

    private float level;

    public FocusLevel(CompoundTag nbt)
    {
        super(nbt);
    }

    public FocusLevel(short priority, float level, String ID)
    {
        super(priority, ID);
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
        CompoundTag thisData = saveCoreNBTData();
        thisData.putFloat("level", level);
        nbt.add(thisData);
    }

    @Override
    public void loadNBTData(CompoundTag nbt)
    {
        loadCoreNBTData(nbt);
        level = nbt.getFloat("level");
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
