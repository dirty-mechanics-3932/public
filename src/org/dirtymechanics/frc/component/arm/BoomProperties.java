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
public interface BoomProperties {

    /**
     * @return the autonomousShot
     */
    PIDBoom.Location getAutonomousShot();

    /**
     * @return the D
     */
    double getD();

    /**
     * @return the ground
     */
    PIDBoom.Location getGround();

    /**
     * @return the highGoal
     */
    PIDBoom.Location getHighGoal();

    /**
     * @return the max
     */
    PIDBoom.Location getMax();

    /**
     * @return the min
     */
    PIDBoom.Location getMin();

    /**
     * @return the P
     */
    double getP();

    /**
     * @return the pass
     */
    PIDBoom.Location getPass();

    /**
     * @return the pidArmDownLimit
     */
    PIDBoom.Location getPidArmDownLimit();

    /**
     * @return the pidArmUpLimit
     */
    PIDBoom.Location getPidArmUpLimit();

    /**
     * @return the pidPass
     */
    PIDBoom.Location getPidPass();

    /**
     * @return the rest
     */
    PIDBoom.Location getRest();
    
}
