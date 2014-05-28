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
public class Cut {
	
	private final static Logger LOGGER = Logger.getLogger(Cut.class.getName()); 
	
    private byte [] stream;
    private FrameBuffer frame_buffer;
    
    /** Creates a new instance of Delete */
    public Cut() {
    }
    
    public FrameBuffer cut(InputStream is, int fStart, int fEnd) throws Exception{
        
        int cont = 0;
        
        Manager reader = new Manager(is);
        if( fStart > fEnd ){
            //System.err.println("ERROR!! fStart can't be greater than fEnd!!");
            throw new Exception("fStart can't be greater than fEnd!!");
        }
        
        frame_buffer = new FrameBuffer();
        
        FrameData fd ;
        final int option = Constants.HUFFMAN_DOMAIN;
        
        do{

            try{
                fd = reader.decodeFrame(option);
            } catch (Exception e){
                LOGGER.info("End of file");
                break;
            }
            
            // effects
            if (cont < fStart || fEnd < cont){
                reader.storeData(fd, reader.getMainData());
            } else {
                frame_buffer.add_entry(reader.getMainData(), fd);
            }
            
            cont++;
        }
        while(true);
        
        stream = reader.getStream();
        return frame_buffer;
    }
    
    public byte[] get_data(){
        return stream;
    }

    /*
    public FrameBuffer get_frame_buffer(){
        return this.frame_buffer;
    }*/
}

