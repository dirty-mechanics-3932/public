package org.dirtymechanics.frc.control;



public class ButtonMap {

    
    

    private final BasicJoystick left;
    private final BasicJoystick right;
    private final GameController cont;



    private int mode = 0;

    public ButtonMap(BasicJoystick left, BasicJoystick right, GameController cont) {
        this.left = left;
        this.right = right;
        this.cont = cont;
    }



    

    

    public boolean fire() {
        return cont.getRawButton(12);
    }

    /**
     * 0 = idle 1 = firing 2 = collecting
     *
     * @return
     */
    public int getMode() { //TODO: add ball sensor
        if (cont.getRawButton(2)) {
            mode = 1;
        } else if (cont.getRawButton(3)) {
            mode = 2;
        } else {
            if (cont.getRawButton(1)) {
                mode = 0;
            }
        }
        return mode;
    }
}
