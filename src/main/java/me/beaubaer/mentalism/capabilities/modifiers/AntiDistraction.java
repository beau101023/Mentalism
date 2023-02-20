package me.beaubaer.mentalism.capabilities.modifiers;

import me.beaubaer.mentalism.capabilities.Focus;
import me.beaubaer.mentalism.capabilities.TickingFocusModifier;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;

public class AntiDistraction extends TickingFocusModifier
{
    public static String BELL_ANTIDISTRACTION = "mentalism.antidistraction.bell";

    float maxAmount;
    float amount;
    float decayTime;

    private float tickDecayAmount;

    // what kind of distraction this is, used to determine some things in the future
    String typeID;

    public AntiDistraction(Focus parent, int priority, float amount, float decayTime, String typeID)
    {
        super(parent, priority);
        this.maxAmount = amount;
        this.amount = amount;
        this.typeID = typeID;
        this.decayTime = decayTime;

        tickDecayAmount = maxAmount/(decayTime*20);
    }

    @Override
    public float apply(float initialValue)
    {
        if(parent.getFocusing())
            return initialValue + amount;
        else return initialValue;
    }

    @Override
    public void saveNBTData(ListTag nbt)
    {
        return;
    }

    @Override
    public void loadNBTData(CompoundTag nbt)
    {
        return;
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
        this.decay();
    }

    private void decay()
    {
        this.amount -= tickDecayAmount;

        if(this.amount <= 0.0f)
        {
            this.parent.removeModifier(this);
        }
    }
}