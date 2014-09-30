/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.dirtymechanics.frc.component.arm;

public class BoomPropsSibling implements BoomProperties {
    //For now sibling and Competition are the same.  Place overrides here.
   double sP = .016d; // Sibling motor's wired the opposite way
   double sD = .012d; // The boom, she wants the D
   
   PIDBoom.Location spidPass = new PIDBoom.Location(500);//GROUND;//new Location(3.26);
   PIDBoom.Location spidArmUpLimit = new PIDBoom.Location(163);//GROUND;//new Location(3.26);
   PIDBoom.Location spidArmDownLimit = new PIDBoom.Location(775);//GROUND;//new Location(3.26);
   PIDBoom.Location smax = new PIDBoom.Location(765);
   PIDBoom.Location smin = new PIDBoom.Location(167);

   PIDBoom.Location srest = new PIDBoom.Location(756);
   PIDBoom.Location sautonomousShot = new PIDBoom.Location(667);
   PIDBoom.Location shighGoal = new PIDBoom.Location(667);
   PIDBoom.Location sground = smin;
   PIDBoom.Location spass = new PIDBoom.Location(367);
   
   int smoveIncrementSize = -10;
   
   
    public double getP() {
        return sP;
    }

    public double getD() {
        return sD;
    }

    public PIDBoom.Location getPidPass() {
        return spidPass;
    }

    public PIDBoom.Location getPidArmUpLimit() {
        return spidArmUpLimit;
    }

    public PIDBoom.Location getPidArmDownLimit() {
        return spidArmDownLimit;
    }

    public PIDBoom.Location getMax() {
        return smax;
    }

    public PIDBoom.Location getMin() {
        return smin;
    }

    public PIDBoom.Location getRest() {
        return srest;
    }

    public PIDBoom.Location getAutonomousShot() {
        return sautonomousShot;
    }

    public PIDBoom.Location getHighGoal() {
        return shighGoal;
    }

    public PIDBoom.Location getGround() {
        return sground;
    }

    public PIDBoom.Location getPass() {
        return spass;
    }
    
    public int getMoveIncrementSize() {
        return smoveIncrementSize;
    }

    public double sign() {
        return -1;
    }

}
