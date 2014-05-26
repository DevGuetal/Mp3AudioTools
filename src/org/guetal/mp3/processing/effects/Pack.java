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
public class Pack {
    private byte [] mp3_data;
    private byte [] mp3_byte_stream = new byte[10000000];
    private static InputStream is;
    
    
    private byte [] data_def;
    
    /** Creates a new instance of Delete */
    public Pack() {
    }
    
    public byte[] pack(InputStream is) {
        
        int cont = 0;
        
        Manager reader = new Manager(is);

        
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
            reader.storeData(fd, reader.getMainData());
                        
            cont++;
        } while(true);
        
        
        mp3_byte_stream = reader.getStream();
        return mp3_byte_stream;
    }
}