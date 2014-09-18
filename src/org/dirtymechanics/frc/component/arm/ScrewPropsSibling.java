/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.dirtymechanics.frc.component.arm;

/**
 *
 * @author frc
 */
public class ScrewPropsSibling implements ScrewProperties {
    ScrewDrive.Location HIGH_GOAL = new ScrewDrive.Location(2.244); //2.55
    ScrewDrive.Location PASS = new ScrewDrive.Location(1.576);
    ScrewDrive.Location RESET = new ScrewDrive.Location(0.265); //(0.577);
    ScrewDrive.Location TRUSS_SHOT = new ScrewDrive.Location(2.244);

    public ScrewDrive.Location highGoal() {
        return HIGH_GOAL;
    }

    public ScrewDrive.Location pass() {
        return PASS;
    }

    public ScrewDrive.Location reset() {
        return RESET;
    }

    public ScrewDrive.Location trussShot() {
        return TRUSS_SHOT;
    }
}
