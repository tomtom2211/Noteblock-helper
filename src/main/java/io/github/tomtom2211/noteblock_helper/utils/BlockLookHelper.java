package io.github.tomtom2211.noteblock_helper.utils;

import net.minecraft.client.MinecraftClient;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Box;


public class BlockLookHelper {


    public static Box getLookedAtBlockBox(MinecraftClient client, double reach) {
        if (client.player == null || client.world == null || client.getCameraEntity() == null) {
            return null;
        }
        HitResult hitResult = client.player.raycast(reach,0f,false);
        if(hitResult.getType() == HitResult.Type.BLOCK) {
            BlockHitResult blockHitResult = (BlockHitResult) hitResult;
            return new Box(blockHitResult.getBlockPos());
        }
        return null;
    }
}
