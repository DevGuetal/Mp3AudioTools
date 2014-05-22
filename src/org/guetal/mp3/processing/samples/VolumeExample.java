/*
 * VolumeExample.java
 *
 * Created on 17 giugno 2007, 17.48
 */

package org.guetal.mp3.processing.samples;

import java.io.InputStream;

import org.guetal.fileManager.FileManager;
import org.guetal.mp3.processing.effects.VolumeControl;



/**
 *
 * @author  Administrator
 * @version
 */
public class VolumeExample {
    private VolumeControl effect;
    private String fileName = "/audio/U2.mp3";

    public void startApp() {

        byte [] stream;
        InputStream is = getClass().getResourceAsStream(fileName);
        
        effect = new VolumeControl();
        
        try {
            long time1 = System.currentTimeMillis();
            
            stream = effect.volume_control(is, -11, 0, 3);
            FileManager file = new FileManager();
            is.close();
            
            long time2 = System.currentTimeMillis();
            System.out.println("time elapsed (ms): " + (time2 - time1));
            
            file.saveMedia(stream, "audio/");
            System.out.println("FILE SAVED");

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
    }
    
    public void pauseApp() {
    }
    
    public void destroyApp(boolean unconditional) {
    }
}
