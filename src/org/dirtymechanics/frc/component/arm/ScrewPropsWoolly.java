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
public class ScrewPropsWoolly implements ScrewProperties {
    Location HIGH_GOAL = new Location(2.5); //2.55
    Location PASS = new Location(1.5);
    Location RESET = new Location(0.45); //(0.577);
    Location TRUSS_SHOT = new Location(2.8);
    Location AUTONOMOUS_SHOT = HIGH_GOAL; //new Location(2.45);


    public Location highGoal() {
       return HIGH_GOAL;
    }

    public Location pass() {
        return PASS;
    }

    public Location reset() {
        return RESET;
    }

    public Location trussShot() {
        return TRUSS_SHOT;
    }
}
