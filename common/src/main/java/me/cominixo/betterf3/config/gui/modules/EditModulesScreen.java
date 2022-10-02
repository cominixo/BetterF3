package me.cominixo.betterf3.config.gui.modules;

import me.cominixo.betterf3.config.ModConfigFile;
import me.cominixo.betterf3.modules.BaseModule;
import me.cominixo.betterf3.modules.ChunksModule;
import me.cominixo.betterf3.modules.CoordsModule;
import me.cominixo.betterf3.modules.EmptyModule;
import me.cominixo.betterf3.modules.EntityModule;
import me.cominixo.betterf3.modules.FpsModule;
import me.cominixo.betterf3.modules.HelpModule;
import me.cominixo.betterf3.modules.SoundModule;
import me.cominixo.betterf3.modules.SystemModule;
import me.cominixo.betterf3.utils.DebugLine;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import me.shedaniel.clothconfig2.gui.entries.BooleanListEntry;
import me.shedaniel.clothconfig2.gui.entries.ColorEntry;
import me.shedaniel.clothconfig2.gui.entries.IntegerListEntry;
import me.shedaniel.clothconfig2.gui.entries.StringListEntry;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
import org.apache.commons.lang3.StringUtils;

/**
 * The Edit Modules screen.
 */
public final class EditModulesScreen {

  private EditModulesScreen() {
    // Do nothing
  }

  /**
   * Gets the config builder.
   *
   * @param module the module
   * @param parent the parent module screen
   * @return The ConfigBuilder for the add module screen
   */
  public static ConfigBuilder configBuilder(final BaseModule module, final ModulesScreen parent) {

    final ConfigBuilder builder = ConfigBuilder.create()
    .setParentScreen(parent);

    builder.setSavingRunnable(ModConfigFile.saveRunnable);

    final ConfigEntryBuilder entryBuilder = builder.entryBuilder();

    final ConfigCategory general = builder.getOrCreateCategory(Text.translatable("config.betterf3" +
    ".category.general"));

    final BooleanListEntry moduleEnabled = entryBuilder.startBooleanToggle(Text.translatable("config" +
    ".betterf3.module.enable"), module.enabled)
    .setDefaultValue(true)
    .setTooltip(Text.translatable("config.betterf3.module.enable.tooltip"))
    .setSaveConsumer(newValue -> {
      module.enabled = newValue;
      module.enabled(newValue);
    })
    .build();

    general.addEntry(moduleEnabled);

    if (module instanceof CoordsModule coordsModule) {

      if (coordsModule.colorX != null && coordsModule.defaultColorX != null) {
        final ColorEntry colorX =
        entryBuilder.startColorField(Text.translatable("config.betterf3.color.x"), coordsModule.colorX.getRgb())
        .setDefaultValue(coordsModule.defaultColorX.getRgb())
        .setTooltip(Text.translatable("config.betterf3.color.x.tooltip"))
        .setSaveConsumer(newValue -> coordsModule.colorX = TextColor.fromRgb(newValue))
        .build();

        general.addEntry(colorX);
      }
      if (coordsModule.colorY != null && coordsModule.defaultColorY != null) {
        final ColorEntry colorY =
        entryBuilder.startColorField(Text.translatable("config.betterf3.color.y"), coordsModule.colorY.getRgb())
        .setDefaultValue(coordsModule.defaultColorY.getRgb())
        .setTooltip(Text.translatable("config.betterf3.color.y.tooltip"))
        .setSaveConsumer(newValue -> coordsModule.colorY = TextColor.fromRgb(newValue))
        .build();

        general.addEntry(colorY);
      }
      if (coordsModule.colorZ != null && coordsModule.defaultColorZ != null) {
        final ColorEntry colorZ =
        entryBuilder.startColorField(Text.translatable("config.betterf3.color.z"), coordsModule.colorZ.getRgb())
        .setDefaultValue(coordsModule.defaultColorZ.getRgb())
        .setTooltip(Text.translatable("config.betterf3.color.z.tooltip"))
        .setSaveConsumer(newValue -> coordsModule.colorZ = TextColor.fromRgb(newValue))
        .build();

        general.addEntry(colorZ);
      }

    }

    if (module instanceof FpsModule fpsModule) {

      if (fpsModule.colorHigh != null && fpsModule.defaultColorHigh != null) {
        final ColorEntry colorHigh = entryBuilder.startColorField(Text.translatable("config.betterf3" +
        ".color.fps.high"), fpsModule.colorHigh.getRgb())
        .setDefaultValue(fpsModule.defaultColorHigh.getRgb())
        .setTooltip(Text.translatable("config.betterf3.color.fps.high.tooltip"))
        .setSaveConsumer(newValue -> fpsModule.colorHigh = TextColor.fromRgb(newValue))
        .build();

        general.addEntry(colorHigh);
      }
      if (fpsModule.colorMed != null && fpsModule.defaultColorMed != null) {
        final ColorEntry colorMed = entryBuilder.startColorField(Text.translatable("config.betterf3" +
        ".color.fps.medium"), fpsModule.colorMed.getRgb())
        .setDefaultValue(fpsModule.defaultColorMed.getRgb())
        .setTooltip(Text.translatable("config.betterf3.color.fps.medium.tooltip"))
        .setSaveConsumer(newValue -> fpsModule.colorMed = TextColor.fromRgb(newValue))
        .build();

        general.addEntry(colorMed);
      }
      if (fpsModule.colorLow != null && fpsModule.defaultColorLow != null) {
        final ColorEntry colorLow = entryBuilder.startColorField(Text.translatable("config.betterf3" +
        ".color.fps.low"), fpsModule.colorLow.getRgb())
        .setDefaultValue(fpsModule.defaultColorLow.getRgb())
        .setTooltip(Text.translatable("config.betterf3.color.fps.low.tooltip"))
        .setSaveConsumer(newValue -> fpsModule.colorLow = TextColor.fromRgb(newValue))
        .build();

        general.addEntry(colorLow);
      }

    }

    if (module instanceof EmptyModule emptyModule) {

      final IntegerListEntry emptyLines = entryBuilder.startIntField(Text.translatable("config.betterf3" +
      ".empty_lines"), emptyModule.emptyLines)
      .setDefaultValue(emptyModule.defaultEmptyLines)
      .setTooltip(Text.translatable("config.betterf3.empty_lines.tooltip"))
      .setSaveConsumer(newValue -> {
        if (newValue > 20) {
          newValue = 20;
        } else if (newValue < 1) {
          newValue = 1;
        }
        emptyModule.emptyLines = newValue;
      })
      .build();

      general.addEntry(emptyLines);

    }

    if (module instanceof ChunksModule chunksModule) {

      if (chunksModule.enabledColor != null && chunksModule.defaultEnabledColor != null) {
        final ColorEntry enabledColor = entryBuilder
          .startColorField(Text.translatable("config.betterf3.color.chunks.enabled"),
            chunksModule.enabledColor.getRgb())
          .setDefaultValue(chunksModule.defaultEnabledColor.getRgb())
          .setTooltip(Text.translatable("config.betterf3.color.chunks.enabled.tooltip"))
          .setSaveConsumer(newValue -> chunksModule.enabledColor = TextColor.fromRgb(newValue))
          .build();

        general.addEntry(enabledColor);
      }

      if (chunksModule.disabledColor != null && chunksModule.defaultDisabledColor != null) {
        final ColorEntry disabledColor = entryBuilder
          .startColorField(Text.translatable("config.betterf3.color.chunks.disabled"),
            chunksModule.disabledColor.getRgb())
          .setDefaultValue(chunksModule.defaultDisabledColor.getRgb())
          .setTooltip(Text.translatable("config.betterf3.color.chunks.disabled.tooltip"))
          .setSaveConsumer(newValue -> chunksModule.disabledColor = TextColor.fromRgb(newValue))
          .build();

        general.addEntry(disabledColor);
      }

      if (chunksModule.totalColor != null && chunksModule.defaultTotalColor != null) {
        final ColorEntry totalColor = entryBuilder
          .startColorField(Text.translatable("config.betterf3.color.chunks.total"), chunksModule.totalColor.getRgb())
          .setDefaultValue(chunksModule.defaultTotalColor.getRgb())
          .setTooltip(Text.translatable("config.betterf3.color.chunks.total.tooltip"))
          .setSaveConsumer(newValue -> chunksModule.totalColor = TextColor.fromRgb(newValue))
          .build();

        general.addEntry(totalColor);
      }
    }

    if (module instanceof EntityModule entityModule) {
      if (entityModule.totalColor != null && entityModule.defaultTotalColor != null) {
        final ColorEntry totalColor = entryBuilder
          .startColorField(Text.translatable("config.betterf3.color.entities.total"), entityModule.totalColor.getRgb())
          .setDefaultValue(entityModule.defaultTotalColor.getRgb())
          .setTooltip(Text.translatable("config.betterf3.color.entities.total.tooltip"))
          .setSaveConsumer(newValue -> entityModule.totalColor = TextColor.fromRgb(newValue))
          .build();

        general.addEntry(totalColor);
      }
    }

    if (module instanceof HelpModule helpModule) {

      if (helpModule.enabledColor != null && helpModule.defaultEnabledColor != null) {
        final ColorEntry enabledColor = entryBuilder
          .startColorField(Text.translatable("config.betterf3.color.help.enabled"), helpModule.enabledColor.getRgb())
          .setDefaultValue(helpModule.defaultEnabledColor.getRgb())
          .setTooltip(Text.translatable("config.betterf3.color.help.enabled.tooltip"))
          .setSaveConsumer(newValue -> helpModule.enabledColor = TextColor.fromRgb(newValue))
          .build();

        general.addEntry(enabledColor);
      }

      if (helpModule.disabledColor != null && helpModule.defaultDisabledColor != null) {
        final ColorEntry disabledColor = entryBuilder
          .startColorField(Text.translatable("config.betterf3.color.help.disabled"), helpModule.disabledColor.getRgb())
          .setDefaultValue(helpModule.defaultDisabledColor.getRgb())
          .setTooltip(Text.translatable("config.betterf3.color.help.disabled.tooltip"))
          .setSaveConsumer(newValue -> helpModule.disabledColor = TextColor.fromRgb(newValue))
          .build();

        general.addEntry(disabledColor);
      }
    }

    if (module instanceof SoundModule soundModule) {
      if (soundModule.maximumColor != null && soundModule.defaultMaximumColor != null) {
        final ColorEntry maximumColor = entryBuilder
          .startColorField(Text.translatable("config.betterf3.color.sound.maximum"), soundModule.maximumColor.getRgb())
          .setDefaultValue(soundModule.defaultMaximumColor.getRgb())
          .setTooltip(Text.translatable("config.betterf3.color.sound.maximum.tooltip"))
          .setSaveConsumer(newValue -> soundModule.maximumColor = TextColor.fromRgb(newValue))
          .build();

        general.addEntry(maximumColor);
      }
    }

    if (module instanceof SystemModule systemModule) {
      final BooleanListEntry memoryColorToggle = entryBuilder
        .startBooleanToggle(Text.translatable("config.betterf3.memory_color_toggle"), systemModule.memoryColorToggle)
        .setDefaultValue(systemModule.defaultMemoryColorToggle)
        .setTooltip(Text.translatable("config.betterf3.memory_color_toggle.tooltip"))
        .setSaveConsumer(newValue -> systemModule.memoryColorToggle = newValue)
        .build();

      general.addEntry(memoryColorToggle);

      final StringListEntry timeFormat = entryBuilder
        .startStrField(Text.translatable("config.betterf3.time_format"), systemModule.timeFormat)
        .setDefaultValue(systemModule.defaultTimeFormat)
        .setTooltip(Text.translatable("config.betterf3.time_format.tooltip"))
        .setSaveConsumer(newValue -> systemModule.timeFormat = newValue)
        .build();

      general.addEntry(timeFormat);
    }

    if (module.nameColor != null && module.defaultNameColor != null) {
      final ColorEntry nameColor = entryBuilder.startColorField(Text.translatable("config.betterf3" +
      ".color.name"), module.nameColor.getRgb())
      .setDefaultValue(module.defaultNameColor.getRgb())
      .setTooltip(Text.translatable("config.betterf3.color.name.tooltip"))
      .setSaveConsumer(newValue -> module.nameColor = TextColor.fromRgb(newValue))
      .build();

      general.addEntry(nameColor);
    }

    if (module.valueColor != null && module.defaultValueColor != null) {
      final ColorEntry valueColor = entryBuilder.startColorField(Text.translatable("config.betterf3" +
      ".color.value"), module.valueColor.getRgb())
      .setDefaultValue(module.defaultValueColor.getRgb())
      .setTooltip(Text.translatable("config.betterf3.color.value.tooltip"))
      .setSaveConsumer(newValue -> module.valueColor = TextColor.fromRgb(newValue))
      .build();

      general.addEntry(valueColor);
    }

    if (module.lines().size() > 1) {
      for (final DebugLine line : module.lines()) {

        if (line.id().equals("") || line.id().equals("nothing")) {
          continue;
        }

        Text name = Text.translatable("text.betterf3.line." + line.id());

        if (name.getString().equals("")) {
          name = Text.of(StringUtils.capitalize(line.id().replace("_", " ")));
        }

        final BooleanListEntry enabled = entryBuilder.startBooleanToggle(name, line.enabled)
        .setDefaultValue(true)
        .setTooltip(Text.translatable("config.betterf3.disable_line.tooltip"))
        .setSaveConsumer(newValue -> line.enabled = newValue)
        .build();

        general.addEntry(enabled);

      }
    }

    builder.transparentBackground();
    return builder;

  }
}
