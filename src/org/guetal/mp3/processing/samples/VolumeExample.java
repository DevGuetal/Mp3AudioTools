/*
 * VolumeExample.java
 *
 * Created on 17 giugno 2007, 17.48
 */

package org.guetal.mp3.processing.samples;

import java.io.InputStream;
import java.util.logging.Logger;

import org.guetal.fileManager.FileManager;
import org.guetal.mp3.processing.effects.VolumeControl;



/**
 *
 * @author  Administrator
 * @version
 */
public class VolumeExample {
    private VolumeControl effect;
    private String fileName = "/song.mp3";

    private final static Logger LOGGER = Logger.getLogger(VolumeExample.class.getName()); 
    
    public void startApp() {

        byte [] stream;
        InputStream is = FileManager.getInputStream(fileName);
        
        effect = new VolumeControl();
        
        try {
            long time1 = System.currentTimeMillis();
            
            stream = effect.volume_control(is, -11, 0, 3);
            FileManager file = new FileManager();
            is.close();
            
            long time2 = System.currentTimeMillis();
            LOGGER.info("time elapsed (ms): " + (time2 - time1));
            
            file.saveMedia(stream, "audio/");
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
