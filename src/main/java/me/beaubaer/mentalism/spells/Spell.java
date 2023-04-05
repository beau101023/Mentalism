package me.beaubaer.mentalism.spells;

import me.beaubaer.mentalism.Mentalism;
import me.beaubaer.mentalism.spells.strategies.conditions.SpellUnlockedCondition;
import me.beaubaer.mentalism.util.MentalMath;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.registries.ForgeRegistryEntry;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * representation of a context-sensitive effect triggered via the radial menu
 */
public class Spell extends ForgeRegistryEntry<Spell>
{
    private final Spell thisSpell;

    private final int spellNum;
    private final String spellID;
    private final float castTimeSeconds;

    Consumer<ServerPlayer> castAction;
    BiConsumer<ServerPlayer, Float> castInProgressAction;
    Predicate<ServerPlayer> castCondition;
    Predicate<ServerPlayer> availabilityCondition;


    /**
     * @param spellNum The integer ID of the spell
     * @param spellID The string ID of the spell
     * @param castTimeSeconds The time in seconds it takes to cast the spell
     */
    public Spell(int spellNum, String spellID, float castTimeSeconds)
    {
        this.spellNum = spellNum;
        this.spellID = spellID;
        this.castTimeSeconds = castTimeSeconds;
        this.thisSpell = this;

        // spells should be unlocked before they can be cast
        this.addAvailabilityCondition(p -> new SpellUnlockedCondition(this).check(p));
    }

    public Spell getSpell()
    {
        return thisSpell;
    }

    public void addCastAction(Consumer<ServerPlayer> action)
    {
        if(castAction == null)
            castAction = action;
        else
            castAction = castAction.andThen(action);
    }

    public void addWhileCastingAction(BiConsumer<ServerPlayer, Float> action)
    {
        if(castInProgressAction == null)
            castInProgressAction = action;
        else
            castInProgressAction = castInProgressAction.andThen(action);
    }

    public void addCastCondition(Predicate<ServerPlayer> condition)
    {
        if(castCondition == null)
            castCondition = condition;
        else
            castCondition = castCondition.and(condition);
    }

    public void addAvailabilityCondition(Predicate<ServerPlayer> condition)
    {
        if(availabilityCondition == null)
            availabilityCondition = condition;
        else
            availabilityCondition = availabilityCondition.and(condition);
    }

    /**
     * @param p The player attempting to cast the spell
     */
    public void activate(ServerPlayer p)
    {
        if(castAction != null)
            castAction.accept(p);
    }

    /**
     * @param p The player attempting to cast the spell
     */
    public void whileCasting(ServerPlayer p, float progress)
    {
        if(castInProgressAction != null)
            castInProgressAction.accept(p, progress);
    }

    /**
     * @param p The player querying if they meet the conditions to cast
     * @return Whether the player meets the conditions to cast
     */
    public boolean canCast(ServerPlayer p)
    {
        if(castCondition == null)
            return true;
        else
            return castCondition.test(p);
    }

    /**
     * @param p The player querying if the spell is available
     * @return Whether the spell shows up in the spell menu
     */
    public boolean available(ServerPlayer p)
    {
        if(availabilityCondition == null)
            return true;
        else
            return availabilityCondition.test(p);
    }

    public int getSpellNum()
    {
        return spellNum;
    }

    public String getSpellID()
    {
        return spellID;
    }

    public ResourceLocation getSpellIcon()
    {
        return new ResourceLocation(Mentalism.MOD_ID, "tex/spells/" + getSpellID() + ".png");
    }

    public float getCastIncrement()
    {
        return MentalMath.secondsToTickIncrements(castTimeSeconds);
    }

}

