/*
 * VolumeExample.java
 *
 * Created on 17 giugno 2007, 17.48
 */

package org.guetal.mp3.processing.samples;

import java.io.InputStream;
import java.util.logging.Logger;

import org.guetal.mp3.processing.effects.Analysis;


/**
 *
 * @author  Administrator
 * @version
 */
public class AnalisysExample {

	private final static Logger LOGGER = Logger.getLogger(AnalisysExample.class.getName()); 

	private String fileName = "/audio/U2.mp3";

    public void startApp() {
        
        InputStream is = getClass().getResourceAsStream(fileName);
        
        try {            
            double duration = Analysis.calc_duration(is);
            
            LOGGER.info("Length of file (sec): " + duration);

            is.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
    }
    
    public void pauseApp() {
    }
    
    public void destroyApp(boolean unconditional) {
    }
}
