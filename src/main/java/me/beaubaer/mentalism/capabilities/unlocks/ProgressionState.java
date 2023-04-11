package me.beaubaer.mentalism.capabilities.unlocks;

import me.beaubaer.mentalism.capabilities.focus.modifiers.FocusLevel;
import me.beaubaer.mentalism.spells.SpellRegistry;
import me.beaubaer.mentalism.spells.Spell;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProgressionState
{
    // list of unlocks
    List<String> stringUnlocks = new ArrayList<>();
    Map<String, Float> floatProgressionValues = new HashMap<>();

    public ProgressionState()
    {
        // add default unlocked spells
        unlock(SpellRegistry.ROCK_CHIPPER.get());
        unlock(SpellRegistry.SHOOT_ARROW.get());
        unlock(SpellRegistry.MINERAL_SIPHON.get());
        unlock(SpellRegistry.AIR_WALK.get());

        // add default progression values
        setProgressionValue("focusLevel", 1.0f);
    }

    private void setProgressionValue(String ID, float v)
    {
        floatProgressionValues.put(ID, v);
    }

    public FocusLevel getFocusLevel()
    {
        FocusLevel fl = FocusLevel.DEFAULT_FOCUSLEVEL.copy();
        fl.setLevel(getProgressionValue("focusLevel"));
        return fl;
    }

    private float getProgressionValue(String ID)
    {
        return floatProgressionValues.get(ID);
    }

    // test if a spell is unlocked
    public boolean isUnlocked(Spell s)
    {
        return stringUnlocks.contains(s.getSpellID());
    }

    // unlock a spell
    public void unlock(Spell s)
    {
        stringUnlocks.add(s.getSpellID());
    }

    public void copyFrom(ProgressionState oldStore)
    {
        stringUnlocks.clear();
        stringUnlocks.addAll(oldStore.stringUnlocks);
    }

    public void saveNBTData(CompoundTag nbt)
    {
        ListTag stringUnlockTags = new ListTag();

        for (String str : stringUnlocks)
        {
            StringTag tag = StringTag.valueOf(str);
            stringUnlockTags.add(tag);
        }

        nbt.put("stringUnlocks", stringUnlockTags);

        ListTag floatProgressionTags = new ListTag();

        for (Map.Entry<String, Float> entry : floatProgressionValues.entrySet())
        {
            CompoundTag tag = new CompoundTag();
            tag.putString("ID", entry.getKey());
            tag.putFloat("value", entry.getValue());
            floatProgressionTags.add(tag);
        }

        nbt.put("floatProgressionValues", floatProgressionTags);
    }

    public void loadNBTData(CompoundTag nbt)
    {
        ListTag stringUnlockTags = nbt.getList("stringUnlocks", Tag.TAG_STRING);

        for (Tag tag : stringUnlockTags)
        {
            String str = tag.getAsString();
            stringUnlocks.add(str);
        }

        ListTag floatProgressionTags = nbt.getList("floatProgressionValues", Tag.TAG_COMPOUND);

        for (Tag tag : floatProgressionTags)
        {
            CompoundTag compoundTag = (CompoundTag) tag;
            String ID = compoundTag.getString("ID");
            float value = compoundTag.getFloat("value");
            floatProgressionValues.put(ID, value);
        }
    }
}
