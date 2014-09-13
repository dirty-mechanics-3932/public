/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.dirtymechanics.frc.component.arm;

import org.dirtymechanics.frc.control.BasicJoystick;
import org.dirtymechanics.frc.control.GameController;
import org.dirtymechanics.frc.control.Joystick;



/**
 *
 * @author agresh
 */
public class Boom {
    private PIDBoom angleControlAssembly;
    private ScrewDrive screDrive;
    private Roller roller;
    private Shooter shooter;
    private GameController operatorController;
    private BasicJoystick operatorJoystick;
    
    public Boom(PIDBoom angleControlAssembly, 
        ScrewDrive screwDrive, 
        Roller roller, 
        Shooter shooter, 
        GameController operatorController, 
        BasicJoystick operatorJoystick) {
        this.angleControlAssembly = angleControlAssembly;
        this.screDrive = screwDrive;
        this.roller = roller;
        this.shooter = shooter;
        this.operatorController = operatorController;
        this.operatorJoystick = operatorJoystick;
    }
    
    
    
}
