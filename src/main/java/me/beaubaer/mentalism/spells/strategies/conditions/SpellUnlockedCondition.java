package me.beaubaer.mentalism.spells.strategies.conditions;

import me.beaubaer.mentalism.capabilities.unlocks.ProgressionStateProvider;
import me.beaubaer.mentalism.spells.Spell;
import net.minecraft.world.entity.player.Player;

public class SpellUnlockedCondition
{
    final Spell spell;

    // constructor
    public SpellUnlockedCondition(Spell spell)
    {
        this.spell = spell;
    }

    // check UnlockManager to see if the spell is unlocked
    public boolean check(Player p)
    {
        return p.getCapability(ProgressionStateProvider.PROGRESSION_STATE).map(u -> u.isUnlocked(spell)).orElse(false);
    }
}
