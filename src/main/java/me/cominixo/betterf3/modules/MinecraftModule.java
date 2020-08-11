package me.cominixo.betterf3.modules;

import me.cominixo.betterf3.utils.DebugLine;
import net.minecraft.SharedConstants;
import net.minecraft.client.ClientBrandRetriever;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.TextColor;
import net.minecraft.util.Formatting;

public class MinecraftModule extends BaseModule{

    public MinecraftModule() {
        defaultNameColor = TextColor.fromRgb(0xA0522D);
        defaultValueColor =  TextColor.fromFormatting(Formatting.DARK_GREEN);

        this.nameColor = defaultNameColor;
        this.valueColor = defaultValueColor;

        lines.add(new DebugLine("minecraft", "format.betterf3.default_no_colon", false));
        lines.get(0).inReducedDebug = true;
    }

    public void update(MinecraftClient client) {
        lines.get(0).setValue(SharedConstants.getGameVersion().getName() + " (" + client.getGameVersion() + "/" + ClientBrandRetriever.getClientModName() + ("release".equalsIgnoreCase(client.getVersionType()) ? "" : "/" + client.getVersionType()) + ")");

    }
}
