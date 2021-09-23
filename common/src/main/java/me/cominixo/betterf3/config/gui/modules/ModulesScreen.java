package me.cominixo.betterf3.config.gui.modules;

import com.mojang.blaze3d.vertex.PoseStack;
import java.util.Objects;
import me.cominixo.betterf3.config.ModConfigFile;
import me.cominixo.betterf3.modules.BaseModule;
import me.cominixo.betterf3.utils.PositionEnum;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.TranslatableComponent;

/**
 * The Modules screen.
 */
public class ModulesScreen extends Screen {

    /**
     * The parent screen.
     */
    Screen parent;
    /**
     * The Modules list widget.
     */
    ModuleListWidget modulesListWidget;
    private boolean initialized = false;

    private Button editButton;
    private Button deleteButton;

    /**
     * The side of the screen (left or right).
     */
    public PositionEnum side;

    /**
     * Instantiates a new Modules screen.
     *
     * @param parent the parent screen
     * @param side   the side of the screen
     */
    public ModulesScreen(final Screen parent, final PositionEnum side) {
        super(new TranslatableComponent("config.betterf3.title.modules"));
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
            this.modulesListWidget = new ModuleListWidget(this, this.minecraft, this.width, this.height, 32, this.height - 64, 36);
            if (this.side == PositionEnum.LEFT) {
                this.modulesListWidget.modules(BaseModule.modules);
            } else if (this.side == PositionEnum.RIGHT) {
                this.modulesListWidget.modules(BaseModule.modulesRight);
            }
        }

        this.addRenderableWidget(this.modulesListWidget);

        this.editButton = this.addRenderableWidget(new Button(this.width / 2 - 50, this.height - 50, 100, 20, new TranslatableComponent("config.betterf3.modules.edit_button"), buttonWidget -> {
            final Screen screen =
                    EditModulesScreen.configBuilder(Objects.requireNonNull(this.modulesListWidget.getSelected()).module, this).build();
            assert minecraft != null;
            minecraft.setScreen(screen);
        }));

        this.addRenderableWidget(new Button(this.width / 2 + 4 + 50, this.height - 50, 100, 20, new TranslatableComponent("config.betterf3.modules.add_button"), buttonWidget -> {
            assert minecraft != null;
            minecraft.setScreen(AddModuleScreen.configBuilder(this).build());
        }));

        this.deleteButton = this.addRenderableWidget(new Button(this.width / 2 - 154, this.height - 50, 100, 20,
                new TranslatableComponent("config.betterf3.modules.delete_button"), buttonWidget -> this.modulesListWidget.removeModule(this.modulesListWidget.moduleEntries.indexOf(Objects.requireNonNull(this.modulesListWidget.getSelected())))));

        this.addRenderableWidget(new Button(this.width / 2 - 154, this.height - 30 + 4, 300 + 8, 20,
                new TranslatableComponent("config.betterf3.modules.done_button"), buttonWidget -> {
            this.onClose();
            assert minecraft != null;
            minecraft.setScreen(this.parent);
        }));

        this.updateButtons();

    }

    @Override
    public void render(final PoseStack matrices, final int mouseX, final int mouseY, final float delta) {
        this.modulesListWidget.render(matrices, mouseX, mouseY, delta);
        drawCenteredString(matrices, this.font, this.title, this.width / 2, 20, 0xFFFFFF);
        super.render(matrices, mouseX, mouseY, delta);

    }

    @Override
    public void onClose() {
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
        assert this.minecraft != null;
        this.minecraft.setScreen(this.parent);
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
        if (this.modulesListWidget.getSelected() != null) {
            this.editButton.active = true;
            this.deleteButton.active = true;
        } else {
            this.editButton.active = false;
            this.deleteButton.active = false;
        }
    }

}
