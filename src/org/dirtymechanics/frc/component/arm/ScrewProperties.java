/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.dirtymechanics.frc.component.arm;
import org.dirtymechanics.frc.component.arm.ScrewDrive.Location;

/**
 *
 * @author frc
 */
public interface ScrewProperties {
    
    public Location highGoal();
    public Location pass();
    public Location reset();
    public Location trussShot();
}
