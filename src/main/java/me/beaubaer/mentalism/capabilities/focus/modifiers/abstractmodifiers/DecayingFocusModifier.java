package me.beaubaer.mentalism.capabilities.focus.modifiers.abstractmodifiers;

import me.beaubaer.mentalism.capabilities.focus.Focus;

public abstract class DecayingFocusModifier extends TickingFocusModifier
{
    protected final float maxAmount;
    protected float amount;
    protected float duration;
    protected float tickDecayRate;

    public DecayingFocusModifier(short priority, float amount, float duration, String ID)
    {
        super(priority, ID);

        this.maxAmount = amount;
        this.amount = amount;
        this.duration = duration;

        updateTickDecayAmount();
    }

    private void updateTickDecayAmount()
    {
        tickDecayRate = maxAmount/(duration *20);
    }

    @Override
    public void update()
    {
        this.decay();
    }

    protected void decay()
    {
        this.amount -= tickDecayRate;

        if(this.amount <= 0.0f)
        {
            this.parent.removeModifier(this);
        }
    }

    public void setDuration(float decayTime)
    {
        this.duration = decayTime;
        updateTickDecayAmount();
    }

    public float getDuration()
    {
        return this.duration;
    }

    public float getAmount() { return this.amount; }

    public void setAmount(float amount) { this.amount = amount; }

    public float getTickDecayRate() { return this.tickDecayRate; }
}
