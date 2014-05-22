/*
 * CutExample.java
 *
 * Created on 9 maggio 2007, 15.44
 */

package org.guetal.mp3.processing.samples;

import java.io.InputStream;

import org.guetal.mp3.processing.effects.Copy;


/**
 *
 * @author  Administrator
 * @version
 */
public class CopyExample {
    //private String fileName = "/audio/trombe.mp3";
    //private String fileName = "/audio/mic.mp3";
    private String fileName = "/audio/song.mp3";
   // private String fileName = "audio/440-128-CBR.mp3";
    private Copy effect;

    
    public void startApp() {
        InputStream is = getClass().getResourceAsStream(fileName);
        
//        FileManager file = new FileManager();
        
        effect = new Copy();
        
        try {
            long tempo1=System.currentTimeMillis();
            
            effect.copy(is, 50, 200);
            
            long tempo2=System.currentTimeMillis();
            System.out.println("time elapsed (ms): " + (tempo2 - tempo1));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        

    }
    
    public void pauseApp() {
    }
    
    public void destroyApp(boolean unconditional) {
    }
}
