
package org.dirtymechanics.frc.component.arm.event;

import org.dirtymechanics.event.ButtonEventHandler;
import org.dirtymechanics.event.impl.ButtonListener;
import org.dirtymechanics.frc.component.arm.ScrewDrive;
import org.dirtymechanics.frc.control.GameController;



public class ScrewDriveDecreaseOffsetButtonEventHandler implements ButtonEventHandler {
    private GameController controller;
    private ScrewDrive screwDrive;
    
    public ScrewDriveDecreaseOffsetButtonEventHandler(GameController operatorController, ScrewDrive screwDrive) {
        this.controller = operatorController;
        this.screwDrive = screwDrive;
    }

    public void onEvent(int buttonEvent) {
        switch (buttonEvent) {
            case ButtonListener.SINGLE_CLICK:
                screwDrive.decreaseOffset();
                break;
            case ButtonListener.HOLD:
                break;
            case ButtonListener.NEUTRAL:
                break;
        }
    }
    
}
