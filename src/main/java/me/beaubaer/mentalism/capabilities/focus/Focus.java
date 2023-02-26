package me.beaubaer.mentalism.capabilities.focus;

import me.beaubaer.mentalism.capabilities.focus.modifiers.abstractmodifiers.FocusModifier;
import me.beaubaer.mentalism.capabilities.focus.modifiers.abstractmodifiers.TickingFocusModifier;
import me.beaubaer.mentalism.datastructures.ModifierPriorityMap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class Focus implements IFocus
{
    protected float focus;

    // whether the player is attempting to focus via KeyMappings.FOCUS_KEY
    protected boolean focusing;

    // whether the player can currently focus
    protected boolean canFocus;
    public static final float DEFAULT_FOCUS_TIME = 1.0f;
    public static final float DEFAULT_FOCUS_DECAY_TIME = 1.0f;
    protected float focusTime = 1.0f;
    protected float focusDecayTime = 1.0f;
    private ModifierPriorityMap modifiers;

    public Focus()
    {
        this.focus = 0.0f;
        focusing = false;
        modifiers = new ModifierPriorityMap();
        canFocus = true;
    }

    public void setCanFocus(boolean canFocus)
    {
        this.canFocus = canFocus;

        if(!canFocus)
            this.focusing = false;
    }

    public boolean getCanFocus()
    {
        return canFocus;
    }

    public void setFocusing(boolean focusing) { this.focusing = focusing && canFocus; }

    public void interruptFocus()
    {
        this.focusing = false;
    }

    @Override
    public boolean getFocusing() {
        return this.focusing;
    }

    public void setFocusTime(float timeToFull)
    {
        focusTime = timeToFull;
    }

    public float getFocusTime()
    {
        return focusTime;
    }

    public float getFocusPower()
    {
        float focusPower = focus;

        for(FocusModifier fm : modifiers.collectAll())
        {
            focusPower = fm.apply(focusPower);
        }

        return Math.max(0f, focusPower);
    }

    public boolean hasModifier(String ID)
    {
        return getModifiers().stream().anyMatch(modifier -> modifier.ID.equals(ID));
    }

    public <T extends FocusModifier> ArrayList<T> getModifiers(Class<T> modifierType)
    {
        return modifiers.collectType(modifierType);
    }

    public FocusModifier getModifier(String ID)
    {
        List<FocusModifier> modifiers = getModifiers().stream().filter(m -> m.ID.equals(ID)).toList();

        if(modifiers.size() > 1)
        {
            throw new RuntimeException("ID " + ID + " has more than one associated modifier.");
        }
        else return modifiers.get(0);
    }

    public ArrayList<FocusModifier> getModifiers()
    {
        return modifiers.collectAll();
    }

    public void putModifier(FocusModifier fm)
    {
        modifiers.put(fm);
    }

    public void removeModifier(FocusModifier fm)
    {
        modifiers.remove(fm);
    }

    // should be called every player tick
    public void updateFocus()
    {
        updateModifiers();

        if(focusing && focus >= 1.0f)
            return;
        if(!focusing && focus <=0.0f)
            return;

        if(focusing && (focus < 1.0f))
        {
            focus += 1/(focusTime*20);
        }
        else if(!focusing && (focus > 0.0f))
        {
            focus -= 1/(focusDecayTime*20);
        }

        focus = Math.min(focus, 1.0f);
        focus = Math.max(focus, 0.0f);
    }

    private void updateModifiers()
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
    private ModifierPriorityMap loadModifiers(ListTag modifierList)
    {
        ModifierPriorityMap loadedModifiers = new ModifierPriorityMap();

        for(Object tag : modifierList.toArray())
        {
            FocusModifier fm = loadModifier((CompoundTag) tag);
            loadedModifiers.put(fm);
        }

        return loadedModifiers;
    }

    private FocusModifier loadModifier(CompoundTag nbt)
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
            fm = (FocusModifier) c.getConstructor(Focus.class, CompoundTag.class).newInstance(this, nbt);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }

        return fm;
    }
}
