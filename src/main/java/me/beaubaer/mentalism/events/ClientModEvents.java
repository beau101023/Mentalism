package me.beaubaer.mentalism.events;

import me.beaubaer.mentalism.Mentalism;
import me.beaubaer.mentalism.attributes.MentalismAttributes;
import me.beaubaer.mentalism.util.KeyMappings;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.EntityAttributeModificationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = Mentalism.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientModEvents
{
    @SubscribeEvent
    public static void clientSetup(FMLClientSetupEvent event)
    {
        KeyMappings.initializeMappings();

    }

    @SubscribeEvent
    public static void addPlayerAttributes(EntityAttributeModificationEvent event)
    {
        event.add(EntityType.PLAYER, MentalismAttributes.FOCUS, 0.0D);
    }
}
