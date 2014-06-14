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
import org.guetal.mp3.processing.commons.data.FrameDataUnpacked;


/**
 *
 * @author Administrator
 */
public class Invert {
    private byte [] mp3_byte_stream;
    byte [] temp;
    
	private final static Logger LOGGER = Logger.getLogger(Invert.class.getName()); 
    
    private Manager reader;
    
    /** Creates a new instance of Delete */
    public Invert() {
    }
    
    public byte[] invert(InputStream is, int fStart, int fEnd, FrameBuffer frame_buffer ) throws Exception{
        
        int cont = 0;
        
        reader = new Manager(is);
        FrameData fd;
                
        if( frame_buffer.get_buffer_len() == 0){
            throw new Exception("Error: buffer is empty!!");
        }

        final int opt = Constants.HUFFMAN_DOMAIN;
        frame_buffer.set_direction_reading(-1);
        
        do{

            if (cont < fStart || cont >= fEnd ) {
                try{
                    fd = reader.decodeFrame(opt);
                } catch (Exception e){
                    LOGGER.info("End of file");
                    break;
                }
                reader.storeData(fd, reader.getMainData());
            } else {
                
                FrameDataUnpacked fd_u = frame_buffer.get_entry();
                reader.storeData(fd_u, fd_u.get_data());
            }
            
            cont++;
        } while(true);
        
        mp3_byte_stream = reader.getStream();
        
        return mp3_byte_stream;
    }
}

