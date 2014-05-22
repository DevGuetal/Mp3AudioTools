/*
 * FrameData.java
 *
 * Created on 24 aprile 2007, 19.59
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.guetal.mp3.processing.commons.data;

import org.guetal.mp3.processing.commons.CommonMethods;

/**
 *
 * @author Administrator
 */
public class FrameData {
    public SideInfo si;
    private static int MAXGR;
    private static int CHANNELS;
    
    private int nSlots = 0;
    private int framesize = 0;
    
    /* Header Info  */
    private int h_bitrate_index = 0;
    private int version, fs = 44100, emphasis, h_mode, h_mode_extension;
    private String mode;
    private int bitrate;
    private int h_protection_bit;
    private int h_padding_bit;
    
    
    public void setVersion(int version) {
        this.version = version;
    }
    
    
    
    /** Creates a new instance of FrameData */
    public FrameData(final int channels, final int max_gr) {
        si = new SideInfo(max_gr, channels);
        CHANNELS = channels;
        MAXGR = max_gr;
    }
    
    
    public int get_fs_index(){
        switch (fs){
            case 48000: return 4;
            case 32000: return 5;
        }
        
        return 3;
    }
    
    public void clone(FrameData fd){
        fd.setInfo(version, bitrate, fs, mode, h_mode_extension, emphasis, CHANNELS, h_mode, h_protection_bit, h_padding_bit);
        fd.setBitrateIndex(h_bitrate_index);
        
        SideInfo si_u = fd.getSi();
        
        si_u.setMainDataBegin(si.getMainDataBegin());
        
        
        for( int gr = 0; gr < MAXGR; gr++ ){
            for (int ch = 0; ch < CHANNELS; ch++ ){
                Channel t = si.gr[gr].ch[ch];
                
                if( t.window_switching_flag != 0)
                    fd.set_granule_info(ch, gr, t.big_values, t.count1table_select, t.global_gain, t.part2_3_length,
                            t.preflag, t.block_type, t.scalefac_compress, t.table_select, t.mixed_block_flag,
                            t.scalefac_scale, t.subblock_gain);
                else
                    fd.set_granule_info(ch, gr, t.big_values, t.count1table_select, t.global_gain, t.part2_3_length, t.preflag,
                            t.region0_count, t.region1_count, t.scalefac_compress, t.table_select, t.block_type,
                            t.scalefac_scale);
            }
        }
        
    }
    
    
    public int [] getScfsi(int ch){
        return this.si.scfsi[ch];
    }
    
    public SideInfo getSi(){
        return this.si;
    }
    
    public Channel getGrInfo(int ch, int gr){
        return si.gr[gr].ch[ch];
    }
    
    public void setInfo(int version, int bitrate, int fs, String mode, int h_mode_extension, int emphasis, int channels, int h_mode, int h_protection_bit, int h_padding_bit) {
        this.version = version;
        this.bitrate = bitrate;
        this.fs = fs;
        this.mode = mode;
        this.h_mode_extension = h_mode_extension;
        this.emphasis = emphasis;
        this.CHANNELS = CHANNELS;
        this.h_mode = h_mode;
        this.h_protection_bit = h_protection_bit;
        this.h_padding_bit = h_padding_bit;
        
        updateFrameLen();
    }
    
    
    public int getPart23Length(int ch, int gr){
        return si.gr[gr].ch[ch].part2_3_length;
    }
    
    public int getBlockType(int ch, int gr){
        return si.gr[gr].ch[ch].block_type;
    }
    
    public int getHPaddingBit(){
        return h_padding_bit;
    }
    
    /** window_switching_flag = 1 */
    public void set_granule_info(int ch, int gr, int big_values,
            int count1_table_select, int global_gain, int part2_3_length,
            int preflag, int block_type,
            int scalefact_compress, int [] table_select,
            int mixed_block_flag, int scalefac_scale, int [] subblock_gain) {
        Channel gi = si.gr[gr].ch[ch];
        
        gi.window_switching_flag = 1;
        gi.big_values = big_values;
        gi.count1table_select = count1_table_select;
        gi.global_gain = global_gain;
        gi.part2_3_length = part2_3_length;
        //System.out.println("frame_data -> setgranule info -> part2_3_length: " + gi.part2_3_length);
        gi.preflag = preflag;
        gi.scalefac_compress = scalefact_compress;
        gi.block_type = block_type;
        gi.mixed_block_flag = mixed_block_flag;
        gi.scalefac_scale = scalefac_scale;
        gi.table_select[0] = table_select[0];
        gi.table_select[1] = table_select[1];
        gi.subblock_gain[0] = subblock_gain[0];
        gi.subblock_gain[1] = subblock_gain[1];
        gi.subblock_gain[2] = subblock_gain[2];
        
        
    }
    
    
    /** window_switching_flag = 0*/
    public void set_granule_info(int ch, int gr, int big_values,
            int count1_table_select, int global_gain, int part2_3_length,
            int preflag, int region0_count, int region1_count,
            int scalefact_compress, int [] table_select, int block_type,
            int scalefac_scale) {
        Channel gi = si.gr[gr].ch[ch];
        gi.window_switching_flag = 0;
        gi.big_values = big_values;
        gi.count1table_select = count1_table_select;
        gi.global_gain = global_gain;
        gi.part2_3_length = part2_3_length;
        //System.out.println("frame_data -> setgranule info -> part2_3_length: " + gi.part2_3_length);
        gi.preflag = preflag;
        gi.region0_count = region0_count;
        gi.region1_count = region1_count;
        gi.scalefac_compress = scalefact_compress;
        gi.scalefac_scale = scalefac_scale;
        gi.table_select[0] = table_select[0];
        gi.table_select[1] = table_select[1];
        gi.table_select[2] = table_select[2];
        
    }
    
    
    public void printheaderInfo(){
        
    }
    
    public void printSideInfo(){
        
        System.out.println("side_info_enc -> main_data_beg: "+ si.getMainDataBegin());
        for(int gr = 0; gr < MAXGR; gr++)
            for(int ch = 0; ch < CHANNELS; ch++) {
            Channel t = this.si.gr[gr].ch[ch];
            System.out.println("");
            System.out.println("side_info_enc -> ch["+ch+"].gr["+gr+"].window_switching_flag: "+ t.window_switching_flag );
            System.out.println("side_info_enc -> ch["+ch+"].gr["+gr+"].big_values: "+ t.big_values );
            System.out.println("side_info_enc -> ch["+ch+"].gr["+gr+"]count1table_select: "+ t.count1table_select );
            System.out.println("side_info_enc -> ch["+ch+"].gr["+gr+"].global_gain: "+ t.global_gain );
            System.out.println("side_info_enc -> ch["+ch+"].gr["+gr+"].part2_3_length: "+ t.part2_3_length );
            //System.out.println("side_info_enc -> gr["+gr+"].ch["+ch+"].part2_length: "+ t.part2_length );
            System.out.println("side_info_enc -> ch["+ch+"].gr["+gr+"].preflag: "+ t.preflag );
            System.out.println("side_info_enc -> ch["+ch+"].gr["+gr+"].region0_count: "+ t.region0_count );
            System.out.println("side_info_enc -> ch["+ch+"].gr["+gr+"].region1_count: "+ t.region1_count );
            System.out.println("side_info_enc -> ch["+ch+"].gr["+gr+"].scalefac_compress: "+ t.scalefac_compress );
            System.out.println("side_info_enc -> ch["+ch+"].gr["+gr+"].scalefac_scale: "+ t.scalefac_scale );
            System.out.println("side_info_enc -> ch["+ch+"].gr["+gr+"].table_select[0: "+ t.table_select[0] );
            System.out.println("side_info_enc -> ch["+ch+"].gr["+gr+"].table_select[1: "+ t.table_select[1] );
            System.out.println("side_info_enc -> ch["+ch+"].gr["+gr+"].table_select[2: "+ t.table_select[2] );
            System.out.println("side_info_enc -> ch["+ch+"].gr["+gr+"].block type: "+ t.block_type );
            
            System.out.println("side_info_enc -> ch["+ch+"].gr["+gr+"].mixed_block_flag: "+ t.mixed_block_flag );
            System.out.println("side_info_enc -> ch["+ch+"].gr["+gr+"].scalefac_scale: "+ t.scalefac_scale );
            System.out.println("side_info_enc -> ch["+ch+"].gr["+gr+"][0].subblock_gain: "+ t.subblock_gain[0] );
            System.out.println("side_info_enc -> ch["+ch+"].gr["+gr+"][1].subblock_gain: "+ t.subblock_gain[1] );
            System.out.println("side_info_enc -> ch["+ch+"].gr["+gr+"][2].subblock_gain: "+ t.subblock_gain[2] );
            
            }
    }
    
    
    public void updateFrameLen(){
        // CommonMethods c = new CommonMethods();
        framesize = CommonMethods.calFrameSize(h_bitrate_index, h_padding_bit, fs);
        //System.out.println("framesize: " + framesize);
        nSlots = CommonMethods.calNSlots(framesize, h_mode, 1);
    }
    
    
    public void setBitrate(int bitrate_index, int bitrate){
        this.h_bitrate_index = bitrate_index;
        this.bitrate = bitrate;
    }
    
    public int getFramesize() {
        return framesize;
    }
    
    public int getNSlots() {
        return nSlots;
    }
    
    
    public void set_nSlots(int nSlots){
        this.nSlots = nSlots;
    }
    
    public int get_md_len(){
        int len = 0;
        for( int gr = 0; gr < MAXGR; gr++ )
            for (int ch = 0; ch < CHANNELS; ch++) {
            len += si.gr[gr].ch[ch].get_part2_3_length();
            }
        
        if (len % 8 != 0)
            len = len / 8 + 1;
        else len /= 8;
        return len;
    }
    
    public int get_main_data_begin(){
        return si.main_data_begin;
    }
    
    
    public int getBitrateIndex() {
        return this.h_bitrate_index;
    }
    
    public void setBitrateIndex(int h_bitrate_index) {
        this.h_bitrate_index = h_bitrate_index;
        updateFrameLen();
    }
    
    public int getBitrate() {
        return this.bitrate;
    }
    
    public String getMode() {
        return this.mode;
    }
    
//    public boolean getIntensityStereo() {
//        return this.intensity_stereo;
//    }
    
    public int getEmphasis() {
        return this.emphasis;
    }
    
//    public boolean getMS_stereo() {
//        return this.MS_stereo;
//    }
    
    public int getHModeExtension(){
        return this.h_mode_extension;
    }
    
    public int getChannels() {
        return this.CHANNELS;
    }
    
    public int getFs() {
        return fs;
    }
    
    public int getMaxGr() {
        return this.MAXGR;
    }
    
    public int getWindowSwitchingFlag(int ch, int gr) {
        return this.si.gr[gr].ch[ch].window_switching_flag;
    }
    
    public int getBigValues(int ch, int gr) {
        return this.si.gr[gr].ch[ch].big_values;
    }
    
    public int[] getSubblockGain(int ch, int gr) {
        return this.si.gr[gr].ch[ch].subblock_gain;
    }
    
    public int getMixedBlockFlag(int ch, int gr) {
        return this.si.gr[gr].ch[ch].mixed_block_flag;
    }
    
    public int[] getTableSelect(int ch, int gr) {
        return this.si.gr[gr].ch[ch].table_select;
    }
    
    public int getScalefacScale(int ch, int gr) {
        return this.si.gr[gr].ch[ch].scalefac_scale;
    }
    
    public int getScalefactCompress(int ch, int gr) {
        return this.si.gr[gr].ch[ch].scalefac_compress;
    }
    
    public int getRegion1Count(int ch, int gr) {
        return this.si.gr[gr].ch[ch].region1_count;
    }
    
    public int getRegion0Count(int ch, int gr) {
        return this.si.gr[gr].ch[ch].region0_count;
    }
    
    public int getPreflag(int ch, int gr) {
        return this.si.gr[gr].ch[ch].preflag;
    }
    
    public int getGlobalGain(int ch, int gr) {
        return this.si.gr[gr].ch[ch].global_gain;
    }
    
    public int getCount1TableSelect(int ch, int gr) {
        return this.si.gr[gr].ch[ch].count1table_select;
    }
    
    public void setGlobalGain(int ch, int gr, int gain) {
        Channel gi = si.gr[gr].ch[ch];
        gi.global_gain = gain;
    }
 
    public int getHmode(){
        return h_mode;
    }
}
