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
public class Tremolo extends VolumeControl {
    
    /** Creates a new instance of Tremolo */
    public Tremolo() {
    }
    
    public byte[] tremolo(InputStream is, int gain, int fStart, int fEnd, int period) throws Exception{
        if( fStart > fEnd ){
            throw new Exception("fStart can't be greater than fEnd!!");
        }
        
        super.stream = new byte [is.available()];
        double fr = 1.0 / period;
        int offset = 0;
        
        int [] env = new int [period];
        for (int i = 0; i < period; i ++){
            env[i] =  (int)(gain * Math.sin(2 * Math.PI * i * fr)) - gain;
            System.out.println("env["+i+"]: "+ env[i] );
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
                int i = cont % period;
                modify_volume_frame(env[i], offset);
            }
            
            offset += framesize;
            cont ++;
        }
        
        return stream;
    }
    
    public byte[] tremolo(InputStream is, int gain, int fStart, int period) throws Exception{
        
        super.stream = new byte [is.available()];
        double fr = 1.0 / period;
        int offset = 0;
        
        int [] env = new int [period];
        for (int i = 0; i < period; i ++){
            env[i] =  (int)(gain * Math.sin(2 * Math.PI * i * fr)) - gain;
            System.out.println("env["+i+"]: "+ env[i] );
        }
        
        is.read(stream);
        
        offset = super.sync_header(stream);
        
        
        while (offset < stream.length - 31)       {
            int bitrate = (stream [2 + offset] & 0xf0) >>> 4;
            int f_index = (stream [2 + offset] & 0xc ) >>> 2;
            int padding = (stream [2 + offset] & 0x2 ) >>> 1;
            
            int frequency = CommonMethods.frequencies[1][f_index];
            int framesize = CommonMethods.calFrameSize(bitrate, padding, frequency);
            
            if( cont > fStart ){
                int i = cont % period;
                modify_volume_frame(env[i], offset);
            }
            
            offset += framesize;
            cont ++;
        }
        
        return stream;
    }
    
    
    public byte[] tremolo(InputStream is, int gain, int period) throws Exception{
        
        super.stream = new byte [is.available()];
        double fr = 1.0 / period;
        int offset = 0;
        
        int [] env = new int [period];
        for (int i = 0; i < period; i ++){
            env[i] =  (int)(gain * Math.sin(2 * Math.PI * i * fr)) - gain;
            System.out.println("env["+i+"]: "+ env[i] );
        }
        
        is.read(stream);
        
        offset = super.sync_header(stream);
        
        
        while (offset < stream.length - 31)       {
            int bitrate = (stream [2 + offset] & 0xf0) >>> 4;
            int f_index = (stream [2 + offset] & 0xc ) >>> 2;
            int padding = (stream [2 + offset] & 0x2 ) >>> 1;
            
            int frequency = CommonMethods.frequencies[1][f_index];
            int framesize = CommonMethods.calFrameSize(bitrate, padding, frequency);
            
            
            int i = cont % period;
            modify_volume_frame(env[i], offset);
            
            
            offset += framesize;
            cont ++;
        }
        
        return stream;
    }
    
    
    
}
