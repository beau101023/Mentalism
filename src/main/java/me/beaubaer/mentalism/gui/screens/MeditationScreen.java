package me.beaubaer.mentalism.gui.screens;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FastColor;
import org.jetbrains.annotations.NotNull;

public class MeditationScreen extends Screen
{
    private int renderTicks = 0;
    private final float fadeInTime = 2.0f;
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

        // update render ticks until completely faded in
        if(renderTicks < fadeInTime*20) renderTicks++;

        this.renderBackground(pPoseStack);

        super.render(pPoseStack, pMouseX, pMouseY, pPartialTick);

        this.font.draw(pPoseStack, "Placeholder", 0.25F, 0.25F, 0xffffffff);
    }

    @Override
    public void renderBackground(@NotNull PoseStack pPoseStack, int pVOffset)
    {
        int alpha = Math.round(255*renderTicks/(fadeInTime*20));

        int screenColor = FastColor.ARGB32.color(alpha, 0, 0, 0);

        if (this.minecraft.level != null) {
            fill(pPoseStack, 0, 0, this.width, this.height, screenColor);
            net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(new net.minecraftforge.client.event.ScreenEvent.BackgroundDrawnEvent(this, pPoseStack));
        }
    }

    @Override
    public boolean shouldCloseOnEsc() { return true; }

    @Override
    public boolean isPauseScreen() { return false; }
}
