package io.github.tomtom2211.noteblock_helper.utils;

import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.VertexRendering;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

import java.awt.*;
import java.util.Objects;

public class BlockHighlighter {
    public static void highlightBlock(Box block, WorldRenderContext context, int rgb){
        float r = ((rgb >> 16) & 0xFF) / 255f;
        float g = ((rgb >> 8) & 0xFF) / 255f;
        float b = (rgb & 0xFF) / 255f;
        float a = 1f; // fully opaque

        if(block!=null) {
            MatrixStack matrices = context.matrixStack(); // Gives a current game view state (yaw/distance etc.)
            VertexConsumerProvider consumers = context.consumers(); // Create a consumer to let minecraft know you want to render something
            Vec3d cameraPos = context.camera().getPos(); // Camera position
                Box shifted = block.offset(-cameraPos.x, -cameraPos.y, -cameraPos.z);
                VertexConsumer buffer = Objects.requireNonNull(consumers).getBuffer(RenderLayer.getLines());// Something, you can actually write into
                VertexRendering.drawBox(
                        Objects.requireNonNull(matrices),
                        buffer,
                        shifted,
                        r,g,b,a
                );
        }
    }
}
