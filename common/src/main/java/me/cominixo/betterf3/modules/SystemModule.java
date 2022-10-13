package me.cominixo.betterf3.modules;

import com.mojang.blaze3d.platform.GlDebugInfo;
import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import me.cominixo.betterf3.utils.DebugLine;
import me.cominixo.betterf3.utils.Utils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.Window;
import net.minecraft.text.TextColor;
import net.minecraft.util.Formatting;
import org.apache.commons.lang3.ArrayUtils;

/**
 * The System module.
 */
public class SystemModule extends BaseModule {

  /**
   * Default enable memory usage color.
   */
  public final boolean defaultMemoryColorToggle = true;

  /**
   * Enable memory usage color.
   */
  public Boolean memoryColorToggle;

  /**
   * Default time format.
   */
  public final String defaultTimeFormat = "HH:mm:ss";

  /**
   * Time format.
   */
  public String timeFormat;

  /**
   * Instantiates a new System module.
   */
  public SystemModule() {
    this.defaultNameColor = TextColor.fromFormatting(Formatting.GOLD);
    this.defaultValueColor = TextColor.fromFormatting(Formatting.AQUA);

    this.nameColor = defaultNameColor;
    this.valueColor = defaultValueColor;
    this.memoryColorToggle = this.defaultMemoryColorToggle;
    this.timeFormat = this.defaultTimeFormat;

    lines.add(new DebugLine("time"));
    lines.add(new DebugLine("java_version"));
    lines.add(new DebugLine("memory_usage"));
    lines.add(new DebugLine("allocation_rate"));
    lines.add(new DebugLine("allocated_memory"));
    lines.add(new DebugLine("cpu"));
    lines.add(new DebugLine("display"));
    lines.add(new DebugLine("gpu"));
    lines.add(new DebugLine("opengl_version"));
    lines.add(new DebugLine("gpu_driver"));

    for (final DebugLine line : lines) {
      line.inReducedDebug = true;
    }
  }

  /**
   * Updates the System module.
   *
   * @param client the Minecraft client
   */
  public void update(final MinecraftClient client) {
    final LocalDateTime currentTime = LocalDateTime.now();
    DateTimeFormatter timeFormatter;
    try {
      timeFormatter = DateTimeFormatter.ofPattern(this.timeFormat);
    } catch (final IllegalArgumentException e) {
      this.timeFormat = this.defaultTimeFormat;
      timeFormatter = DateTimeFormatter.ofPattern(this.timeFormat);
    }

    final String time = currentTime.format(timeFormatter);

    final long maxMemory = Runtime.getRuntime().maxMemory();
    final long totalMemory = Runtime.getRuntime().totalMemory();
    final long freeMemory = Runtime.getRuntime().freeMemory();
    final long usedMemory = totalMemory - freeMemory;

    final Window window = client.getWindow();

    final String javaVersion = String.format("%s %dbit", System.getProperty("java.version"), client.is64Bit() ? 64 : 32);
    final String memoryUsage = String.format("% 2d%% %03d/%03d MB", usedMemory * 100 / maxMemory, usedMemory / 1024 / 1024, maxMemory / 1024 / 1024);
    final String allocationRate = String.format("% 2d MB/s", this.allocationRate(usedMemory) / 1024 / 1024);
    final String allocatedMemory = String.format("% 2d%% %03dMB", totalMemory * 100 / maxMemory, totalMemory / 1024 / 1024);
    final String displayInfo = String.format("%d x %d (%s)", window.getFramebufferWidth(), window.getFramebufferHeight(), GlDebugInfo.getVendor());

    final String[] versionSplit = GlDebugInfo.getVersion().split(" ");

    final String openGlVersion = versionSplit[0];
    final String gpuDriverVersion = String.join(" ", ArrayUtils.remove(versionSplit, 0));

    lines.get(0).value(time);
    lines.get(1).value(javaVersion);
    lines.get(2).value(this.memoryColorToggle ? Utils.percentColor((int) (usedMemory * 100 / maxMemory)) + memoryUsage : memoryUsage);
    lines.get(3).value(allocationRate);
    lines.get(4).value(allocatedMemory);
    lines.get(5).value(GlDebugInfo.getCpuInfo());
    lines.get(6).value(displayInfo);
    lines.get(7).value(GlDebugInfo.getRenderer());
    lines.get(8).value(openGlVersion);
    lines.get(9).value(gpuDriverVersion);
  }

  private static final List<GarbageCollectorMXBean> GARBAGE_COLLECTORS = ManagementFactory.getGarbageCollectorMXBeans();
  private long lastCalculated = 0L;
  private long allocatedBytes = -1L;
  private long collectionCount = -1L;
  private long allocationRate = 0L;

  long allocationRate(final long allocatedBytes) {
    final long lastCalculated = System.currentTimeMillis();
    if (lastCalculated - this.lastCalculated >= 500L) {
      final long collectionCount = collectionCount();
      if (this.lastCalculated != 0L && collectionCount == this.collectionCount) {
        final double d = (double) TimeUnit.SECONDS.toMillis(1L) / (double) (lastCalculated - this.lastCalculated);
        final long n = allocatedBytes - this.allocatedBytes;
        this.allocationRate = Math.round((double) n * d);
      }

      this.lastCalculated = lastCalculated;
      this.allocatedBytes = allocatedBytes;
      this.collectionCount = collectionCount;
    }
    return this.allocationRate;
  }

  private static long collectionCount() {
    long l = 0L;

    GarbageCollectorMXBean garbageCollectorMXBean;
    for (Iterator<GarbageCollectorMXBean> var2 = GARBAGE_COLLECTORS.iterator(); var2.hasNext(); l += garbageCollectorMXBean.getCollectionCount()) {
      garbageCollectorMXBean = var2.next();
    }

    return l;
  }
}
