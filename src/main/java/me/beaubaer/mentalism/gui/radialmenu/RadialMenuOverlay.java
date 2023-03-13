package me.beaubaer.mentalism.gui.radialmenu;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHandler;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiComponent;
import net.minecraftforge.client.gui.ForgeIngameGui;
import net.minecraftforge.client.gui.IIngameOverlay;

public class RadialMenuOverlay implements IIngameOverlay
{
    public static RadialMenuOverlay SPELL_MENU = new RadialMenuOverlay();
    boolean active = false;
    boolean previousActive = false;


    public RadialMenuOverlay()
    {

    }

    @Override
    public void render(ForgeIngameGui gui, PoseStack poseStack, float partialTick, int width, int height)
    {
        // render code goes here later
    }

    public void setActive(boolean active)
    {
        if(active)
        {
            Minecraft.getInstance().mouseHandler.releaseMouse();
        }
        else
        {
            Minecraft.getInstance().mouseHandler.grabMouse();
        }

        this.active = active;
    }
}