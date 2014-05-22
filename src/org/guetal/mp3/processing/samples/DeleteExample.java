/*
 * DeleteExample.java
 *
 * Created on 4 maggio 2007, 15.44
 */

package org.guetal.mp3.processing.samples;

import java.io.InputStream;

import org.guetal.mp3.processing.effects.Delete;


/**
 *
 * @author  Administrator
 * @version
 */
public class DeleteExample {
    //
    private String fileName = "/audio/song.mp3";
    //private String fileName = "/audio/mic.mp3";
    //private String fileName = "/audio/song.mp3";
    private Delete effect;
    private byte [] stream;
    
    public void startApp() {
        InputStream is = getClass().getResourceAsStream(fileName);
        
        InputStream real = getClass().getResourceAsStream(fileName);
        
//        FileManager file = new FileManager();
        
        effect = new Delete();
        try {
            long tempo1=System.currentTimeMillis();
            stream = effect.delete(is, 15, 400);
            is.close();
            
            long tempo2=System.currentTimeMillis();
            System.out.println("time elapsed (ms): " + (tempo2 - tempo1));
            
//            file.saveMedia(stream, "audio/");
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
