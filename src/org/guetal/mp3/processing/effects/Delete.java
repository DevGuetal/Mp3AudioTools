/*
 * Delete.java
 *
 * Created on 4 maggio 2007, 10.12
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.guetal.mp3.processing.effects;

import java.io.IOException;
import java.io.InputStream;

import org.guetal.mp3.processing.commons.Constants;
import org.guetal.mp3.processing.commons.Manager;
import org.guetal.mp3.processing.commons.data.FrameData;


/**
 *
 * @author Administrator
 */
public class Delete {
    private byte [] mp3_data;
    private byte [] mp3_byte_stream;
    private static InputStream is;

    
    private byte [] data_def;
    
    /** Creates a new instance of Delete */
    public Delete() {
    }
    
    public byte[] delete(InputStream is, int fStart, int fEnd) throws Exception{
         
        int cont = 0;
        
        Manager reader = new Manager(is);
        if( fStart > fEnd ){
            throw new Exception("fStart can't be greater than fEnd!!");
        }
        
        int len_buffer = fEnd - fStart + 1;
        int n_frame = cont - len_buffer;
                
        FrameData fd ;
        
        final int opt = Constants.HUFFMAN_DOMAIN;
        do{
            System.out.println("---------------------------------------------(Step 1 - Iteration number: " + cont + ")---------------------------------------------");
            
            try{
                fd = reader.decodeFrame(opt);
            } catch (Exception e){
                System.out.println("End of file");
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