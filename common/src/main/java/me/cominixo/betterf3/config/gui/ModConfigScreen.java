package me.cominixo.betterf3.config.gui;

import me.cominixo.betterf3.config.gui.modules.ModulesScreen;
import me.cominixo.betterf3.utils.PositionEnum;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

/**
 * The Mod Config screen.
 */
public class ModConfigScreen extends Screen {
  private final Screen parent;

  /**
   * Instantiates a new Mod Config screen.
   *
   * @param parent the parent screen
   */
  public ModConfigScreen(final Screen parent) {
    super(Text.translatable("config.betterf3.title.config"));
    this.parent = parent;
  }

  @Override
  public void init() {
    final MinecraftClient client = MinecraftClient.getInstance();

    final ButtonWidget leftButton = ButtonWidget.builder(Text.translatable("config.betterf3.order_left_button"),
        button -> client.setScreen(new ModulesScreen(client.currentScreen, PositionEnum.LEFT)))
      .dimensions(this.width / 2 - 130, this.height / 4, 120, 20).build();
    this.addDrawableChild(leftButton);

    final ButtonWidget rightButton = ButtonWidget.builder(Text.translatable("config.betterf3.order_right_button"),
        button -> client.setScreen(new ModulesScreen(client.currentScreen, PositionEnum.RIGHT)))
      .dimensions(this.width / 2 + 10, this.height / 4, 120, 20).build();
    this.addDrawableChild(rightButton);

    final ButtonWidget configButton = ButtonWidget.builder(Text.translatable("config.betterf3.general_settings"),
        button -> client.setScreen(GeneralOptionsScreen.configBuilder(client.currentScreen).build()))
      .dimensions(this.width / 2 - 130, this.height / 4 - 24, 260, 20).build();
    this.addDrawableChild(configButton);

    final ButtonWidget doneButton = ButtonWidget.builder(Text.translatable("config.betterf3.modules.done_button"),
        button -> client.setScreen(this.parent))
      .dimensions(this.width / 2 - 130, this.height - 50, 260, 20).build();
    this.addDrawableChild(doneButton);
  }

  @Override
  public void render(final MatrixStack matrices, final int mouseX, final int mouseY, final float delta) {
    this.renderBackground(matrices);
    drawCenteredText(matrices, this.textRenderer, this.title, this.width / 2, 20, 16777215);
    super.render(matrices, mouseX, mouseY, delta);
  }
}
