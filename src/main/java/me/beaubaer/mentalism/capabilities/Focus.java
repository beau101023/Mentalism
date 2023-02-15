package me.beaubaer.mentalism.capabilities;

import net.minecraft.nbt.CompoundTag;

import java.util.ArrayList;

public class Focus implements IFocus
{
    protected float focus;
    protected float focusLevel;
    protected boolean focusing;
    public ArrayList<IFocusModifier> modifiers;

    public Focus(float focusLevel)
    {
        this.focusLevel = focusLevel;
        this.focus = 0.0f;
        focusing = false;
        modifiers = new ArrayList<IFocusModifier>();
    }

    public void setFocusing(boolean focusing) { this.focusing = focusing; }

    @Override
    public boolean getFocusing() {
        return this.focusing;
    }

    public float getFocusPower()
    {
        float focusPower = focus;

        for (IFocusModifier fm : modifiers)
        {
            if(!fm.isAfterFocusLevel())
            {
                focusPower = fm.apply(focusPower);
            }
        }

        focusPower *= getFocusLevel();

        for(IFocusModifier fm : modifiers)
        {
            if(fm.isAfterFocusLevel())
            {
                focusPower = fm.apply(focusPower);
            }
        }

        return focusPower;
    }

    public float getFocusLevel()
    {
        return this.focusLevel;
    }

    public void setFocusLevel(float focusLevel)
    {
        this.focusLevel = focusLevel;
    }

    // should be called every player tick
    public void updateFocus()
    {
        if(focusing && (focus < 1.0f))
        {
            focus += 0.01f;
        }
        else if(!focusing && (focus > 0.0f))
        {
            focus -= 0.01f;
        }

        focus = Math.min(focus, 1.0f);
        focus = Math.max(focus, 0.0f);
    }

    public void copyFrom(Focus other)
    {
        this.focusLevel = other.focusLevel;
    }

    public void saveNBTData(CompoundTag nbt)
    {
        nbt.putFloat("focusLevel", focusLevel);
    }

    public void loadNBTData(CompoundTag nbt)
    {
        focusLevel = nbt.getFloat("focusLevel");
    }
}
