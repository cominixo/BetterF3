package me.cominixo.betterf3.config.gui.modules;

import me.cominixo.betterf3.config.ModConfigFile;
import me.cominixo.betterf3.modules.BaseModule;
import me.cominixo.betterf3.utils.PositionEnum;
import net.minecraft.client.gui.screen.*;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.TranslatableText;

import java.util.Objects;

public class ModulesScreen extends Screen {

    Screen parent;
    ModuleListWidget modulesListWidget;
    private boolean initialized = false;

    private ButtonWidget editButton, deleteButton;

    public PositionEnum side;

    public ModulesScreen(Screen parent, PositionEnum side) {
        super(new TranslatableText("config.betterf3.title.modules"));
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
                this.modulesListWidget.setModules(BaseModule.modules);
            } else if (this.side == PositionEnum.RIGHT) {
                this.modulesListWidget.setModules(BaseModule.modulesRight);
            }
        }

        this.addDrawableChild(this.modulesListWidget);

        this.editButton = this.addDrawableChild(new ButtonWidget(this.width / 2 - 50, this.height - 50, 100, 20, new TranslatableText("config.betterf3.modules.edit_button"), (buttonWidget) -> {
            Screen screen = (EditModulesScreen.getConfigBuilder(Objects.requireNonNull(this.modulesListWidget.getSelected()).module).build());
            client.openScreen(screen);
        }));

        this.addDrawableChild(new ButtonWidget(this.width / 2 + 4 + 50, this.height - 50, 100, 20, new TranslatableText("config.betterf3.modules.add_button"), (buttonWidget) -> client.openScreen(AddModuleScreen.getConfigBuilder(this).build())));

        this.deleteButton = this.addDrawableChild(new ButtonWidget(this.width / 2 - 154, this.height - 50, 100, 20, new TranslatableText("config.betterf3.modules.delete_button"), (buttonWidget) -> this.modulesListWidget.removeModule(this.modulesListWidget.moduleEntries.indexOf(Objects.requireNonNull(this.modulesListWidget.getSelected())))));

        this.addDrawableChild(new ButtonWidget(this.width / 2 - 154, this.height - 30 + 4, 300 + 8, 20, new TranslatableText("config.betterf3.modules.done_button"), (buttonWidget) -> {
            this.onClose();
            client.openScreen(parent);
        }));

        updateButtons();



    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.modulesListWidget.render(matrices, mouseX, mouseY, delta);
        this.drawCenteredText(matrices, this.textRenderer, this.title, this.width / 2, 20, 0xFFFFFF);
        super.render(matrices, mouseX, mouseY, delta);

    }

    @Override
    public void onClose() {
        if (this.side == PositionEnum.LEFT) {
            BaseModule.modules.clear();
            for (ModuleListWidget.ModuleEntry entry : this.modulesListWidget.moduleEntries) {
                BaseModule.modules.add(entry.module);
            }
        } else if (this.side == PositionEnum.RIGHT) {
            BaseModule.modulesRight.clear();
            for (ModuleListWidget.ModuleEntry entry : this.modulesListWidget.moduleEntries) {
                BaseModule.modulesRight.add(entry.module);
            }
        }
        this.client.openScreen(parent);
        ModConfigFile.saveRunnable.run();
    }

    public void select(ModuleListWidget.ModuleEntry entry) {
        this.modulesListWidget.setSelected(entry);
        updateButtons();
    }

    public void updateButtons() {
        if (this.modulesListWidget.getSelected() != null) {
            editButton.active = true;
            deleteButton.active = true;
        } else {
            editButton.active = false;
            deleteButton.active = false;
        }
    }

}
