package me.cominixo.betterf3.config.gui.modules;

import me.cominixo.betterf3.config.ModConfigFile;
import me.cominixo.betterf3.modules.BaseModule;
import me.cominixo.betterf3.modules.CoordsModule;
import me.cominixo.betterf3.utils.DebugLine;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import me.shedaniel.clothconfig2.gui.entries.BooleanListEntry;
import me.shedaniel.clothconfig2.gui.entries.ColorEntry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
import net.minecraft.text.TranslatableText;
import org.apache.commons.lang3.text.WordUtils;

public class EditModulesScreen {

    public static ConfigBuilder getConfigBuilder(BaseModule module) {

        ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(MinecraftClient.getInstance().currentScreen);



        builder.setSavingRunnable(ModConfigFile.saveRunnable);

        ConfigEntryBuilder entryBuilder = builder.entryBuilder();

        ConfigCategory general = builder.getOrCreateCategory(new TranslatableText("config.betterf3.category.general"));


        BooleanListEntry moduleEnabled = entryBuilder.startBooleanToggle(new TranslatableText("config.betterf3.module.enable"), module.enabled)
                .setDefaultValue(true)
                .setTooltip(new TranslatableText("config.betterf3.module.enable.tooltip"))
                .setSaveConsumer(newValue -> {
                    module.enabled = newValue;
                    module.setEnabled(newValue);
                })
                .build();


        general.addEntry(moduleEnabled);

        if (module instanceof CoordsModule) {
            CoordsModule coordsModule = (CoordsModule) module;

            if (coordsModule.colorX != null && coordsModule.defaultColorX != null) {
                ColorEntry colorX = entryBuilder.startColorField(new TranslatableText("config.betterf3.color.x"), coordsModule.colorX.getRgb())
                        .setDefaultValue(coordsModule.defaultColorX.getRgb())
                        .setTooltip(new TranslatableText("config.betterf3.color.x.tooltip"))
                        .setSaveConsumer(newValue -> coordsModule.colorX = TextColor.fromRgb(newValue))
                        .build();

                general.addEntry(colorX);
            }
            if (coordsModule.colorY != null && coordsModule.defaultColorY != null) {
                ColorEntry colorY = entryBuilder.startColorField(new TranslatableText("config.betterf3.color.y"), coordsModule.colorY.getRgb())
                        .setDefaultValue(coordsModule.defaultColorY.getRgb())
                        .setTooltip(new TranslatableText("config.betterf3.color.y.tooltip"))
                        .setSaveConsumer(newValue -> coordsModule.colorY = TextColor.fromRgb(newValue))
                        .build();

                general.addEntry(colorY);
            }
            if (coordsModule.colorZ != null && coordsModule.defaultColorZ != null) {
                ColorEntry colorZ = entryBuilder.startColorField(new TranslatableText("config.betterf3.color.z"), coordsModule.colorZ.getRgb())
                        .setDefaultValue(coordsModule.defaultColorZ.getRgb())
                        .setTooltip(new TranslatableText("config.betterf3.color.z.tooltip"))
                        .setSaveConsumer(newValue -> coordsModule.colorZ = TextColor.fromRgb(newValue))
                        .build();

                general.addEntry(colorZ);
            }

        }

        if (module.nameColor != null && module.defaultNameColor != null) {
            ColorEntry nameColor = entryBuilder.startColorField(new TranslatableText("config.betterf3.color.name"), module.nameColor.getRgb())
                    .setDefaultValue(module.defaultNameColor.getRgb())
                    .setTooltip(new TranslatableText("config.betterf3.color.name.tooltip"))
                    .setSaveConsumer(newValue -> module.nameColor = TextColor.fromRgb(newValue))
                    .build();

            general.addEntry(nameColor);
        }

        if (module.valueColor != null && module.defaultValueColor != null) {
            ColorEntry valueColor = entryBuilder.startColorField(new TranslatableText("config.betterf3.color.value"), module.valueColor.getRgb())
                    .setDefaultValue(module.defaultValueColor.getRgb())
                    .setTooltip(new TranslatableText("config.betterf3.color.value.tooltip"))
                    .setSaveConsumer(newValue -> module.valueColor = TextColor.fromRgb(newValue))
                    .build();


            general.addEntry(valueColor);
        }


        if (module.getLines().size() > 1) {
            for (DebugLine line : module.getLines()) {

                if (line.getId().equals("")) {
                    continue;
                }

                Text name = new TranslatableText("text.betterf3.line." + line.getId());

                if (name.getString().equals("")) {
                    name = new LiteralText(WordUtils.capitalizeFully(line.getId().replace("_", " ")));
                }

                BooleanListEntry enabled = entryBuilder.startBooleanToggle(name, line.enabled)
                        .setDefaultValue(true)
                        .setTooltip(new TranslatableText("config.betterf3.disable_line.tooltip"))
                        .setSaveConsumer(newValue -> line.enabled = newValue)
                        .build();

                general.addEntry(enabled);

            }
        }


        builder.transparentBackground();

        return builder;

    }
}
