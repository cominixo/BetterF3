package me.cominixo.betterf3.config.gui;

import me.cominixo.betterf3.config.gui.modules.ModulesScreen;
import me.cominixo.betterf3.utils.PositionEnum;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.TranslatableText;


public class ModConfigScreen extends Screen {
    private final Screen parent;

    public ModConfigScreen(Screen parent) {
        super(new TranslatableText("config.betterf3.title.config"));
        this.parent = parent;
    }
    @Override
    public void init() {

        MinecraftClient client = MinecraftClient.getInstance();


        this.addDrawableChild(new ButtonWidget(this.width / 2 - 130, this.height/4, 120, 20, new TranslatableText("config.bettef3.order_left_button"), (buttonWidget) -> client.openScreen(new ModulesScreen(client.currentScreen, PositionEnum.LEFT))));
        this.addDrawableChild(new ButtonWidget(this.width / 2 + 10, this.height/4, 120, 20, new TranslatableText("config.bettef3.order_right_button"), (buttonWidget) -> client.openScreen(new ModulesScreen(client.currentScreen, PositionEnum.RIGHT))));
        this.addDrawableChild(new ButtonWidget(this.width / 2 - 130, this.height/4 - 24, 260, 20, new TranslatableText("config.bettef3.general_settings"), (buttonWidget) -> client.openScreen(GeneralOptionsScreen.getConfigBuilder().build())));

        this.addDrawableChild(new ButtonWidget(this.width / 2 - 130, this.height - 50, 260, 20, new TranslatableText("config.betterf3.modules.done_button"), (buttonWidget) -> client.openScreen(parent)));


    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);
        this.drawCenteredText(matrices, this.textRenderer, this.title, this.width / 2, 20, 16777215);
        super.render(matrices, mouseX, mouseY, delta);

    }


}
