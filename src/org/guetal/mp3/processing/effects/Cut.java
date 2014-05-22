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
public class Cut {
    private byte [] stream;
    private static InputStream is;
    private FrameBuffer frame_buffer;
    
    private int tot;
    
    private byte [] data_def;
    
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
        
        int len_buffer = fEnd - fStart + 1;
        int n_frame = cont - len_buffer;
        frame_buffer = new FrameBuffer();
        
        FrameData fd ;
        final int option = Constants.HUFFMAN_DOMAIN;
        
        do{
            System.out.println("---------------------------------------------(Step 1 - Iteration number: " + cont + ")---------------------------------------------");
            
            try{
                fd = reader.decode_frame(option);
            } catch (Exception e){
                System.out.println("End of file");
                break;
            }
            
            // effects
            if (cont < fStart || fEnd < cont){
                reader.store_data(fd, reader.getMainData());
            } else {
                frame_buffer.add_entry(reader.getMainData(), fd);
            }
            
            cont++;
        }
        while(true);
        
        stream = reader.get_stream();
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

