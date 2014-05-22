/*
 * Mp3Frame.java
 *
 * Created on 27 novembre 2006, 15.42
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.guetal.mp3.processing.encoder;

import org.guetal.mp3.processing.commons.CommonMethods;
import org.guetal.mp3.processing.commons.data.Channel;
import org.guetal.mp3.processing.commons.data.FrameData;


/**
 *
 * @author Tommy
 */
public class Mp3Frame {
    private HeaderEnc header;
    private SideInfoEnc side_info;
    private byte [] anc_data;
    
    private byte [] main_data;
    private byte [] header_string;
    private byte [] side_info_string;
    private int position_data;
    
    private byte [] reserve;
    private int res_size = 0;
    
    private int framesize;
    private int nSlots;
    
    private int main_data_beg;
    private int channels;
    
    private int max_gr;
    
    MainDataEnc md;
    HuffmanEnc huf;

    private int h_mode;
    
    
    /** Creates a new instance of Mp3Frame */
  /*  public Mp3Frame(int version, int bitrate, int fs, String mode, boolean intensity_stereo,
            boolean MS_stereo, int emphasis, int channels, int h_padding_bit, int h_protection_bit) {
   
        header = new HeaderEnc(version, bitrate, fs, mode, intensity_stereo, MS_stereo, emphasis);
        side_info = new SideInfoEnc(channels);
   
        this.channels = channels;
        framesize = CommonMethods.calFrameSize(version, fs, h_padding_bit);
        nSlots = CommonMethods.calNSlots(framesize, channels, 0);
    }*/
    
    /** Creates a new instance of Mp3Frame */
    public Mp3Frame(FrameData fd) {
        
        int fs          = fd.getFs();
        int padding     = fd.getHPaddingBit();
        this.channels   = fd.getChannels();
        this.max_gr     = fd.getMaxGr();
        header          = new HeaderEnc(1, fd.getBitrate(), fs, fd.getMode(), fd.getHModeExtension(), fd.getEmphasis(), padding);
        side_info       = new SideInfoEnc(channels, max_gr);
        this.h_mode     = fd.getHmode();

        framesize = CommonMethods.calFrameSize(fd.getBitrateIndex(), padding,fs);

        nSlots = CommonMethods.calNSlots(framesize, h_mode, 1);

        md = new MainDataEnc(nSlots);
        huf = new HuffmanEnc(md);
    }
    
    
    public void set_side_info(FrameData fd){
        for(int ch = 0; ch < channels; ch++)
            side_info.set_scfsi(fd.si.scfsi[ch], ch);
        
        for(int gr = 0; gr < max_gr; gr++)
            for(int ch = 0; ch < channels; ch++){
            Channel gi = fd.si.gr[gr].ch[ch];
            if(gi.window_switching_flag != 0)
                side_info.set_granule_info(ch, gr, fd.getBigValues(ch, gr),
                        fd.getCount1TableSelect(ch, gr), fd.getGlobalGain(ch, gr),
                        fd.getPart23Length(ch, gr), 0, fd.getPreflag(ch, gr),
                        fd.getBlockType(ch, gr), fd.getScalefactCompress(ch, gr),
                        fd.getTableSelect(ch, gr), fd.getMixedBlockFlag(ch, gr),
                        fd.getScalefacScale(ch, gr), fd.getSubblockGain(ch, gr));
            
            else side_info.set_granule_info(ch, gr, gi.big_values,
                    gi.count1table_select, gi.global_gain, gi.part2_3_length,
                    0, gi.preflag, gi.region0_count, gi.region1_count,
                    gi.scalefac_compress, gi.table_select, gi.block_type,
                    gi.scalefac_scale);
            }
    }
    
    
    
    
    public int get_main_data_len(){
        int ans = 0;
        
        for(int gr = 0; gr < max_gr; gr ++)
            for(int ch = 0; ch < channels; ch ++) {
            ans += side_info.gr[gr].ch[ch].part2_3_length;
            }
        ans = ((ans % 8) > 0)? (ans/8 + 1): (ans / 8);
        
        return ans;
    }
    
    
    
    public void create_reserve(int used_space){
        res_size = (nSlots - used_space);
        reserve = new byte [res_size];
    }
    
    
    public void set_reserve(byte [] data, int offset){
        
        int start = reserve.length - res_size;// + offset;
        int end = offset + start;
        
        for ( int i = start; i < end; i++)
            reserve[i] = 77;                    //filling ancillary space with ones.
        
        start = end;
        
        System.arraycopy(data, 0, reserve, start, data.length);
        
        res_size -= (data.length + offset);
    }
    
    
    /** Set data and creates reserve*/
    public void set_main_data(byte [] data){
        this.main_data = data;
    }
    
    
    public byte[] get_main_data(){
        return this.main_data;
    }
    
    public HuffmanEnc get_huffman_encoder(){
        return this.huf;
    }
    
    
    public byte[] get_anc_data(){
        return anc_data;
    }
    
    
    public void set_main_data_beg(int main_data_beg){
        this.main_data_beg = main_data_beg;
        side_info.set_main_data_beg(main_data_beg);
    }
    
    
    public int get_main_data_beg(){
        return main_data_beg;
    }
    
    /** Getter for frame information (header + side info) */
    public byte[] get_info(){
        
        header_string = header.format_header();
        side_info_string = side_info.format_side_info();
        
        byte [] temp = new byte[header_string.length + side_info_string.length];
        
        int i;
        
        /*for (i = 0; i < header_string.length; i++)
            temp[i] = header_string[i];*/
        
        System.arraycopy(header_string, 0, temp, 0, header_string.length);
        //int off = i;
        
        /*for (i = 0; i < side_info_string.length; i++)
            temp[i + off] = side_info_string[i];*/
        System.arraycopy(side_info_string, 0, temp, header_string.length, side_info_string.length);
        
        return temp;
    }
    
    
    public int get_nSlots(){
        return nSlots;
    }
    
    
 /*   private void calFrameSize(int version, int bitrate, int fs, int padding_bit, int channels) {
        //if(version == 1) framesize = (144 * bitrate) / fs;
        framesize = CommonMethods.calFrameSize(version, fs, padding_bit);
        if(((144 * bitrate) % fs) != 0) framesize++;
        if(version == 1) nSlots = framesize - 4 - ((channels == 1) ? 17 : 32);
    }*/
    
    
    void setBitrate(int bitrate, int framsize, int nSlots) {
        this.framesize = framsize;
        this.nSlots = nSlots;
        header.setBitRate(bitrate);
    }
    
    
    public SideInfoEnc getSideInfo() {
        return this.side_info;
    }
    
    
    public int get_reserve_size(){
        return res_size;
    }
    
    
    public byte [] get_reserve(){
        return reserve;
    }
    
    
    public int get_framesize(){
        return framesize;
    }
    
    public void set_bitrate(int bitrate){
        header.setBitRate(bitrate);
        //  calFrameSize(1, bitrate, fs, channels);
    }
}
