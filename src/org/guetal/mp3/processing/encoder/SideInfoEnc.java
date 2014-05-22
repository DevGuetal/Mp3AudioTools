package org.guetal.mp3.processing.encoder;

/**
 *  The following class generates an array containing frame's side info.
 *
 *  @return byte array containing side info
 *  @author Thomas Gualino
 */

public class SideInfoEnc {
    private int main_data_beg;
    private int max_gr;
    private int channels;
        
    private int num_bytes;
    public EGranule[] gr;
    
    private int [][] GR_info_array;
    
    private FormatStream format;
    public int [][] scfsi;// = new int[4];   
    
    /**
     * Creates a new instance of SideInfoEnc
     *  
     * @param channels  number of channels         
     */
    public SideInfoEnc( final int channels, final int granule) {
        this.main_data_beg = 0;
        this.channels = channels;
        this.max_gr = granule;
        this.num_bytes = ( channels == 1 )? 17: 32;
        
        scfsi = new int [channels][4];
        format = new FormatStream(num_bytes);
        gr = new EGranule [granule];
        for(int i = 0; i < granule; i++)
            gr[i] = new EGranule(channels);
    }
    
    
    /**
     * Set begin of main data in frame
     *  
     * @param main_data_beg    begin of data expressed in bytes         
     */
    public void set_main_data_beg(int main_data_beg){
        this.main_data_beg = main_data_beg;
    }
    
    
    /** Getter for main_data_begin */
    public int get_main_data_beg(){
        return main_data_beg;
    }
    
    
    
    /**
     * Stores side_info in array
     *  
     * @return   byte array containing side_info         
     */
    public byte[] format_side_info(){
        store_md_begin();
        store_private_bits();
        store_scfsi();
        store_granule_info();
     
        
        ////////////
//        System.out.println("/////////////////////////////// frame in scrittura ////////////////////////////////////////////////////");
//        System.out.println("side_info_enc -> main_data_beg: "+ this.main_data_beg );
//        for(int ch = 0; ch < channels; ch++)
//            for(int ch = 0; ch < 2; ch++){
//            System.out.println("");
//            System.out.println("side_info_enc -> ch["+ch+"].ch["+ch+"].window_switching_flag: "+ this.ch[ch].ch[ch].window_switching_flag );
//            System.out.println("side_info_enc -> ch["+ch+"].ch["+ch+"].big_values: "+ this.ch[ch].ch[ch].big_values );
//            System.out.println("side_info_enc -> ch["+ch+"].ch["+ch+"]count1table_select: "+ this.ch[ch].ch[ch].count1table_select );
//            System.out.println("side_info_enc -> ch["+ch+"].ch["+ch+"].global_gain: "+ this.ch[ch].ch[ch].global_gain );
//            System.out.println("side_info_enc -> ch["+ch+"].ch["+ch+"].part2_3_length: "+ this.ch[ch].ch[ch].part2_3_length );
//            System.out.println("side_info_enc -> ch["+ch+"].ch["+ch+"].part2_length: "+ this.ch[ch].ch[ch].part2_length );
//            System.out.println("side_info_enc -> ch["+ch+"].ch["+ch+"].preflag: "+ this.ch[ch].ch[ch].preflag );
//            System.out.println("side_info_enc -> ch["+ch+"].ch["+ch+"].region0_count: "+ this.ch[ch].ch[ch].region0_count );
//            System.out.println("side_info_enc -> ch["+ch+"].ch["+ch+"].region1_count: "+ this.ch[ch].ch[ch].region1_count );
//            System.out.println("side_info_enc -> ch["+ch+"].ch["+ch+"].scalefac_compress: "+ this.ch[ch].ch[ch].scalefac_compress );
//            System.out.println("side_info_enc -> ch["+ch+"].ch["+ch+"].scalefac_scale: "+ this.ch[ch].ch[ch].scalefac_scale );
//            System.out.println("side_info_enc -> ch["+ch+"].ch["+ch+"].table_select[0: "+ this.ch[ch].ch[ch].table_select[0] );
//            System.out.println("side_info_enc -> ch["+ch+"].ch["+ch+"].table_select[1: "+ this.ch[ch].ch[ch].table_select[1] );
//            System.out.println("side_info_enc -> ch["+ch+"].ch["+ch+"].table_select[2: "+ this.ch[ch].ch[ch].table_select[2] );
//            System.out.println("side_info_enc -> ch["+ch+"].ch["+ch+"].block type: "+ this.ch[ch].ch[ch].block_type );
//            
//            System.out.println("side_info_enc -> ch["+ch+"].ch["+ch+"].mixed_block_flag: "+ this.ch[ch].ch[ch].mixed_block_flag );
//            System.out.println("side_info_enc -> ch["+ch+"].ch["+ch+"].scalefac_scale: "+ this.ch[ch].ch[ch].scalefac_scale );
//            System.out.println("side_info_enc -> ch["+ch+"].ch["+ch+"][0].subblock_gain: "+ this.ch[ch].ch[ch].subblock_gain[0] );
//            System.out.println("side_info_enc -> ch["+ch+"].ch["+ch+"][1].subblock_gain: "+ this.ch[ch].ch[ch].subblock_gain[1] );
//            System.out.println("side_info_enc -> ch["+ch+"].ch["+ch+"][2].subblock_gain: "+ this.ch[ch].ch[ch].subblock_gain[2] );
//            }
        return format.get_array();
    }
    
    
    public void set_scfsi(int [] scfsi, int ch){
        this.scfsi[ch][0] = scfsi[0];
        this.scfsi[ch][1] = scfsi[1];
        this.scfsi[ch][2] = scfsi[2];
        this.scfsi[ch][3] = scfsi[3];
    }
    
    /** window_switching_flag = 1 */
    public void set_granule_info(int ch, int gr, int big_values,
                                 int count1_table_select, int global_gain, int part2_3_length,
                                 int part2_length, int preflag, int block_type,
                                 int scalefact_compress, int [] table_select,
                                 int mixed_block_flag, int scalefac_scale, int [] subblock_gain)
    {
        EChannel gi = this.gr[gr].ch[ch];
        
        gi.window_switching_flag = 1;
        gi.big_values = big_values;
        gi.count1table_select = count1_table_select;
        gi.global_gain = global_gain;
        gi.part2_3_length = part2_3_length;
        gi.part2_length = part2_length;
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
                                 int part2_length, int preflag, int region0_count, int region1_count,
                                 int scalefact_compress, int [] table_select, int block_type,
                                 int scalefac_scale)
    {
        EChannel gi = this.gr[gr].ch[ch];
        gi.window_switching_flag = 0;
        gi.big_values = big_values;
        gi.count1table_select = count1_table_select;
        gi.global_gain = global_gain;
        gi.part2_3_length = part2_3_length;
        gi.part2_length = part2_length;
        gi.preflag = preflag;
        gi.region0_count = region0_count;
        gi.region1_count = region1_count;
        gi.scalefac_compress = scalefact_compress;
        gi.scalefac_scale = scalefac_scale;
        gi.table_select[0] = table_select[0];
        gi.table_select[1] = table_select[1];
        gi.table_select[2] = table_select[2];
        
    }
        
    /** stores main data begin in side info */
    private void store_md_begin(){
        format.store_in_array(main_data_beg, 9);
    }
    
    /** stores private bits in side info */
    private void store_private_bits(){
        if(channels == 1)
            format.store_in_array(0, 5);
        else
            format.store_in_array(0, 3);
    }
    
    
    /** stores scfsi bits in side info */
    private void store_scfsi(){
        for (int ch = 0; ch < channels; ch++) {
            format.store_in_array(scfsi[ch][0], 1);
            format.store_in_array(scfsi[ch][1], 1);
            format.store_in_array(scfsi[ch][2], 1);
            format.store_in_array(scfsi[ch][3], 1);
        } 
    }
    
    
    /** store info for each frame granule */
    private void store_granule_info(){
//        
//        for(int ch = 0; ch < max_gr; ch++){
//            for(int gr1 = 0; gr1 < channels; gr1++){
//                format.store_in_array(gr[gr1].ch[ch].part2_3_length, 12);
//                format.store_in_array(gr[gr1].ch[ch].big_values, 9);
//                format.store_in_array(gr[gr1].ch[ch].global_gain, 8);
//                format.store_in_array(gr[gr1].ch[ch].scalefac_compress, 4);
//                format.store_in_array(gr[gr1].ch[ch].window_switching_flag, 1);
//                
//                if(gr[gr1].ch[ch].window_switching_flag == 1){
//                    format.store_in_array(gr[gr1].ch[ch].block_type, 2);
//                    format.store_in_array(gr[gr1].ch[ch].mixed_block_flag, 1);
//                    format.store_in_array(gr[gr1].ch[ch].table_select[0], 5);
//                    format.store_in_array(gr[gr1].ch[ch].table_select[1], 5);
//                    format.store_in_array(gr[gr1].ch[ch].subblock_gain[0], 3);
//                    format.store_in_array(gr[gr1].ch[ch].subblock_gain[1], 3);
//                    format.store_in_array(gr[gr1].ch[ch].subblock_gain[2], 3);
//                    format.store_in_array(gr[gr1].ch[ch].preflag, 1);
//                    format.store_in_array(gr[gr1].ch[ch].scalefac_scale, 1);
//                    format.store_in_array(gr[gr1].ch[ch].count1table_select, 1);
//                } else {
//                    format.store_in_array(gr[gr1].ch[ch].table_select[0], 5);
//                    format.store_in_array(gr[gr1].ch[ch].table_select[1], 5);
//                    format.store_in_array(gr[gr1].ch[ch].table_select[2], 5);
//                    format.store_in_array(gr[gr1].ch[ch].region0_count, 4);
//                    format.store_in_array(gr[gr1].ch[ch].region1_count, 3);
//                    format.store_in_array(gr[gr1].ch[ch].preflag, 1);
//                    format.store_in_array(gr[gr1].ch[ch].scalefac_scale, 1);
//                    format.store_in_array(gr[gr1].ch[ch].count1table_select, 1);
//                }
//            }
//        }
        
        
        for(int granule = 0; granule < max_gr; granule++){
            for(int ch = 0; ch < channels; ch++){
                EChannel t = gr[granule].ch[ch];
                format.store_in_array(t.part2_3_length, 12);
                format.store_in_array(t.big_values, 9);
                format.store_in_array(t.global_gain, 8);
                format.store_in_array(t.scalefac_compress, 4);
                format.store_in_array(t.window_switching_flag, 1);
                
                if(t.window_switching_flag == 1){
                    format.store_in_array(t.block_type, 2);
                    format.store_in_array(t.mixed_block_flag, 1);
                    format.store_in_array(t.table_select[0], 5);
                    format.store_in_array(t.table_select[1], 5);
                    format.store_in_array(t.subblock_gain[0], 3);
                    format.store_in_array(t.subblock_gain[1], 3);
                    format.store_in_array(t.subblock_gain[2], 3);
                    format.store_in_array(t.preflag, 1);
                    format.store_in_array(t.scalefac_scale, 1);
                    format.store_in_array(t.count1table_select, 1);
                } else {
                    format.store_in_array(t.table_select[0], 5);
                    format.store_in_array(t.table_select[1], 5);
                    format.store_in_array(t.table_select[2], 5);
                    format.store_in_array(t.region0_count, 4);
                    format.store_in_array(t.region1_count, 3);
                    format.store_in_array(t.preflag, 1);
                    format.store_in_array(t.scalefac_scale, 1);
                    format.store_in_array(t.count1table_select, 1);
                }
            }
        }
    }
    
    
    
}
