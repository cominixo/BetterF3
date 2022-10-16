package me.cominixo.betterf3.config;

import com.electronwill.nightconfig.core.Config;
import com.electronwill.nightconfig.core.file.FileConfig;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
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
import net.minecraft.text.TextColor;

/**
 * The Mod config file.
 */
public final class ModConfigFile {

  private ModConfigFile() {
    // Do nothing
  }

  private static FileType storedFileType;

  /**
   * Saves the config.
   */
  public final static Runnable saveRunnable = () -> {

    final FileConfig config = FileConfig.builder(Paths.get(storedFileType == FileType.JSON ? "config/betterf3" +
    ".json" :
    "config/betterf3.toml")).concurrent().autosave().build();

    final Config general = Config.inMemory();
    general.set("disable_mod", GeneralOptions.disableMod);
    general.set("auto_start", GeneralOptions.autoF3);
    general.set("space_modules", GeneralOptions.spaceEveryModule);
    general.set("shadow_text", GeneralOptions.shadowText);
    general.set("animations", GeneralOptions.enableAnimations);
    general.set("animationSpeed", GeneralOptions.animationSpeed);
    general.set("fontScale", GeneralOptions.fontScale);
    general.set("background_color", GeneralOptions.backgroundColor);
    general.set("hide_debug_crosshair", GeneralOptions.hideDebugCrosshair);
    general.set("hide_sidebar", GeneralOptions.hideSidebar);
    general.set("hide_bossbar", GeneralOptions.hideBossbar);
    general.set("always_show_profiler", GeneralOptions.alwaysEnableProfiler);
    general.set("always_show_tps", GeneralOptions.alwaysEnableTPS);

    final List<Config> configsLeft = new ArrayList<>();

    for (final BaseModule module : BaseModule.modules) {

      final Config moduleConfig = saveModule(module);

      configsLeft.add(moduleConfig);

    }

    final List<Config> configsRight = new ArrayList<>();

    for (final BaseModule module : BaseModule.modulesRight) {

      final Config moduleConfig = saveModule(module);

      configsRight.add(moduleConfig);

    }

    config.set("modules_left", configsLeft);
    config.set("modules_right", configsRight);

    config.set("general", general);

    config.close();
  };

  /**
   * Loads the config.
   *
   * @param filetype the filetype (JSON or TOML)
   */
  public static void load(final FileType filetype) {

    storedFileType = filetype;

    final File file = new File(storedFileType == FileType.JSON ? "config/betterf3.json" : "config/betterf3.toml");

    if (!file.exists()) {
      return;
    }

    final FileConfig config = FileConfig.builder(file).concurrent().autosave().build();

    config.load();

    final Config allModulesConfig = config.getOrElse("modules", () -> null);

    // Support for old configs
    if (allModulesConfig != null) {

      for (final BaseModule module : BaseModule.allModules) {

        final String moduleName = module.id;

        final Config moduleConfig = allModulesConfig.getOrElse(moduleName, () -> null);

        if (moduleConfig == null) {
          continue;
        }

        final Config lines = moduleConfig.getOrElse("lines", () -> null);

        if (lines != null) {
          for (final Config.Entry e : lines.entrySet()) {
            final DebugLine line = module.line(e.getKey());

            if (line != null) {
              line.enabled = e.getValue();
            }

          }
        }

        if (module.defaultNameColor != null) {
          module.nameColor = TextColor.fromRgb(moduleConfig.getOrElse("name_color",
          module.defaultNameColor.getRgb()));
        }
        if (module.defaultValueColor != null) {
          module.valueColor = TextColor.fromRgb(moduleConfig.getOrElse("value_color",
          module.defaultValueColor.getRgb()));
        }

        if (module instanceof CoordsModule coordsModule) {

          if (coordsModule.defaultColorX != null)
            coordsModule.colorX = TextColor.fromRgb(moduleConfig.getOrElse("color_x",
            coordsModule.defaultColorX.getRgb()));
          if (coordsModule.defaultColorY != null)
            coordsModule.colorY = TextColor.fromRgb(moduleConfig.getOrElse("color_y",
            coordsModule.defaultColorY.getRgb()));
          if (coordsModule.defaultColorZ != null)
            coordsModule.colorZ = TextColor.fromRgb(moduleConfig.getOrElse("color_z",
            coordsModule.defaultColorZ.getRgb()));
        }

        if (module instanceof SoundModule soundModule) {
          if (soundModule.defaultMaximumColor != null)
            soundModule.maximumColor = TextColor.fromRgb(moduleConfig.getOrElse("maximum_color",
            soundModule.defaultMaximumColor.getRgb()));
        }

        if (module instanceof EntityModule entityModule) {
          if (entityModule.defaultTotalColor != null)
            entityModule.totalColor = TextColor.fromRgb(moduleConfig.getOrElse("total_entities_color",
            entityModule.defaultTotalColor.getRgb()));
        }

        if (module instanceof HelpModule helpModule) {
          if (helpModule.defaultEnabledColor != null)
            helpModule.enabledColor = TextColor.fromRgb(moduleConfig.getOrElse("enabled_color",
            helpModule.defaultEnabledColor.getRgb()));
          if (helpModule.defaultDisabledColor != null)
            helpModule.disabledColor = TextColor.fromRgb(moduleConfig.getOrElse("disabled_color",
            helpModule.defaultDisabledColor.getRgb()));
        }

        if (module instanceof ChunksModule chunkModule) {
          if (chunkModule.defaultEnabledColor != null)
            chunkModule.enabledColor = TextColor.fromRgb(moduleConfig.getOrElse("chunks_enabled_color",
            chunkModule.defaultEnabledColor.getRgb()));
          if (chunkModule.defaultDisabledColor != null)
            chunkModule.disabledColor = TextColor.fromRgb(moduleConfig.getOrElse("chunks_disabled_color",
            chunkModule.defaultDisabledColor.getRgb()));
          if (chunkModule.defaultTotalColor != null)
            chunkModule.totalColor = TextColor.fromRgb(moduleConfig.getOrElse("total_chunks_color",
            chunkModule.defaultTotalColor.getRgb()));
        }

        if (module instanceof SystemModule systemModule) {
          if (systemModule.memoryColorToggle == null) {
            systemModule.memoryColorToggle = moduleConfig.getOrElse("memory_color_toggle", systemModule.defaultMemoryColorToggle);
          }
          if (systemModule.timeFormat == null) {
            systemModule.timeFormat = moduleConfig.getOrElse("time_format", systemModule.defaultTimeFormat);
          }
        }

        module.enabled = moduleConfig.getOrElse("enabled", true);

      }
    } else {
      // New config
      final List<BaseModule> modulesLeft = new ArrayList<>();
      final List<BaseModule> modulesRight = new ArrayList<>();

      final List<Config> modulesLeftConfig = config.getOrElse("modules_left", () -> null);

      if (modulesLeftConfig != null) {

        for (final Config moduleConfig : modulesLeftConfig) {
          final String moduleName = moduleConfig.getOrElse("name", null);

          if (moduleName == null) {
            continue;
          }

          final BaseModule baseModule = ModConfigFile.loadModule(moduleConfig);

          modulesLeft.add(baseModule);
        }
      }

      final List<Config> modulesRightConfig = config.getOrElse("modules_right", () -> null);

      if (modulesRightConfig != null) {
        for (final Config moduleConfig : modulesRightConfig) {

          final String moduleName = moduleConfig.getOrElse("name", () -> null);

          if (moduleName == null) {
            continue;
          }

          final BaseModule baseModule = ModConfigFile.loadModule(moduleConfig);

          modulesRight.add(baseModule);
        }
      }

      if (!modulesLeft.isEmpty() || !modulesRight.isEmpty()) {
        BaseModule.modules = modulesLeft;
        BaseModule.modulesRight = modulesRight;
      }

    }

    final Config general = config.getOrElse("general", () -> null);

    if (general != null) {

      if (allModulesConfig != null) {
        final List<BaseModule> modulesLeft = new ArrayList<>();
        final List<BaseModule> modulesRight = new ArrayList<>();

        for (final Object s : general.getOrElse("modules_left_order", new ArrayList<>())) {
          final BaseModule baseModule = BaseModule.moduleById(s.toString());
          if (baseModule != null) {
            modulesLeft.add(baseModule);
          }
        }

        if (!modulesLeft.isEmpty()) {
          BaseModule.modules = modulesLeft;
        }

        for (final Object s : general.getOrElse("modules_right_order", new ArrayList<>())) {
          final BaseModule baseModule = BaseModule.moduleById(s.toString());
          if (baseModule != null) {
            modulesRight.add(baseModule);
          }
        }

        if (!modulesRight.isEmpty()) {
          BaseModule.modulesRight = modulesRight;
        }
      }

      GeneralOptions.disableMod = general.getOrElse("disable_mod", false);
      GeneralOptions.autoF3 = general.getOrElse("auto_start", false);
      GeneralOptions.spaceEveryModule = general.getOrElse("space_modules", false);
      GeneralOptions.shadowText = general.getOrElse("shadow_text", true);
      GeneralOptions.enableAnimations = general.getOrElse("animations", true);
      GeneralOptions.animationSpeed = general.getOrElse("animationSpeed", 1.0);
      GeneralOptions.fontScale = general.getOrElse("fontScale", 1.0);
      GeneralOptions.backgroundColor = general.getOrElse("background_color", 0x6F505050);
      GeneralOptions.hideDebugCrosshair = general.getOrElse("hide_debug_crosshair", false);
      GeneralOptions.hideSidebar = general.getOrElse("hide_sidebar", true);
      GeneralOptions.hideBossbar = general.getOrElse("hide_bossbar", true);
      GeneralOptions.alwaysEnableProfiler = general.getOrElse("always_show_profiler", false);
      GeneralOptions.alwaysEnableTPS = general.getOrElse("always_show_tps", false);
    }

    config.close();

  }

  private static BaseModule loadModule(final Config moduleConfig) {
    final String moduleName = moduleConfig.getOrElse("name", null);

    BaseModule baseModule;
    try {
      baseModule = BaseModule.moduleById(moduleName).getClass().getDeclaredConstructor().newInstance();
    } catch (InstantiationException | IllegalAccessException | NullPointerException | NoSuchMethodException | InvocationTargetException e) {
      baseModule = new EmptyModule(false);
    }

    final Config lines = moduleConfig.getOrElse("lines", () -> null);

    if (lines != null) {
      for (final Config.Entry e : lines.entrySet()) {
        final DebugLine line = baseModule.line(e.getKey());

        if (line != null) {
          line.enabled = e.getValue();
        }

      }
    }

    if (baseModule.defaultNameColor != null) {
      baseModule.nameColor = TextColor.fromRgb(moduleConfig.getOrElse("name_color",
      baseModule.defaultNameColor.getRgb()));
    }
    if (baseModule.defaultValueColor != null) {
      baseModule.valueColor = TextColor.fromRgb(moduleConfig.getOrElse("value_color",
      baseModule.defaultValueColor.getRgb()));
    }

    if (baseModule instanceof CoordsModule coordsModule) {

      if (coordsModule.defaultColorX != null)
        coordsModule.colorX = TextColor.fromRgb(moduleConfig.getOrElse("color_x",
        coordsModule.defaultColorX.getRgb()));
      if (coordsModule.defaultColorY != null)
        coordsModule.colorY = TextColor.fromRgb(moduleConfig.getOrElse("color_y",
        coordsModule.defaultColorY.getRgb()));
      if (coordsModule.defaultColorZ != null)
        coordsModule.colorZ = TextColor.fromRgb(moduleConfig.getOrElse("color_z",
        coordsModule.defaultColorZ.getRgb()));
    }

    if (baseModule instanceof FpsModule fpsModule) {

      if (fpsModule.defaultColorHigh != null)
        fpsModule.colorHigh = TextColor.fromRgb(moduleConfig.getOrElse("color_high",
        fpsModule.defaultColorHigh.getRgb()));
      if (fpsModule.defaultColorMed != null)
        fpsModule.colorMed = TextColor.fromRgb(moduleConfig.getOrElse("color_med",
        fpsModule.defaultColorMed.getRgb()));
      if (fpsModule.defaultColorLow != null)
        fpsModule.colorLow = TextColor.fromRgb(moduleConfig.getOrElse("color_low",
        fpsModule.defaultColorLow.getRgb()));
    }

    if (baseModule instanceof EmptyModule emptyModule) {
      emptyModule.emptyLines = moduleConfig.getOrElse("empty_lines", 1);
    }

    if (baseModule instanceof SoundModule soundModule) {
      if (soundModule.defaultMaximumColor != null)
        soundModule.maximumColor = TextColor.fromRgb(moduleConfig.getOrElse("maximum_color",
        soundModule.defaultMaximumColor.getRgb()));
    }

    if (baseModule instanceof EntityModule entityModule) {
      if (entityModule.defaultTotalColor != null)
        entityModule.totalColor = TextColor.fromRgb(moduleConfig.getOrElse("total_entities_color",
        entityModule.defaultTotalColor.getRgb()));
    }

    if (baseModule instanceof HelpModule helpModule) {
      if (helpModule.defaultEnabledColor != null)
        helpModule.enabledColor = TextColor.fromRgb(moduleConfig.getOrElse("enabled_color",
        helpModule.defaultEnabledColor.getRgb()));
      if (helpModule.defaultDisabledColor != null)
        helpModule.disabledColor = TextColor.fromRgb(moduleConfig.getOrElse("disabled_color",
        helpModule.defaultDisabledColor.getRgb()));
    }

    if (baseModule instanceof ChunksModule chunkModule) {
      if (chunkModule.defaultEnabledColor != null)
        chunkModule.enabledColor = TextColor.fromRgb(moduleConfig.getOrElse("chunks_enabled_color",
        chunkModule.defaultEnabledColor.getRgb()));
      if (chunkModule.defaultDisabledColor != null)
        chunkModule.disabledColor = TextColor.fromRgb(moduleConfig.getOrElse("chunks_disabled_color",
        chunkModule.defaultDisabledColor.getRgb()));
      if (chunkModule.defaultTotalColor != null)
        chunkModule.totalColor = TextColor.fromRgb(moduleConfig.getOrElse("total_chunks_color",
        chunkModule.defaultTotalColor.getRgb()));
    }

    if (baseModule instanceof SystemModule systemModule) {
      if (systemModule.memoryColorToggle == null) {
        systemModule.memoryColorToggle = moduleConfig.getOrElse("memory_color_toggle", systemModule.defaultMemoryColorToggle);
      }
      if (systemModule.timeFormat == null) {
        systemModule.timeFormat = moduleConfig.getOrElse("time_format", systemModule.defaultTimeFormat);
      }
    }

    baseModule.enabled = moduleConfig.getOrElse("enabled", true);
    return baseModule;
  }

  private static Config saveModule(final BaseModule module) {
    final Config moduleConfig = Config.inMemory();
    final Config lines = Config.inMemory();

    for (final DebugLine line : module.lines()) {

      final String lineId = line.id();

      lines.set(lineId, line.enabled);
    }

    moduleConfig.set("name", module.id);

    if (module.nameColor != null) {
      moduleConfig.set("name_color", module.nameColor.getRgb());
    }
    if (module.valueColor != null) {
      moduleConfig.set("value_color", module.valueColor.getRgb());
    }

    if (module instanceof CoordsModule coordsModule) {
      if (coordsModule.colorX != null) {
        moduleConfig.set("color_x", coordsModule.colorX.getRgb());
      }
      if (coordsModule.colorY != null) {
        moduleConfig.set("color_y", coordsModule.colorY.getRgb());
      }
      if (coordsModule.colorZ != null) {
        moduleConfig.set("color_z", coordsModule.colorZ.getRgb());
      }
    }

    if (module instanceof FpsModule fpsModule) {
      if (fpsModule.colorHigh != null) {
        moduleConfig.set("color_high", fpsModule.colorHigh.getRgb());
      }
      if (fpsModule.colorMed != null) {
        moduleConfig.set("color_med", fpsModule.colorMed.getRgb());
      }
      if (fpsModule.colorLow != null) {
        moduleConfig.set("color_low", fpsModule.colorLow.getRgb());
      }
    }

    if (module instanceof EmptyModule emptyModule) {
      moduleConfig.set("empty_lines", emptyModule.emptyLines);
    }

    if (module instanceof SoundModule soundModule) {
      if (soundModule.maximumColor != null)
        moduleConfig.set("maximum_color", soundModule.maximumColor.getRgb());
    }

    if (module instanceof EntityModule entityModule) {
      if (entityModule.totalColor != null)
        moduleConfig.set("total_entities_color", entityModule.totalColor.getRgb());
    }

    if (module instanceof HelpModule helpModule) {
      if (helpModule.enabledColor != null)
        moduleConfig.set("enabled_color", helpModule.enabledColor.getRgb());
      if (helpModule.disabledColor != null)
        moduleConfig.set("disabled_color", helpModule.disabledColor.getRgb());
    }

    if (module instanceof ChunksModule chunkModule) {
      if (chunkModule.enabledColor != null)
        moduleConfig.set("chunks_enabled_color", chunkModule.enabledColor.getRgb());
      if (chunkModule.disabledColor != null)
        moduleConfig.set("chunks_disabled_color", chunkModule.disabledColor.getRgb());
      if (chunkModule.totalColor != null)
        moduleConfig.set("total_chunks_color", chunkModule.totalColor.getRgb());
    }

    moduleConfig.set("enabled", module.enabled);
    moduleConfig.set("lines", lines);

    return moduleConfig;
  }

  /**
   * The enum File type.
   */
  public enum FileType {
    /**
     * Json file type.
     */
    JSON,
    /**
     * Toml file type.
     */
    TOML
  }

}
