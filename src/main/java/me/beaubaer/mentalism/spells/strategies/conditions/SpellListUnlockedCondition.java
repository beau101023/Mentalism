package me.beaubaer.mentalism.spells.strategies.conditions;

import me.beaubaer.mentalism.capabilities.unlocks.UnlockStateProvider;
import me.beaubaer.mentalism.spells.Spell;
import me.beaubaer.mentalism.spells.strategies.interfaces.ConditionCheckStrategy;
import net.minecraft.world.entity.player.Player;

import java.util.List;

// condition that checks if the player has unlocked all spells in a list
public class SpellListUnlockedCondition implements ConditionCheckStrategy
{
    List<Spell> spells;

    // constructor
    public SpellListUnlockedCondition(List<Spell> spells)
    {
        this.spells = spells;
    }

    // check UnlockManager to see if the spells are unlocked
    @Override
    public boolean check(Player p)
    {
        for(Spell spell : spells)
        {
            if(!p.getCapability(UnlockStateProvider.UNLOCK_STATE).map(u -> u.isUnlocked(spell)).orElse(false))
            {
                return false;
            }
        }

        return true;
    }
}
