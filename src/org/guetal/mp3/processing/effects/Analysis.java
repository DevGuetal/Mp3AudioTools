/*
 * Analysis.java
 *
 * Created on 21 giugno 2007, 18.01
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.guetal.mp3.processing.effects;

import java.io.InputStream;

import org.guetal.mp3.processing.commons.CommonMethods;

/**
 *
 * @author Administrator
 */
public final class Analysis {
    
    /**
     * Creates a new instance of Analysis
     */
    public Analysis() {
    }
    
    public static double calc_duration(InputStream is) throws Exception{
        byte [] stream = new byte[is.available()];
        
        is.read(stream);
        
        return calc_duration(stream);
    }
    
    public static double calc_duration(byte [] stream) throws Exception {
        int offset = 0;
        
        
        
        offset = sync_header(stream);
        
        int cont = 0;
        int frequency = 0, framesize = 0, padding = 0, f_index = 0, bitrate = 0;
        double duration = 0;
        while (offset < stream.length - 31)       {
            bitrate = (stream [2 + offset] & 0xf0) >>> 4;
            f_index = (stream [2 + offset] & 0xc ) >>> 2;
            padding = (stream [2 + offset] & 0x2 ) >>> 1;
            
            frequency = CommonMethods.frequencies[1][f_index];
            framesize = CommonMethods.calFrameSize(bitrate, padding, frequency);
            
            offset += framesize;
            
            duration += (1152f / frequency);
        }
        
        return duration;
    }
    
    public static double cont_frames(InputStream is) throws Exception{
        byte [] stream = new byte[is.available()];
        
        is.read(stream);
        
        return calc_duration(stream);
    }
    
    public static int cont_frames(byte stream []) throws Exception {
        int offset = 0;
        
        offset = sync_header(stream);
        
        int cont = 0;
        int frequency = 0, framesize = 0, padding = 0, f_index = 0, bitrate = 0;
        
        while (offset < stream.length - 31)       {
            bitrate = (stream [2 + offset] & 0xf0) >>> 4;
            f_index = (stream [2 + offset] & 0xc ) >>> 2;
            padding = (stream [2 + offset] & 0x2 ) >>> 1;
            
            frequency = CommonMethods.frequencies[1][f_index];
            framesize = CommonMethods.calFrameSize(bitrate, padding, frequency);
            
            offset += framesize;
            cont ++;
        }
        
        return cont;
    }
    
    public static double average_bitrate(InputStream is) throws Exception {
        byte [] stream = new byte[is.available()];
        
        is.read(stream);
        
        return average_bitrate(stream);
    }
    
    public static double average_bitrate(byte stream []) throws Exception {
        int offset = 0;
        
        offset = sync_header(stream);
        
        int cont = 0;
        int frequency = 0, framesize = 0, padding = 0, f_index = 0, bitrate = 0;
        double avg_bitrate = 0;
        
        while (offset < stream.length - 31)       {
            bitrate = (stream [2 + offset] & 0xf0) >>> 4;
            f_index = (stream [2 + offset] & 0xc ) >>> 2;
            padding = (stream [2 + offset] & 0x2 ) >>> 1;
            
            frequency = CommonMethods.frequencies[1][f_index];
            framesize = CommonMethods.calFrameSize(bitrate, padding, frequency);
            
            avg_bitrate += bitrate;
            offset += framesize;
            cont ++;
        }
        
        avg_bitrate /= cont;
        
        return avg_bitrate;
    }
    
    public static int sync_header(byte [] stream){
        int offset = 0;
        
        for(; offset < stream.length; offset++)
            if(stream[offset] == -1 &&
                ((stream[offset+1] & 0xf0) >>> 4)
                == 0xf && (stream[offset+1]&0xf ) != 0xf)
                break;
        
        return offset;
    }
}
