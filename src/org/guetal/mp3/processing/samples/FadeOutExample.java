/*
 * VolumeExample.java
 *
 * Created on 17 giugno 2007, 17.48
 */

package org.guetal.mp3.processing.samples;

import java.io.InputStream;

import org.guetal.fileManager.FileManager;
import org.guetal.mp3.processing.effects.FadeOut;



/**
 *
 * @author  Administrator
 * @version
 */
public class FadeOutExample {
    private FadeOut effect;
    private String fileName = "/audio/usa.mp3";

    public void startApp() {

        byte [] stream;
        InputStream is = getClass().getResourceAsStream(fileName);
        
        effect = new FadeOut();
        
        try {
            long time1 = System.currentTimeMillis();
            stream = effect.fade_out(is, 100, 10);
            is.close();
            
            long time2 = System.currentTimeMillis();
            System.out.println("time elapsed (ms): " + (time2 - time1));
            
            FileManager file = new FileManager();
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
