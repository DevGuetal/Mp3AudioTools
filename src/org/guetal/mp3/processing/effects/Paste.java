/*
 * Paste.java
 *
 * Created on 9 maggio 2007, 10.12
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.guetal.mp3.processing.effects;

import java.io.IOException;
import java.io.InputStream;

import org.guetal.mp3.processing.commons.Constants;
import org.guetal.mp3.processing.commons.Manager;
import org.guetal.mp3.processing.commons.data.FrameBuffer;
import org.guetal.mp3.processing.commons.data.FrameData;
import org.guetal.mp3.processing.commons.data.FrameDataUnpacked;


/**
 *
 * @author Administrator
 */
public class Paste {
    private byte [] mp3_byte_stream;
    byte [] temp;
    private static InputStream is;
    
    private int tot;
    
    private byte [] data_def;
    
    private Manager reader;
    
    /** Creates a new instance of Delete */
    public Paste() {
    }
    
    public byte[] paste(InputStream is, int fStart, FrameBuffer frame_buffer ) throws Exception{
        
        if( frame_buffer.get_buffer_len() == 0){
            throw new Exception("Error: buffer is empty!!");
        }
        
        int fEnd = fStart + frame_buffer.get_buffer_len() - 1;
        int cont = 0;
        
        reader = new Manager(is);
        FrameData fd;
        
        int position = 0;
        
        final int opt = Constants.HUFFMAN_DOMAIN;
        FrameDataUnpacked fd_u = null;
        do{
            System.out.println("---------------------------------------------(Step 2 - Iteration number: " + cont + ")---------------------------------------------");
            
            if (cont < fStart || cont >= fEnd ) {
                try{
                    fd = reader.decodeFrame(opt);
                } catch (Exception e){
                    System.out.println("End of file");
                    break;
                }
                reader.storeData(fd, reader.getMainData());
            } else{
                fd_u = frame_buffer.get_entry();
                reader.storeData(fd_u, fd_u.get_data());
            }
            
            cont++;
        } while(true);
        
        mp3_byte_stream = reader.getStream();
        
        return mp3_byte_stream;
    }
}

