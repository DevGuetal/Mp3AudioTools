/*
 * DeleteExample.java
 *
 * Created on 4 maggio 2007, 15.44
 */

package org.guetal.mp3.processing.samples;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import org.guetal.fileManager.FileManager;
import org.guetal.mp3.processing.commons.data.FrameBuffer;
import org.guetal.mp3.processing.effects.Copy;
import org.guetal.mp3.processing.effects.Invert;



/**
 *
 * @author  Administrator
 * @version
 */
public class InvertExample  {
    //
    private String fileName = System.getProperty("user.dir") + "/input/song.mp3";
    //private String fileName = "/audio/mic.mp3";
    //private String fileName = "/audio/song.mp3";
    private Invert effect;
    private byte [] stream;
    
    public void startApp() throws FileNotFoundException {
    	
    	File inputFile = new File(fileName);
        InputStream is = new FileInputStream(inputFile);
        InputStream is2 = new FileInputStream(inputFile);
        
        FileManager file = new FileManager();
        Copy copy = new Copy();
        
        effect = new Invert();
        try {
            
            long tempo1=System.currentTimeMillis();
            
            FrameBuffer buffer  = copy.copy(is, 300, 800);
            stream = effect.invert(is2, 400, 600, buffer);
            
            is.close();
            is2.close();
            long tempo2=System.currentTimeMillis();
            System.out.println("time elapsed (ms): " + (tempo2 - tempo1));
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        file.saveMedia(stream, "audio/");
        System.out.println("FILE SAVED");
        
    }
    
    public void pauseApp() {
    }
    
    public void destroyApp(boolean unconditional) {
    }
}
