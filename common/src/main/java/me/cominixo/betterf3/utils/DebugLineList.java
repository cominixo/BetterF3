package me.cominixo.betterf3.utils;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;

/**
 * The Debug line list.
 */
public class DebugLineList extends DebugLine {

  private List<String> values = new ArrayList<>();

  /**
   * Instantiates a new Debug line list from the id.
   *
   * @param id the id
   */
  public DebugLineList(final String id) {
    super(id);
  }

  /**
   * Sets values.
   *
   * @param values the values
   */
  public void values(final List<String> values) {
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
  public List<Text> toTexts(final TextColor nameColor, final TextColor valueColor) {

    final List<Text> texts = new ArrayList<>();

    for (final String v : this.values) {
      texts.add(Utils.formattedFromString(v, nameColor, valueColor));
    }
    return texts;
  }
}
