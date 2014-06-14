/*
 * CutExample.java
 *
 * Created on 9 maggio 2007, 15.44
 */

package org.guetal.mp3.processing.samples;

import java.io.InputStream;
import java.util.logging.Logger;

import org.guetal.mp3.processing.effects.Copy;


/**
 *
 * @author  Administrator
 * @version
 */
public class CopyExample {
    private String fileName = "/audio/song.mp3";
    private Copy effect;

    private final static Logger LOGGER = Logger.getLogger(CopyExample.class.getName()); 
    
    public void startApp() {
        InputStream is = getClass().getResourceAsStream(fileName);
        
        effect = new Copy();
        
        try {
            long time1=System.currentTimeMillis();
            
            effect.copy(is, 50, 200);
            
            long time2=System.currentTimeMillis();
            LOGGER.info("time elapsed (ms): " + (time2 - time1));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        

    }
    
    public void pauseApp() {
    }
    
    public void destroyApp(boolean unconditional) {
    }
}
