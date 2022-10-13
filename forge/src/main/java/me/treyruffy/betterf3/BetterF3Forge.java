package me.treyruffy.betterf3;

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
import me.cominixo.betterf3.utils.Utils;
import net.minecraft.client.resource.language.I18n;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.IExtensionPoint;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.network.NetworkConstants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * BetterF3 Forge Mod.
 */
// The value here should match an entry in the META-INF/mods.toml file
@Mod("betterf3")
public class BetterF3Forge {

  // Directly references a log4j logger.
  private static final Logger LOGGER = LogManager.getLogger();

  /**
   * Instantiates a new Better F3 mod for Forge.
   */
  public BetterF3Forge() {
    LOGGER.info("[BetterF3] Starting...");

    if (FMLEnvironment.dist == Dist.DEDICATED_SERVER) {
      LOGGER.warn("[BetterF3] Not supported on dedicated server!");
    } else {
      DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> ClientSetup::setup);
    }
  }

  private static class ClientSetup {
    private static void setup() {
      setupModules();

      // Register ourselves for server and other game events we are interested in
      MinecraftForge.EVENT_BUS.register(BetterF3Forge.class);
      // Make sure the mod being absent on the other network side does not cause the client to display the server
      // as incompatible
      ModLoadingContext.get().registerExtensionPoint(IExtensionPoint.DisplayTest.class, () -> new IExtensionPoint.DisplayTest(() -> NetworkConstants.IGNORESERVERONLY, (a, b) -> true));

      // Sets up Cloth Config if it is installed
      if (ModList.get().isLoaded("cloth_config"))
        ForgeModMenu.registerModsPage();
      else LOGGER.info(I18n.translate("config.betterf3.need_cloth_config"));
    }

    private static void setupModules() {
      LOGGER.info("[BetterF3] Loading...");

      Utils.modVersion(modVersion());

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

      // Setup config with TOML file type
      ModConfigFile.load(ModConfigFile.FileType.TOML);

      LOGGER.info("[BetterF3] All done!");
    }

    private static String modVersion() {
      return ModList.get().getModContainerById("betterf3").orElseThrow(NullPointerException::new).getModInfo().getVersion().toString();
    }
  }
}
