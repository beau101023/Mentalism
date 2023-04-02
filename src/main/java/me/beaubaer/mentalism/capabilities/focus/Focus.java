package me.beaubaer.mentalism.capabilities.focus;

import me.beaubaer.mentalism.Mentalism;
import me.beaubaer.mentalism.capabilities.focus.modifiers.Distraction;
import me.beaubaer.mentalism.capabilities.focus.modifiers.abstractmodifiers.DecayingFocusModifier;
import me.beaubaer.mentalism.capabilities.focus.modifiers.abstractmodifiers.FocusModifier;
import me.beaubaer.mentalism.capabilities.focus.modifiers.abstractmodifiers.TickingFocusModifier;
import me.beaubaer.mentalism.datastructures.ModifierPriorityMap;
import me.beaubaer.mentalism.networking.S2C.FocusValueSyncS2CPacket;
import me.beaubaer.mentalism.networking.MentalismMessages;
import me.beaubaer.mentalism.util.MentalMath;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerPlayer;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Focus implements IFocus
{
    protected float focus = 0.0f;

    // whether focus should tick up
    protected boolean focusing = false;

    // whether the player can currently focus
    protected boolean canFocus = true;

    // whether the player is attempting to focus via KeyMappings.FOCUS_KEY
    protected boolean focusPressed = false;

    // default times for focus to reach max and min value
    // modified in special situations- if this is lowered, it is a major upgrade for the player
    public static final float defaultFocusTime = 1f;
    public static final float defaultSlowedFocusTime = 2f;

    // times in seconds determining how long it takes for focus to reach 1 from 0, or 0 from 1
    protected float focusTime = defaultFocusTime;
    protected float focusDecayTime = defaultFocusTime;

    // calculated rate values from focusTime and focusDecayTime, to be used every tick
    protected float focusTickIncrement;
    protected float focusDecayIncrement;
    private ModifierPriorityMap modifiers;

    public Focus()
    {
        updateFocusTickIncrement();
        updateFocusDecayIncrement();
        modifiers = new ModifierPriorityMap();
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

    // when set to false, the player must re-press the focus key to begin focusing again
    public void setFocusPressed(boolean focusPressed)
    {
        this.focusPressed = focusPressed;
        this.focusing = focusPressed && canFocus;
    }

    @Override
    public boolean getFocusing() {
        return this.focusing;
    }

    public void setFocusTime(float timeToFull)
    {
        focusTime = timeToFull;
        updateFocusTickIncrement();
    }

    private void updateFocusTickIncrement()
    {
        focusTickIncrement = MentalMath.secondsToTickIncrements(focusTime);
    }

    public float getFocusTime()
    {
        return focusTime;
    }

    public void setFocusDecayTime(float timeToZero)
    {
        focusDecayTime = timeToZero;
        updateFocusDecayIncrement();
    }

    private void updateFocusDecayIncrement()
    {
        focusDecayIncrement = MentalMath.secondsToTickIncrements(focusDecayTime);
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

    public boolean hasModifier(FocusModifier modifier)
    {
        return modifiers.collectAll().contains(modifier);
    }

    public <T extends FocusModifier> ArrayList<T> getModifiers(Class<T> modifierType)
    {
        return modifiers.collectType(modifierType);
    }

    public <T extends FocusModifier> Optional<T> getModifier(FocusModifier modifier, Class<T> type)
    {
        List<FocusModifier> modifiers = getModifiers().stream().filter(fm -> fm.equals(modifier)).toList();

        if(modifiers.isEmpty())
            return Optional.empty();

        if(modifiers.size() > 1)
        {
            // normally, every modifier should have a unique ID
            throw new RuntimeException("More than one modifier with ID \"" + modifier.getID() + "\" found!");
        }
        else return Optional.of(type.cast(modifiers.get(0)));
    }

    public ArrayList<FocusModifier> getModifiers()
    {
        return modifiers.collectAll();
    }

    // if the modifier is a decayingFocusModifier which already exists, reset the old modifier's amount and add the new one
    public void putModifier(DecayingFocusModifier fm)
    {
        if(this.hasModifier(fm))
        {
            Optional<DecayingFocusModifier> oldModifier = getModifier(fm, DecayingFocusModifier.class);

            if(oldModifier.isPresent())
            {
                oldModifier.get().reset();
                return;
            }
        }

        fm.intitializeParent(this);

        modifiers.put(fm);
    }


    public void putModifier(FocusModifier fm)
    {
        if(this.hasModifier(fm))
            throw new RuntimeException("putModifier called with an ID that already exists: " + fm.getID());

        fm.intitializeParent(this);

        modifiers.put(fm);
    }

    public void removeModifier(FocusModifier fm)
    {
        modifiers.remove(fm);
    }

    // should be called every player tick
    public void update()
    {
        updateModifiers();

        if(focusing && focus >= 1.0f)
            return;
        if(!focusing && focus <=0.0f)
            return;

        if(focusing && (focus < 1.0f))
        {
            focus += focusTickIncrement;
        }
        else if(!focusing && (focus > 0.0f))
        {
            focus -= focusDecayIncrement;
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
        Class<?> c;
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
            fm = (FocusModifier) c.getConstructor(CompoundTag.class).newInstance( nbt);
            fm.intitializeParent(this);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }

        return fm;
    }

    public void sync(ServerPlayer p)
    {
        MentalismMessages.sendToPlayer(new FocusValueSyncS2CPacket(getFocusPower()), p);
    }
}
