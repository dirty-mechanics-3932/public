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
public class SiblingBoomProps extends CompetitionBoomProps {
    //For now sibling and Competition are the same.  Place overrides here.
    
    public int getMoveIncrementSize(){
        return -10;
    }
}
