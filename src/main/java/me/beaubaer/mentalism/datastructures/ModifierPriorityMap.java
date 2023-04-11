package me.beaubaer.mentalism.datastructures;

import me.beaubaer.mentalism.capabilities.focus.modifiers.abstractmodifiers.FocusModifier;

import java.util.ArrayList;
import java.util.TreeMap;

public class ModifierPriorityMap extends TreeMap<Short, ArrayList<FocusModifier>>
{
    public void add(FocusModifier fm)
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

        this.values().forEach(all::addAll);

        return all;
    }

    public ArrayList<FocusModifier> collectPriority(short toCollect)
    {
        return this.get(toCollect);
    }

    public <T extends FocusModifier> ArrayList<T> collectType(Class<T> modifierType)
    {
        return (ArrayList<T>) this.collectAll().stream().filter(modifierType::isInstance).map(modifierType::cast).toList();
    }
}
