package io.github.tomtom2211.noteblock_helper;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.VertexRendering;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Objects;


public class Noteblock_helper implements ModInitializer {
    @Environment(EnvType.CLIENT)
    public static final Logger LOGGER = LoggerFactory.getLogger("TomsAddons");
    private static ArrayList<Box> noteBlock = new ArrayList<>();
    @Override
    public void onInitialize() {
        ClientTickEvents.END_CLIENT_TICK.register(client-> {
            if(client.player!=null && client.world!=null){
                World world = client.world;
                BlockPos playerPos = client.player.getBlockPos();
                for(int x = -5; x<=5 ;x++){
                    for(int y = -5; y<=5 ; y++){
                        for(int z = -5; z<=5 ; z++){
                            if(world.getBlockState(playerPos.add(x,y,z)).toString().contains("instrument")){
                                noteBlock.add(new Box(playerPos.add(x, y, z)));
                            }
                            else{
                                try{
                                    noteBlock.remove(new Box(playerPos.add(x,y,z)));
                                }
                                catch(RuntimeException e){
                                    System.out.println();
                                }
                            }
                        }
                    }
                }
            }
        });
        WorldRenderEvents.BEFORE_ENTITIES.register(context -> {
            if(noteBlock !=null ) {
                MatrixStack matrices = context.matrixStack(); // Gives a current game view state (yaw/distance etc.)
                VertexConsumerProvider consumers = context.consumers(); // Create a consumer to let minecraft know you want to render something
                Vec3d cameraPos = context.camera().getPos(); // Camera position
                for (Box box : noteBlock) {
                    Box shifted = box.offset(-cameraPos.x, -cameraPos.y, -cameraPos.z);
                    VertexConsumer buffer = Objects.requireNonNull(consumers).getBuffer(RenderLayer.getLines());// Something, you can actually write into
                    VertexRendering.drawBox(
                            Objects.requireNonNull(matrices),
                            buffer,
                            shifted,
                            1.0f, 0.0f, 0.0f, 1.0f // RGBA = red
                    );
                }
            }
        });
        LOGGER.info("Noteblock_helper successfully launched!");
    }
}
