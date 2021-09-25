package me.cominixo.betterf3.modules;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import me.cominixo.betterf3.utils.DebugLine;
import me.cominixo.betterf3.utils.DebugLineList;
import me.cominixo.betterf3.utils.PositionEnum;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;

/**
 * The Base module.
 */
public abstract class BaseModule {

    /**
     * The color of the tag.
     */
    public TextColor nameColor;

    /**
     * The color of the value of the tag.
     */
    public TextColor valueColor;

    /**
     * The default color of the tag.
     */
    public TextColor defaultNameColor;
    /**
     * The default color of the value of the tag.
     */
    public TextColor defaultValueColor;

    /**
     * Is module enabled.
     */
    public boolean enabled = true;

    /**
     * The module's lines.
     */
    protected final List<DebugLine> lines = new ArrayList<>();

    /**
     * The left modules.
     */
    public static List<BaseModule> modules = new ArrayList<>();

    /**
     * The right modules.
     */
    public static List<BaseModule> modulesRight = new ArrayList<>();

    /**
     * The modules both left and right.
     */
    public final static List<BaseModule> allModules = new ArrayList<>();

    /**
     * Module id.
     */
    public final String id = this.getClass().getSimpleName().replace("Module", "").toLowerCase();

    /**
     * Instantiates a new module.
     */
    public BaseModule() {
        // Do nothing
    }

    /**
     * Instantiates a new module.
     *
     * @param invisible sets invisibility
     */
    public BaseModule(final boolean invisible) {
        if (!invisible) {
            allModules.add(this);
        }
    }

    /**
     * Initializes the module.
     *
     * @param positionEnum the position
     */
    public void init(final PositionEnum positionEnum) {
        switch (positionEnum) {
            case RIGHT -> modulesRight.add(this);
            case LEFT -> modules.add(this);
            case BOTH -> {
                modulesRight.add(this);
                modules.add(this);
            }
        }
        allModules.add(this);

    }

    /**
     * Initializes the module on the left.
     */
    public void init() {
        modules.add(this);
        allModules.add(this);
    }

    /**
     * Gets lines.
     *
     * @return the lines
     */
    public List<DebugLine> lines() {
        return this.lines;
    }

    /**
     * Gets formatted lines.
     *
     * @param reducedDebug has reduced debug on
     * @return the lines formatted
     */
    public List<Text> linesFormatted(final boolean reducedDebug) {
        final List<Text> linesString = new ArrayList<>();

        for (final DebugLine line : this.lines) {
            if (reducedDebug && !line.inReducedDebug) {
                continue;
            }
            if (!line.active || !line.enabled) {
                continue;
            }
            if (line instanceof DebugLineList lineList) {
                linesString.addAll(lineList.toTexts(this.nameColor, this.valueColor));
                continue;
            }

            if (!line.isCustom) {
                linesString.add(line.toText(this.nameColor, this.valueColor));
            } else {
                linesString.add(line.toTextCustom(this.nameColor));
            }
        }
        return linesString;
    }

    /**
     * Gets line at id.
     *
     * @param id the id
     * @return the line
     */
    public DebugLine line(final String id) {
        final Optional<DebugLine> lineOptional =
                this.lines.stream().filter(line -> line.id().equals(id)).findFirst();
        return lineOptional.orElse(null);
    }

    /**
     * Gets module by string.
     *
     * @param string the string
     * @return the module
     */
    public static BaseModule module(final String string) {
        return BaseModule.allModules.stream().filter(baseModule -> baseModule.toString().equals(string)).findFirst().orElse(null);
    }

    /**
     * Gets module by id.
     *
     * @param id the id
     * @return the module
     */
    public static BaseModule moduleById(final String id) {
        return BaseModule.allModules.stream().filter(baseModule -> baseModule.id.equals(id)).findFirst().orElse(null);
    }

    /**
     * Gets localized module name.
     *
     * @return localized module name
     */
    public String toString() {
        return I18n.translate("text.betterf3.module." + this.id);
    }

    /**
     * Sets enabled.
     *
     * @param enabled enabled
     */
    public void enabled(final boolean enabled) {
        this.enabled = enabled;
    }

    /**
     * Updates the module.
     *
     * @param client the Minecraft client
     */
    public abstract void update(MinecraftClient client);
}
