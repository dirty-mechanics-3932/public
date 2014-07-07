package org.dirtymechanics.frc.util;

/**
 * Represents an object that must be updated per-cycle.
 *
 * @author Daniel Ruess
 */
public interface Updatable {

    /**
     * Called per iteration.
     */
    public void update();
}
