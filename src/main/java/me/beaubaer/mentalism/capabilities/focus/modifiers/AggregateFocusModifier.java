package me.beaubaer.mentalism.capabilities.focus.modifiers;

import me.beaubaer.mentalism.capabilities.focus.modifiers.abstractmodifiers.FocusModifier;
import net.minecraft.nbt.CompoundTag;

import java.util.ArrayList;
import java.util.List;

public class AggregateFocusModifier extends FocusModifier
{
    // this class will be used to aggregate multiple focus modifiers under one ID
    // this will allow for multiple modifiers with different durations, amounts, etc but the same source
    // to be applied to the same focus

    private List<FocusModifier> modifiers = new ArrayList<>();

    public AggregateFocusModifier(CompoundTag tag)
    {
        super(tag);
    }

    public AggregateFocusModifier(short priority, String ID, List<FocusModifier> modifiers)
    {
        super(priority, ID);
        this.modifiers.addAll(modifiers);
    }

    @Override
    public float apply(float initialValue)
    {
        float result = initialValue;
        for (FocusModifier modifier : modifiers)
        {
            result = modifier.apply(result);
        }
        return result;
    }

    public void addModifier(FocusModifier modifier)
    {
        this.modifiers.add(modifier);
    }

    public void removeModifier(FocusModifier modifier)
    {
        this.modifiers.remove(modifier);
    }
}
