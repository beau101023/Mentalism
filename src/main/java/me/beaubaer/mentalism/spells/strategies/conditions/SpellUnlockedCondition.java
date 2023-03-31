package me.beaubaer.mentalism.spells.strategies.conditions;

import me.beaubaer.mentalism.capabilities.unlocks.UnlockStateProvider;
import me.beaubaer.mentalism.spells.Spell;
import me.beaubaer.mentalism.spells.strategies.interfaces.ConditionCheckStrategy;
import net.minecraft.world.entity.player.Player;

public class SpellUnlockedCondition implements ConditionCheckStrategy
{
    Spell spell;

    // constructor
    public SpellUnlockedCondition(Spell spell)
    {
        this.spell = spell;
    }

    // check UnlockManager to see if the spell is unlocked
    @Override
    public boolean check(Player p)
    {
        return p.getCapability(UnlockStateProvider.UNLOCK_STATE).map(u -> u.isUnlocked(spell)).orElse(false);
    }
}
