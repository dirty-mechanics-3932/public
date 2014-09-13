/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.dirtymechanics.frc.component.arm;

import edu.wpi.first.wpilibj.Talon;
import org.dirtymechanics.frc.sensor.RotationalEncoder;

/**
 *
 * @author agresh
 */
public class PIDBoomSibling extends PIDBoom {

    public PIDBoomSibling(Talon motor, RotationalEncoder rot) {
        super(motor, rot);
    }
    
}
