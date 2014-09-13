package org.dirtymechanics.frc.actuator;

import edu.wpi.first.wpilibj.Solenoid;
import org.dirtymechanics.frc.util.Updatable;
public class DoubleSolenoid implements Updatable {
    private final Solenoid openSpike;
    private final Solenoid closeSpike;
    private boolean isOpen = false;
    
    public DoubleSolenoid(Solenoid openSpike, Solenoid closeSpike) {
        this.openSpike = openSpike;
        this.closeSpike = closeSpike;
    }


    /**
     * Sets the state of the solenoid. True being open, false being close.
     *
     * @param state The state of the solenoid.
     */
    public void setOpen(boolean state) {
        this.isOpen = state;
    }

    /**
     * Inverts the state.
     */
    public void flip() {
        setOpen(!isOpen);
    }

    public void setOpen() {
        setOpen(true);
    }

    public void setClosed() {
        setOpen(false);
    }

    /**
     * Called per cycle to update the state of the valves.
     */
    public void update() {
        if (isOpen) {
            openSpike.set(true);
            closeSpike.set(false);
        } else {
            openSpike.set(false);
            closeSpike.set(true);
        }
    }
}
