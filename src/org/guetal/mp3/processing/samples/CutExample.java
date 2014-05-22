/*
 * CutExample.java
 *
 * Created on 9 maggio 2007, 15.44
 */

package org.guetal.mp3.processing.samples;

import java.io.InputStream;

import org.guetal.mp3.processing.commons.data.FrameBuffer;
import org.guetal.mp3.processing.effects.Cut;


/**
 *
 * @author  Administrator
 * @version
 */
public class CutExample {
    private String fileName = "/audio/trombe.mp3";
    //private String fileName = "/audio/mic.mp3";
    // private String fileName = "/audio/song.mp3";
    private Cut effect;
    private byte [] stream;
    
    public void startApp() {
        InputStream is = getClass().getResourceAsStream(fileName);
        
        
//        FileManager file = new FileManager();
        
        effect = new Cut();
        
        try {
            long tempo1=System.currentTimeMillis();
            FrameBuffer buffer = effect.cut(is, 300, 1200);
            
            long tempo2=System.currentTimeMillis();
            System.out.println("time elapsed (ms): " + (tempo2 - tempo1));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
//        file.saveMedia(stream, "audio/");
        System.out.println("FILE SAVED");
        
    }
    
    public void pauseApp() {
    }
    
    public void destroyApp(boolean unconditional) {
    }
}
