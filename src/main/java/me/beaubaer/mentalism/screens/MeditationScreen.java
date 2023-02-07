package me.beaubaer.mentalism.screens;

import com.mojang.blaze3d.vertex.PoseStack;
import me.beaubaer.mentalism.attributes.MentalismAttributes;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

public class MeditationScreen extends Screen
{
    public MeditationScreen(Component title) {
        super(title);
    }

    @Override
    public void init()
    {
        super.init();
    }

    @Override
    public void render(@NotNull PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick)
    {
        if(minecraft == null)
            return;

        this.renderBackground(pPoseStack);

        super.render(pPoseStack, pMouseX, pMouseY, pPartialTick);

        String str = String.valueOf(minecraft.player.getAttribute(MentalismAttributes.FOCUS).getValue());

        this.font.draw(pPoseStack, str, 0.25F, 0.25F, 0xffffffff);
    }

    @Override
    public void renderBackground(PoseStack pPoseStack, int pVOffset)
    {

        int colorBlack = 0xff000000;

        if (this.minecraft.level != null) {
            this.fill(pPoseStack, 0, 0, this.width, this.height, colorBlack);
            net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(new net.minecraftforge.client.event.ScreenEvent.BackgroundDrawnEvent(this, pPoseStack));
        } else {
            return;
        }

    }

    @Override
    public boolean shouldCloseOnEsc() { return true; }

    @Override
    public boolean isPauseScreen() { return false; }
}
