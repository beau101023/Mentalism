package me.beaubaer.mentalism.capabilities;

import net.minecraftforge.common.capabilities.*;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class FocusRegistration
{
    public static final Capability<FocusCapability> FOCUS = CapabilityManager.get(new CapabilityToken<>(){});;

    @SubscribeEvent
    public static void register(RegisterCapabilitiesEvent event)
    {
        event.register(FocusCapability.class);
    }
}