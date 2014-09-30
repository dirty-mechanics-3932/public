package org.dirtymechanics.frc.actuator;

import edu.wpi.first.wpilibj.Solenoid;

public class DoubleSolenoid {
    private final Solenoid openSpike;
    private final Solenoid closeSpike;

    
    public DoubleSolenoid(Solenoid openSpike, Solenoid closeSpike) {
        this.openSpike = openSpike;
        this.closeSpike = closeSpike;
    }


    /**
     * Sets the state of the solenoid. True being open, false being close.
     *
     * @param state The state of the solenoid.
     */
    private void setOpen(boolean isOpen) {
        if (isOpen) {
            open();
        } else {
            close();
        }
    }

    /**
     * Inverts the state.
     */
    public void flip() {
        setOpen(!isOpen());
    }

    public void open() {
        openSpike.set(true);
        closeSpike.set(false);
    }

    public void close() {
        openSpike.set(false);
        closeSpike.set(true);
    }

    /**
     * Returns true if the openSpike solenoid is on.
     * @return 
     */
    public boolean isOpen() {
        return openSpike.get();
    }
}
