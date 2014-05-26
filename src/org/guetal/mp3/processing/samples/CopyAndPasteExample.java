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
    private String fileName = "/audio/U2.mp3";
    private Paste effect;
    private Copy copy;
    private byte [] stream;
    
    public void startApp() {
        InputStream is = getClass().getResourceAsStream(fileName);
        InputStream is2 = getClass().getResourceAsStream(fileName);
        
        copy = new Copy();
        effect = new Paste();
        
        
        try {
            long time1=System.currentTimeMillis();
            
            FrameBuffer buffer  = copy.copy(is, 0, 100);
            stream = effect.paste(is2, 20, buffer);
            
            long time2=System.currentTimeMillis();
            System.out.println("time elapsed (ms): " + (time2 - time1));
            
            is = getClass().getResourceAsStream(fileName);
            Analysis.average_bitrate(is);
            
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public void pauseApp() {
    }
    
    public void destroyApp(boolean unconditional) {
    }
}
