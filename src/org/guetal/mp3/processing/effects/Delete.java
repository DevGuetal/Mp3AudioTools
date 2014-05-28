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
public class Delete {
	
	private final static Logger LOGGER = Logger.getLogger(Checker.class.getName()); 
	
    private byte [] mp3_byte_stream;
    
    /** Creates a new instance of Delete */
    public Delete() {
    }
    
    public byte[] delete(InputStream is, int fStart, int fEnd) throws Exception{
         
        int cont = 0;
        
        Manager reader = new Manager(is);
        if( fStart > fEnd ){
            throw new Exception("fStart can't be greater than fEnd!!");
        }
    
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
            if (cont < fStart || cont > fEnd){
                reader.storeData(fd, reader.getMainData());
            } 
            
            cont++;
        }
        while(true);
        
        mp3_byte_stream = reader.getStream();
        return mp3_byte_stream;
    }
}