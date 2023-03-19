package me.beaubaer.mentalism.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Matrix4f;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;

public class GraphicsUtil
{
    public static void renderRadialSeparators(Matrix4f pose, int segAmount, float innerRadius, float outerRadius, float centerX, float centerY, int color)
    {
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

            line(linePose, 1, 0, 0, 0f, length, color);

        }
    }

    public static void line(Matrix4f pose, float lineWidth, int x1, int y1, int x2, int y2, int color)
    {
        Vector3f pos1 = new Vector3f(x1, y1, 0);
        Vector3f pos2 = new Vector3f(x2, y2, 0);

        // vector points from pos1 to pos2
        Vector3f pointingVector = pos2.copy();
        pointingVector.sub(pos1);

        float rotation = angleFromXHat2D(pointingVector.x(), pointingVector.y());
        float length = magnitude2D(pointingVector.x(), pointingVector.y());

        line(pose, lineWidth, x1, y1, rotation, length, color);
    }

    public static void line(Matrix4f pose, float lineWidth, int x1, int y1, float rotation, float length, int color)
    {
        Matrix4f innerPose = pose.copy();
        innerPose.translate(new Vector3f(x1, y1, 0));
        Quaternion rotQuat = Quaternion.fromXYZ(new Vector3f(0, 0, rotation));
        innerPose.multiply(rotQuat);

        innerFill(innerPose, 0, -lineWidth/2, length, lineWidth/2, color);
    }

    public static void debugString(PoseStack ps, String str, int offset, int width, int height)
    {
        int textColor = FastColor.ARGB32.color(255, 255, 255, 255);
        Gui.drawCenteredString(ps, Minecraft.getInstance().font, str, width/2, (height/2)+offset, textColor);
    }

    public static void renderSquareTextureOverlay(Matrix4f pMatrix, float radius, float centerX, float centerY, ResourceLocation pTextureLocation, float pAlpha)
    {
        renderTextureOverlay(pMatrix, centerX-radius, centerY-radius, centerX+radius, centerY+radius, pTextureLocation, pAlpha);
    }

    public static void renderTextureOverlay(Matrix4f pMatrix, float pMinX, float pMinY, float pMaxX, float pMaxY, ResourceLocation pTextureLocation, float pAlpha)
    {
        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, pAlpha);
        RenderSystem.setShaderTexture(0, pTextureLocation);
        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder bufferbuilder = tesselator.getBuilder();
        bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        bufferbuilder.vertex(pMatrix, pMinX, pMaxY, -90F).uv(0.0F, 1.0F).endVertex();
        bufferbuilder.vertex(pMatrix, pMaxX, pMaxY, -90F).uv(1.0F, 1.0F).endVertex();
        bufferbuilder.vertex(pMatrix, pMaxX, pMinY, -90F).uv(1.0F, 0.0F).endVertex();
        bufferbuilder.vertex(pMatrix, pMinX, pMinY, -90F).uv(0.0F, 0.0F).endVertex();
        tesselator.end();
        RenderSystem.depthMask(true);
        RenderSystem.enableDepthTest();
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.disableBlend();
    }

    // angle clockwise from x hat to vec in radians
    public static float angleFromXHat2D(float x, float y)
    {
        return (float) ( Math.atan2(-y, -x) + Math.PI );
    }

    public static float magnitude2D(float x, float y)
    {
        return (float) Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
    }

    private static void innerFill(Matrix4f pMatrix, float pMinX, float pMinY, float pMaxX, float pMaxY, int pColor)
    {
        if (pMinX < pMaxX) {
            float i = pMinX;
            pMinX = pMaxX;
            pMaxX = i;
        }

        if (pMinY < pMaxY) {
            float j = pMinY;
            pMinY = pMaxY;
            pMaxY = j;
        }

        float f3 = (float)(pColor >> 24 & 255) / 255.0F;
        float f = (float)(pColor >> 16 & 255) / 255.0F;
        float f1 = (float)(pColor >> 8 & 255) / 255.0F;
        float f2 = (float)(pColor & 255) / 255.0F;
        BufferBuilder bufferbuilder = Tesselator.getInstance().getBuilder();
        RenderSystem.enableBlend();
        RenderSystem.disableTexture();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
        bufferbuilder.vertex(pMatrix, pMinX, pMaxY, 0.0F).color(f, f1, f2, f3).endVertex();
        bufferbuilder.vertex(pMatrix, pMaxX, pMaxY, 0.0F).color(f, f1, f2, f3).endVertex();
        bufferbuilder.vertex(pMatrix, pMaxX, pMinY, 0.0F).color(f, f1, f2, f3).endVertex();
        bufferbuilder.vertex(pMatrix, pMinX, pMinY, 0.0F).color(f, f1, f2, f3).endVertex();
        bufferbuilder.end();
        BufferUploader.end(bufferbuilder);
        RenderSystem.enableTexture();
        RenderSystem.disableBlend();
    }
}
