/*
 * CopyAndPasteExample.java
 *
 * Created on 9 maggio 2007, 15.44
 */

package org.guetal.mp3.processing.samples;

import java.io.InputStream;

import org.guetal.mp3.processing.commons.data.FrameBuffer;
import org.guetal.mp3.processing.effects.Analysis;
import org.guetal.mp3.processing.effects.Copy;
import org.guetal.mp3.processing.effects.Paste;


/**
 *
 * @author  Administrator
 * @version
 */
public class CopyAndPasteExample {
    //private String fileName = "/audio/age.mp3";
    //private String fileName = "/audio/mic.mp3";
    private String fileName = "/audio/U2.mp3";
    //private String fileName = "/audio/440-128-CBR.mp3";
    private Paste effect;
    private Copy copy;
    private byte [] stream;
    
    public void startApp() {
        InputStream is = getClass().getResourceAsStream(fileName);
        InputStream is2 = getClass().getResourceAsStream(fileName);
        
        copy = new Copy();
        effect = new Paste();
        
        
        try {
            long tempo1=System.currentTimeMillis();
            
            FrameBuffer buffer  = copy.copy(is, 0, 100);
            stream = effect.paste(is2, 20, buffer);
            
            //is.close();
            //is2.close();
            long tempo2=System.currentTimeMillis();
            System.out.println("time elapsed (ms): " + (tempo2 - tempo1));
            
            is = getClass().getResourceAsStream(fileName);
            Analysis.average_bitrate(is);
            
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
        
//        file.saveMedia(stream, "audio/");
//        System.out.println("FILE SAVED");
    }
    
    public void pauseApp() {
    }
    
    public void destroyApp(boolean unconditional) {
    }
}
