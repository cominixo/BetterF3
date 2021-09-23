package me.cominixo.betterf3.utils;

import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextColor;
import net.minecraft.network.chat.TranslatableComponent;

import java.util.ArrayList;
import java.util.List;

/**
 * A Debug line.
 */
public class DebugLine {

    private Object value;
    private String format;
    private final String id;

    public boolean active = true;
    public boolean enabled = true;
    public boolean isCustom = false;
    /**
     * Is in reduced debug
     */
    public boolean inReducedDebug = false;

    /**
     * Instantiates a new Debug line.
     *
     * @param id the id
     */
    public DebugLine(String id) {
        this.id = id;
        this.format = "format.betterf3.default_format";
        this.value = "";
    }

    /**
     * Instantiates a new Debug line.
     *
     * @param id           the id
     * @param formatString the format string
     * @param isCustom     custom
     */
    public DebugLine(String id, String formatString, boolean isCustom) {
        this.id = id;
        this.value = "";
        this.format = formatString;
        this.isCustom = isCustom;
    }

    public Component toText(TextColor nameColor, TextColor valueColor) {
        String name = this.getName();

        Component nameStyled = Utils.getStyledText(name, nameColor);
        Component valueStyled;

        if (this.value instanceof Component) {
            valueStyled = (Component) this.value;
        } else {
            valueStyled = Utils.getStyledText(this.value, valueColor);
        }
        if (this.value.toString().equals("")) {
            this.active = false;
        }
        return new TranslatableComponent(format, nameStyled, valueStyled);
    }

    public Component toTextCustom(TextColor nameColor) {
        String name = this.getName();

        if (value instanceof List) {
            // format properly if value is a List (bad)
            List<Object> values = new ArrayList<>();
            List<?> value = (List<?>) this.value;

            if (!name.equals("")) {
                values.add(Utils.getStyledText(name, nameColor));
            }
            values.addAll(value);
            return new TranslatableComponent(format, values.toArray()).withStyle((style) -> style.withColor(nameColor));
        } else {
            return new TranslatableComponent(format, name, value);
        }
    }


    /**
     * Sets value.
     *
     * @param value the value
     */
    public void setValue(Object value) {
        this.active = true;
        this.value = value;
    }

    /**
     * Sets format.
     *
     * @param format the format
     */
    public void setFormat(String format) {
        this.format = format;
    }

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        if (id.isEmpty()) {
            this.format = "%s%s";
            return "";
        }
        Language language = Language.getInstance();
        return language.getOrDefault("text.betterf3.line." + id);
    }

    /**
     * Gets the id.
     *
     * @return the id
     */
    public String getId() {
        return id;
    }

}
