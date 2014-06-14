/*
 * Delete.java
 *
 * Created on 4 maggio 2007, 10.12
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.guetal.mp3.processing.effects;

import java.io.InputStream;
import java.util.logging.Logger;

import org.guetal.mp3.processing.checker.Checker;
import org.guetal.mp3.processing.commons.Constants;
import org.guetal.mp3.processing.commons.Manager;
import org.guetal.mp3.processing.commons.data.FrameData;


/**
 *
 * @author Administrator
 */
public class Pack {
    private byte [] mp3_byte_stream = new byte[10000000];

    private final static Logger LOGGER = Logger.getLogger(Checker.class.getName()); 
    
    /** Creates a new instance of Delete */
    public Pack() {
    }
    
    public byte[] pack(InputStream is) {
                
        Manager reader = new Manager(is);

        
        FrameData fd ;
        final int opt = Constants.HUFFMAN_DOMAIN;
        do{
            try{
                fd = reader.decodeFrame(opt);

            } catch (Exception e){
                LOGGER.info("End of file");
                break;
            }
            
            // effects
            reader.storeData(fd, reader.getMainData());
        } while(true);
        
        
        mp3_byte_stream = reader.getStream();
        return mp3_byte_stream;
    }
}