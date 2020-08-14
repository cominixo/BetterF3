package me.cominixo.betterf3.modules;

import me.cominixo.betterf3.utils.DebugLine;
import me.cominixo.betterf3.utils.DebugLineList;
import me.cominixo.betterf3.utils.PositionEnum;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class BaseModule {


    public TextColor nameColor;
    public TextColor valueColor;

    public TextColor defaultNameColor;
    public TextColor defaultValueColor;

    public boolean enabled = true;

    protected List<DebugLine> lines = new ArrayList<>();
    public static List<BaseModule> modules = new ArrayList<>();
    public static List<BaseModule> modulesRight = new ArrayList<>();
    public static List<BaseModule> allModules = new ArrayList<>();

    public String id = this.getClass().getSimpleName().replace("Module", "").toLowerCase();


    public BaseModule() {

    }

    public BaseModule(boolean invisible) {
        if (!invisible) {
            allModules.add(this);
        }
    }

    public void init(PositionEnum positionEnum) {
        switch (positionEnum) {
            case RIGHT:
                modulesRight.add(this);
                break;
            case LEFT:
                modules.add(this);
                break;
            case BOTH:
                modulesRight.add(this);
                modules.add(this);
                break;
        }
        allModules.add(this);

    }
    public void init() {
        modules.add(this);
        allModules.add(this);
    }


    public List<DebugLine> getLines() {
        return lines;
    }


    public List<Text> getLinesFormatted(boolean reducedDebug) {

        List<Text> linesString = new ArrayList<>();


        for (DebugLine line : lines) {

            if (reducedDebug && !line.inReducedDebug) {
                continue;
            }
            if (!line.active || !line.enabled) {
                continue;
            }

            if (line instanceof DebugLineList) {
                DebugLineList lineList = (DebugLineList) line;
                linesString.addAll(lineList.toTexts(nameColor, valueColor));
                continue;
            }


            if (!line.isCustom) {
                linesString.add(line.toText(nameColor, valueColor));
            } else {
                linesString.add(line.toTextCustom(nameColor));
            }

        }

        return linesString;

    }

    public DebugLine getLine(String id) {

        Optional<DebugLine> lineOptional = lines.stream().filter(line -> line.getId().equals(id)).findFirst();

        return lineOptional.orElse(null);

    }

    public static BaseModule getModule(String string) {
        return BaseModule.allModules.stream().filter(baseModule -> baseModule.toString().equals(string)).findFirst().orElse(null);
    }

    public static BaseModule getModuleById(String id) {
        return BaseModule.allModules.stream().filter(baseModule -> baseModule.id.equals(id)).findFirst().orElse(null);
    }

    public String toString() {
        return I18n.translate("text.betterf3.module." + id);
    }


    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public abstract void update(MinecraftClient client);

}
