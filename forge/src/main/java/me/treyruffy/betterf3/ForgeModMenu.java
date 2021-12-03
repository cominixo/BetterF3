package me.treyruffy.betterf3;

import me.cominixo.betterf3.config.gui.ModConfigScreen;
import net.minecraftforge.client.ConfigGuiHandler;
import net.minecraftforge.fml.ModLoadingContext;

/**
 * Used to create mod configuration in the Forge mod menu.
 */
public final class ForgeModMenu {

    private ForgeModMenu() {
        // Do nothing
    }

    /**
     * Registers BetterF3 in the mod menu.
     */
    public static void registerModsPage() {
        ModLoadingContext.get().registerExtensionPoint(ConfigGuiHandler.ConfigGuiFactory.class,
                () -> new ConfigGuiHandler.ConfigGuiFactory((client, parent) -> new ModConfigScreen(parent)));
    }
}
