package me.beaubaer.mentalism.spells;

import me.beaubaer.mentalism.Mentalism;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;

import java.util.function.Supplier;

public class SpellRegistry
{
    public static final DeferredRegister<Spell> DEFERRED_REGISTER_SPELL = DeferredRegister.create(new ResourceLocation(Mentalism.MOD_ID, "spell_registry"), Mentalism.MOD_ID);

    public static final Supplier<IForgeRegistry<Spell>> SPELL_REGISTRY = DEFERRED_REGISTER_SPELL.makeRegistry(Spell.class, RegistryBuilder::new);
}
