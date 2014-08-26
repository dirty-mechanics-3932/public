/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.dirtymechanics.frc.component.arm;

/**
 *
 * @author agresh
 */
public class CompetitionBoomProps implements BoomProperties {
   double P = .018d;
   double D = .009; // The boom, she wants the D
   
   PIDBoom.Location pidPass = new PIDBoom.Location(500);//GROUND;//new Location(3.26);
   PIDBoom.Location pidArmUpLimit = new PIDBoom.Location(150);//GROUND;//new Location(3.26);
   PIDBoom.Location pidArmDownLimit = new PIDBoom.Location(769);//GROUND;//new Location(3.26);
   PIDBoom.Location max = new PIDBoom.Location(200);
   PIDBoom.Location min = new PIDBoom.Location(769);

   PIDBoom.Location rest = new PIDBoom.Location(247);
   PIDBoom.Location autonomousShot = new PIDBoom.Location(336);
   PIDBoom.Location highGoal = new PIDBoom.Location(336);
   PIDBoom.Location ground = min;
   PIDBoom.Location pass = new PIDBoom.Location(535);

    public double getP() {
        return P;
    }

    public double getD() {
        return D;
    }

    public PIDBoom.Location getPidPass() {
        return pidPass;
    }

    public PIDBoom.Location getPidArmUpLimit() {
        return pidArmUpLimit;
    }

    public PIDBoom.Location getPidArmDownLimit() {
        return pidArmDownLimit;
    }

    public PIDBoom.Location getMax() {
        return max;
    }

    public PIDBoom.Location getMin() {
        return min;
    }

    public PIDBoom.Location getRest() {
        return rest;
    }

    public PIDBoom.Location getAutonomousShot() {
        return autonomousShot;
    }

    public PIDBoom.Location getHighGoal() {
        return highGoal;
    }

    public PIDBoom.Location getGround() {
        return ground;
    }

    public PIDBoom.Location getPass() {
        return pass;
    }

    
}
