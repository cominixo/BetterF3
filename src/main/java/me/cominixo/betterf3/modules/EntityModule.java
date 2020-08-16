package me.cominixo.betterf3.modules;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import me.cominixo.betterf3.utils.DebugLine;
import me.cominixo.betterf3.utils.Utils;
import me.cominixo.betterf3.mixin.chunk.WorldRendererAccessor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.server.integrated.IntegratedServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
import net.minecraft.util.Formatting;
import net.minecraft.world.SpawnHelper;

import java.util.Arrays;
import java.util.List;

public class EntityModule extends BaseModule {

    public final TextColor totalColor = TextColor.fromFormatting(Formatting.GOLD);

    public EntityModule() {



        this.defaultNameColor = TextColor.fromFormatting(Formatting.RED);
        this.defaultValueColor = TextColor.fromFormatting(Formatting.YELLOW);

        this.nameColor = defaultNameColor;
        this.valueColor = defaultValueColor;



        lines.add(new DebugLine("particles"));
        lines.add(new DebugLine("entities", "format.betterf3.total", true));

        // Monster, Creature, Ambient, Water Creature, Water Ambient, Misc
        for (SpawnGroup spawnGroup : SpawnGroup.values()) {
            String name = spawnGroup.toString().toLowerCase();
            lines.add(new DebugLine(name));
        }


        lines.get(0).inReducedDebug = true;
        lines.get(1).inReducedDebug = true;


    }
    public void update(MinecraftClient client) {

        WorldRendererAccessor worldRendererMixin = (WorldRendererAccessor) client.worldRenderer;

        List<Text> entityValues = Arrays.asList(Utils.getStyledText(I18n.translate("text.betterf3.line.rendered"), valueColor), Utils.getStyledText(I18n.translate("text.betterf3.line.total"), totalColor),
                Utils.getStyledText(worldRendererMixin.getRegularEntityCount(), valueColor),
                Utils.getStyledText(worldRendererMixin.getWorld().getRegularEntityCount(), totalColor));

        IntegratedServer integratedServer = client.getServer();

        if (client.world != null) {
            ServerWorld serverWorld = integratedServer != null ? integratedServer.getWorld(client.world.getRegistryKey()) : null;
            if (serverWorld != null) {
                SpawnHelper.Info info = serverWorld.getChunkManager().getSpawnInfo();
                if (info != null) {
                    Object2IntMap<SpawnGroup> spawnGroupCount = info.getGroupToCount();
                    // Entities (separated) (kinda bad)
                    for (int i = 0; i < SpawnGroup.values().length; i++) {
                        SpawnGroup group = SpawnGroup.values()[i];
                        lines.get(i+2).setValue(spawnGroupCount.getInt(group));
                    }
                }
            }
        }

        // Particles
        lines.get(0).setValue(client.particleManager.getDebugString());
        // Entities
        lines.get(1).setValue(entityValues);



    }

}
