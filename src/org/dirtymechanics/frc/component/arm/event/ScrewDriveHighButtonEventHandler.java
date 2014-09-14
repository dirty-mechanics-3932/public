
package org.dirtymechanics.frc.component.arm.event;

import org.dirtymechanics.event.ButtonEventHandler;
import org.dirtymechanics.event.impl.ButtonListener;
import org.dirtymechanics.frc.component.arm.ScrewDrive;
import org.dirtymechanics.frc.control.GameController;



public class ScrewDriveHighButtonEventHandler implements ButtonEventHandler {
    private GameController controller;
    private ScrewDrive screwDrive;
    
    public ScrewDriveHighButtonEventHandler(GameController operatorController, ScrewDrive screwDrive) {
        this.controller = operatorController;
        this.screwDrive = screwDrive;
    }

    public void onEvent(int buttonEvent) {
        switch (buttonEvent) {
            case ButtonListener.SINGLE_CLICK:
                screwDrive.high();
                break;
            case ButtonListener.HOLD:
                break;
            case ButtonListener.NEUTRAL:
                break;
        }
    }
    
}
