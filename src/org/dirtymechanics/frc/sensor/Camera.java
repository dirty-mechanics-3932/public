/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.dirtymechanics.frc.sensor;

import edu.wpi.first.wpilibj.Solenoid;

/**
 *
 * @author frc
 */
public class Camera {
    private Solenoid ledA;
    private Solenoid ledB;
    
    public Camera(Solenoid ledA, Solenoid ledB) {
        this.ledA = ledA;
        this.ledB = ledB;
    }
    
    public void ledOn() {
        ledA.set(true);
        ledB.set(true);
    }
    
    public void ledOff() {
        ledA.set(false);
        ledB.set(false);
    }
    
}
