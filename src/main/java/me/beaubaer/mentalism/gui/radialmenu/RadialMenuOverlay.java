package me.beaubaer.mentalism.gui.radialmenu;

import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Matrix4f;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import me.beaubaer.mentalism.gui.GraphicsUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.gui.ForgeIngameGui;
import net.minecraftforge.client.gui.IIngameOverlay;

@OnlyIn(Dist.CLIENT)
public class RadialMenuOverlay implements IIngameOverlay
{
    public static RadialMenuOverlay SPELL_MENU = new RadialMenuOverlay();
    boolean active = false;
    int numSegments = 1;
    int timer = 0;

    ResourceLocation circle = new ResourceLocation("mentalism", "tex/circle.png");

    public RadialMenuOverlay()
    {

    }

    @Override
    public void render(ForgeIngameGui gui, PoseStack poseStack, float partialTick, int width, int height)
    {
        if(!active)
            return;

        // rendering
        // REFERENCE GuiComponent.innerFill for how to do some render calls
        // REFERENCE Gui.renderTextureOverlay

        Matrix4f pose = poseStack.last().pose().copy();

        GraphicsUtil.renderTextureOverlay(pose, (width/2)-(height/2), 0, (width/2)+(height/2), height, circle, 0.25f);

        renderSegmentSeparators(pose, numSegments, 50f, (height/2)*0.75f, width/2, height/2);

        if(timer == 39)
        {
            numSegments = (numSegments % 15) + 1;
        }
        timer = (timer +1) % 40;
    }

    private void renderSegmentSeparators(Matrix4f pose, int segAmount, float innerRadius, float outerRadius, float centerX, float centerY)
    {
        int color = FastColor.ARGB32.color(255, 255, 255, 255);
        for(float i = 0; i<(2*Math.PI); i+= (2*Math.PI)/segAmount)
        {
            float length = outerRadius-innerRadius;

            Matrix4f linePose = pose.copy();

            // translate line to the center
            linePose.translate(new Vector3f(centerX, centerY, 0));

            // rotate line
            Quaternion lineRot = Quaternion.fromXYZ(0, 0, i);
            linePose.multiply(lineRot);

            // offset line from the center
            Matrix4f translator = Matrix4f.createTranslateMatrix(innerRadius, 0, 0);
            linePose.multiply(translator);

            GraphicsUtil.line(linePose, 1, 0, 0, 0f, length, color);

        }
    }

    public void setActive(boolean active)
    {
        if(this.active == active)
            return;

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