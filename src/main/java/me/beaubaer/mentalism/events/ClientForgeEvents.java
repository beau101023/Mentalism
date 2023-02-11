package me.beaubaer.mentalism.events;

import me.beaubaer.mentalism.Mentalism;
import me.beaubaer.mentalism.screens.MeditationScreen;
import me.beaubaer.mentalism.util.KeyMappings;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Mentalism.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ClientForgeEvents
{
    @SubscribeEvent
    public static void playerTick(TickEvent.PlayerTickEvent event)
    {
        if(event.player instanceof ServerPlayer)
            return;

        Minecraft mc = Minecraft.getInstance();
        if(KeyMappings.FOCUS_KEY.isDown())
        {
            Component c = new TranslatableComponent("title.mentalism.menu.void");
            MeditationScreen ms = new MeditationScreen(c);
            mc.setScreen(ms);
        }
    }
}
