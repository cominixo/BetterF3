package me.cominixo.betterf3;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.cominixo.betterf3.config.gui.ModConfigScreen;

/**
 * Sets up Mod Menu.
 */
public class ModMenuIntegration implements ModMenuApi {

  // The method that sets up our Mod Menu entry
  @Override
  public ConfigScreenFactory<?> getModConfigScreenFactory() {
    return ModConfigScreen::new;
  }
}
