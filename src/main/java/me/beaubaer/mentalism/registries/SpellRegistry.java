package me.beaubaer.mentalism.registries;

import me.beaubaer.mentalism.Mentalism;
import me.beaubaer.mentalism.spells.Spell;
import me.beaubaer.mentalism.spells.strategies.activations.DestroyBlockAtCursor;
import me.beaubaer.mentalism.spells.strategies.activations.ExplodeAtCursor;
import me.beaubaer.mentalism.spells.strategies.activations.InterruptFocus;
import me.beaubaer.mentalism.spells.strategies.activations.ShootArrow;
import me.beaubaer.mentalism.spells.strategies.conditions.MouseOnPickableInMeleeRangeCondition;
import me.beaubaer.mentalism.spells.strategies.conditions.PlayerFocusCondition;
import me.beaubaer.mentalism.spells.strategies.conditions.SpellUnlockedCondition;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class SpellRegistry
{
    public static final DeferredRegister<Spell> SPELLS_DEFERRED = DeferredRegister.create(new ResourceLocation(Mentalism.MOD_ID, "spell_registry"), Mentalism.MOD_ID);

    public static final RegistryObject<Spell> SHOOT_ARROW = registerSpell(shootArrow());
    public static final RegistryObject<Spell> BIDEN_BLAST = registerSpell(bidenBlast());
    public static final RegistryObject<Spell> ROCK_CHIPPER = registerSpell(rockChipper());

    public static RegistryObject<Spell> registerSpell(Spell s)
    {
        return SpellRegistry.SPELLS_DEFERRED.register(s.getSpellID(), s::getSpell);
    }

    public static Spell shootArrow()
    {
        Spell shootArrow = new Spell(1, "shootarrow", 1.0f);

        shootArrow.addAvailabilityCondition(
                player -> new SpellUnlockedCondition(shootArrow).check(player)
        );
        shootArrow.addCastCondition(
                player -> new PlayerFocusCondition(0.9f, true).check(player)
        );
        shootArrow.addCastAction(
                player -> new ShootArrow().activate(player)
        );

        return shootArrow;
    }

    public static Spell bidenBlast()
    {
        Spell bidenBlast = new Spell(2, "bidenblast", 1.0f);

        bidenBlast.addAvailabilityCondition(
                player -> new MouseOnPickableInMeleeRangeCondition(null, null, true, true).check(player)
        );
        bidenBlast.addCastCondition(
                player -> new PlayerFocusCondition(0.9f, true).check(player)
        );
        bidenBlast.addCastAction(
                player ->
                {
                    new ExplodeAtCursor().activate(player);
                    new InterruptFocus().activate(player);
                }
        );

        return bidenBlast;
    }

    public static Spell rockChipper()
    {
        Spell rockChipper = new Spell(3, "rockchipper", 1.0f);

        rockChipper.addAvailabilityCondition(
                player -> new MouseOnPickableInMeleeRangeCondition(null, null, false, true).check(player)
        );
        rockChipper.addCastCondition(
                player -> new PlayerFocusCondition(0.9f, true).check(player)
        );
        rockChipper.addCastAction(
                player -> new DestroyBlockAtCursor().activate(player)
        );

        return rockChipper;
    }
}
