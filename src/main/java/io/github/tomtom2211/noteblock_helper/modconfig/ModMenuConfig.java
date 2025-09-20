package io.github.tomtom2211.noteblock_helper.modconfig;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import net.minecraft.text.Text;

public class ModMenuConfig implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> {
            // Create builder instance
            ConfigBuilder builder = ConfigBuilder.create()
                    .setParentScreen(parent)
                    .setTitle(Text.of("Noteblock helper"));

            // Create general category
            var general = builder.getOrCreateCategory(Text.of("General"));

            // Creating the user input section
            var entryBuilder = builder.entryBuilder()
                    .startBooleanToggle(Text.of("Enable block highlighter"), Config.config.highlightSelectedBlocks)
                    .setDefaultValue(true)
                    .setSaveConsumer(newValue -> Config.config.highlightSelectedBlocks = newValue)
                    .build();
            // Add the user input section into the general category
            general.addEntry(entryBuilder);
            // Add the save button
            builder.setSavingRunnable(Config::save);
            // Add the config builder into the modmenu config button
            return builder.build();
        };
    }

}
