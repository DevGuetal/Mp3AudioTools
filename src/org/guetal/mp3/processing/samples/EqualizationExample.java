/*
 * EqualizationExample.java
 *
 * Created on 21 maggio 2007, 12.12
 */

package org.guetal.mp3.processing.samples;

import java.io.InputStream;

import org.guetal.fileManager.FileManager;
import org.guetal.mp3.processing.effects.Filtering;



/**
 *
 * @author  Administrator
 * @version
 */
public class EqualizationExample {
    //private String fileName = "/audio/spettro equispaziato.mp3";
   // private String fileName = "/audio/impulsi.mp3";
        //private String fileName = "/audio/osc.mp3";
    private String fileName = "/audio/trombe.mp3";
    //private String fileName = "/audio/U2.mp3";
    private Filtering effect;
    private byte [] stream;
    
    public void startApp() {
        InputStream is = getClass().getResourceAsStream(fileName);
        FileManager file = new FileManager();
        
        double [] filter = new double [576];
        
        for(int i = 0; i < filter.length; i++){
            if( i <= 5 )
                filter [i] = 1.0;
            else if (i >= 50)
                filter [i] = 0.8;
            else filter [i] = 0.5;
        }
        
        effect = new Filtering();
        
        try {    
            stream = effect.filter(is, 0, 3000, filter);
            is.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        file.saveMedia(stream, "audio/");
        
        
        
    }
    
    public void pauseApp() {
    }
    
    public void destroyApp(boolean unconditional) {
    }
}
