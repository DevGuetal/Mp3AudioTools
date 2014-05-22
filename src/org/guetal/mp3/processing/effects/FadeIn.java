/*
 * Tremolo.java
 *
 * Created on 17 giugno 2007, 21.24
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.guetal.mp3.processing.effects;

import java.io.IOException;
import java.io.InputStream;

import org.guetal.mp3.processing.commons.CommonMethods;

/**
 *
 * @author Administrator
 */
public class FadeIn extends VolumeControl {
    
    /** Creates a new instance of Tremolo */
    public FadeIn() {
    }
    
    public byte[] fade_in(InputStream is, int fStart, int fEnd, int delta_gain) throws Exception{
        if( fStart > fEnd ){
            throw new Exception("fStart can't be greater than fEnd!!");
        }
        
        super.stream = new byte [is.available()];
        int offset = 0;
        int len = fEnd - fStart  + 1;
        
        int [] env = new int [len];
        for (int i = 0; i < len; i ++){
            env[i] = (int) Math.floor( - delta_gain * ((double) i / len));
            System.out.println("env["+i+"]: " + env[i] );
        }
        
        is.read(stream);
        
        offset = super.sync_header(stream);
        
        
        while (offset < stream.length - 31)       {
            int bitrate = (stream [2 + offset] & 0xf0) >>> 4;
            int f_index = (stream [2 + offset] & 0xc ) >>> 2;
            int padding = (stream [2 + offset] & 0x2 ) >>> 1;
            
            int frequency = CommonMethods.frequencies[1][f_index];
            int framesize = CommonMethods.calFrameSize(bitrate, padding, frequency);
            
            if( cont > fStart && cont < fEnd){
                modify_volume_frame(env[cont - fStart], offset);
            }
            
            offset += framesize;
            cont ++;
        }
        
        return stream;
    }
    
    public byte[] fade_in(InputStream is, int fStart, int delta_gain) throws Exception{
        
        
        stream = new byte [is.available()];
        is.read(stream);
        int offset = 0;
        int fEnd = Analysis.cont_frames(stream);
        int len = fEnd - fStart  + 1;
        
        int [] env = new int [len];
        for (int i = 0; i < len; i ++){
            env[i] = (int) Math.floor( - delta_gain * ((double) i / len));
            System.out.println("env["+i+"]: " + env[i] );
        }
        if( fStart > fEnd ){
            throw new Exception("fStart can't be greater than fEnd!!");
        }
        
        
        offset = super.sync_header(stream);
        
        
        while (offset < stream.length - 31)       {
            int bitrate = (stream [2 + offset] & 0xf0) >>> 4;
            int f_index = (stream [2 + offset] & 0xc ) >>> 2;
            int padding = (stream [2 + offset] & 0x2 ) >>> 1;
            
            int frequency = CommonMethods.frequencies[1][f_index];
            int framesize = CommonMethods.calFrameSize(bitrate, padding, frequency);
            
            if( cont > fStart && cont < fEnd){
                modify_volume_frame(env[cont - fStart], offset);
            }
            
            offset += framesize;
            cont ++;
        }
        
        return stream;
    }
    
    
    
    
}
