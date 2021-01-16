package me.cominixo.betterf3.utils;

import net.minecraft.client.resource.language.I18n;
import net.minecraft.state.property.Property;
import net.minecraft.text.LiteralText;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
import net.minecraft.util.Formatting;
import net.minecraft.util.Util;
import net.minecraft.util.math.Direction;
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

    public static Formatting getPercentColor(int percent) {

        if (percent >= 90) {
            return Formatting.RED;
        } else if (percent >= 60) {
            return Formatting.YELLOW;
        } else {
            return Formatting.GREEN;
        }

    }

    public static String getFacingString(Direction facing) {

        switch(facing) {
            case NORTH:
                return I18n.translate("text.betterf3.line.negative_z");
            case SOUTH:
                return I18n.translate("text.betterf3.line.positive_z");
            case WEST:
                return I18n.translate("text.betterf3.line.negative_x");
            case EAST:
                return I18n.translate("text.betterf3.line.positive_x");
            default:
                return "";
        }
    }

    public static MutableText getStyledText(Object string, TextColor color) {
        if (string == null) {
            string = "";
        }
        return new LiteralText(string.toString()).styled((style) -> style.withColor(color));
    }

    public static String enumToString(Enum<?> e) {
        return WordUtils.capitalizeFully(e.toString().replace("_", " "));
    }

    public static Text getFormattedFromString(String string, TextColor nameColor, TextColor valueColor) {
        String[] split = string.split(":");

        if (string.contains(":")) {

            MutableText name = Utils.getStyledText(split[0], nameColor);
            MutableText value = Utils.getStyledText(String.join(":", Arrays.asList(split).subList(1, split.length)), valueColor);

            return name.append(new LiteralText(":")).append(value);
        } else {
            return new LiteralText(string);
        }
    }

    public static String propertyToString(Map.Entry<Property<?>, Comparable<?>> propEntry) {

        Property<?> key = propEntry.getKey();
        Comparable<?> value = propEntry.getValue();

        String newValue = Util.getValueAsString(key, value);

        if (Boolean.TRUE.equals(value)) {
            newValue = Formatting.GREEN + newValue;
        } else if (Boolean.FALSE.equals(value)) {
            newValue = Formatting.RED + newValue;
        }

        return key.getName() + ": " + newValue;
    }


}
