package me.beaubaer.mentalism.capabilities.focus.modifiers.abstractmodifiers;

import me.beaubaer.mentalism.capabilities.focus.Focus;

public abstract class DecayingFocusModifier extends TickingFocusModifier
{
    protected float maxAmount;
    protected float amount;
    protected float decayTime;
    protected float tickDecayRate;

    public DecayingFocusModifier(Focus parent, int priority, float amount, float decayTime, String ID)
    {
        super(parent, priority, ID);

        this.maxAmount = amount;
        this.amount = amount;
        this.decayTime = decayTime;

        updateTickDecayAmount();
    }

    private void updateTickDecayAmount()
    {
        tickDecayRate = maxAmount/(decayTime*20);
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

    public void setDecayTime(float decayTime)
    {
        this.decayTime = decayTime;
        updateTickDecayAmount();
    }

    public float getDecayTime()
    {
        return this.decayTime;
    }

    public float getAmount() { return this.amount; }

    public float getTickDecayRate() { return this.tickDecayRate; }
}
