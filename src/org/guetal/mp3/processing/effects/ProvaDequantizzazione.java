/*
 * Huffman.java
 *
 * Created on 3 gennaio 2007, 17.05
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.guetal.mp3.processing.effects;

import java.io.InputStream;

import org.guetal.mp3.processing.commons.Constants;
import org.guetal.mp3.processing.commons.data.FrameData;
import org.guetal.mp3.processing.decoder.BitStream;
import org.guetal.mp3.processing.decoder.Header;
import org.guetal.mp3.processing.decoder.LayerIIIDecoder;
import org.guetal.mp3.processing.encoder.LayerIIIEnc;

/**
 *
 * @author Tommy
 */
public class ProvaDequantizzazione {
    private BitStream bitstream;
    private InputStream is, real_stream;
    //private BitReserveBackup br;
    private Header header;
    // private SideInfo sideinfo;
    private LayerIIIDecoder decoder;
    private LayerIIIEnc encoder;
    
    
    private byte [] data_def;
    private byte [] mp3_data;
    private byte [] mp3_byte_stream;
    private final int N_FRAME = 1700;
    
    int cont = 0;
    int tot = 0;
    
    // private byte[] stream;
    
    /** Creates a new instance of Huffman */
    public ProvaDequantizzazione(String fileName) throws Exception {
        encoder = new LayerIIIEnc(N_FRAME);
        is = getClass().getResourceAsStream(fileName);
        bitstream = new BitStream(is);
        
        real_stream = getClass().getResourceAsStream(fileName);
        //byte [] b = new byte [100000];
        //real_stream.read(b,0,100000);
        
        header =  bitstream.readFrame();
        decoder = new LayerIIIDecoder(bitstream, header);
        
        mp3_byte_stream = new byte[418 * N_FRAME];
        int position = 0;
        System.out.print("\n\nE3 = [");
        while(cont <  N_FRAME){
            
            //    System.out.println("\n\n---------------------------------------------(Step 1 - Iteration number: " + cont + ")---------------------------------------------");
            
            if(header.getFramesize() > 0){
                decoder.decodeFrame(Constants.DEQUANTIZED_DOMAIN);
                
                float ro [][][] = decoder.get_ro();
                FrameData fd = decoder.getFrameInfo();
                
                float [][] energy = {{0.0f, 0.0f}, {0.0f, 0.0f}};
                
                for(int gr = 0; gr < 2; gr++){
                    for(int ch = 0; ch < 2; ch++){
                        energy[gr][ch] = 0.0f;
                        // System.out.println("\n**************************** ch: "+ch+" gr: "+gr+" *************************************");
                        if(fd.getBlockType(ch, gr) != 2){
                            for(int b = 0; b < ro[gr][ch].length; b++)
                                energy[gr][ch] += (float)((ro[gr][ch][b]) * (ro[gr][ch][b]));
                            
                            energy[gr][ch] /= 576;
                            
                        }
                        
                        
                      /*  if(fd.getBlockType(ch, gr) == 2){
                            float energy1 = 0, energy2 = 0, energy3 = 0;
                            int sb = 0;
                            for(sb = 0; sb < 192; sb++){
                                energy1 += (float)((ro[ch][gr][sb]) * (ro[ch][gr][sb]));
                            }
                       
                            for(; sb < 384; sb++){
                                energy2 += (float)((ro[ch][gr][sb]) * (ro[ch][gr][sb]));
                            }
                       
                            for(; sb < 576; sb++){
                                energy3 += (float)((ro[ch][gr][sb]) * (ro[ch][gr][sb]));
                            }
                       
                            energy[ch][gr] = energy1 + energy2 +energy3;
                            energy[ch][gr] /= 192;
                        }*/
                        
                        //System.out.println("block flag: " + fd.getMixedBlockFlag(ch, gr));
                        //System.out.println("subblock gain: " + fd.getSubblockGain(ch, gr));
                        
                    }
                    
                }
                // System.out.println("MS stereo: " + fd.getMS_stereo());
                //float E = energy[0][0] + energy[0][1] + energy[1][0] + energy[1][1];
                float E = energy[0][1] +  energy[1][1];
                System.out.print(" " + E);
          /*      System.out.println("energy 0,0: " + energy[0][0]);
                System.out.println("energy 1,0: " + energy[1][0]);
                System.out.println("energy 0,,: " + energy[0][1]);
                System.out.println("energy 1,1: " + energy[1][1]);
               System.out.println("en totale: " + E);    */
            }
            
            header = bitstream.readFrame();
            cont++;
            
        }
        
        System.out.print("];\n");
        
        
        
        
        // if(N_FRAME <= 10) Checker.compareStream(mp3_byte_stream, b);
        
/*            for(int i = 0; i < mp3_byte_stream.length; i++) {
 
            System.out.print("byte " + i + ": " + mp3_byte_stream[i]);
            System.out.print(" | " + b[i + off]);
            if(b[i+off]!= mp3_byte_stream[i])
                System.out.print("<-------------------------------------------------- DIVERSO!!!!!!!");
            if((i % 418) == 0)  System.out.print(" <-- begin of frame " + (i / 418 + 1));
            if((i % 418) == 4){
                System.out.print(" <-- main_data_beg: ");
                int beg = (mp3_byte_stream[i] >= 0) ? ((mp3_byte_stream[i] << 1)+((mp3_byte_stream[i+1]>>> 7) & 1)): (((mp3_byte_stream[i] + 256) << 1)+((mp3_byte_stream[i+1]>>> 7) & 1));
                System.out.print(beg);
                beg = (b[i+off] >= 0) ? ((b[i+off] << 1)+((b[i+1+off]>>> 7) & 1)): (((b[i+off] + 256) << 1)+((b[off+i+1]>>> 7) & 1));
                System.out.print(" | "+beg);
            }
            if((i % 418) == 36)  System.out.print(" <-- begin of data in frame");
            System.out.print("\n");
            }*/
        
    }
    
//    private void store_data(){
//
//        FrameData fd = decoder.get_frame_info();
//
//        header.create_main_info(encoder, decoder.get_channels());
//        //decoder.copy_data(fd);
//        byte [] data = encoder.encode_main_data(fd);
//
//        Checker c = new Checker();
//        // c.print_info(fd_u);
//        //encoder.encode_main_data(ix, global_gain, scalefact_compress, scfsi, preflag, scalefact_scale,
//        //      window_switching_flag, block_type, mixed_block_flag, subblock_gain);
//
//
////System.out.println("huffman -> ritorno encode_main_data");
//    }
    
    
    public byte [] get_stream(){
        return mp3_byte_stream;
    }
    
    
    
    
}
