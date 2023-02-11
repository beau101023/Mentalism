package me.beaubaer.mentalism.capabilities;

import net.minecraft.nbt.FloatTag;
import net.minecraft.nbt.Tag;
import net.minecraft.util.Mth;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.ArrayList;

public class FocusCapability implements INBTSerializable<Tag>
{
    protected float focus;
    protected float focusLevel;
    protected boolean focusing;
    public ArrayList<IFocusModifier> modifiers;

    public FocusCapability(float focusLevel)
    {
        this.focusLevel = focusLevel;
        this.focus = 0;
        focusing = false;
    }

    public void setFocusing(boolean focusing) { this.focusing = focusing; }

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

        focusPower = focusPower * getFocusLevel();

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
        if(focusing && focus < 1.0f)
            focus += 0.01f;
        else if(focus > 0.0f)
            focus -= 0.01f;
        else
            return;
    }

    @Override
    public Tag serializeNBT()
    {
        return FloatTag.valueOf(this.getFocusLevel());
    }

    @Override
    public void deserializeNBT(Tag nbt)
    {
        if(!(nbt instanceof FloatTag floatNbt))
            throw new IllegalArgumentException("AAA WHAT THE FUCK??!?");
        this.focusLevel = floatNbt.getAsFloat();
    }
}
