package me.beaubaer.mentalism.spells;

import net.minecraftforge.registries.ForgeRegistryEntry;

/**
 * representation of a context-sensitive effect triggered via the radial menu
 */
public abstract class Spell extends ForgeRegistryEntry<Spell>
{
    public abstract void activate();
}