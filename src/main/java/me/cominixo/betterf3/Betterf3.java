package me.cominixo.betterf3;

import me.cominixo.betterf3.config.ModConfigFile;
import me.cominixo.betterf3.modules.*;
import me.cominixo.betterf3.utils.PositionEnum;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.Environment;
import net.fabricmc.api.EnvType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


@Environment(EnvType.CLIENT)
public class Betterf3 implements ClientModInitializer {

    public final Logger logger = LogManager.getLogger("betterf3");

    @Override
    public void onInitializeClient() {

        this.logger.info("[BetterF3] Loading...");


        // Init all modules and add spaces (default order)

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
        BaseModule.modules.add(EmptyModule.INSTANCE);
        new MiscLeftModule().init();

        new SystemModule().init(PositionEnum.RIGHT);
        new MiscRightModule().init(PositionEnum.RIGHT);
        BaseModule.modulesRight.add(EmptyModule.INSTANCE);
        new TargetModule().init(PositionEnum.RIGHT);

        // Config
        ModConfigFile.load();

        this.logger.info("[BetterF3] All done!");


    }
}
