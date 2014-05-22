
package org.guetal.mp3.processing.encoder;

/**
 *  Class for rapresentation of granule information
 *  @author Thomas Gualino
 */ 

public final class EChannel {
    
    protected int part2_3_length;                   // length of main_data field in the frame
    protected int big_values;                       // size of bigvalues in main_data
    protected int global_gain;                      // quantization step size
    protected int scalefac_compress;                // number of bits used for the transmission of scalefactors
    protected int window_switching_flag;            // determines if block is SHORT or LONG type
    protected int block_type;                       // deremines the type of short blocks
    protected int mixed_block_flag;                 // indicates that different types of windows are used in the lower and higher frequencies
    protected int [] table_select = new int[3];     // tables for decoding bigvalues
    protected int [] subblock_gain = new int[3];    // indicates the gain offset from global_gain for each short block
    protected int region0_count;                    // contains one less than the number of scalefactor bands in region0
    protected int region1_count;                    // contains one less than the number of scalefactor bands in region1
    protected int preflag;                          // additional high frequency amplification
    protected int scalefac_scale;                   // determines the step for logaritmic quantization of scalefactors
    protected int count1table_select;               // determines which table (A or B) is used in coding count1
    
    protected int count1;                           // number of frequecy lines of count1 
    protected int address1;                         // region1 of bigvalues
    protected int address2;                         // region2 of bigvalues
    protected int address3;                         // region3 of bigvalues
    protected int part2_length;                     // length of scalefactors
    
    /**
     * Creates a new instance of EChannel
     */
    public EChannel() {
        
    }
    
    public void setBigValues(int big_values){
        this.big_values = big_values;
    }

    void setCount1(int count1) {
        this.count1 = count1;
    }
    
}
