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


public interface  BasicJoystick extends IInputOutput {
    public double getX(Hand hand);
    public double getY(Hand hand);
    public double getZ(Hand hand);
    public double getTwist() ;
    public double getThrottle();
    public double getRawAxis(int axis);
    public double getAxis(AxisType axis);
    public boolean getTrigger(Hand hand);
    public boolean getTop(Hand hand);
    public boolean getBumper(Hand hand);
    public boolean getRawButton(int button);
    public boolean getButton(ButtonType button);
    public double getMagnitude();
    public double getDirectionRadians();
    public double getDirectionDegrees();
    public int getAxisChannel(AxisType axis);
    public void setAxisChannel(AxisType axis, int channel);

    public double getY();
}
