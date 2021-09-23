package me.cominixo.betterf3.config.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import me.cominixo.betterf3.config.gui.modules.ModulesScreen;
import me.cominixo.betterf3.utils.PositionEnum;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.TranslatableComponent;

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
    public ModConfigScreen(Screen parent) {
        super(new TranslatableComponent("config.betterf3.title.config"));
        this.parent = parent;
    }
    @Override
    public void init() {

        Minecraft client = Minecraft.getInstance();


        this.addRenderableWidget(new Button(this.width / 2 - 130, this.height/4, 120, 20, new TranslatableComponent("config.betterf3.order_left_button"), (buttonWidget) -> client.setScreen(new ModulesScreen(client.screen, PositionEnum.LEFT))));
        this.addRenderableWidget(new Button(this.width / 2 + 10, this.height/4, 120, 20, new TranslatableComponent("config.betterf3.order_right_button"), (buttonWidget) -> client.setScreen(new ModulesScreen(client.screen, PositionEnum.RIGHT))));
        this.addRenderableWidget(new Button(this.width / 2 - 130, this.height/4 - 24, 260, 20, new TranslatableComponent("config.betterf3.general_settings"), (buttonWidget) -> client.setScreen(GeneralOptionsScreen.getConfigBuilder().build())));

        this.addRenderableWidget(new Button(this.width / 2 - 130, this.height - 50, 260, 20, new TranslatableComponent("config.betterf3.modules.done_button"), (buttonWidget) -> client.setScreen(parent)));


    }

    @Override
    public void render(PoseStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);
        drawCenteredString(matrices, this.font, this.title, this.width / 2, 20, 16777215);
        super.render(matrices, mouseX, mouseY, delta);

    }


}
