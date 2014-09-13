package org.dirtymechanics.frc.actuator;

import edu.wpi.first.wpilibj.Solenoid;
import org.dirtymechanics.frc.util.Updatable;

/**
 * Controls a double solenoid with logic to debounce input and disable redundant
 * relays to conserve power.
 *
 * @author Daniel Ruess
 */
public class DoubleSolenoid implements Updatable {

    /**
     * The time to wait after firing to disable the open valve.
     */
    private static final int FIRE_WAIT = 1000;

    /**
     * The spike controlling the opening of the valve.
     */
    private final Solenoid openSpike;
    /**
     * The spike controlling the closing of the valve.
     */
    private final Solenoid closeSpike;
    /**
     * The state of the solenoid.
     */
    private boolean state;
    /**
     * Whether or not the state has changes.
     */
    private boolean stateChanged = true;
    /**
     * The time of the last state change.
     */
    private long lastStateChange;

    /**
     * Creates a new double solenoid with a 1000ms debounce and initial state of
     * false.
     *
     * @param openSpike The spike controlling the open valve.
     * @param closeSpike The spike controlling the close valve.
     */
    public DoubleSolenoid(Solenoid openSpike, Solenoid closeSpike) {
        this(openSpike, closeSpike, false);
    }

    /**
     * Creates a new double solenoid.
     *
     * @param openSpike The spike controlling the open valve.
     * @param closeSpike The spike controlling the close valve.
     * @param initialState The initial state to start in.
     */
    public DoubleSolenoid(Solenoid openSpike, Solenoid closeSpike, boolean initialState) {
        this.openSpike = openSpike;
        this.closeSpike = closeSpike;
        this.state = initialState;
    }

    /**
     * Sets the state of the solenoid. True being open, false being close.
     *
     * @param state The state of the solenoid.
     */
    public void set(boolean state) {
        if (this.state != state) {
            stateChanged = true;
        }
        this.state = state;
    }

    /**
     * Inverts the state.
     */
    public void flip() {
        set(!state);
    }

    public void setOpen() {
        set(true);
    }

    public void setClosed() {
        set(false);
    }

    /**
     * Called per cycle to update the state of the valves.
     */
    public void update() {
        if (state) {
            openSpike.set(true);
            closeSpike.set(false);
        } else {
            openSpike.set(false);
            closeSpike.set(true);
        }
    }
}
