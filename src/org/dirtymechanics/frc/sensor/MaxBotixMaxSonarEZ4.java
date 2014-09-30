
package org.dirtymechanics.frc.sensor;

import edu.wpi.first.wpilibj.AnalogChannel;

/**
 *
 * @author Daniel Ruess
 */
public class MaxBotixMaxSonarEZ4 extends AnalogChannel{
    public static final double VOLTS_PER_INCH = .009766;  //from spec for LV-MaxSonar-EZ4 (MB1040)
    public static final double INCHES_PER_VOLT = 102.39606; //inverse of volts per inch
    
    public MaxBotixMaxSonarEZ4(int channel) {
        super(channel);
    }
    
    public double getRangeInInches() {
        return getRangeInInches(getAverageVoltage());
    }
    
    public double getRangeInInches(double averageVoltage) {
        double inches = averageVoltage * INCHES_PER_VOLT;
        
        return inches;
    }
    
   
    
    
}
