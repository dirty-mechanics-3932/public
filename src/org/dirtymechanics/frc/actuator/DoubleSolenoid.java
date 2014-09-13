package org.dirtymechanics.frc.actuator;

import edu.wpi.first.wpilibj.Solenoid;
import org.dirtymechanics.frc.util.Updatable;
public class DoubleSolenoid implements Updatable {
    private final Solenoid openSpike;
    private final Solenoid closeSpike;
    private boolean state;

    public DoubleSolenoid(Solenoid openSpike, Solenoid closeSpike) {
        this(openSpike, closeSpike, false);
    }

    /**
     * Creates a new double solenoid.
     *
     * @param openSpike The spike controlling the open valve.
     * @param closeSpike The spike controlling the close valve.
     * @param initialState 
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
