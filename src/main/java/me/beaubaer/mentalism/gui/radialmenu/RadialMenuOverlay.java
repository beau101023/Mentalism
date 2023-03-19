package me.beaubaer.mentalism.gui.radialmenu;

import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Matrix4f;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import me.beaubaer.mentalism.clientdata.FocusData;
import me.beaubaer.mentalism.gui.GraphicsUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHandler;
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
    int numSegments = 40;
    int selectedSegment = -1;

    // used for fading the menu in and out
    float menuAlpha = 0f;

    float menuOuterRadius = 0f;
    float menuInnerRadius = 0f;

    static ResourceLocation magicCircle = new ResourceLocation("mentalism", "tex/magic_circle.png");
    static ResourceLocation circle = new ResourceLocation("mentalism", "tex/circle.png");
    static ResourceLocation circleThicc = new ResourceLocation("mentalism", "tex/circle_thicc.png");

    Minecraft mc = Minecraft.getInstance();
    MouseHandler mouse = Minecraft.getInstance().mouseHandler;

    public RadialMenuOverlay()
    {

    }

    @Override
    public void render(ForgeIngameGui gui, PoseStack poseStack, float partialTick, int width, int height)
    {
        // rendering
        // REFERENCE GuiComponent.innerFill for how to do some render calls
        // REFERENCE Gui.renderTextureOverlay
        updateMenuAlpha();

        if(menuAlpha == 0)
            return;

        Matrix4f pose = poseStack.last().pose().copy();

        updateMenuRadius(height);
        renderBackground(pose, width, height);

        updateSelectedSegment(width, height);
        renderSelectionIndicator(pose, width, height);
    }

    private void updateMenuAlpha()
    {
        menuAlpha = Math.min(FocusData.localFocus, 1.0f);
    }

    private void updateSelectedSegment(int width, int height)
    {
        float mouseXScreen = (float) mouse.xpos() * (float)mc.getWindow().getGuiScaledWidth() / (float)mc.getWindow().getScreenWidth();
        float mouseYScreen = (float) mouse.ypos() * (float)mc.getWindow().getGuiScaledWidth() / (float)mc.getWindow().getScreenWidth();

        float mouseXRelToCenter = mouseXScreen-(width /2);
        float mouseYRelToCenter = mouseYScreen-(height /2);

        float distFromCenter = GraphicsUtil.magnitude2D(mouseXRelToCenter, mouseYRelToCenter);
        float angleFromXHat = GraphicsUtil.angleFromXHat2D(mouseXRelToCenter, mouseYRelToCenter);

        if(distFromCenter < menuOuterRadius)
        {
            float segmentWidth = (float) (2 * Math.PI) / numSegments;
            selectedSegment = (int) Math.floor(angleFromXHat / segmentWidth);
        }
        else
            selectedSegment = -1;
    }

    private void updateMenuRadius(int height)
    {
        menuOuterRadius = (height /2f)*0.75f;
        menuInnerRadius = (height /2f)*0.4f;
    }

    private void renderBackground(Matrix4f pose, int width, int height)
    {

        GraphicsUtil.renderSquareTextureOverlay(pose, height/2f, width/2f, height/2f, magicCircle, 0.3f* menuAlpha);

        if(numSegments > 1)
        {
            int color = FastColor.ARGB32.color((int) (0.5*255* menuAlpha), 255, 255, 255);
            GraphicsUtil.renderRadialSeparators(pose, numSegments, (height/2f) * 0.40f, (height/2f) * 0.75f, width/2f, height/2f, color);
        }

        // big circles
        GraphicsUtil.renderSquareTextureOverlay(pose, (height/2f)*0.75f, width/2f, height/2f, circle, 0.5f* menuAlpha);
        GraphicsUtil.renderSquareTextureOverlay(pose, (height/2f)*0.40f, width/2f, height/2f, circle, 0.5f* menuAlpha);
    }

    private void renderSelectionIndicator(Matrix4f pose, int width, int height)
    {
        if(selectedSegment > -1 && selectedSegment < numSegments)
        {
            // little selection circle
            Matrix4f selectionPose = getPoseForSelection(pose, width / 2, height / 2, (menuOuterRadius+menuInnerRadius)/2, selectedSegment, numSegments);
            GraphicsUtil.renderSquareTextureOverlay(selectionPose, 50f / numSegments, 0, 0, magicCircle, 0.75f * menuAlpha);
        }
    }

    private Matrix4f getPoseForSelection(Matrix4f inPose, int centerX, int centerY, float radius, int selectedSegment, int numSegments)
    {
        Matrix4f selectedPose = inPose.copy();
        selectedPose.translate(new Vector3f(centerX, centerY, 0));

        // angle to the middle of the selected 'pie slice'
        float selectionAngle = 2*(float)Math.PI * (2*selectedSegment + 1) / (2*numSegments);
        selectedPose.multiply(Quaternion.fromXYZ(0, 0, selectionAngle));
        selectedPose.multiply(Matrix4f.createTranslateMatrix(radius, 0f, 0f));
        selectedPose.multiply(Quaternion.fromXYZ(0, 0, -selectionAngle));

        return selectedPose;
    }

    public void setActive(boolean active)
    {
        if(this.active == active)
            return;

        if(active)
        {
            mouse.releaseMouse();
        }
        else
        {
            mouse.grabMouse();
        }

        this.active = active;
    }
}