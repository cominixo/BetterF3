package me.cominixo.betterf3.utils;

import java.util.Arrays;
import java.util.Map;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.state.property.Property;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
import net.minecraft.util.Formatting;
import net.minecraft.util.Util;
import net.minecraft.util.math.Direction;
import org.apache.commons.lang3.StringUtils;

/**
 * The Utils.
 */
public final class Utils {

  private Utils() {
    // Do nothing
  }

  // Animation stuff
  /**
   * Starting x position.
   */
  public static final int START_X_POS = 200;

  /**
   * X position.
   */
  public static int xPos = START_X_POS;

  /**
   * Last animation update.
   */
  public static long lastAnimationUpdate = 0;

  /**
   * If closing animation.
   */
  public static boolean closingAnimation = false;

  /**
   * Gets fps color.
   *
   * @param currentFps the current fps
   * @return the fps color
   */
  public static FpsEnum fpsColor(final int currentFps) {
    if (currentFps >= 60) {
      return FpsEnum.HIGH;
    } else if (currentFps >= 20) {
      return FpsEnum.MEDIUM;
    } else {
      return FpsEnum.LOW;
    }
  }

  /**
   * Gets percent color.
   *
   * @param percent the percent
   * @return the percent color
   */
  public static Formatting percentColor(final int percent) {
    if (percent >= 90) {
      return Formatting.RED;
    } else if (percent >= 60) {
      return Formatting.YELLOW;
    } else {
      return Formatting.GREEN;
    }
  }

  /**
   * Gets facing string.
   *
   * @param facing the direction
   * @return the facing string
   */
  public static String facingString(final Direction facing) {

    return switch (facing) {
      case NORTH -> I18n.translate("text.betterf3.line.negative_z");
      case SOUTH -> I18n.translate("text.betterf3.line.positive_z");
      case WEST -> I18n.translate("text.betterf3.line.negative_x");
      case EAST -> I18n.translate("text.betterf3.line.positive_x");
      default -> "";
    };
  }

  /**
   * Gets styled text.
   *
   * @param string the string
   * @param color  the color
   * @return the styled text
   */
  public static MutableText styledText(Object string, final TextColor color) {
    if (string == null) {
      string = "";
    }
    return Text.literal(string.toString()).styled(style -> style.withColor(color));
  }

  /**
   * Converts Enum to string.
   *
   * @param enumToConvert the enum to convert
   * @return converted string
   */
  public static String enumToString(final Enum<?> enumToConvert) {
    return StringUtils.capitalize(enumToConvert.toString().replace("_", " "));
  }

  /**
   * Formats from string.
   *
   * @param string the string to format
   * @param nameColor the key color
   * @param valueColor the value color
   * @return the formatted string
   */
  public static Text formattedFromString(final String string, final TextColor nameColor,
                                         final TextColor valueColor) {
    if (string == null) {
      return Text.of("");
    }
    final String[] split = string.split(":");

    if (string.contains(":")) {
      final MutableText name = Utils.styledText(split[0], nameColor);
      final MutableText value = Utils.styledText(String.join(":", Arrays.asList(split).subList(1,
      split.length)), valueColor);

      return name.append(Text.of(":")).append(value);
    } else {
      return Text.of(string);
    }
  }

  /**
   * Converts property to string.
   *
   * @param propEntry the property to convert
   * @return converted string
   */
  public static String propertyToString(final Map.Entry<Property<?>, Comparable<?>> propEntry) {
    final Property<?> key = propEntry.getKey();
    final Comparable<?> value = propEntry.getValue();

    String newValue = Util.getValueAsString(key, value);

    if (Boolean.TRUE.equals(value)) {
      newValue = Formatting.GREEN + newValue;
    } else if (Boolean.FALSE.equals(value)) {
      newValue = Formatting.RED + newValue;
    }
    return key.getName() + ": " + newValue;
  }

}
