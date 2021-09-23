package me.cominixo.betterf3.config.gui.modules;

import me.cominixo.betterf3.config.ModConfigFile;
import me.cominixo.betterf3.modules.BaseModule;
import me.cominixo.betterf3.modules.CoordsModule;
import me.cominixo.betterf3.modules.FpsModule;
import me.cominixo.betterf3.utils.DebugLine;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import me.shedaniel.clothconfig2.gui.entries.BooleanListEntry;
import me.shedaniel.clothconfig2.gui.entries.ColorEntry;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextColor;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import org.apache.commons.lang3.text.WordUtils;

/**
 * The Edit Modules screen.
 */
public class EditModulesScreen {

    /**
     * Gets the config builder.
     *
     * @param parent The parent module screen
     * @return The ConfigBuilder for the add module screen
     */
    public static ConfigBuilder getConfigBuilder(BaseModule module, ModulesScreen parent) {

        ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(parent);

        builder.setSavingRunnable(ModConfigFile.saveRunnable);

        ConfigEntryBuilder entryBuilder = builder.entryBuilder();

        ConfigCategory general = builder.getOrCreateCategory(new TranslatableComponent("config.betterf3.category.general"));

        BooleanListEntry moduleEnabled = entryBuilder.startBooleanToggle(new TranslatableComponent("config.betterf3.module.enable"), module.enabled)
                .setDefaultValue(true)
                .setTooltip(new TranslatableComponent("config.betterf3.module.enable.tooltip"))
                .setSaveConsumer(newValue -> {
                    module.enabled = newValue;
                    module.setEnabled(newValue);
                })
                .build();

        general.addEntry(moduleEnabled);

        if (module instanceof CoordsModule coordsModule) {

            if (coordsModule.colorX != null && coordsModule.defaultColorX != null) {
                ColorEntry colorX = entryBuilder.startColorField(new TranslatableComponent("config.betterf3.color.x"), coordsModule.colorX.getValue())
                        .setDefaultValue(coordsModule.defaultColorX.getValue())
                        .setTooltip(new TranslatableComponent("config.betterf3.color.x.tooltip"))
                        .setSaveConsumer(newValue -> coordsModule.colorX = TextColor.fromRgb(newValue))
                        .build();

                general.addEntry(colorX);
            }
            if (coordsModule.colorY != null && coordsModule.defaultColorY != null) {
                ColorEntry colorY = entryBuilder.startColorField(new TranslatableComponent("config.betterf3.color.y"), coordsModule.colorY.getValue())
                        .setDefaultValue(coordsModule.defaultColorY.getValue())
                        .setTooltip(new TranslatableComponent("config.betterf3.color.y.tooltip"))
                        .setSaveConsumer(newValue -> coordsModule.colorY = TextColor.fromRgb(newValue))
                        .build();

                general.addEntry(colorY);
            }
            if (coordsModule.colorZ != null && coordsModule.defaultColorZ != null) {
                ColorEntry colorZ = entryBuilder.startColorField(new TranslatableComponent("config.betterf3.color.z"), coordsModule.colorZ.getValue())
                        .setDefaultValue(coordsModule.defaultColorZ.getValue())
                        .setTooltip(new TranslatableComponent("config.betterf3.color.z.tooltip"))
                        .setSaveConsumer(newValue -> coordsModule.colorZ = TextColor.fromRgb(newValue))
                        .build();

                general.addEntry(colorZ);
            }

        }

        if (module instanceof FpsModule fpsModule) {

            if (fpsModule.colorHigh != null && fpsModule.defaultColorHigh != null) {
                ColorEntry colorHigh = entryBuilder.startColorField(new TranslatableComponent("config.betterf3.color.fps.high"), fpsModule.colorHigh.getValue())
                        .setDefaultValue(fpsModule.defaultColorHigh.getValue())
                        .setTooltip(new TranslatableComponent("config.betterf3.color.fps.high.tooltip"))
                        .setSaveConsumer(newValue -> fpsModule.colorHigh = TextColor.fromRgb(newValue))
                        .build();

                general.addEntry(colorHigh);
            }
            if (fpsModule.colorMed != null && fpsModule.defaultColorMed != null) {
                ColorEntry colorMed = entryBuilder.startColorField(new TranslatableComponent("config.betterf3.color.fps.medium"), fpsModule.colorMed.getValue())
                        .setDefaultValue(fpsModule.defaultColorMed.getValue())
                        .setTooltip(new TranslatableComponent("config.betterf3.color.fps.medium.tooltip"))
                        .setSaveConsumer(newValue -> fpsModule.colorMed = TextColor.fromRgb(newValue))
                        .build();

                general.addEntry(colorMed);
            }
            if (fpsModule.colorLow != null && fpsModule.defaultColorLow != null) {
                ColorEntry colorLow = entryBuilder.startColorField(new TranslatableComponent("config.betterf3.color.fps.low"), fpsModule.colorLow.getValue())
                        .setDefaultValue(fpsModule.defaultColorLow.getValue())
                        .setTooltip(new TranslatableComponent("config.betterf3.color.fps.low.tooltip"))
                        .setSaveConsumer(newValue -> fpsModule.colorLow = TextColor.fromRgb(newValue))
                        .build();

                general.addEntry(colorLow);
            }

        }


        if (module.nameColor != null && module.defaultNameColor != null) {
            ColorEntry nameColor = entryBuilder.startColorField(new TranslatableComponent("config.betterf3.color.name"), module.nameColor.getValue())
                    .setDefaultValue(module.defaultNameColor.getValue())
                    .setTooltip(new TranslatableComponent("config.betterf3.color.name.tooltip"))
                    .setSaveConsumer(newValue -> module.nameColor = TextColor.fromRgb(newValue))
                    .build();

            general.addEntry(nameColor);
        }

        if (module.valueColor != null && module.defaultValueColor != null) {
            ColorEntry valueColor = entryBuilder.startColorField(new TranslatableComponent("config.betterf3.color.value"), module.valueColor.getValue())
                    .setDefaultValue(module.defaultValueColor.getValue())
                    .setTooltip(new TranslatableComponent("config.betterf3.color.value.tooltip"))
                    .setSaveConsumer(newValue -> module.valueColor = TextColor.fromRgb(newValue))
                    .build();


            general.addEntry(valueColor);
        }


        if (module.getLines().size() > 1) {
            for (DebugLine line : module.getLines()) {

                if (line.getId().equals("")) {
                    continue;
                }

                Component name = new TranslatableComponent("text.betterf3.line." + line.getId());

                if (name.getString().equals("")) {
                    name = new TextComponent(WordUtils.capitalizeFully(line.getId().replace("_", " ")));
                }

                BooleanListEntry enabled = entryBuilder.startBooleanToggle(name, line.enabled)
                        .setDefaultValue(true)
                        .setTooltip(new TranslatableComponent("config.betterf3.disable_line.tooltip"))
                        .setSaveConsumer(newValue -> line.enabled = newValue)
                        .build();

                general.addEntry(enabled);

            }
        }

        builder.transparentBackground();
        return builder;

    }
}
