package me.beaubaer.mentalism.spells.strategies.conditions;

import me.beaubaer.mentalism.capabilities.focus.FocusProvider;
import net.minecraft.world.entity.player.Player;

public class PlayerFocusCondition
{
    private final float focusThreshold;
    private final boolean greaterThan;

    public PlayerFocusCondition(float focusThreshold, boolean greaterThan)
    {
        this.focusThreshold = focusThreshold;
        this.greaterThan = greaterThan;
    }

    public boolean check(Player p)
    {
        if(greaterThan)
        {
            return p.getCapability(FocusProvider.FOCUS).map(f -> f.getFocusPower() > focusThreshold).orElse(false);
        }
        else
        {
            return p.getCapability(FocusProvider.FOCUS).map(f -> f.getFocusPower() < focusThreshold).orElse(false);
        }
    }
}
