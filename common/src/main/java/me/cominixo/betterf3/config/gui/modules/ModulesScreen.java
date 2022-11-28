package me.cominixo.betterf3.config.gui.modules;

import java.util.Objects;
import me.cominixo.betterf3.config.ModConfigFile;
import me.cominixo.betterf3.modules.BaseModule;
import me.cominixo.betterf3.utils.PositionEnum;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

/**
 * The Modules screen.
 */
public class ModulesScreen extends Screen {

  /**
   * The parent screen.
   */
  final Screen parent;
  /**
   * The Modules list widget.
   */
  ModuleListWidget modulesListWidget;
  private boolean initialized = false;

  private ButtonWidget editButton;
  private ButtonWidget deleteButton;

  /**
   * The side of the screen (left or right).
   */
  public final PositionEnum side;

  /**
   * Instantiates a new Modules screen.
   *
   * @param parent the parent screen
   * @param side   the side of the screen
   */
  public ModulesScreen(final Screen parent, final PositionEnum side) {
    super(Text.translatable("config.betterf3.title.modules"));
    this.parent = parent;
    this.side = side;

  }

  @Override
  protected void init() {
    super.init();

    if (this.initialized) {
      this.modulesListWidget.updateSize(this.width, this.height, 32, this.height - 64);
    } else {
      this.initialized = true;
      this.modulesListWidget = new ModuleListWidget(this, this.client, this.width, this.height, 32, this.height - 64, 36);
      if (this.side == PositionEnum.LEFT) {
        this.modulesListWidget.modules(BaseModule.modules);
      } else if (this.side == PositionEnum.RIGHT) {
        this.modulesListWidget.modules(BaseModule.modulesRight);
      }
    }

    this.addDrawableChild(this.modulesListWidget);

    final ButtonWidget editButton = ButtonWidget.builder(Text.translatable("config.betterf3.modules.edit_button"),
        button -> {
          final Screen screen = EditModulesScreen.configBuilder(Objects.requireNonNull(this.modulesListWidget.getSelectedOrNull()).module, this).build();
          assert client != null;
          client.setScreen(screen);
        })
      .dimensions(this.width / 2 - 50, this.height - 50, 100, 20).build();
    this.editButton = this.addDrawableChild(editButton);

    final ButtonWidget addButton = ButtonWidget.builder(Text.translatable("config.betterf3.modules.add_button"),
        button -> {
          assert client != null;
          client.setScreen(AddModuleScreen.configBuilder(this).build());
        })
      .dimensions(this.width / 2 + 4 + 50, this.height - 50, 100, 20).build();
    this.addDrawableChild(addButton);

    final ButtonWidget deleteButton = ButtonWidget.builder(Text.translatable("config.betterf3.modules.delete_button"),
        button -> this.modulesListWidget.removeModule(this.modulesListWidget.moduleEntries.indexOf(Objects.requireNonNull(this.modulesListWidget.getSelectedOrNull()))))
      .dimensions(this.width / 2 - 154, this.height - 50, 100, 20).build();
    this.deleteButton = this.addDrawableChild(deleteButton);

    final ButtonWidget doneButton = ButtonWidget.builder(Text.translatable("config.betterf3.modules.done_button"),
        button -> {
          this.close();
          assert client != null;
          client.setScreen(this.parent);
        })
      .dimensions(this.width / 2 - 154, this.height - 30 + 4, 308, 20).build();
    this.addDrawableChild(doneButton);

    this.updateButtons();

  }

  @Override
  public void render(final MatrixStack matrices, final int mouseX, final int mouseY, final float delta) {
    this.modulesListWidget.render(matrices, mouseX, mouseY, delta);
    drawCenteredText(matrices, this.textRenderer, this.title, this.width / 2, 20, 0xFFFFFF);
    super.render(matrices, mouseX, mouseY, delta);

  }

  @Override
  public void close() {
    if (this.side == PositionEnum.LEFT) {
      BaseModule.modules.clear();
      for (final ModuleListWidget.ModuleEntry entry : this.modulesListWidget.moduleEntries) {
        BaseModule.modules.add(entry.module);
      }
    } else if (this.side == PositionEnum.RIGHT) {
      BaseModule.modulesRight.clear();
      for (final ModuleListWidget.ModuleEntry entry : this.modulesListWidget.moduleEntries) {
        BaseModule.modulesRight.add(entry.module);
      }
    }
    assert this.client != null;
    this.client.setScreen(this.parent);
    ModConfigFile.saveRunnable.run();
  }

  /**
   * Selects a module.
   *
   * @param entry the entry
   */
  public void select(final ModuleListWidget.ModuleEntry entry) {
    this.modulesListWidget.setSelected(entry);
    this.updateButtons();
  }

  /**
   * Updates the buttons.
   */
  public void updateButtons() {
    if (this.modulesListWidget.getSelectedOrNull() != null) {
      this.editButton.active = true;
      this.deleteButton.active = true;
    } else {
      this.editButton.active = false;
      this.deleteButton.active = false;
    }
  }

}
