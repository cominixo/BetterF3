package me.cominixo.betterf3.config;

import com.electronwill.nightconfig.core.Config;
import com.electronwill.nightconfig.core.file.FileConfig;
import me.cominixo.betterf3.modules.BaseModule;
import me.cominixo.betterf3.modules.CoordsModule;
import me.cominixo.betterf3.utils.DebugLine;
import net.minecraft.text.TextColor;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class ModConfigFile {

    public static Runnable saveRunnable = () -> {

            FileConfig config = FileConfig.builder(Paths.get("config/betterf3.json")).concurrent().autosave().build();

            Config allModules = Config.inMemory();

            for (BaseModule module : BaseModule.allModules) {

                String moduleName = module.id;

                Config modules = Config.inMemory();
                Config lines = Config.inMemory();


                for (DebugLine line : module.getLines()) {

                    String lineId = line.getId();

                    lines.set(lineId, line.enabled);
                }

                if (module.nameColor != null) {
                    modules.set("name_color", module.nameColor.getRgb());
                }
                if (module.valueColor != null) {
                    modules.set("value_color", module.valueColor.getRgb());
                }

                if (module instanceof CoordsModule) {
                    CoordsModule coordsModule = (CoordsModule) module;
                    if (coordsModule.colorX != null) {
                        modules.set("color_x", coordsModule.colorX.getRgb());
                    }
                    if (coordsModule.colorY != null) {
                        modules.set("color_y", coordsModule.colorY.getRgb());
                    }
                    if (coordsModule.colorZ != null) {
                        modules.set("color_z", coordsModule.colorZ.getRgb());
                    }
                }


                modules.set("enabled", module.enabled);
                modules.set("lines", lines);

                allModules.set(moduleName, modules);

            }

            config.set("modules", allModules);

            Config general = Config.inMemory();
            general.set("disable_mod", GeneralOptions.disableMod);
            general.set("space_modules", GeneralOptions.spaceEveryModule);
            general.set("shadow_text", GeneralOptions.shadowText);
            general.set("background_color", GeneralOptions.backgroundColor);

            List<String> modulesLeftString = new ArrayList<>();
            List<String> modulesRightString = new ArrayList<>();

            for (BaseModule module : BaseModule.modules) {
                modulesLeftString.add(module.id);
            }
            for (BaseModule module : BaseModule.modulesRight) {
                modulesRightString.add(module.id);
            }
            general.set("modules_left_order", modulesLeftString);
            general.set("modules_right_order", modulesRightString);

            config.set("general", general);

            config.close();
    };


    public static void load() {

        File file = new File("config/betterf3.json");

        if (!file.exists()) {
            return;
        }

        FileConfig config = FileConfig.builder(file).concurrent().autosave().build();

        config.load();

        Config allModulesConfig = config.getOrElse("modules", () -> null);
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
        }

        Config general = config.getOrElse("general", () -> null);

        if (general != null) {
            GeneralOptions.disableMod = general.getOrElse("disable_mod", false);
            GeneralOptions.spaceEveryModule = general.getOrElse("space_modules", false);
            GeneralOptions.shadowText = general.getOrElse("shadow_text", true);
            GeneralOptions.backgroundColor = general.getOrElse("background_color", 0x6F505050);

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

        config.close();

    }

}
