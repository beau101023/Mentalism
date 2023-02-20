package me.beaubaer.mentalism.events;

import me.beaubaer.mentalism.Mentalism;
import me.beaubaer.mentalism.networking.FocusSyncC2SPacket;
import me.beaubaer.mentalism.networking.MentalismMessages;
import me.beaubaer.mentalism.util.KeyMappings;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Mentalism.MOD_ID, value = Dist.CLIENT)
public class ClientEvents
{
    private static boolean previousFKeyState = false;

    @SubscribeEvent
    public static void checkKeyOnTick(TickEvent.ClientTickEvent e)
    {
        boolean currentFKeyState = KeyMappings.FOCUS_KEY.isDown();

        if(currentFKeyState != previousFKeyState)
        {
            previousFKeyState = currentFKeyState;

            // set focusing on server to focus key state
            MentalismMessages.sendToServer(new FocusSyncC2SPacket(currentFKeyState));
        }
    }
}
