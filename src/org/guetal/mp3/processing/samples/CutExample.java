/*
 * CutExample.java
 *
 * Created on 9 maggio 2007, 15.44
 */

package org.guetal.mp3.processing.samples;

import java.io.InputStream;
import java.util.logging.Logger;

import org.guetal.mp3.processing.effects.Cut;


/**
 *
 * @author  Administrator
 * @version
 */
public class CutExample {
    private String fileName = "/audio/trombe.mp3";
    private Cut effect;
    
    private final static Logger LOGGER = Logger.getLogger(CutExample.class.getName()); 
    
    public void startApp() {
        InputStream is = getClass().getResourceAsStream(fileName);
        
        effect = new Cut();
        
        try {
            long tempo1=System.currentTimeMillis();
            effect.cut(is, 300, 1200);
            
            long tempo2=System.currentTimeMillis();
            System.out.println("time elapsed (ms): " + (tempo2 - tempo1));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
        LOGGER.info("FILE SAVED");
        
    }
    
    public void pauseApp() {
    }
    
    public void destroyApp(boolean unconditional) {
    }
}
