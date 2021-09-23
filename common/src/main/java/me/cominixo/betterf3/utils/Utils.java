package me.cominixo.betterf3.utils;

import java.util.Arrays;
import java.util.Map;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextColor;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.level.block.state.properties.Property;
import org.apache.commons.lang3.text.WordUtils;

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
    public static ChatFormatting percentColor(final int percent) {
        if (percent >= 90) {
            return ChatFormatting.RED;
        } else if (percent >= 60) {
            return ChatFormatting.YELLOW;
        } else {
            return ChatFormatting.GREEN;
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
            case NORTH -> I18n.get("text.betterf3.line.negative_z");
            case SOUTH -> I18n.get("text.betterf3.line.positive_z");
            case WEST -> I18n.get("text.betterf3.line.negative_x");
            case EAST -> I18n.get("text.betterf3.line.positive_x");
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
    public static MutableComponent styledText(Object string, final TextColor color) {
        if (string == null) {
            string = "";
        }
        return new TextComponent(string.toString()).withStyle(style -> style.withColor(color));
    }

    /**
     * Converts Enum to string.
     *
     * @param enumToConvert the enum to convert
     * @return converted string
     */
    public static String enumToString(final Enum<?> enumToConvert) {
        return WordUtils.capitalizeFully(enumToConvert.toString().replace("_", " "));
    }

    /**
     * Formats from string.
     *
     * @param string the string to format
     * @param nameColor the key color
     * @param valueColor the value color
     * @return the formatted string
     */
    public static Component formattedFromString(final String string, final TextColor nameColor,
                                                final TextColor valueColor) {
        final String[] split = string.split(":");

        if (string.contains(":")) {
            final MutableComponent name = Utils.styledText(split[0], nameColor);
            final MutableComponent value = Utils.styledText(String.join(":", Arrays.asList(split).subList(1,
            split.length)), valueColor);

            return name.append(new TextComponent(":")).append(value);
        } else {
            return new TextComponent(string);
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

        String newValue = Util.getPropertyName(key, value);

        if (Boolean.TRUE.equals(value)) {
            newValue = ChatFormatting.GREEN + newValue;
        } else if (Boolean.FALSE.equals(value)) {
            newValue = ChatFormatting.RED + newValue;
        }
        return key.getName() + ": " + newValue;
    }

}
