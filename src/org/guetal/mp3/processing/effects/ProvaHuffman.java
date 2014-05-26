/*
 * ProvaHuffman.java
 *
 * Created on 3 gennaio 2007, 17.05
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.guetal.mp3.processing.effects;

import java.io.InputStream;

import org.guetal.mp3.processing.commons.Constants;
import org.guetal.mp3.processing.commons.Manager;
import org.guetal.mp3.processing.commons.data.FrameDataQuantized;
import org.guetal.mp3.processing.decoder.BitStream;
import org.guetal.mp3.processing.decoder.Header;
import org.guetal.mp3.processing.decoder.LayerIIIDecoder;
import org.guetal.mp3.processing.encoder.LayerIIIEnc;

/**
 *
 * @author Tommy
 */
public class ProvaHuffman {
    private BitStream bitstream;
    private InputStream is, real_stream;
    //private BitReserveBackup br;
    private Header header;
    // private SideInfo sideinfo;
    private LayerIIIDecoder decoder;
    private LayerIIIEnc encoder;
    
    
    private byte [] data_def;
    private byte [] mp3_data;
    private byte [] stream;
    private final int N_FRAME = 100;
    
    int cont = 0;
    int tot = 0;
    
    // private byte[] stream;
    
    /**
     * Creates a new instance of ProvaHuffman
     */
    public ProvaHuffman(String fileName){
        int bytes = 0;
        int position = 0;
        int n_frames = 0;
        is = getClass().getResourceAsStream(fileName);
        Manager reader = new Manager(is);
        final int opt = Constants.QUANTIZED_DOMAIN;
        do{
            FrameDataQuantized fd = (FrameDataQuantized) reader.decodeFrame(opt);
            System.out.println("---------------------------------------------(Step 1 - Iteration number: " + cont + ")---------------------------------------------");
            
            if(fd.getFramesize() > 1){
                reader.encodeFrame(fd);
                //System.out.println(fd.getFramesize());
//                int[] scfsi = fd.getScfsi(0);
//                for(int gr = 0; gr < 2; gr++){
//                    for(int ch = 0; ch < 2 ; ch ++){
//                        if(fd.getBlockType(ch, gr) != 2){
//                            fd.scalefac0L
//                        }
//                    }
//                }
                
//                for(int i = 0; i<fd.getScfsi(0).length; i++)
//                    if(scfsi[i]!= 0)
//                    System.out.println(scfsi[i]);
                
//                scfsi = fd.getScfsi(1);
//                for(int i = 0; i<fd.getScfsi(1).length; i++)
//                    if(scfsi[i]!= 0)
//                        System.out.println(scfsi[i]);
                //bytes += fd.getFramesize();
                n_frames ++;
                
            } else break;
            
            cont++;
        }
        //while(cont < 100);
          while(true);
        //   System.out.println("n_frames: " + n_frames);
        stream = reader.getStream();
        
    }
    
//    public byte[] provaHuffman(String fileName) throws Exception {
//        int bytes = 0;
//        int position = 0;
//
//        Manager reader = new Manager(is);
//        is = getClass().getResourceAsStream(fileName);
//
//        do{
//            FrameData fd = reader.decode_frame(Commons.QUANTIZED_DOMAIN);
//            System.out.println("\n\n---------------------------------------------(Step 1 - Filtering - Iteration number: " + cont + ")---------------------------------------------");
//
//            if(fd.getFramesize() > 1){
//                byte [] data = reader.encode_frame(fd);
//
//                bytes += fd.getFramesize();
//
//            } else break;
//
//            cont++;
//        }
//        while(cont < 15);
//        // while(true);
//
//        stream = reader.getStream(bytes - 1);
//        return stream;
//    }
    
    public byte [] get_stream(){
        return stream;
    }
    
    
    
    
}
