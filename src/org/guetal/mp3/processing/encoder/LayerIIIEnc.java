/*
 * LayerIIIEnc.java
 *
 * Created on 23 novembre 2006, 14.46
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.guetal.mp3.processing.encoder;

import org.guetal.mp3.processing.commons.CommonMethods;
import org.guetal.mp3.processing.commons.data.FrameData;
import org.guetal.mp3.processing.commons.data.FrameDataQuantized;


//import java.io.DataOutputStream;


/**
 *
 * @author Tommy
 */
public class LayerIIIEnc {
    private final int SIZE;
    private Mp3Frame [] mpf;    //  circular buffer of SIZE length
    
    private int index;
    private int version;
    private int frame_num;
    private final int MAX_RES_SIZE = 511;
    private final int MAX_N_BACK = 9;
    
    
    /**
     * Creates a new instance of LayerIIIEnc. Buffer size can be set
     */
    public LayerIIIEnc(int size) {
        index = -1;
        //main_data_beg = 0;
        version = 1;
        frame_num = 0;
        this.SIZE = size;
        mpf = new Mp3Frame [SIZE];
    }
    
    /**
     * Creates a new instance of LayerIIIEnc. Buffer size is the default size
     */
    public LayerIIIEnc() {
        index = -1;
        //main_data_beg = 0;
        version = 1;
        frame_num = -1;
        this.SIZE = 10;
        mpf = new Mp3Frame [SIZE];
    }
    
    
    
    /**
     * returns byte array containing frame's data (header + side info + frame_data + bit reservoir).
     */
    public byte [] get_frame_data(int n){
        
        byte [] frame_info   = mpf[n].get_info();
        byte [] frame_data   = mpf[n].get_main_data();
        byte [] anc_data    = mpf[n].get_anc_data();
        byte [] reserve     = mpf[n].get_reserve();
        int     len         = mpf[n].get_framesize();
        byte [] frame       = new byte [len];
        
        int end = frame_info.length;
        int start = 0;
        System.arraycopy(frame_info, 0, frame, start, end);
        
        if( frame_data != null ){
            start = end;
            end += frame_data.length;
            System.arraycopy(frame_data, 0, frame, start, frame_data.length);
            
        }
        
        if( anc_data!=null ){
            start = end;
            end += anc_data.length;
            
            System.arraycopy(anc_data, 0, frame, start, anc_data.length);
        }
        
        if(reserve!=null){
            start = end;
            end += reserve.length;
            
            System.arraycopy(reserve, 0, frame, start, reserve.length);
        }
        
        return frame;
    }
    
    public void set_side_info( FrameData fd ){
        mpf[index].set_side_info(fd);
    }
    
    
    
    public byte[] encode_main_data(FrameDataQuantized fd){
        int mean_bits = (fd.getNSlots() * 8) / fd.getMaxGr() ;
        SideInfoEnc side_info = mpf[index].getSideInfo();
        
        HuffmanEnc huf = mpf[index].get_huffman_encoder();
        
        byte [] main_data = huf.iteration_loop(side_info, fd, mean_bits);
        
        return main_data;
    }
    
    
    
    public void set_index(int index){
        if(index > SIZE - 1)
            index = 0;
        
        this.index = index;
    }
    
    
    public int get_index(){
        return index;
    }
    
    
    /**
     * Stores data in mp3 frames in a circular way. Due to the presence of bit reservoir, this method finds free space in previous frames.
     *  This method is able to store more than two different frame's main data in a single frame.
     *
     *
     * @param data   data of current frame
     */
    public int store_data(byte [] data, FrameData fd){
        int free_space      = 0;
        int size            = 0;
        int n_step_back     = 0;
        int offset          = 0;
        int main_data_beg   = 0;
        
        int main_data_len = fd.getMdLen();
        int i = frame_num > 0? index - 1: 0;
        if( i < 0 ) i = SIZE - 1;

        do{
            if( mpf[i].get_reserve_size() > 0 ){
                
                n_step_back ++;
                free_space += mpf[i].get_reserve_size();
                i--;
                if( i < 0 ) i = SIZE - 1;
                if((frame_num - i) < 0) break;
            } else break;
            
        } while( free_space <= MAX_RES_SIZE );
        int anc_space = 0;
        
        if( free_space > 511 ){
            anc_space = free_space - 511;
            free_space = 511;
        }
        
        /* control of bitrate*/
        if(main_data_len - free_space > mpf[index].get_nSlots()){
            int bitrate_index = fd.getBitrateIndex();
            int freq_index    = fd.getFsIndex();
            int version_index = 1;
            
            int bitrate = CommonMethods.bitrates[version_index][freq_index][bitrate_index + 1];
            fd.setBitrate( bitrate_index + 1, bitrate);
            fd.updateFrameLen();
            mpf[index].setBitrate(bitrate, fd.getFramesize(), fd.getNSlots());
            
            System.out.println("bitrate modified at frame " + frame_num + ": " + bitrate);
            return 0;
        }
        
        /* storing in the reserve */
        int start = (index - n_step_back) > 0 ? index - n_step_back: 0;
        
        if( n_step_back > frame_num ) n_step_back = frame_num; // 8/6/2007
        
        if(anc_space > 0){
            i = (index- n_step_back);
            if (i < 0)
                i = SIZE + i;
            
            byte anc_data [] = new byte[anc_space];
            mpf[i].set_reserve(anc_data, 0);
            anc_space = 0;
        }
        
        
        for( int n = n_step_back; n > 0; n-- ){
            i = (index - n);
            if (i < 0)
                i = SIZE + i;
            
            int len = (mpf[i].get_reserve_size() - anc_space) > (main_data_len - offset) ?
                main_data_len - offset: mpf[i].get_reserve_size()-anc_space;
            
            if(len > 0){
                byte [] reserve = new byte[len];
                System.arraycopy(data, offset, reserve, 0, len);
                
                mpf[i].set_reserve(reserve, anc_space);
                offset += len;
            };
            anc_space = 0;
        }
        
        if(offset > 511) offset = 511;
        
        mpf[index].set_main_data_beg(free_space);
        
        /* storing main_data in current frame and
         creating new reserve for current frame  */
        if(main_data_len > offset){
            byte [] main_data = new byte [main_data_len - offset];
            System.arraycopy(data, offset, main_data, 0, main_data_len - offset);
            mpf[index].set_main_data(main_data);
            mpf[index].create_reserve(main_data.length);
        } else mpf[index].create_reserve(0);
        
        return 1;
    }
    
    
    /** Create new frame
     *
     *  @param  version         mpeg version (1 or 2)
     *  @param  bitrate         mp3 bitrate (fps)
     *  @param  mode            string containg mode: "stereo", "joint_stereo", "dual_channel", "single_channel"
     *  @param intensity_stereo boolean indicating if intensity stereo is used
     *  @param  MS_stereo       boolean indicatin if MS_stereo is used
     *  @param emphasis         idicate the type of emphasis is used. Can assume the following values: 0 => no emphasis; 1 => 50/15 microsec. emphasis; 3 => CCITT J.17
     */
    public void init_frame(FrameData fd) {
        index++;
        frame_num++;
        
        if(index > SIZE - 1)
            index = 0;

        mpf[index] = new Mp3Frame(fd);
    }
}
