package me.beaubaer.mentalism.capabilities.focus;

import me.beaubaer.mentalism.Mentalism;
import me.beaubaer.mentalism.capabilities.focus.modifiers.abstractmodifiers.FocusModifier;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class FocusModifierRegistry
{
    // TODO: implement at some point
    //  should allow for retrieving focus modifiers by a numerical ID for network transmission
    //  this will allow arbitrary focus modifiers to be applied to a focus from both the client and server
    //  However, for now we can just implement packets for each modifier we want to use instead.
}
