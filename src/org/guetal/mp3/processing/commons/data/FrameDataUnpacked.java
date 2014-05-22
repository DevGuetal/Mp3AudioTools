/*
 * FrameData.java
 *
 * Created on 24 aprile 2007, 19.59
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.guetal.mp3.processing.commons.data;


/**
 *
 * @author Administrator
 */
public class FrameDataUnpacked extends FrameData {
    
    private byte [] data;
    
    /** Creates a new instance of FrameData */
    public FrameDataUnpacked(int channels, int max_gr) {
        super(channels, max_gr);
    }
    
//    public String esiste(){
//        System.out.println("io esisto!!");
//        return "io esisto!!";
//    }
    
    
    public void set_data(byte [] dataToCopy){
        data = new byte [ dataToCopy.length ];              // modificare con super.get_main_data_len(); dopo essersi accertati che questo non causi guai
        System.arraycopy(dataToCopy, 0, this.data, 0, dataToCopy.length);
    }
    
    public byte[] get_data(){       
        return this.data;
    }
    
//    public void printMainData(){
//        int end_gr_1 = 0, end_gr_2 = 0, end_gr_3 = 0, end_gr_4 = 0;
//
//        end_gr_1 = super.getPart23Length(0,0)/8;      
//        end_gr_2 = (int)((float)(super.getPart23Length(0,1))/8 + (float)(super.getPart23Length(0,0))/8);        
//        end_gr_3 = (int)((float)(super.getPart23Length(0,1))/8 + (float)(super.getPart23Length(0,0))/8 + 
//                            (float)(super.getPart23Length(1,0))/8);
//        
//        end_gr_4 = (int)((float)(super.getPart23Length(0,1))/8 + (float)(super.getPart23Length(0,0))/8 + 
//                            (float)(super.getPart23Length(1,0))/8 + (float)(super.getPart23Length(1,1))/8);
//
//        
//        for(int i = 0; i < super.get_md_len(); i++){
//            System.out.print("data["+i+"]: "+ data[i]);
//            
//            if (i == 0)            System.out.print(" - begin of block 1 (block_type " + super.getBlockType(0, 0) +")");
//            if (end_gr_1 - 1 == i) System.out.print(" - end of block 1 - begin of block 2 (block_type " + super.getBlockType(0, 1) +")");
//            if (end_gr_2 - 1 == i) System.out.print(" - end of block 2 - begin of block 3 (block_type " + super.getBlockType(1, 0) +")");
//            if (end_gr_3 - 1 == i) System.out.print(" - end of block 3 - begin of block 4 (block_type " + super.getBlockType(1, 1) +")");
//            if (end_gr_4 - 1 == i) System.out.print(" - end of block 4");
//            
//            System.out.print("\n");
//        }
//    }
}
