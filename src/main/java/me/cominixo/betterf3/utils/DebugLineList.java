package me.cominixo.betterf3.utils;

import net.minecraft.text.Text;
import net.minecraft.text.TextColor;

import java.util.ArrayList;
import java.util.List;

public class DebugLineList extends DebugLine {

    private List<String> values = new ArrayList<>();

    public DebugLineList(String id) {
        super(id);
    }

    public void setValues(List<String> values) {
        this.values = values;
        this.active = true;
    }

    public List<Text> toTexts(TextColor nameColor, TextColor valueColor) {

        List<Text> texts = new ArrayList<>();

        for (String v : values) {
            texts.add(Utils.getFormattedFromString(v, nameColor, valueColor));
        }

        return texts;
    }
}
