package me.beaubaer.mentalism.gui;

import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Matrix4f;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import me.beaubaer.mentalism.Mentalism;
import me.beaubaer.mentalism.clientdata.FocusData;
import me.beaubaer.mentalism.clientdata.SpellCastData;
import me.beaubaer.mentalism.clientdata.ProgressionData;
import me.beaubaer.mentalism.networking.MentalismMessages;
import me.beaubaer.mentalism.networking.C2S.SelectedSpellSyncC2SPacket;
import me.beaubaer.mentalism.spells.SpellRegistry;
import me.beaubaer.mentalism.spells.Spell;
import me.beaubaer.mentalism.util.MentalMath;
import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHandler;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.gui.ForgeIngameGui;
import net.minecraftforge.client.gui.IIngameOverlay;

@OnlyIn(Dist.CLIENT)
public class RadialMenu implements IIngameOverlay
{
    public static final RadialMenu SPELL_MENU = new RadialMenu();
    boolean active = false;
    int numSegments = 1;
    int selectedSegment = -1;

    // used for fading the menu in and out
    private float menuAlpha = 0f;

    private float renderFade = 0f;
    private float menuOuterRadius = 0f;
    private float menuInnerRadius = 0f;
    private float iconRadius = 0f;
    private float backgroundRadius = 0f;
    private final float menuCenterX = 0f;
    private final float menuCenterY = 0f;

    private static final FocusBar focusBar = new FocusBar(100, 5, 30, 30);

    static final ResourceLocation magicCircle = new ResourceLocation(Mentalism.MOD_ID, "tex/magic_circle.png");
    static final ResourceLocation circle = new ResourceLocation(Mentalism.MOD_ID, "tex/circle.png");
    static ResourceLocation circleThicc = new ResourceLocation(Mentalism.MOD_ID, "tex/circle_thicc.png");
    static final ResourceLocation black = new ResourceLocation(Mentalism.MOD_ID, "tex/black.png");
    static final ResourceLocation cantcast = new ResourceLocation(Mentalism.MOD_ID, "tex/cantcast.png");

    final Minecraft mc = Minecraft.getInstance();
    final MouseHandler mouse = Minecraft.getInstance().mouseHandler;

    public RadialMenu()
    {

    }

    @Override
    public void render(ForgeIngameGui gui, PoseStack poseStack, float partialTick, int width, int height)
    {
        // logic
        if(active)
        {
            updateNumSegments();
            updateSelectedSegment(width, height);
        }

        if(Mentalism.SHOW_FOCUS_INDICATOR)
            GraphicsUtil.debugString(poseStack,"Focus: " + FocusData.localFocus, 0, 5, 50);

        // rendering
        float renderFadeTarget = updateRenderFadeTarget();
        renderFade = MentalMath.smoothTickedToRender(renderFade, renderFadeTarget);

        updateMenuAlpha();

        // if menuAlpha is 0, skip everything except the focus bar
        if(menuAlpha == 0f)
        {
            focusBar.render(poseStack, partialTick);
        }
        else
        {
            updateIconSize();
            updateMenuRadius(height);

            Matrix4f pose = poseStack.last().pose().copy();

            renderHyperfocusDarkness(pose, width, height);

            // when the menu is active, we still want to see the focus bar above the hyperfocus darkness
            focusBar.render(poseStack, partialTick);

            renderMagicMenuLayout(pose, width, height);

            renderSpellIcons(pose, width, height);

            if (!active)
                return;
            // selected segment highlight
            renderSegmentIcon(pose, width, height, magicCircle, selectedSegment, 0.75f * menuAlpha);
        }
    }

    private float updateRenderFadeTarget()
    {
        float renderFadeTarget;
        if(active)
        {
            renderFadeTarget = FocusData.localFocus > 0.9f ? 1.0f : 0.0f;
        }
        else
        {
            renderFadeTarget = 0.0f;
        }
        return renderFadeTarget;
    }

    private void syncSelectionToServer()
    {
        if(selectedSegment == -1)
            MentalismMessages.sendToServer(new SelectedSpellSyncC2SPacket(-1));
        else
            MentalismMessages.sendToServer(new SelectedSpellSyncC2SPacket(SpellCastData.availableNums.get(selectedSegment)));
    }

    private void updateIconSize()
    {
        float radialWidth = menuOuterRadius-menuInnerRadius;
        float radialCenter = (menuOuterRadius+menuInnerRadius)/2f;
        float segmentWidth = (float) (2*Math.PI/numSegments)*radialCenter;
        iconRadius = Math.min(radialWidth, segmentWidth) * 0.375f;
    }

    private void updateNumSegments()
    {
        numSegments = SpellCastData.availableNums.size();
    }

    private void updateMenuAlpha()
    {
        menuAlpha = renderFade;
    }

    private void updateSelectedSegment(int width, int height)
    {
        if(numSegments < 1)
        {
            selectedSegment = -1;
            return;
        }

        float mouseXScreen = (float) mouse.xpos() * (float)mc.getWindow().getGuiScaledWidth() / (float)mc.getWindow().getScreenWidth();
        float mouseYScreen = (float) mouse.ypos() * (float)mc.getWindow().getGuiScaledWidth() / (float)mc.getWindow().getScreenWidth();

        float mouseXRelToCenter = mouseXScreen-(width /2f);
        float mouseYRelToCenter = mouseYScreen-(height /2f);

        float distFromCenter = GraphicsUtil.magnitude2D(mouseXRelToCenter, mouseYRelToCenter);
        float angleFromXHat = GraphicsUtil.angleFromXHat2D(mouseXRelToCenter, mouseYRelToCenter);

        float segmentWidth = (float) (2 * Math.PI) / numSegments;
        selectedSegment = (int) Math.floor(angleFromXHat / segmentWidth);

        // if mouse is over the wheel, player is casting
        /*boolean mouseInCastingWheel = distFromCenter < menuOuterRadius && distFromCenter > menuInnerRadius;
        MentalismMessages.sendToServer(new CastingStateSyncC2SPacket(mouseInCastingWheel));*/
    }

    private void updateMenuRadius(int height)
    {
        backgroundRadius = (height/2f) * Mth.map(renderFade, 0.0f, 1.0f, 0.3f, 1.0f);
        menuOuterRadius = backgroundRadius*0.75f;
        menuInnerRadius = backgroundRadius*0.4f;
    }

    private void renderMagicMenuLayout(Matrix4f pose, int width, int height)
    {
        // big background overlay circle
        GraphicsUtil.renderSquareTextureOverlay(pose, backgroundRadius, width/2f, height/2f, magicCircle, 0.3f* menuAlpha);

        if(numSegments > 1)
        {
            int color = FastColor.ARGB32.color((int) (0.5*255* menuAlpha), 255, 255, 255);
            GraphicsUtil.renderRadialSeparators(pose, numSegments, menuInnerRadius, menuOuterRadius, width/2f, height/2f, color);
        }

        // menu border circles
        GraphicsUtil.renderSquareTextureOverlay(pose, menuOuterRadius, width/2f, height/2f, circle, 0.5f* menuAlpha);
        GraphicsUtil.renderSquareTextureOverlay(pose, menuInnerRadius, width/2f, height/2f, circle, 0.5f* menuAlpha);
    }

    private void renderHyperfocusDarkness(Matrix4f pose, int width, int height)
    {
        if(!ProgressionData.canSeeWhenCasting)
        {
            // black background
            GraphicsUtil.renderTextureOverlay(pose, 0f, 0f, width, height, black, menuAlpha);
        }
    }

    private void renderSpellIcons(Matrix4f pose, int screenWidth, int screenHeight)
    {
        int currentSegment = 0;

        for(Spell sp : SpellRegistry.SPELLS.get().getValues())
        {
            if(SpellCastData.availableNums.contains(sp.getSpellNum()))
            {
                renderSegmentIcon(pose, screenWidth, screenHeight, sp.getSpellIcon(), currentSegment, menuAlpha);
                if(!SpellCastData.canCastNums.contains(sp.getSpellNum()))
                {
                    renderSegmentIcon(pose, screenWidth, screenHeight, cantcast, currentSegment, menuAlpha*0.5f);
                }
                currentSegment++;
            }
        }
    }

    private void renderSegmentIcon(Matrix4f pose, int screenWidth, int screenHeight, ResourceLocation icon, int segment, float alpha)
    {
        if(segment > -1 && segment < numSegments)
        {
            // little selection circle
            Matrix4f selectionPose = getPoseForSegment(pose, screenWidth / 2, screenHeight / 2, (menuOuterRadius+menuInnerRadius)/2, segment);
            GraphicsUtil.renderSquareTextureOverlay(selectionPose, this.iconRadius, 0, 0, icon, alpha);
        }
    }

    private Matrix4f getPoseForSegment(Matrix4f inPose, int centerX, int centerY, float radius, int segment)
    {
        Matrix4f selectedPose = inPose.copy();
        selectedPose.translate(new Vector3f(centerX, centerY, 0));

        // angle to the middle of the selected 'pie slice'
        float selectionAngle = 2*(float)Math.PI * (2*segment + 1) / (2*numSegments);
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

            // if the menu is fully rendered and we close it, sync the selection to the server
            if(renderFade > 0.9f)
                syncSelectionToServer();
        }

        this.active = active;
    }

    public void setNumSegments(int numSegments)
    {
        this.numSegments = numSegments;
    }
}