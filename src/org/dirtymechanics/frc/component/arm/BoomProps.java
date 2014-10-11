package org.dirtymechanics.frc.component.arm;

public class BoomProps implements BoomProperties {
   double P = .016d;
   double D = .016d; // The boom, she wants the D
   
   PIDBoom.Location pidPass = new PIDBoom.Location(500);//GROUND;//new Location(3.26);
   PIDBoom.Location pidArmUpLimit = new PIDBoom.Location(150);//GROUND;//new Location(3.26);
   PIDBoom.Location pidArmDownLimit = new PIDBoom.Location(769);//GROUND;//new Location(3.26);
   PIDBoom.Location max = new PIDBoom.Location(200);
   PIDBoom.Location min = new PIDBoom.Location(769);

   PIDBoom.Location rest = new PIDBoom.Location(197); //68.5 degrees
   PIDBoom.Location autonomousShot = new PIDBoom.Location(328);
   PIDBoom.Location highGoal = new PIDBoom.Location(262);  //57.8 degrees
   PIDBoom.Location ground = new PIDBoom.Location(789);  //-23.8 degrees  
   PIDBoom.Location pass = new PIDBoom.Location(479);
   int moveIncrementSize = 10;

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
    
    public int getMoveIncrementSize() {
        return moveIncrementSize;
    }

    public double sign() {
        return 1;
    }

    
}
