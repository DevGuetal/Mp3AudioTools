package org.guetal.mp3.processing.samples;
/*
 * TimeStretchingExample.java
 *
 * Created on 9 maggio 2007, 15.44
 */

import java.io.IOException;
import java.io.InputStream;

import org.guetal.fileManager.FileManager;
import org.guetal.mp3.processing.effects.TimeStretching;

/**
 *
 * @author  Administrator
 * @version
 */
public class TimeStretchingExample {
   // private String fileName = "/audio/trombe.mp3";
    //private String fileName = "/audio/mic.mp3";
    private String fileName = "song.mp3";
    //private String fileName = "/audio/440-128-CBR.mp3";
    private TimeStretching effect;
    private byte [] stream;
    
    public void startApp() {
        InputStream is = FileManager.getInputStream(fileName);

        effect = new TimeStretching();
        try {
            //System.out.println("buff len: " + buffer.length+"\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
            stream = effect.stretch(is, 1.5F, 20, 200);
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        FileManager file = new FileManager();
        
        if (stream.length > 100000)
            file.saveMedia(stream, "audio/");
    }
    
    public void pauseApp() {
    }
    
    public void destroyApp(boolean unconditional) {
    }
}
