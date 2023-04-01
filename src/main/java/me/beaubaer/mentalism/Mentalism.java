package me.beaubaer.mentalism;

import com.mojang.logging.LogUtils;
import me.beaubaer.mentalism.networking.MentalismMessages;
import me.beaubaer.mentalism.registries.SpellRegistry;
import me.beaubaer.mentalism.spells.Spell;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.RegistryBuilder;
import org.slf4j.Logger;

import java.util.function.Supplier;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(Mentalism.MOD_ID)
public class Mentalism
{
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();
    public static final String MOD_ID = "mentalism";

    public Mentalism()
    {
        // Register the setup method for mod loading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);

        SpellRegistry.SPELLS = SpellRegistry.SPELLS_DEFERRED.makeRegistry(Spell.class, RegistryBuilder::new);
        SpellRegistry.SPELLS_DEFERRED.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    private void setup(final FMLCommonSetupEvent event)
    {
        // some preinit code
        LOGGER.info("TIME TO GET MENTAL");

        event.enqueueWork(MentalismMessages::register);
    }
}
