package me.beaubaer.mentalism.spells;

import me.beaubaer.mentalism.Mentalism;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class SpellRegistry
{
    public static final DeferredRegister<Spell> SPELLS_DEFERRED = DeferredRegister.create(new ResourceLocation(Mentalism.MOD_ID, "spell_registry"), Mentalism.MOD_ID);
    public static Supplier<IForgeRegistry<Spell>> SPELLS;

    public static final RegistryObject<Spell> SHOOT_ARROW = registerSpell(SpellFactory.shootArrow());
    public static final RegistryObject<Spell> BIDEN_BLAST = registerSpell(SpellFactory.bidenBlast());
    public static final RegistryObject<Spell> ROCK_CHIPPER = registerSpell(SpellFactory.rockChipper());
    public static final RegistryObject<Spell> MINERAL_SIPHON = registerSpell(SpellFactory.mineralSiphon());
    public static final RegistryObject<Spell> AIR_WALK = registerSpell(SpellFactory.airWalk());

    public static RegistryObject<Spell> registerSpell(Spell s)
    {
        return SpellRegistry.SPELLS_DEFERRED.register(s.getSpellID(), s::getSpell);
    }
}
