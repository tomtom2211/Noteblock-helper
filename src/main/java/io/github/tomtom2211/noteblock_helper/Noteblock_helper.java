package io.github.tomtom2211.noteblock_helper;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Noteblock_helper implements ModInitializer {
    @Environment(EnvType.CLIENT)
    public static final Logger LOGGER = LoggerFactory.getLogger("TomsAddons");
    @Override
    public void onInitialize() {
        LOGGER.info("Noteblock_helper successfully launched!");
    }
}
