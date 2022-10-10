package me.cominixo.betterf3;

import me.cominixo.betterf3.config.ModConfigFile;
import me.cominixo.betterf3.modules.BaseModule;
import me.cominixo.betterf3.modules.ChunksModule;
import me.cominixo.betterf3.modules.CoordsModule;
import me.cominixo.betterf3.modules.EmptyModule;
import me.cominixo.betterf3.modules.EntityModule;
import me.cominixo.betterf3.modules.FpsModule;
import me.cominixo.betterf3.modules.GraphicsModule;
import me.cominixo.betterf3.modules.HelpModule;
import me.cominixo.betterf3.modules.LocationModule;
import me.cominixo.betterf3.modules.MinecraftModule;
import me.cominixo.betterf3.modules.MiscLeftModule;
import me.cominixo.betterf3.modules.MiscRightModule;
import me.cominixo.betterf3.modules.ServerModule;
import me.cominixo.betterf3.modules.SoundModule;
import me.cominixo.betterf3.modules.SystemModule;
import me.cominixo.betterf3.modules.TargetModule;
import me.cominixo.betterf3.utils.PositionEnum;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * BetterF3 Fabric Mod.
 */
@Environment(EnvType.CLIENT)
public class BetterF3Fabric implements ClientModInitializer {

  /**
   * The Log4J logger.
   */
  public final Logger logger = LogManager.getLogger("betterf3");

  @Override
  public void onInitializeClient() {
    this.logger.info("[BetterF3] Loading...");

    // Initializes all modules and add spaces (default order)
    new MinecraftModule().init();
    new FpsModule().init();
    new GraphicsModule().init();
    new ServerModule().init();
    new CoordsModule().init();
    new ChunksModule().init();
    new LocationModule().init();
    new EntityModule().init();
    new SoundModule().init();
    new HelpModule().init();
    BaseModule.modules.add(new EmptyModule(false));
    new MiscLeftModule().init();

    new SystemModule().init(PositionEnum.RIGHT);
    new MiscRightModule().init(PositionEnum.RIGHT);
    BaseModule.modulesRight.add(new EmptyModule(false));
    new TargetModule().init(PositionEnum.RIGHT);

    // Setup config with JSON file type
    ModConfigFile.load(ModConfigFile.FileType.JSON);

    this.logger.info("[BetterF3] All done!");
  }
}
