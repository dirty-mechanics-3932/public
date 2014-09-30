/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.dirtymechanics.frc.control;

import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.Joystick.AxisType;
import edu.wpi.first.wpilibj.Joystick.ButtonType;
import edu.wpi.first.wpilibj.parsing.IInputOutput;


/**
 * A marker interface that can be used to distinguish between a game controller,
 * which has multiple sticks, and a basic joystick.
 * @author agresh
 */
public interface  GameController extends BasicJoystick, IInputOutput {
}
