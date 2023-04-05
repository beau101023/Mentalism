package me.beaubaer.mentalism.capabilities.unlocks;

import me.beaubaer.mentalism.registries.SpellRegistry;
import me.beaubaer.mentalism.spells.Spell;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.ArrayList;
import java.util.List;

public class UnlockState
{
    // list of unlocked spells
    final List<Spell> unlockedSpells = new ArrayList<>();

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
        unlock(SpellRegistry.ROCK_CHIPPER.get());
        unlock(SpellRegistry.SHOOT_ARROW.get());
        unlock(SpellRegistry.MINERAL_SIPHON.get());
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

    public void copyFrom(UnlockState oldStore)
    {
        unlockedSpells.clear();
        unlockedSpells.addAll(oldStore.unlockedSpells);
    }

    public void saveNBTData(CompoundTag nbt)
    {
        ListTag spellListTag = new ListTag();

        for (Spell spell : unlockedSpells)
        {
            CompoundTag spellTag = new CompoundTag();
            spellTag.putString("spell", spell.getRegistryName().toString());
            spellListTag.add(spellTag);
        }

        nbt.put("spells", spellListTag);
    }

    public void loadNBTData(CompoundTag nbt)
    {
        ListTag spellListTag = nbt.getList("spells", 10);

        for (Tag tag : spellListTag)
        {
            CompoundTag spellTag = (CompoundTag) tag;
            IForgeRegistry<Spell> spellRegistry = SpellRegistry.SPELLS.get();

            Spell spell = spellRegistry.getValue(new ResourceLocation(spellTag.getString("spell")));
        }
    }
}
