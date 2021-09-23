package me.cominixo.betterf3.config.gui.modules;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import java.util.ArrayList;
import java.util.List;
import me.cominixo.betterf3.modules.BaseModule;
import me.cominixo.betterf3.modules.CoordsModule;
import me.cominixo.betterf3.modules.FpsModule;
import me.cominixo.betterf3.utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;

/**
 * The Module list widget.
 */
public class ModuleListWidget extends ObjectSelectionList<ModuleListWidget.ModuleEntry> {

    /**
     * The parent screen.
     */
    ModulesScreen parentScreen;
    /**
     * The Module entries.
     */
    List<ModuleEntry> moduleEntries = new ArrayList<>();

    /**
     * Instantiates a new Module list widget.
     *
     * @param parent      the parent screen
     * @param client      the Minecraft client
     * @param width       the width
     * @param height      the height
     * @param top         the top
     * @param bottom      the bottom
     * @param entryHeight the entry height
     */
    public ModuleListWidget(final ModulesScreen parent, final Minecraft client, final int width, final int height,
                            final int top, final int bottom, final int entryHeight) {
        super(client, width, height, top, bottom, entryHeight);
        this.parentScreen = parent;
    }

    /**
     * Gets scrollbar position x.
     *
     * @return the scrollbar position x
     */
    protected int scrollbarPositionX() {
        return super.getScrollbarPosition() + 30;
    }

    /**
     * Gets the row width.
     *
     * @return the row width
     */
    public int rowWidth() {
        return super.getRowWidth() + 85;
    }

    /**
     * The entry.
     *
     * @param index the index
     *
     * @return ModuleEntry
     */
    public ModuleEntry entry(final int index) {
        return this.moduleEntries.get(index);
    }

    /**
     * Sets modules.
     *
     * @param modules the modules
     */
    public void modules(final List<BaseModule> modules) {
        this.moduleEntries.clear();
        this.clearEntries();

        for (final BaseModule module : modules) {
            this.addModule(module);
        }
    }

    /**
     * Updates the modules.
     */
    public void updateModules() {
        this.clearEntries();
        this.moduleEntries.forEach(this::addEntry);

    }

    /**
     * Add a module.
     *
     * @param module the module
     */
    public void addModule(final BaseModule module) {
        final ModuleEntry entry = new ModuleEntry(this.parentScreen, module);
        this.moduleEntries.add(entry);
        this.addEntry(entry);
    }

    /**
     * Remove a module.
     *
     * @param index the index of the module
     */
    public void removeModule(final int index) {
        final ModuleEntry entry = this.moduleEntries.get(index);
        this.moduleEntries.remove(entry);
        this.removeEntry(entry);
        //BaseModule.modules.remove(index);
    }

    /**
     * A module entry.
     */
    public class ModuleEntry extends Entry<ModuleEntry> {
        private final ModulesScreen parent;
        private final Minecraft client;
        /**
         * The Module.
         */
        public final BaseModule module;

        /**
         * Instantiates a new Module entry.
         *
         * @param parent the parent screen
         * @param module the module
         */
        protected ModuleEntry(final ModulesScreen parent, final BaseModule module) {
            this.parent = parent;
            this.module = module;
            this.client = Minecraft.getInstance();
        }

        // Fixes 1.17 crash
        @Override
        public Component getNarration() {
            return new TextComponent(this.module.toString());
        }

        /**
         * Renders the module list widget.
         *
         */
        public void render(final PoseStack matrices, final int index, final int y, final int x, final int entryWidth, final int entryHeight,
                           final int mouseX, final int mouseY, final boolean hovered, final float tickDelta) {

            this.client.font.draw(matrices, this.module.toString(), (float) (x + 32 + 3), (float) (y + 1), 0xffffff);

            final Component exampleText;

            if (this.module instanceof CoordsModule coordsModule) {
                exampleText = Utils.styledText("X", coordsModule.colorX).append(Utils.styledText("Y", coordsModule.colorY)).append(Utils.styledText("Z", coordsModule.colorZ)).append(Utils.styledText(": ", coordsModule.nameColor))
                                .append(Utils.styledText("100 ", coordsModule.colorX).append(Utils.styledText("200 ", coordsModule.colorY)).append(Utils.styledText("300", coordsModule.colorZ)));

            } else if (this.module instanceof FpsModule fpsModule) {
                exampleText = Utils.styledText("60 fps  ", fpsModule.colorHigh).append(Utils.styledText("40 fps  ", fpsModule.colorMed)).append(Utils.styledText("10 fps", fpsModule.colorLow));
            } else if (this.module.nameColor != null && this.module.valueColor != null) {
                exampleText = Utils.styledText("Name: ", this.module.nameColor).append(Utils.styledText("Value", this.module.valueColor));
            } else {
                exampleText = new TextComponent("");
            }

            this.client.font.draw(matrices, exampleText, (float) (x + 40 + 3), (float) (y + 13), 0xffffff);

            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            this.client.getTextureManager().bindForSetup(GuiComponent.GUI_ICONS_LOCATION);

            if (this.client.options.touchscreen || hovered) {
                RenderSystem.setShaderTexture(0, new ResourceLocation("textures/gui/server_selection.png"));
                GuiComponent.fill(matrices, x, y, x + 32, y + 32, -1601138544);
                RenderSystem.setShader(GameRenderer::getPositionTexShader);
                RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
                final int v = mouseX - x;
                final int w = mouseY - y;

                if (index > 0) {
                    if (v < 16 && w < 16) {
                        GuiComponent.blit(matrices, x, y, 96.0F, 32.0F, 32, 32, 256, 256);
                    } else {
                        GuiComponent.blit(matrices, x, y, 96.0F, 0.0F, 32, 32, 256, 256);
                    }
                }

                if (index < ModuleListWidget.this.moduleEntries.size() - 1) {
                    if (v < 16 && w > 16) {
                        GuiComponent.blit(matrices, x, y, 64.0F, 32.0F, 32, 32, 256, 256);
                    } else {
                        GuiComponent.blit(matrices, x, y, 64.0F, 0.0F, 32, 32, 256, 256);
                    }
                }
            }
        }

        /**
         * Gets mouse clicked.
         */

        public boolean mouseClicked(final double mouseX, final double mouseY, final int button) {
            final double d = mouseX - (double) this.parent.modulesListWidget.getRowLeft();
            final double e =
                    mouseY - (double) ModuleListWidget.this.getRowTop(ModuleListWidget.this.children().indexOf(this));
            if (d <= 32.0D) {
                final int i = this.parent.modulesListWidget.children().indexOf(this);
                if (d < 16.0D && e < 16.0D && i > 0) {
                    this.swapEntries(i, i - 1);
                    return true;
                }

                if (d < 16.0D && e > 16.0D && i < ModuleListWidget.this.moduleEntries.size() - 1) {
                    this.swapEntries(i, i + 1);
                    return true;
                }
            }

            this.parent.select(this);
            return false;
        }

        private void swapEntries(final int i, final int j) {

            final ModuleEntry temp = ModuleListWidget.this.moduleEntries.get(i);

            ModuleListWidget.this.moduleEntries.set(i, ModuleListWidget.this.moduleEntries.get(j));
            ModuleListWidget.this.moduleEntries.set(j, temp);

            /* this.screen.modulesListWidget.setModules(moduleEntries);
            ModuleEntry entry = this.screen.modulesListWidget.children().get(j); */
            this.parent.modulesListWidget.setSelected(temp);
            this.parent.updateButtons();
            this.parent.modulesListWidget.updateModules();

        }

    }

}
