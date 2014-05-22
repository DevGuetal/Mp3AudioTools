/*
 * EnergyCalculationExample.java
 *
 * Created on 29 maggio 2007, 14.57
 */

package org.guetal.mp3.processing.samples;

import java.io.InputStream;

import org.guetal.mp3.processing.effects.EnergyCalculation;


/**
 *
 * @author  Administrator
 * @version
 */
public class EnergyCalculationExample {

    private EnergyCalculation effect;
    float [][] E;

    private InputStream is;
    private String fileName = "/audio/U2.mp3";
    
    public void startApp() {
        effect = new EnergyCalculation();
        InputStream is = getClass().getResourceAsStream(fileName);
        try {    
            E = effect.calcEnergy(is, 0, 3000);
            is.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        System.out.print("E = [");
        
        for (int frame = 0; frame < E[0].length; frame++)
            System.out.print(" " + E[0][frame]);
        
        System.out.print(";");
        
        for (int frame = 0; frame < E[1].length; frame++)
            System.out.print(" " + E[1][frame]);
        
        System.out.print("];\n");
    }
    
    public void pauseApp() {
    }
    
    public void destroyApp(boolean unconditional) {
    }
}
