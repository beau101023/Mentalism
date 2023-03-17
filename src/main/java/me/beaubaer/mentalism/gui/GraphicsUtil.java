package me.beaubaer.mentalism.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Matrix4f;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;

public class GraphicsUtil
{
    public static void line(Matrix4f pose, float lineWidth, int x1, int y1, int x2, int y2, int color)
    {
        Vector3f pos1 = new Vector3f(x1, y1, 0);
        Vector3f pos2 = new Vector3f(x2, y2, 0);

        // vector points from pos1 to pos2
        Vector3f pointingVector = pos2.copy();
        pointingVector.sub(pos1);

        float rotation = angleFromX(pointingVector);
        float length = magnitude(pointingVector);

        line(pose, lineWidth, x1, y1, rotation, length, color);
    }

    public static void line(Matrix4f pose, float lineWidth, int x1, int y1, float rotation, float length, int color)
    {
        Quaternion rotQuat = Quaternion.fromXYZ(new Vector3f(0, 0, rotation));
        Matrix4f innerPose = pose.copy();
        innerPose.translate(new Vector3f(x1, y1, 0));
        innerPose.multiply(rotQuat);

        innerFill(innerPose, 0, -lineWidth/2, length, lineWidth/2, color);
    }

    public static void renderTextureOverlay(Matrix4f pMatrix, float pMinX, float pMinY, float pMaxX, float pMaxY, ResourceLocation pTextureLocation, float pAlpha) {
        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
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
    }

    // angle from x hat to vec in radians
    private static float angleFromX(Vector3f vec)
    {
        return (float) Math.acos(vec.x() / magnitude(vec));
    }

    private static float magnitude(Vector3f vec)
    {
        return (float) Math.sqrt(Math.pow(vec.x(), 2) + Math.pow(vec.y(), 2) + Math.pow(vec.z(), 2));
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
