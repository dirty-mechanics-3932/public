/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.dirtymechanics.frc.component.arm;

public class BoomPropsSibling extends BoomProps {
    //For now sibling and Competition are the same.  Place overrides here.
   double P = -.016d; // Sibling motor's wired the opposite way
   double D = -.012; // The boom, she wants the D
   
   PIDBoom.Location pidPass = new PIDBoom.Location(500);//GROUND;//new Location(3.26);
   PIDBoom.Location pidArmUpLimit = new PIDBoom.Location(163);//GROUND;//new Location(3.26);
   PIDBoom.Location pidArmDownLimit = new PIDBoom.Location(775);//GROUND;//new Location(3.26);
   PIDBoom.Location max = new PIDBoom.Location(765);
   PIDBoom.Location min = new PIDBoom.Location(167);

   PIDBoom.Location rest = new PIDBoom.Location(756);
   PIDBoom.Location autonomousShot = new PIDBoom.Location(667);
   PIDBoom.Location highGoal = new PIDBoom.Location(667);
   PIDBoom.Location ground = min;
   PIDBoom.Location pass = new PIDBoom.Location(367);
   
    public int getMoveIncrementSize(){
        return -10;
    }
}
