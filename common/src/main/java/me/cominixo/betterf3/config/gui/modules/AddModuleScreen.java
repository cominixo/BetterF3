package me.cominixo.betterf3.config.gui.modules;

import java.lang.reflect.InvocationTargetException;
import me.cominixo.betterf3.config.ModConfigFile;
import me.cominixo.betterf3.modules.BaseModule;
import me.cominixo.betterf3.modules.EmptyModule;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import me.shedaniel.clothconfig2.gui.entries.DropdownBoxEntry;
import me.shedaniel.clothconfig2.impl.builders.DropdownMenuBuilder;
import net.minecraft.text.Text;

/**
 * The Add Module screen.
 */
public final class AddModuleScreen {

  private AddModuleScreen() {
    // Not called
  }

  /**
   * Gets the config builder.
   *
   * @param parent The parent screen
   * @return The ConfigBuilder for the add module screen
   */

  public static ConfigBuilder configBuilder(final ModulesScreen parent) {

    final ConfigBuilder builder = ConfigBuilder.create()
      .setParentScreen(parent);

    builder.setSavingRunnable(ModConfigFile.saveRunnable);

    final ConfigEntryBuilder entryBuilder = builder.entryBuilder();

    final ConfigCategory general = builder.getOrCreateCategory(Text.translatable("config.betterf3" +
      ".category.general"));

    final DropdownBoxEntry<BaseModule> dropdownEntry = entryBuilder.startDropdownMenu(Text.translatable(
      "config.betterf3.add_button.module_name"),
        DropdownMenuBuilder.TopCellElementBuilder.of(new EmptyModule(true),
          BaseModule::module,
          object -> Text.translatable(object.toString()))).setSelections(BaseModule.allModules)
        .setSaveConsumer((BaseModule newValue) -> {
          try {
            parent.modulesListWidget.addModule(newValue.getClass().getDeclaredConstructor().newInstance());
          } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            parent.modulesListWidget.addModule(newValue);
          }
        })
    .build();

    general.addEntry(dropdownEntry);
    builder.transparentBackground();

    return builder;
  }
}
