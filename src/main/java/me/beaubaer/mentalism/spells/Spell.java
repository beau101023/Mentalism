package me.beaubaer.mentalism.spells;

import me.beaubaer.mentalism.Mentalism;
import me.beaubaer.mentalism.spells.strategies.conditions.SpellUnlockedCondition;
import me.beaubaer.mentalism.util.MentalMath;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.registries.ForgeRegistryEntry;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * representation of a context-sensitive effect triggered via the radial menu
 */
public class Spell extends ForgeRegistryEntry<Spell>
{
    private Spell thisSpell;

    private int spellNum;
    private String spellID;
    private float castTimeSeconds;

    List<Consumer<Player>> castActions = new ArrayList<>();
    List<Predicate<Player>> castConditions = new ArrayList<>();
    List<Predicate<Player>> availabilityConditions = new ArrayList<>();


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
        this.availabilityConditions.add(p -> new SpellUnlockedCondition(this).check(p));
    }

    public Spell getSpell()
    {
        return thisSpell;
    }

    public void addCastAction(Consumer<Player> action)
    {
        castActions.add(action);
    }

    public void addCastCondition(Predicate<Player> condition)
    {
        castConditions.add(condition);
    }

    public void addAvailabilityCondition(Predicate<Player> condition)
    {
        availabilityConditions.add(condition);
    }

    /**
     * @param p The player attempting to cast the spell
     */
    public void activate(Player p)
    {
        castActions.forEach(s -> s.accept(p));
    }

    /**
     * @param p The player querying if they meet the conditions to cast
     * @return Whether the player meets the conditions to cast
     */
    public boolean canCast(Player p)
    {
        return castConditions.stream().allMatch(s -> s.test(p)) && available(p);
    }

    /**
     * @param p The player querying if the spell is available
     * @return Whether the spell shows up in the spell menu
     */
    public boolean available(Player p)
    {
        return availabilityConditions.stream().allMatch(s -> s.test(p));
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

