package me.beaubaer.mentalism.attributes;

import me.beaubaer.mentalism.Mentalism;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class MentalismAttributes
{
    public static final Attribute FOCUS = new RangedAttribute("attribute.name.mentalism.focus", 0.0D, 0.0D, 1024.0D);

    public static final DeferredRegister<Attribute> ATTRIBUTES = DeferredRegister.create(ForgeRegistries.ATTRIBUTES, Mentalism.MOD_ID);

    public static final RegistryObject<Attribute> FOCUS_REGISTRY = ATTRIBUTES.register("attribute.name.mentalism.focus", () -> FOCUS);

    public static void registerAttributes()
    {
        ATTRIBUTES.register(FMLJavaModLoadingContext.get().getModEventBus());
    }
}
