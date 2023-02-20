package me.beaubaer.mentalism.datastructures;

import me.beaubaer.mentalism.capabilities.FocusModifier;

import java.util.ArrayList;
import java.util.TreeMap;

public class ModifierPriorityMap extends TreeMap<Integer, ArrayList<FocusModifier>>
{
    public void put(FocusModifier fm)
    {
        if(!this.containsKey(fm.priority))
        {
            this.put(fm.priority, new ArrayList<>());
        }
        this.get(fm.priority).add(fm);
    }

    public void remove(FocusModifier fm)
    {
        this.collectPriority(fm.priority).remove(fm);
    }

    public ArrayList<FocusModifier> collectAll()
    {
        ArrayList<FocusModifier> all = new ArrayList<>();

        for (ArrayList<FocusModifier> list : this.values())
        {
            all.addAll(list);
        }

        return all;
    }

    public ArrayList<FocusModifier> collectPriority(int toCollect)
    {
        return this.get(toCollect);
    }

    public <T extends FocusModifier> ArrayList<T> collectType(Class<T> modifierType)
    {
        ArrayList<T> selected = new ArrayList<>();

        for(FocusModifier fm : this.collectAll())
        {
            if (modifierType.isInstance(fm))
            {
                selected.add((T)fm);
            }
        }

        return selected;
    }
}
