package me.cominixo.betterf3.modules;

import com.mojang.blaze3d.platform.GlUtil;
import com.mojang.blaze3d.platform.Window;
import me.cominixo.betterf3.utils.DebugLine;
import me.cominixo.betterf3.utils.Utils;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.TextColor;
import org.apache.commons.lang3.ArrayUtils;

public class SystemModule extends BaseModule{

    public SystemModule() {

        this.defaultNameColor = TextColor.fromLegacyFormat(ChatFormatting.GOLD);
        this.defaultValueColor = TextColor.fromLegacyFormat(ChatFormatting.AQUA);

        this.nameColor = defaultNameColor;
        this.valueColor = defaultValueColor;

        lines.add(new DebugLine("java_version"));
        lines.add(new DebugLine("memory_usage"));
        lines.add(new DebugLine("allocated_memory"));
        lines.add(new DebugLine("cpu"));
        lines.add(new DebugLine("display"));
        lines.add(new DebugLine("gpu"));
        lines.add(new DebugLine("opengl_version"));
        lines.add(new DebugLine("gpu_driver"));

        for (DebugLine line : lines) {
            line.inReducedDebug = true;
        }

    }

    public void update(Minecraft client) {

        long maxMemory = Runtime.getRuntime().maxMemory();
        long totalMemory = Runtime.getRuntime().totalMemory();
        long freeMemory = Runtime.getRuntime().freeMemory();
        long usedMemory = totalMemory - freeMemory;

        Window window = client.getWindow();


        String javaVersion = String.format("%s %dbit", System.getProperty("java.version"), client.is64Bit() ? 64 : 32);
        String memoryUsage = String.format("% 2d%% %03d/%03d MB", usedMemory * 100 / maxMemory, usedMemory / 1024 / 1024, maxMemory / 1024 / 1024);
        String allocatedMemory = String.format("% 2d%% %03dMB", totalMemory * 100 / maxMemory, totalMemory / 1024 / 1024);
        String displayInfo = String.format("%d x %d (%s)", window.getWidth(), window.getHeight(), GlUtil.getVendor());

        String[] versionSplit = GlUtil.getOpenGLVersion().split(" ");

        String openGlVersion = versionSplit[0];
        String gpuDriverVersion = String.join(" ", ArrayUtils.remove(versionSplit, 0));

        lines.get(0).setValue(javaVersion);
        lines.get(1).setValue(Utils.getPercentColor((int) (usedMemory * 100 / maxMemory)) + memoryUsage);
        lines.get(2).setValue(allocatedMemory);
        lines.get(3).setValue(GlUtil.getCpuInfo());
        lines.get(4).setValue(displayInfo);
        lines.get(5).setValue(GlUtil.getRenderer());
        lines.get(6).setValue(openGlVersion);
        lines.get(7).setValue(gpuDriverVersion);

    }
}
