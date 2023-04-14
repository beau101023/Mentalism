package me.beaubaer.mentalism.spells;

import me.beaubaer.mentalism.spells.strategies.activations.AddLingeringEffect;
import me.beaubaer.mentalism.spells.strategies.activations.*;
import me.beaubaer.mentalism.spells.strategies.conditions.BlockInMeleeRangeCondition;
import me.beaubaer.mentalism.spells.strategies.conditions.MouseOnPickableInMeleeRangeCondition;
import me.beaubaer.mentalism.spells.strategies.conditions.PlayerFocusCondition;
import me.beaubaer.mentalism.spells.strategies.conditions.SpellUnlockedCondition;
import me.beaubaer.mentalism.spells.strategies.lingeringeffects.AirWalk;
import me.beaubaer.mentalism.spells.strategies.whilecastingactions.SiphonParticleEffect;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.common.Tags;

import java.util.List;

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
                player -> ShootArrow.activate(player)
        );

        return shootArrow;
    }

    public static Spell bidenBlast()
    {
        Spell bidenBlast = new Spell(getNum(), "bidenblast", 1.0f);

        bidenBlast.addAvailabilityCondition(
                player -> MouseOnPickableInMeleeRangeCondition.check(player, res -> true)
        );
        bidenBlast.addCastCondition(
                player -> new PlayerFocusCondition(0.9f, true).check(player)
        );
        bidenBlast.addCastAction(
                player ->
                {
                    ExplodeAtCursor.activate(player);
                    InterruptFocus.activate(player);
                }
        );

        return bidenBlast;
    }

    public static Spell rockChipper()
    {
        Spell rockChipper = new Spell(getNum(), "rockchipper", 1.0f);
        rockChipper.addCastCondition(
                player ->
                        new PlayerFocusCondition(0.9f, true).check(player) &&
                                MouseOnPickableInMeleeRangeCondition.check(player,
                                        res -> res.getType() == HitResult.Type.BLOCK)
        );
        rockChipper.addCastAction(
                player ->
                {
                    DestroyBlockAtCursor.activate(player);
                    DistractFocus.activate(player);
                }
        );

        return rockChipper;
    }

    // this spell is a mining spell.
    // When cast, it will cause the targeted block to drop the ore it contains and convert the block into normal stone.
    public static Spell mineralSiphon()
    {
        Spell mineralSiphon = new Spell(getNum(), "mineralsiphon", 1.0f);
        mineralSiphon.addCastCondition(
                player -> new PlayerFocusCondition(0.9f, true).check(player)
        );
        mineralSiphon.addCastCondition(player ->
                BlockInMeleeRangeCondition.test(player,
                            List.of(Tags.Blocks.ORES_IN_GROUND_DEEPSLATE,
                                    Tags.Blocks.ORES_IN_GROUND_STONE,
                                    Tags.Blocks.ORES_IN_GROUND_NETHERRACK))
        );
        mineralSiphon.addCastAction(
                ExtractOreFromBlock::activate
        );
        mineralSiphon.addWhileCastingAction( (player, castProgress) -> SiphonParticleEffect.activate(player));

        return mineralSiphon;
    }

    public static Spell airWalk()
    {
        Spell airWalk = new Spell(getNum(), "airwalk", 1.0f);
        airWalk.addCastCondition(
                player -> new PlayerFocusCondition(0.9f, true).check(player)
        );
        airWalk.addCastAction(
                    player -> AddLingeringEffect.addAndInit(player, new AirWalk(7f))
        );
        airWalk.addCastAction(
                player -> PlayLocalSound.activate(SoundEvents.ENDERMAN_TELEPORT, 0.5f, 0.6f)
        );

        return airWalk;
    }
}
