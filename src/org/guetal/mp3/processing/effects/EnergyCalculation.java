/*
 * EnergyCalculation.java
 *
 * Created on 29 maggio 2007, 14.56
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.guetal.mp3.processing.effects;

import java.io.InputStream;

import org.guetal.mp3.processing.commons.Constants;
import org.guetal.mp3.processing.commons.Manager;
import org.guetal.mp3.processing.commons.data.FrameData;
import org.guetal.mp3.processing.commons.data.FrameDataDequantized;
import org.guetal.mp3.processing.commons.util.MathUtil;


/**
 *
 * @author Administrator
 */
public class EnergyCalculation {
    
    private FrameDataDequantized fd;
    
    
    
    /**
     * Creates a new instance of EnergyCalculation
     */
    public EnergyCalculation() {
    }
    
    public float [][] calcEnergy(InputStream is, int fStart, int fEnd){
        Manager manager = new Manager(is);
        
        int cont = 0;
        int bytes = 0;
        final int opt = Constants.DEQUANTIZED_DOMAIN;
        
        float [][] E      = new float [2][fEnd - fStart];
        float [][] energy = {{0f, 0f},
        {0f, 0f}};
        do{
            try{
                fd = (FrameDataDequantized) manager.decode_frame(opt);
            } catch (Exception e){
                System.out.println("End of file" );
                break;
            }
            
            //System.out.println("\n\n---------------------------------------------(Step 1 - Filtering - Iteration number: " + cont + ")---------------------------------------------");
            
            
            if((cont >= fStart) && (cont < fEnd)){
                for(int ch = 0; ch < fd.getChannels(); ch++)
                    for(int gr = 0; gr < fd.getMaxGr(); gr++){
                    float [] ro = fd.getRo(ch, gr);
                    
                    for(int line = 0; line < Constants.FREQ_LINES; line++)
                        energy[gr][ch] += (float)((ro[line]) * (ro[line]));
                    
                    }
                
                E[0][cont - fStart] = energy[0][0] +  energy[1][0];
                E[1][cont - fStart] = energy[0][1] +  energy[1][1];
                energy[0][1] = 0f;
                energy[1][1] = 0f;
                energy[0][0] = 0f;
                energy[1][0] = 0f;
                
            }
            
            if (cont > fEnd) break;
            
            cont++;
        } while (true);
        
        System.out.println("Frames processed: " + cont);
        return E;
    }
}
