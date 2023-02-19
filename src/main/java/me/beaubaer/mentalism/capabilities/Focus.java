package me.beaubaer.mentalism.capabilities;

import me.beaubaer.mentalism.datastructures.ModifierPriorityMap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

public class Focus implements IFocus
{
    protected float focus;
    protected boolean focusing;
    public ModifierPriorityMap modifiers;

    public Focus()
    {
        this.focus = 0.0f;
        focusing = false;
        modifiers = new ModifierPriorityMap();
    }

    public void setFocusing(boolean focusing) { this.focusing = focusing; }

    @Override
    public boolean getFocusing() {
        return this.focusing;
    }

    public float getFocusPower()
    {
        float focusPower = focus;

        for(FocusModifier fm : modifiers.collectAll())
        {
            focusPower = fm.apply(focusPower);
        }

        return focusPower;
    }

    public <T extends FocusModifier> ArrayList<T> getModifiers(Class<T> modifierType)
    {
        return modifiers.collectType(modifierType);
    }

    // should be called every player tick
    public void updateFocus()
    {
        if(focusing && focus >= 1.0f)
            return;
        if(!focusing && focus <=0.0f)
            return;

        if(focusing && (focus < 1.0f))
        {
            focus += 0.01f;
        }
        else if(!focusing && (focus > 0.0f))
        {
            focus -= 0.01f;
        }

        focus = Math.min(focus, 1.0f);
        focus = Math.max(focus, 0.0f);
    }

    public void updateModifiers()
    {
        for(TickingFocusModifier tfm : modifiers.collectType(TickingFocusModifier.class))
        {
            tfm.update();
        }
    }

    public void copyFrom(Focus other)
    {
        for(FocusModifier fm : other.modifiers.collectAll())
        {
            if(fm.shouldCopy())
            {
                this.modifiers.put(fm);
            }
        }
    }

    public void saveNBTData(CompoundTag nbt)
    {
        ListTag modifierList = new ListTag();
        for(FocusModifier fm : modifiers.collectAll())
        {
            if(fm.shouldSave())
            {
                fm.saveNBTData(modifierList);
            }
        }
        nbt.put("modifiers", modifierList);
    }

    public void loadNBTData(CompoundTag nbt)
    {
        ListTag modifierListTag = nbt.getList("modifiers", Tag.TAG_COMPOUND);

        modifiers = loadModifiers(modifierListTag);
    }

    // take a list of compoundTags and turn them into modifiers! yay!
    public ModifierPriorityMap loadModifiers(ListTag modifierList)
    {
        ModifierPriorityMap loadedModifiers = new ModifierPriorityMap();

        for(Object tag : modifierList.toArray())
        {
            FocusModifier fm = loadModifier((CompoundTag) tag);
            loadedModifiers.put(fm);
        }

        return loadedModifiers;
    }

    public FocusModifier loadModifier(CompoundTag nbt)
    {
        String className = nbt.getString("type");
        Class c;
        try
        {
            c = Class.forName(className);
        }
        catch (ClassNotFoundException e)
        {
            throw new RuntimeException("No modifier found with the corresponding class name.", e);
        }

        FocusModifier fm;
        try {
            fm = (FocusModifier) c.getDeclaredConstructor().newInstance(this);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }

        fm.loadNBTData(nbt);

        return fm;
    }
}
