package me.beaubaer.mentalism.spells;

import me.beaubaer.mentalism.spells.strategies.activations.DestroyBlockAtCursor;
import me.beaubaer.mentalism.spells.strategies.activations.ExplodeAtCursor;
import me.beaubaer.mentalism.spells.strategies.activations.InterruptFocus;
import me.beaubaer.mentalism.spells.strategies.activations.ShootArrow;
import me.beaubaer.mentalism.spells.strategies.conditions.MouseOnPickableInMeleeRangeCondition;
import me.beaubaer.mentalism.spells.strategies.conditions.PlayerFocusCondition;
import me.beaubaer.mentalism.spells.strategies.conditions.SpellUnlockedCondition;

public class SpellFactory
{
    private static int spellNum = 0;
    private static int getNum()
    {
        return spellNum++;
    }

    public static Spell shootArrow()
    {
        Spell shootArrow = new Spell(getNum(), "shootarrow", 1.0f);

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
        Spell bidenBlast = new Spell(getNum(), "bidenblast", 1.0f);

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
        Spell rockChipper = new Spell(getNum(), "rockchipper", 1.0f);

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
