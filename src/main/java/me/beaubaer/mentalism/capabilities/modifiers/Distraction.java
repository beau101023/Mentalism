package me.beaubaer.mentalism.capabilities.modifiers;

import me.beaubaer.mentalism.capabilities.Focus;
import me.beaubaer.mentalism.capabilities.TickingFocusModifier;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;

public class Distraction extends TickingFocusModifier
{
    public Distraction(Focus parent, int priority)
    {
        super(parent, priority);
    }

    @Override
    public float apply(float initialValue)
    {
        return 0;
    }

    @Override
    public void saveNBTData(ListTag nbt)
    {

    }

    @Override
    public void loadNBTData(CompoundTag nbt)
    {

    }

    @Override
    public boolean shouldCopy()
    {
        return false;
    }

    @Override
    public boolean shouldSave()
    {
        return false;
    }

    @Override
    public void update()
    {

    }
}
