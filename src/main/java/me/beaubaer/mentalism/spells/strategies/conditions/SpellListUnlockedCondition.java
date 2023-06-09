package me.beaubaer.mentalism.spells.strategies.conditions;

import me.beaubaer.mentalism.capabilities.unlocks.ProgressionStateProvider;
import me.beaubaer.mentalism.spells.Spell;
import net.minecraft.world.entity.player.Player;

import java.util.List;

// condition that checks if the player has unlocked all spells in a list
public class SpellListUnlockedCondition
{
    final List<Spell> spells;

    // constructor
    public SpellListUnlockedCondition(List<Spell> spells)
    {
        this.spells = spells;
    }

    // check UnlockManager to see if the spells are unlocked
    public boolean check(Player p)
    {
        for(Spell spell : spells)
        {
            if(!p.getCapability(ProgressionStateProvider.PROGRESSION_STATE).map(u -> u.isUnlocked(spell)).orElse(false))
            {
                return false;
            }
        }

        return true;
    }
}
