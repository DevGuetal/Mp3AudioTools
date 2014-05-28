/*
 * Paste.java
 *
 * Created on 9 maggio 2007, 10.12
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.guetal.mp3.processing.effects;

import java.io.InputStream;
import java.util.logging.Logger;

import org.guetal.mp3.processing.commons.Constants;
import org.guetal.mp3.processing.commons.Manager;
import org.guetal.mp3.processing.commons.data.FrameBuffer;
import org.guetal.mp3.processing.commons.data.FrameData;


/**
 *
 * @author Administrator
 */
public class Copy {
	
	private final static Logger LOGGER = Logger.getLogger(Copy.class.getName()); 
	
    private Manager reader;
    
    //private FrameDataUnpacked[] frame_buffer;
    
    /** Creates a new instance of Delete */
    public Copy() {
    }
    
    public FrameBuffer copy(InputStream is, int fStart, int fEnd) throws Exception{
        
        
        int cont = 0;
        
        if( fStart > fEnd ){
            throw new Exception("fStart can't be greater than fEnd!!");
        }
        
        reader = new Manager(is);

        FrameBuffer buffer = new FrameBuffer();
        
        FrameData fd ;
        
        final int opt = Constants.HUFFMAN_DOMAIN;
        do{

            if(fEnd > cont){
                try{
                    fd = reader.decodeFrame(opt);
                } catch (NegativeArraySizeException e){
                    if ( fStart <= cont && cont <= fEnd)
                    	LOGGER.warning("WARNING: end of file reached before finishing to copy!");
                    break;
                }
                // effects
                if (fStart <= cont && cont <= fEnd){
                    buffer.add_entry(reader.getMainData(), fd);
                }
                
            } else break;
            cont++;
        } while(true);
        
        return buffer;
    }
    
    public FrameBuffer copy(InputStream is, int fStart) throws Exception{
        
        
        int count = 0;
        
        reader = new Manager(is);
        
        FrameBuffer buffer = new FrameBuffer();
        
        FrameData fd ;
        
        final int opt = Constants.HUFFMAN_DOMAIN;
        do{
            try{
                fd = reader.decodeFrame(opt);
            } catch (Exception e){
                break;
            }
            // effects
            if (fStart <= count){
                buffer.add_entry(reader.getMainData(), fd);
            }
            
            count++;
        } while(true);
        
        return buffer;
    }
    
    
    
    
}

