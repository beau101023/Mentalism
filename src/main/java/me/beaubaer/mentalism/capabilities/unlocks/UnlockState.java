package me.beaubaer.mentalism.capabilities.unlocks;

import me.beaubaer.mentalism.registries.SpellRegistry;
import me.beaubaer.mentalism.spells.Spell;

import java.util.ArrayList;
import java.util.List;

public class UnlockState
{
    // list of unlocked spells
    List<Spell> unlockedSpells = new ArrayList<>();

    public UnlockState()
    {
        // future spells
        /*unlockedSpells.add(Spell.FIREBALL);
        unlockedSpells.add(Spell.FLASH);
        unlockedSpells.add(Spell.FREEZE);
        unlockedSpells.add(Spell.LIGHTNING);
        unlockedSpells.add(Spell.MINDREAD);
        unlockedSpells.add(Spell.PUSH);
        unlockedSpells.add(Spell.SLOW);
        unlockedSpells.add(Spell.STUN);
        unlockedSpells.add(Spell.TELEKINESIS);
        unlockedSpells.add(Spell.TELEPORT);*/

        // add default unlocked spells
        unlockedSpells.add(SpellRegistry.ROCK_CHIPPER.get());
        unlockedSpells.add(SpellRegistry.SHOOT_ARROW.get());
    }

    // test if a spell is unlocked
    public boolean isUnlocked(Spell s)
    {
        return unlockedSpells.contains(s);
    }

    // unlock a spell
    public void unlock(Spell s)
    {
        unlockedSpells.add(s);
    }
}
