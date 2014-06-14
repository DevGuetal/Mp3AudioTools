/*
 * VolumeExample.java
 *
 * Created on 17 giugno 2007, 17.48
 */

package org.guetal.mp3.processing.samples;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.logging.Logger;

import org.guetal.fileManager.FileManager;
import org.guetal.mp3.processing.effects.Tremolo;



/**
 *
 * @author  Administrator
 * @version
 */
public class TremoloExample {
	
	private final static Logger LOGGER = Logger.getLogger(TremoloExample.class.getName()); 
	
    private Tremolo effect;
    private String fileName = System.getProperty("user.dir") + "/input/song.mp3";

    public void startApp() throws FileNotFoundException {
        byte [] stream;
        
        File inputFile = new File(fileName);
        InputStream is = new FileInputStream(inputFile);
        
        effect = new Tremolo();
        
        try {
            stream = effect.tremolo(is, 6, 50);
            FileManager file = new FileManager();
            file.saveMedia(stream, "audio/");
            LOGGER.info("FILE SAVED");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
    }
    
    public void pauseApp() {
    }
    
    public void destroyApp(boolean unconditional) {
    }
}
