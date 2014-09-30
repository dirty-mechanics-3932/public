/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.dirtymechanics.frc;

/**
 * Enumerable describing each robot type we have.
 * 
 * 
 * @author Zach Sussman
 */
public class RobotType {    
    
    private static int next_number = 0;
    
    private final int value;
    public RobotType(int type){
        value = type;
    }
    
    
    public static final RobotType WOOLLY = new RobotType(1);
    public static final RobotType SIBLING = new RobotType(2);
    public static final RobotType GENERIC = new RobotType(3);
    



}
