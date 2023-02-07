package me.beaubaer.mentalism.events;

import me.beaubaer.mentalism.Mentalism;
import me.beaubaer.mentalism.attributes.MentalismAttributes;
import me.beaubaer.mentalism.screens.MeditationScreen;
import me.beaubaer.mentalism.util.KeyMappings;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Mentalism.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ClientForgeEvents
{
    static AttributeModifier focusing = new AttributeModifier("focus_key", 0.1D, AttributeModifier.Operation.ADDITION);
    @SubscribeEvent
    public static void playerTick(TickEvent.PlayerTickEvent event)
    {
        if(event.player instanceof ServerPlayer)
            return;

        Minecraft mc = Minecraft.getInstance();
        if(KeyMappings.FOCUS_KEY.isDown())
        {
            if(event.player.getAttribute(MentalismAttributes.FOCUS) == null)
                return;

            if(!event.player.getAttribute(MentalismAttributes.FOCUS).hasModifier(focusing))
                event.player.getAttribute(MentalismAttributes.FOCUS).addTransientModifier(focusing);
            Component c = new TranslatableComponent("title.mentalism.menu.void");
            MeditationScreen ms = new MeditationScreen(c);
            mc.setScreen(ms);
        }
        else
        {
            if(event.player.getAttribute(MentalismAttributes.FOCUS).hasModifier(focusing))
                event.player.getAttribute(MentalismAttributes.FOCUS).removeModifier(focusing);
        }
    }
}
