package org.dirtymechanics.frc.component.arm;

import org.dirtymechanics.frc.component.arm.event.RollerReverseButtonEventHandler;
import org.dirtymechanics.frc.component.arm.event.RollerForwardButtonEventHandler;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.networktables.NetworkTable;
import org.dirtymechanics.event.impl.ButtonListener;
import org.dirtymechanics.frc.actuator.DoubleSolenoid;
import org.dirtymechanics.frc.component.arm.event.RollerArmButtonEventHandler;
import org.dirtymechanics.frc.control.OperatorGameController;
import org.dirtymechanics.frc.util.Updatable;

//TODO most of the public methods here should be package scope
public class Roller implements Updatable {
    NetworkTable server = NetworkTable.getTable("SmartDashboard");
    
    private Relay motorRelay = new Relay(2);
    private Solenoid rollerOpen = new Solenoid(1, 3);
    private Solenoid rollerClose = new Solenoid(1, 4);
    private DoubleSolenoid armSolenoid = new DoubleSolenoid(rollerOpen, rollerClose);
    //TODO move all these handlers and listeners into the operator controller
    RollerForwardButtonEventHandler rollerForwardButtonEventHandler; 
    ButtonListener rollerForwardButtonListener = new ButtonListener();
    RollerReverseButtonEventHandler rollerReverseButtonEventHandler; 
    ButtonListener rollerReverseButtonListener = new ButtonListener();
    RollerArmButtonEventHandler rollerArmButtonEventHandler;
    ButtonListener rollerArmButtonListener = new ButtonListener();
    private OperatorGameController operatorController;
    

    public Roller(OperatorGameController operatorController) {
        this.operatorController = operatorController;
    }
    
    public void init() {
        rollerForwardButtonEventHandler = new RollerForwardButtonEventHandler(operatorController, this);
        rollerForwardButtonListener.addHandler(rollerForwardButtonEventHandler);
        rollerArmButtonEventHandler = new RollerArmButtonEventHandler(operatorController, this);
        rollerArmButtonListener.addHandler(rollerArmButtonEventHandler);
        rollerReverseButtonEventHandler = new RollerReverseButtonEventHandler(operatorController, this);
        rollerReverseButtonListener.addHandler(rollerReverseButtonEventHandler);
    }
    
    public void forward() {
        motorRelay.set(Relay.Value.kForward);
    }

    public void stop() {
        motorRelay.set(Relay.Value.kOff);

    }

    public void reverse() {
        motorRelay.set(Relay.Value.kReverse);
    }

    public void openArm() {
        armSolenoid.close();
        reportStatus("Roller.Arm", "Open");
    }

    public void closeArm() {
        armSolenoid.open();
        reportStatus("Roller.Arm", "Closed");
    }
    
    public void toggleForward() {
        if (isRollerDirection(Relay.Value.kForward)) {
            stop();
            reportStatus("stop");
        } else {
            forward();
            reportStatus("forward");
        }
        
    }
    
    public void toggleReverse() {
        if (isRollerDirection(Relay.Value.kReverse)) {
            stop();
            reportStatus("stop");
        } else {
            reverse();
            reportStatus("forward");
        }
        
    }

    public void reportStatus(String status) {
        server.putString("Roller", status);
    }
    
    public void reportStatus(String component, String status) {
        server.putString(component, status);
    }


    public boolean isRollerDirection(final Relay.Value direction) {
        return motorRelay.get().equals(direction);
    }
    
 
    
    
    public void toggleArm() {
        if (!armSolenoid.isOpen()) {
            closeArm();
        } else {
            openArm();
        }
    }

    public void update() {
        long currentTime = System.currentTimeMillis();
        rollerForwardButtonListener.updateState(operatorController.isRollerForwardButtonPressed(), currentTime);
        rollerReverseButtonListener.updateState(operatorController.isRollerReverseButtonPressed(), currentTime);
        rollerArmButtonListener.updateState(operatorController.isRollerArmButtonPressed(), currentTime);
    }
}
