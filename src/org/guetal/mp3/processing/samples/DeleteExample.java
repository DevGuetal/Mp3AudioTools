/*
 * DeleteExample.java
 *
 * Created on 4 maggio 2007, 15.44
 */

package org.guetal.mp3.processing.samples;

import java.io.InputStream;
import java.util.logging.Logger;

import org.guetal.mp3.processing.effects.Delete;


/**
 *
 * @author  Administrator
 * @version
 */
public class DeleteExample {
	
	
	private final static Logger LOGGER = Logger.getLogger(DeleteExample.class.getName()); 
    private String fileName = "/audio/song.mp3";
    private Delete effect;
    
    public void startApp() {
        InputStream is = getClass().getResourceAsStream(fileName);
        
        effect = new Delete();
        
        try {
            long tempo1=System.currentTimeMillis();
            effect.delete(is, 15, 400);
            is.close();
            
            long tempo2=System.currentTimeMillis();
            LOGGER.info("time elapsed (ms): " + (tempo2 - tempo1));
            
            LOGGER.info("FILE SAVED");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public void pauseApp() {
    }
    
    public void destroyApp(boolean unconditional) {
    }
}
