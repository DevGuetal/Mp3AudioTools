/*
 * VolumeControl.java
 *
 * Created on 17 giugno 2007, 17.49
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
public class VolumeControl {
    int cont = 0;
    byte [] stream;
    /** Creates a new instance of VolumeControl */
    public VolumeControl(){
    }
    
    public byte[] volume_control(InputStream is, int gain, int fStart, int fEnd) throws Exception {
        int offset = 0;
        
        if( fStart > fEnd ){
            throw new Exception("fStart can't be greater than fEnd!!");
        }
        
        stream = new byte[is.available()];
        
        is.read(stream);
        
//        for(int i = 0; i < 20; i++)
//            System.out.println(i+") "+ stream[i]);
        offset = sync_header(stream);
        
        
        
        while (offset < stream.length - 31)       {
            int bitrate = (stream [2 + offset] & 0xf0) >>> 4;
            int f_index = (stream [2 + offset] & 0xc ) >>> 2;
            int padding = (stream [2 + offset] & 0x2 ) >>> 1;
            
            int frequency = CommonMethods.frequencies[1][f_index];
            int framesize = CommonMethods.calFrameSize(bitrate, padding, frequency);
            
            if( cont > fStart && cont < fEnd)
                modify_volume_frame(gain, offset);
            
            
            offset += framesize;
            cont ++;
        }
        
        return stream;
    }
    
    public byte[] volume_control(InputStream is, int gain, int fStart) throws Exception {
        int offset = 0;
        
        
        stream = new byte[is.available()];
        
        is.read(stream);
        
//        for(int i = 0; i < 20; i++)
//            System.out.println(i+") "+ stream[i]);
        offset = sync_header(stream);
        
        
        
        while (offset < stream.length - 31)       {
            int bitrate = (stream [2 + offset] & 0xf0) >>> 4;
            int f_index = (stream [2 + offset] & 0xc ) >>> 2;
            int padding = (stream [2 + offset] & 0x2 ) >>> 1;
            
            int frequency = CommonMethods.frequencies[1][f_index];
            int framesize = CommonMethods.calFrameSize(bitrate, padding, frequency);
            
            if( cont > fStart )
                modify_volume_frame(gain, offset);
            
            
            offset += framesize;
            cont ++;
        }
        
        return stream;
    }
    
    
    
    public byte[] volume_control(InputStream is, int gain) throws Exception {
        int offset = 0;
        
        
        stream = new byte[is.available()];
        
        is.read(stream);
        
//        for(int i = 0; i < 20; i++)
//            System.out.println(i+") "+ stream[i]);
        offset = sync_header(stream);
        
        
        
        while (offset < stream.length - 31)       {
            int bitrate = (stream [2 + offset] & 0xf0) >>> 4;
            int f_index = (stream [2 + offset] & 0xc ) >>> 2;
            int padding = (stream [2 + offset] & 0x2 ) >>> 1;
            
            int frequency = CommonMethods.frequencies[1][f_index];
            int framesize = CommonMethods.calFrameSize(bitrate, padding, frequency);
            
            
            modify_volume_frame(gain, offset);
            
            
            offset += framesize;
            cont ++;
        }
        
        return stream;
    }
    
    
    protected void modify_volume_frame(int gain, int offset){
        int [] global_gain = new int [4];
        global_gain[0] = ((stream [9 + offset]  & 0x7f) << 1 )+ ((stream [10 + offset] & 0x80) >>> 7 );
        global_gain[1] = ((stream [16 + offset] & 0x0f) << 4 )+ ((stream [17 + offset] & 0xf0) >>> 4 );
        global_gain[2] = ((stream [23 + offset] & 0x01) << 7 )+ ((stream [24 + offset] & 0xfe) >>> 1 );
        global_gain[3] = ((stream [31 + offset] & 0x3f) << 2 )+ ((stream [32 + offset] & 0xc0) >>> 6 );
        
        
        
//            System.out.println("globalgain: " + global_gain[0]);
//            System.out.println("globalgain: " + global_gain[1]);
//            System.out.println("globalgain: " + global_gain[2]);
//            System.out.println("globalgain: " + global_gain[3]);
        
        int new_gain = calc_gain(global_gain[0], gain);
        
        
        int part1 = new_gain >>> 1;
        int part2 = (new_gain & 0x1) <<  7;
        
        stream[9  + offset] = (byte) (((stream[9 + offset] & 0x80) + part1) & 0xff);
        stream[10 + offset] = (byte) (((stream[10 + offset] & 0x7f) + part2) & 0xff);
        
        
        
        new_gain = calc_gain(global_gain[1], gain);
        
        part1 = new_gain >>> 4;
        part2 = (new_gain & 0x0f) << 4;
        stream[16 + offset] = (byte) (((stream[16 + offset] & 0xf0) + part1) & 0xff);
        stream[17 + offset] = (byte) (((stream[17 + offset] & 0x0f) + part2) & 0xff);
        
        
        
        new_gain = calc_gain(global_gain[2], gain);
        
        part1 = new_gain >>> 7;
        part2 = (new_gain & 0x7f) << 1;
        stream[23 + offset] = (byte) (((stream[23 + offset] & 0xfe) + part1) & 0xff);
        stream[24 + offset] = (byte) (((stream[24 + offset] & 0x1) + part2) & 0xff);
        
        
        new_gain = calc_gain(global_gain[3], gain);
        
        part1 = new_gain >>> 2;
        part2 = (new_gain & 0x03) << 6;
        stream[31 + offset] = (byte) (((stream[31 + offset] & 0xc0) + part1) & 0xff);
        stream[32 + offset] = (byte) (((stream[32 + offset] & 0x3f) + part2) & 0xff);
        
        
        global_gain[0] = ((stream [9 + offset]  & 0x7f) << 1 )+ ((stream [10 + offset] & 0x80) >>> 7 );
        global_gain[1] = ((stream [16 + offset] & 0x0f) << 4 )+ ((stream [17 + offset] & 0xf0) >>> 4 );
        global_gain[2] = ((stream [23 + offset] & 0x01) << 7 )+ ((stream [24 + offset] & 0xfe) >>> 1 );
        global_gain[3] = ((stream [31 + offset] & 0x3f) << 2 )+ ((stream [32 + offset] & 0xc0) >>> 6 );
    }
    
    
    protected void modify_volume_frame(double factor, int offset){
        int [] global_gain = new int [4];
        global_gain[0] = ((stream [9 + offset]  & 0x7f) << 1 )+ ((stream [10 + offset] & 0x80) >>> 7 );
        global_gain[1] = ((stream [16 + offset] & 0x0f) << 4 )+ ((stream [17 + offset] & 0xf0) >>> 4 );
        global_gain[2] = ((stream [23 + offset] & 0x01) << 7 )+ ((stream [24 + offset] & 0xfe) >>> 1 );
        global_gain[3] = ((stream [31 + offset] & 0x3f) << 2 )+ ((stream [32 + offset] & 0xc0) >>> 6 );
        
        
        
//            System.out.println("globalgain: " + global_gain[0]);
//            System.out.println("globalgain: " + global_gain[1]);
//            System.out.println("globalgain: " + global_gain[2]);
//            System.out.println("globalgain: " + global_gain[3]);
        
        int new_gain = calc_gain(global_gain[0], factor);
        System.out.println("new_gain: " + new_gain);
        
        int part1 = new_gain >>> 1;
        int part2 = (new_gain & 0x1) <<  7;
        
        stream[9  + offset] = (byte) (((stream[9 + offset] & 0x80) + part1) & 0xff);
        stream[10 + offset] = (byte) (((stream[10 + offset] & 0x7f) + part2) & 0xff);
        
        
        
        new_gain = calc_gain(global_gain[1], factor);
        System.out.println("new_gain: " + new_gain);
        part1 = new_gain >>> 4;
        part2 = (new_gain & 0x0f) << 4;
        stream[16 + offset] = (byte) (((stream[16 + offset] & 0xf0) + part1) & 0xff);
        stream[17 + offset] = (byte) (((stream[17 + offset] & 0x0f) + part2) & 0xff);
        
        
        
        new_gain = calc_gain(global_gain[2], factor);
        System.out.println("new_gain: " + new_gain);
        part1 = new_gain >>> 7;
        part2 = (new_gain & 0x7f) << 1;
        stream[23 + offset] = (byte) (((stream[23 + offset] & 0xfe) + part1) & 0xff);
        stream[24 + offset] = (byte) (((stream[24 + offset] & 0x1) + part2) & 0xff);
        
        
        new_gain = calc_gain(global_gain[3], factor);
        System.out.println("new_gain: " + new_gain);
        part1 = new_gain >>> 2;
        part2 = (new_gain & 0x03) << 6;
        stream[31 + offset] = (byte) (((stream[31 + offset] & 0xc0) + part1) & 0xff);
        stream[32 + offset] = (byte) (((stream[32 + offset] & 0x3f) + part2) & 0xff);
        
        
        global_gain[0] = ((stream [9 + offset]  & 0x7f) << 1 )+ ((stream [10 + offset] & 0x80) >>> 7 );
        global_gain[1] = ((stream [16 + offset] & 0x0f) << 4 )+ ((stream [17 + offset] & 0xf0) >>> 4 );
        global_gain[2] = ((stream [23 + offset] & 0x01) << 7 )+ ((stream [24 + offset] & 0xfe) >>> 1 );
        global_gain[3] = ((stream [31 + offset] & 0x3f) << 2 )+ ((stream [32 + offset] & 0xc0) >>> 6 );
    }
    
    
    private int calc_gain(int global_gain, int gain){
        int new_gain = global_gain + gain;
        if(new_gain < 0){
            System.out.println("gain too low at frame " + cont);
            System.out.println("volume increased by " + (-new_gain * 1.5) +" db" );
            new_gain = 0;
        }
        if(new_gain > 255)  {
            System.out.println("gain too high at frame " + cont);
            System.out.println("volume reduced by " + (new_gain * 1.5) +" db" );
            new_gain = 255;
        }
        
        return new_gain;
    }
    
    private int calc_gain(int global_gain, double factor){
        int new_gain = (int)Math.floor(global_gain * factor);
        if(new_gain < 0){
            System.out.println("gain too low at frame " + cont);
            System.out.println("volume increased by " + (-new_gain * 1.5) +" db" );
            new_gain = 0;
        }
        if(new_gain > 255)  {
            System.out.println("gain too high at frame " + cont);
            System.out.println("volume reduced by " + (new_gain * 1.5) +" db" );
            new_gain = 255;
        }
        
        return new_gain;
    }
    
    protected int sync_header(byte [] stream){
        int offset = 0;
        
        for(; offset < stream.length; offset++)
            if(stream[offset] == -1 &&
                ((stream[offset+1] & 0xf0) >>> 4)
                == 0xf && (stream[offset+1]&0xf ) != 0xf)
                break;
        
        return offset;
    }
}
