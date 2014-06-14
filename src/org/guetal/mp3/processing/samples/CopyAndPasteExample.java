/*
 * CopyAndPasteExample.java
 *
 * Created on 9 maggio 2007, 15.44
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
public class CopyAndPasteExample {
    private String fileName = "/audio/U2.mp3";
    
    private final static Logger LOGGER = Logger.getLogger(CopyAndPasteExample.class.getName()); 
    
    public void startApp() {
        InputStream is = getClass().getResourceAsStream(fileName);
            
        try {
            long time1=System.currentTimeMillis();
            
            is = getClass().getResourceAsStream(fileName);
            Analysis.average_bitrate(is);
            
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
