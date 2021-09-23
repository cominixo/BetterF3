package me.cominixo.betterf3.utils;

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

import java.util.Arrays;
import java.util.Map;

public class Utils {


    // Animation stuff
    public static final int START_X_POS = 200;
    public static int xPos = START_X_POS;
    public static long lastAnimationUpdate = 0;
    public static boolean closingAnimation = false;

    public static int getFpsColor(int currentFps) {

        if (currentFps >= 60) {
            return 0;
        } else if (currentFps >= 20) {
            return 1;
        } else {
            return 2;
        }
    }

    public static ChatFormatting getPercentColor(int percent) {

        if (percent >= 90) {
            return ChatFormatting.RED;
        } else if (percent >= 60) {
            return ChatFormatting.YELLOW;
        } else {
            return ChatFormatting.GREEN;
        }

    }

    public static String getFacingString(Direction facing) {

        return switch (facing) {
            case NORTH -> I18n.get("text.betterf3.line.negative_z");
            case SOUTH -> I18n.get("text.betterf3.line.positive_z");
            case WEST -> I18n.get("text.betterf3.line.negative_x");
            case EAST -> I18n.get("text.betterf3.line.positive_x");
            default -> "";
        };
    }

    public static MutableComponent getStyledText(Object string, TextColor color) {
        if (string == null) {
            string = "";
        }
        return new TextComponent(string.toString()).withStyle((style) -> style.withColor(color));
    }

    public static String enumToString(Enum<?> e) {
        return WordUtils.capitalizeFully(e.toString().replace("_", " "));
    }

    public static Component getFormattedFromString(String string, TextColor nameColor, TextColor valueColor) {
        String[] split = string.split(":");

        if (string.contains(":")) {

            MutableComponent name = Utils.getStyledText(split[0], nameColor);
            MutableComponent value = Utils.getStyledText(String.join(":", Arrays.asList(split).subList(1, split.length)), valueColor);

            return name.append(new TextComponent(":")).append(value);
        } else {
            return new TextComponent(string);
        }
    }

    public static String propertyToString(Map.Entry<Property<?>, Comparable<?>> propEntry) {

        Property<?> key = propEntry.getKey();
        Comparable<?> value = propEntry.getValue();

        String newValue = Util.getPropertyName(key, value);

        if (Boolean.TRUE.equals(value)) {
            newValue = ChatFormatting.GREEN + newValue;
        } else if (Boolean.FALSE.equals(value)) {
            newValue = ChatFormatting.RED + newValue;
        }

        return key.getName() + ": " + newValue;
    }


}
