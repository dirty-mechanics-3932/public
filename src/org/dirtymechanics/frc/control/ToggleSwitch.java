package org.dirtymechanics.frc.control;

/**
 *
 * @author Daniel Ruess
 */
public class ToggleSwitch {

    private boolean released = true;
    private int toggle = 0;

    public final void update(boolean val) {
        if (val) {
            if (released) {
                if (toggle++ % 2 == 0) {
                    setOn();
                } else {
                    setOff();
                }
                released = false;
            }
        } else {
            released = true;
        }
    }

    public void setOn() {
        if (toggle % 2 != 0) {
            toggle++;
        }
    }

    public void setOff() {
        if (toggle % 2 != 0) {
            toggle++;
        }
    }

    public void set(boolean state) {
        if (state) {
            setOn();
        } else {
            setOff();
        }
    }

    public boolean getState() {
        return toggle % 2 == 0;
    }
}
