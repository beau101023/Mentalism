package me.beaubaer.mentalism.capabilities;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

public class Focus implements IFocus
{
    protected float focus;
    protected boolean focusing;
    public ArrayList<FocusModifier> modifiers;

    public Focus()
    {
        this.focus = 0.0f;
        focusing = false;
        modifiers = new ArrayList<>();
    }

    public void setFocusing(boolean focusing) { this.focusing = focusing; }

    @Override
    public boolean getFocusing() {
        return this.focusing;
    }

    public float getFocusPower()
    {
        float focusPower = focus;

        for (FocusModifier fm : modifiers)
        {
            focusPower = fm.apply(focusPower);
        }

        return focusPower;
    }

    public ArrayList<FocusModifier> getModifiers(Class<? extends FocusModifier> modifierType)
    {
        ArrayList<FocusModifier> selected = new ArrayList<>();

        for(FocusModifier fm : modifiers)
        {
            if(fm.getClass() == modifierType)
            {
                selected.add(fm);
            }
        }

        return selected;
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

    public void copyFrom(Focus other)
    {
        for(FocusModifier fm : other.modifiers)
        {
            if(fm.shouldCopy())
            {
                this.modifiers.add(fm);
            }
        }
    }

    public void saveNBTData(CompoundTag nbt)
    {
        ListTag modifierList = new ListTag();
        for(FocusModifier fm : modifiers)
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
    public ArrayList<FocusModifier> loadModifiers(ListTag modifierList)
    {
        ArrayList<FocusModifier> loadedModifiers = new ArrayList<>();

        for(Object tag : modifierList.toArray())
        {
            FocusModifier fm = loadModifier((CompoundTag) tag);
            loadedModifiers.add(fm);
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
