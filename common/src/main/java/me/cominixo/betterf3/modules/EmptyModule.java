package me.cominixo.betterf3.modules;

import me.cominixo.betterf3.utils.DebugLine;
import net.minecraft.client.Minecraft;

/**
 * An Empty module.
 */
public class EmptyModule extends BaseModule {

    /**
     * Empty module instance.
     */
    public final static EmptyModule INSTANCE = new EmptyModule(false);

    /**
     * Instantiates a new Empty module.
     *
     * @param invisible sets invisibility
     */
    public EmptyModule(final boolean invisible) {
        super(invisible);
        lines.add(new DebugLine("", "", false));

        lines.get(0).inReducedDebug = true;
    }

    @Override
    public void update(final Minecraft client) {
        lines.get(0).value("");
    }
}
