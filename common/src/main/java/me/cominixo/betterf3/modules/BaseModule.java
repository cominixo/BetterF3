package me.cominixo.betterf3.modules;

import me.cominixo.betterf3.utils.DebugLine;
import me.cominixo.betterf3.utils.DebugLineList;
import me.cominixo.betterf3.utils.PositionEnum;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextColor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * The Base module.
 */
public abstract class BaseModule {

	/**
	 * The key color.
	 */
	public TextColor nameColor;
	/**
	 * The value color.
	 */
	public TextColor valueColor;

	/**
	 * The default key color.
	 */
	public TextColor defaultNameColor;
	/**
	 * The default value color.
	 */
	public TextColor defaultValueColor;

	public boolean enabled = true;

	protected List<DebugLine> lines = new ArrayList<>();
	/**
	 * The left modules
	 */
	public static List<BaseModule> modules = new ArrayList<>();
	/**
	 * The right modules
	 */
	public static List<BaseModule> modulesRight = new ArrayList<>();
	/**
	 * The modules both left and right
	 */
	public static List<BaseModule> allModules = new ArrayList<>();

	public String id = this.getClass().getSimpleName().replace("Module", "").toLowerCase();


	/**
	 * Instantiates a new module.
	 */
	public BaseModule() {}

	/**
	 * Instantiates a new module.
	 *
	 * @param invisible sets invisibility
	 */
	public BaseModule(boolean invisible) {
		if (!invisible) {
			allModules.add(this);
		}
	}

	/**
	 * Initializes the module
	 *
	 * @param positionEnum the position
	 */
	public void init(PositionEnum positionEnum) {
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
	 * Initializes the module on the left
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
	public List<DebugLine> getLines() {
		return lines;
	}


	/**
	 * Gets formatted lines
	 *
	 * @param reducedDebug has reduced debug on
	 * @return the lines formatted
	 */
	public List<Component> getLinesFormatted(boolean reducedDebug) {

		List<Component> linesString = new ArrayList<>();

		for (DebugLine line : lines) {
			if (reducedDebug && !line.inReducedDebug) {
				continue;
			}
			if (!line.active || !line.enabled) {
				continue;
			}

			if (line instanceof DebugLineList lineList) {
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

	/**
	 * Gets line at id
	 *
	 * @param id the id
	 * @return the line
	 */
	public DebugLine getLine(String id) {

		Optional<DebugLine> lineOptional = lines.stream().filter(line -> line.getId().equals(id)).findFirst();

		return lineOptional.orElse(null);

	}

	/**
	 * Gets module by string.
	 *
	 * @param string the string
	 * @return the module
	 */
	public static BaseModule getModule(String string) {
		return BaseModule.allModules.stream().filter(baseModule -> baseModule.toString().equals(string)).findFirst().orElse(null);
	}

	/**
	 * Gets module by id.
	 *
	 * @param id the id
	 * @return the module
	 */
	public static BaseModule getModuleById(String id) {
		return BaseModule.allModules.stream().filter(baseModule -> baseModule.id.equals(id)).findFirst().orElse(null);
	}

	/**
	 * Gets localized module name
	 * @return localized module name
	 */
	public String toString() {
		return I18n.get("text.betterf3.module." + id);
	}

	/**
	 * Sets enabled.
	 *
	 * @param enabled enabled
	 */
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	/**
	 * Updates the module
	 *
	 * @param client the Minecraft client
	 */
	public abstract void update(Minecraft client);
}