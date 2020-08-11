package me.cominixo.betterf3.utils;

import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Language;

import java.util.ArrayList;
import java.util.List;

public class DebugLine {

    private Object value;
    private String format;
    private final String id;

    public boolean active = true;
    public boolean enabled = true;
    public boolean isCustom = false;
    public boolean inReducedDebug = false;

    public DebugLine(String id) {
        this.id = id;
        this.format = "format.betterf3.default_format";
        this.value = "";
    }


    public DebugLine(String id, String formatString, boolean isCustom) {

        this.id = id;
        this.value = "";
        this.format = formatString;
        this.isCustom = isCustom;

    }

    public Text toText(TextColor nameColor, TextColor valueColor) {

        String name = this.getName();

        Text nameStyled = Utils.getStyledText(name, nameColor);
        Text valueStyled;

        if (this.value instanceof Text) {
            valueStyled = (Text) this.value;
        } else {
            valueStyled = Utils.getStyledText(this.value, valueColor);
        }

        if (this.value.toString().equals("")) {
            this.active = false;
        }


        return new TranslatableText(format, nameStyled, valueStyled);
    }


    public Text toTextCustom(TextColor nameColor) {

        String name = this.getName();

        if (value instanceof List) {
            // format properly if value is a List (bad)
            List<Object> values = new ArrayList<>();
            List<?> value = (List<?>) this.value;


            if (!name.equals("")) {
                values.add(Utils.getStyledText(name, nameColor));
            }

            values.addAll(value);
            return new TranslatableText(format, values.toArray()).styled((style) -> style.withColor(nameColor));
        } else {
            return new TranslatableText(format, name, value);
        }

    }


    public void setValue(Object value) {
        this.active = true;
        this.value = value;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getName() {
        if (id.isEmpty()) {
            this.format = "%s%s";
            return "";
        }
        Language language = Language.getInstance();
        return language.get("text.betterf3.line." + id);
    }

    public String getId() {
        return id;
    }


}
