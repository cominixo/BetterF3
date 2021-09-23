package me.cominixo.betterf3.utils;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextColor;

import java.util.ArrayList;
import java.util.List;

/**
 * The Debug line list.
 */
public class DebugLineList extends DebugLine {

    private List<String> values = new ArrayList<>();

    /**
     * Instantiates a new Debug line list from id.
     *
     * @param id the id
     */
    public DebugLineList(String id) {
        super(id);
    }

    /**
     * Sets values.
     *
     * @param values the values
     */
    public void setValues(List<String> values) {
        this.values = values;
        this.active = true;
    }

    /**
     * To texts list.
     *
     * @param nameColor  the key color
     * @param valueColor the value color
     * @return the list
     */
    public List<Component> toTexts(TextColor nameColor, TextColor valueColor) {

        List<Component> texts = new ArrayList<>();

        for (String v : values) {
            texts.add(Utils.getFormattedFromString(v, nameColor, valueColor));
        }
        return texts;
    }
}
