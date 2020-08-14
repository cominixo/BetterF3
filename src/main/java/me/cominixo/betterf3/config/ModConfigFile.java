package me.cominixo.betterf3.config;

import com.electronwill.nightconfig.core.Config;
import com.electronwill.nightconfig.core.file.FileConfig;
import me.cominixo.betterf3.modules.BaseModule;
import me.cominixo.betterf3.modules.CoordsModule;
import me.cominixo.betterf3.modules.EmptyModule;
import me.cominixo.betterf3.modules.FpsModule;
import me.cominixo.betterf3.utils.DebugLine;
import net.minecraft.text.TextColor;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class ModConfigFile {

    public static Runnable saveRunnable = () -> {

        FileConfig config = FileConfig.builder(Paths.get("config/betterf3.json")).concurrent().autosave().build();

        Config general = Config.inMemory();
        general.set("disable_mod", GeneralOptions.disableMod);
        general.set("space_modules", GeneralOptions.spaceEveryModule);
        general.set("shadow_text", GeneralOptions.shadowText);
        general.set("animations", GeneralOptions.enableAnimations);
        general.set("background_color", GeneralOptions.backgroundColor);

        List<Config> configsLeft = new ArrayList<>();

            for (BaseModule module : BaseModule.modules) {

                Config moduleConfig = saveModule(module);

                configsLeft.add(moduleConfig);

            }


            List<Config> configsRight = new ArrayList<>();

            for (BaseModule module : BaseModule.modulesRight) {

                Config moduleConfig = saveModule(module);

                configsRight.add(moduleConfig);

            }

        config.set("modules_left", configsLeft);
        config.set("modules_right", configsRight);

        config.set("general", general);

        config.close();
    };


    public static void load(){

        File file = new File("config/betterf3.json");

        if (!file.exists()) {
            return;
        }

        FileConfig config = FileConfig.builder(file).concurrent().autosave().build();

        config.load();

        Config allModulesConfig = config.getOrElse("modules", () -> null);

        // Support for old configs
        if (allModulesConfig != null) {

            for (BaseModule module : BaseModule.allModules) {

                String moduleName = module.id;

                Config moduleConfig = allModulesConfig.getOrElse(moduleName, () -> null);


                if (moduleConfig == null) {
                    continue;
                }

                Config lines = moduleConfig.getOrElse("lines", () -> null);

                if (lines != null) {
                    for (Config.Entry e : lines.entrySet()) {
                        DebugLine line = module.getLine(e.getKey());

                        if (line != null) {
                            line.enabled = e.getValue();
                        }

                    }
                }

                if (module.defaultNameColor != null) {
                    module.nameColor = TextColor.fromRgb(moduleConfig.getOrElse("name_color", module.defaultNameColor.getRgb()));
                }
                if (module.defaultValueColor != null) {
                    module.valueColor = TextColor.fromRgb(moduleConfig.getOrElse("value_color", module.defaultValueColor.getRgb()));
                }

                if (module instanceof CoordsModule) {

                    CoordsModule coordsModule = (CoordsModule) module;

                    coordsModule.colorX = TextColor.fromRgb(moduleConfig.getOrElse("color_x", coordsModule.defaultColorX.getRgb()));
                    coordsModule.colorY = TextColor.fromRgb(moduleConfig.getOrElse("color_y", coordsModule.defaultColorY.getRgb()));
                    coordsModule.colorZ = TextColor.fromRgb(moduleConfig.getOrElse("color_z", coordsModule.defaultColorZ.getRgb()));
                }

                module.enabled = moduleConfig.getOrElse("enabled", true);

            }
        } else {
            // New config
            List<BaseModule> modulesLeft = new ArrayList<>();
            List<BaseModule> modulesRight = new ArrayList<>();


            List<Config> modulesLeftConfig = config.getOrElse("modules_left", () -> null);

            if (modulesLeftConfig != null) {

                for (Config moduleConfig : modulesLeftConfig) {
                    String moduleName = moduleConfig.getOrElse("name", null);

                    if (moduleName == null) {
                        continue;
                    }

                    BaseModule baseModule = ModConfigFile.loadModule(moduleConfig);

                    modulesLeft.add(baseModule);
                }
            }

            if (!modulesLeft.isEmpty()) {
                BaseModule.modules = modulesLeft;
            }

            List<Config> modulesRightConfig = config.getOrElse("modules_right", () -> null);

            if (modulesRightConfig != null) {
                for (Config moduleConfig : modulesRightConfig) {

                    String moduleName = moduleConfig.getOrElse("name", () -> null);

                    if (moduleName == null) {
                        continue;
                    }

                    BaseModule baseModule = ModConfigFile.loadModule(moduleConfig);

                    modulesRight.add(baseModule);
                }
            }

            if (!modulesRight.isEmpty()) {
                BaseModule.modulesRight = modulesRight;
            }

        }

        Config general = config.getOrElse("general", () -> null);

        if (general != null) {

            if (allModulesConfig != null) {
                List<BaseModule> modulesLeft = new ArrayList<>();
                List<BaseModule> modulesRight = new ArrayList<>();

                for (Object s : general.getOrElse("modules_left_order", new ArrayList<>())) {
                    BaseModule baseModule = BaseModule.getModuleById(s.toString());
                    if (baseModule != null) {
                        modulesLeft.add(baseModule);
                    }
                }

                if (!modulesLeft.isEmpty()) {
                    BaseModule.modules = modulesLeft;
                }

                for (Object s : general.getOrElse("modules_right_order", new ArrayList<>())) {
                    BaseModule baseModule = BaseModule.getModuleById(s.toString());
                    if (baseModule != null) {
                        modulesRight.add(baseModule);
                    }
                }

                if (!modulesRight.isEmpty()) {
                    BaseModule.modulesRight = modulesRight;
                }
            }

            GeneralOptions.disableMod = general.getOrElse("disable_mod", false);
            GeneralOptions.spaceEveryModule = general.getOrElse("space_modules", false);
            GeneralOptions.shadowText = general.getOrElse("shadow_text", true);
            GeneralOptions.enableAnimations = general.getOrElse("animations", true);
            GeneralOptions.backgroundColor = general.getOrElse("background_color", 0x6F505050);
        }

        config.close();

    }

    private static BaseModule loadModule(Config moduleConfig) {
        String moduleName = moduleConfig.getOrElse("name", null);

        BaseModule baseModule;
        try {
            baseModule = BaseModule.getModuleById(moduleName).getClass().newInstance();
        } catch (InstantiationException | IllegalAccessException | NullPointerException e) {
            baseModule = EmptyModule.INSTANCE;
        }

        Config lines = moduleConfig.getOrElse("lines", () -> null);

        if (lines != null) {
            for (Config.Entry e : lines.entrySet()) {
                DebugLine line = baseModule.getLine(e.getKey());

                if (line != null) {
                    line.enabled = e.getValue();
                }

            }
        }

        if (baseModule.defaultNameColor != null) {
            baseModule.nameColor = TextColor.fromRgb(moduleConfig.getOrElse("name_color", baseModule.defaultNameColor.getRgb()));
        }
        if (baseModule.defaultValueColor != null) {
            baseModule.valueColor = TextColor.fromRgb(moduleConfig.getOrElse("value_color", baseModule.defaultValueColor.getRgb()));
        }

        if (baseModule instanceof CoordsModule) {

            CoordsModule coordsModule = (CoordsModule) baseModule;

            coordsModule.colorX = TextColor.fromRgb(moduleConfig.getOrElse("color_x", coordsModule.defaultColorX.getRgb()));
            coordsModule.colorY = TextColor.fromRgb(moduleConfig.getOrElse("color_y", coordsModule.defaultColorY.getRgb()));
            coordsModule.colorZ = TextColor.fromRgb(moduleConfig.getOrElse("color_z", coordsModule.defaultColorZ.getRgb()));
        }

        if (baseModule instanceof FpsModule) {

            FpsModule fpsModule = (FpsModule) baseModule;

            fpsModule.colorHigh = TextColor.fromRgb(moduleConfig.getOrElse("color_high", fpsModule.defaultColorHigh.getRgb()));
            fpsModule.colorMed = TextColor.fromRgb(moduleConfig.getOrElse("color_med", fpsModule.defaultColorMed.getRgb()));
            fpsModule.colorLow = TextColor.fromRgb(moduleConfig.getOrElse("color_low", fpsModule.defaultColorLow.getRgb()));
        }

        baseModule.enabled = moduleConfig.getOrElse("enabled", true);
        return baseModule;
    }

    private static Config saveModule(BaseModule module) {
        Config moduleConfig = Config.inMemory();
        Config lines = Config.inMemory();


        for (DebugLine line : module.getLines()) {

            String lineId = line.getId();

            lines.set(lineId, line.enabled);
        }

        moduleConfig.set("name", module.id);

        if (module.nameColor != null) {
            moduleConfig.set("name_color", module.nameColor.getRgb());
        }
        if (module.valueColor != null) {
            moduleConfig.set("value_color", module.valueColor.getRgb());
        }

        if (module instanceof CoordsModule) {
            CoordsModule coordsModule = (CoordsModule) module;
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

        if (module instanceof FpsModule) {
            FpsModule fpsModule = (FpsModule) module;
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

        moduleConfig.set("enabled", module.enabled);
        moduleConfig.set("lines", lines);

        return moduleConfig;
    }

}
