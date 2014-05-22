/*
 * Channel.java
 *
 * Created on 24 aprile 2007, 20.24
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.guetal.mp3.processing.commons.data;

/**
 *
 * @author Administrator
 */


public class Channel {
    public int part2_3_length;
    public int big_values;
    public int global_gain;
    public int scalefac_compress;
    public int window_switching_flag;
    public int block_type;
    public int mixed_block_flag;
    public final int[] table_select = new int[3];
    public final int[] subblock_gain = new int[3];
    public int region0_count;
    public int region1_count;
    public int preflag;
    public int scalefac_scale;
    public int count1table_select;
    
    
    public int get_part2_3_length(){
        return this.part2_3_length;
    }

}


