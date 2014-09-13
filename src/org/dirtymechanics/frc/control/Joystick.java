/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.dirtymechanics.frc.control;

/**
 *
 * @author agresh
 */
public class Joystick extends edu.wpi.first.wpilibj.Joystick implements BasicJoystick, GameController {

    public Joystick(int port) {
        super(port);
    }
    
}
