package me.cominixo.betterf3.config.gui.modules;

import me.cominixo.betterf3.config.ModConfigFile;
import me.cominixo.betterf3.modules.BaseModule;
import me.cominixo.betterf3.modules.EmptyModule;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import me.shedaniel.clothconfig2.gui.entries.DropdownBoxEntry;
import me.shedaniel.clothconfig2.impl.builders.DropdownMenuBuilder;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;

/**
 * The Add Module screen.
 */
public class AddModuleScreen {


    /**
     * Gets the config builder.
     *
     * @param parent The parent screen
     * @return The ConfigBuilder for the add module screen
     */
    public static ConfigBuilder getConfigBuilder(ModulesScreen parent) {

        ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(parent);

        builder.setSavingRunnable(ModConfigFile.saveRunnable);


        ConfigEntryBuilder entryBuilder = builder.entryBuilder();

        ConfigCategory general = builder.getOrCreateCategory(new TranslatableComponent("config.betterf3.category.general"));



        DropdownBoxEntry<BaseModule> dropdownEntry = entryBuilder.startDropdownMenu(new TranslatableComponent("config.betterf3.add_button.module_name"),
                        DropdownMenuBuilder.TopCellElementBuilder.of(new EmptyModule(true),
                                BaseModule::getModule,
                                (object) -> new TextComponent(object.toString()))).setSelections(BaseModule.allModules)
                .setSaveConsumer((BaseModule newValue) -> {
                    try {
                        parent.modulesListWidget.addModule(newValue.getClass().newInstance());
                    } catch (InstantiationException | IllegalAccessException e) {
                        parent.modulesListWidget.addModule(newValue);
                    }
                })
                .build();

        general.addEntry(dropdownEntry);
        builder.transparentBackground();

        builder.transparentBackground();

        return builder;
    }
}
