package org.guetal.mp3.processing.effects;
///*
// * Paste.java
// *
// * Created on 9 maggio 2007, 10.12
// *
// * To change this template, choose Tools | Template Manager
// * and open the template in the editor.
// */
//
//package processing.effects;
//
//import java.io.IOException;
//import java.io.InputStream;
//import processing.commons.data.FrameDataUnpacked;
//import processing.decoder.BitStream;
//import processing.commons.data.FrameData;
//import processing.decoder.Header;
//import processing.decoder.ByteReserve;
//import processing.decoder.LayerIIIDecoder;
//import processing.commons.data.SideInfo;
//
//import processing.encoder.LayerIIIEnc;
///**
// *
// * @author Administrator
// */
//public class TimeStretching {
//    private byte [] mp3_data;
//    private byte [] mp3_byte_stream = new byte[10000000];
//    private static InputStream is;
//    
//    private static BitStream bitstream;
//    private LayerIIIEnc encoder;
//    private ByteReserve reserve = new ByteReserve();
//    //private Cut cut;
//    
//    private Header header;
//    private SideInfo sideinfo;
//    
//    private LayerIIIDecoder decoder;
//    
//    private FrameDataUnpacked frame_buffer;
//    
//    private int tot;
//    
//    private byte [] data_def;
//    
//    /** Creates a new instance of Delete */
//    public TimeStretching() {
//    }
//    
//    public byte[] stretch(InputStream is, int factor, int f_start, int f_end ) throws IOException{
//        int cont = 0;
//        
//        bitstream = new BitStream(is);
//        header =  bitstream.readFrame();
//        
//        decoder = new LayerIIIDecoder(bitstream, header);
//        FrameData fd ;
//        encoder = new LayerIIIEnc(6500);
//        
//        do{
//            System.out.println("---------------------------------------------(Step 1 - stretching - Iteration number: " + cont + ")---------------------------------------------");
//            if (cont < f_start || cont > f_end ){
//                if(header.framesize() > 0){
//                    fd = read_data(decoder);
//                    
//                    store_data(fd);
//                    
//                } else break;
//                
//                header = bitstream.readFrame();
//                
//            }   else {
//                //System.out.println("incollo frame " + (cont - dst_f_start) + " al frame " + cont);
//                
//                fd = read_data(decoder);
//                store_in_buffer(data_def, fd);
//                store_data(fd);
//                for (int i = 0; i < factor; i++)
//                    store_from_buffer(frame_buffer);
//                header = bitstream.readFrame();
//            }
//            
//            cont++;
//        }  while (cont < 1000);
//       // while(true);
//        
//        int n_frame = cont + ( f_end - f_start) * factor;
//        //int n_frame = 1760;
//        int position = 0;
//        
//        cont = 0;
//        while (cont < n_frame){
//            System.out.println("\n\n---------------------------------------------(Step 2 - Iteration number: " + cont + ")---------------------------------------------");
//            byte[] frame_data = encoder.get_frame_data(cont);
//            
//            if(frame_data.length > 1){
//                for(int i = position; i < (frame_data.length + position); i++)
//                    mp3_byte_stream[i] = frame_data[i - position];
//                
//                position += frame_data.length;
//            }
//            
//            cont++;
//        }
//        
//        
//        return mp3_byte_stream;
//    }
//    
//    
//    public byte[] stretch(InputStream is, float factor, int f_start, int f_end ) throws IOException{
//        int cont = 0;
//        
//        bitstream = new BitStream(is);
//        header =  bitstream.readFrame();
//        
//        decoder = new LayerIIIDecoder(bitstream, header);
//        FrameData fd ;
//        encoder = new LayerIIIEnc(10000);
//        
//        do{
//            System.out.println("\n\n---------------------------------------------(Step 1 - Iteration number: " + cont + ")---------------------------------------------");
//            if (cont < f_start || cont > f_end ){
//                if(header.framesize() > 0){
//                    fd = read_data(decoder);
//                    
//                    store_data(fd);
//                    
//                } else break;
//                
//                header = bitstream.readFrame();
//                
//            }   else {
//                //System.out.println("incollo frame " + (cont - dst_f_start) + " al frame " + cont);
//                
//                fd = read_data(decoder);
//                store_in_buffer(data_def, fd);
//                store_data(fd);
//                if(cont % 5 == 0)
//                    store_from_buffer(frame_buffer);
//                header = bitstream.readFrame();
//            }
//            
//            cont++;
//        }
//           while (cont < 330);
//        //while(true);
//        
//        int n_frame = (int)(cont + ( f_end - f_start) * factor)-1;
//        n_frame = 300;
//        int position = 0;
//        
//        cont = 0;
//        while (cont < n_frame){
//            System.out.println("\n\n---------------------------------------------(Step 2 - Iteration number: " + cont + ")---------------------------------------------");
//            byte[] frame_data = encoder.get_frame_data(cont);
//            
//            if(frame_data.length > 1){
//                for(int i = position; i < (frame_data.length + position); i++)
//                    mp3_byte_stream[i] = frame_data[i - position];
//                
//                position += frame_data.length;
//            }
//            
//            cont++;
//        }
//        
//        
//        return mp3_byte_stream;
//    }
//    
//    private FrameData read_data(LayerIIIDecoder decoder){
////        sideinfo.read_side_info();
////        sideinfo.calc_parameters();
//        int mdb = 0;
//        
//        decoder.read_side_info();
//        FrameData fd = decoder.get_frame_info();
//        header.copy_info(fd);
//        
//        byte [] buffer = bitstream.get_main_data();
//        
//        if( (mdb = fd.get_main_data_begin()) != 0 ){
//            byte[] data_res = reserve.read_from_reserve(mdb);
//            
//            int totlen = fd.get_md_len() ;
//            
//            int main_data_beg = fd.get_main_data_begin();
//            data_def = new byte[totlen];
//            int end = (totlen > main_data_beg)? main_data_beg: totlen;
//            
//            for(int i = 0; i < end; i++){
//                data_def[i] = data_res[i];
//            }
//            
//            if(totlen > main_data_beg){
//                int offset = end;
//                for(int i = offset; i < totlen; i++){
//                    data_def[i] = buffer[i - end];
//                }
//            }
//            
//            reserve.store_in_reserve(buffer);
//            tot += totlen;
//        } else {
//            int totlen = fd.get_md_len();
//            int end = ( totlen > buffer.length ) ? buffer.length: totlen;
//            
//            data_def = new byte[totlen];
//            
//            for(int i = 0; i < totlen; i++)
//                data_def[i] = buffer[i];
//            
//            tot += totlen;
//            reserve.store_in_reserve(buffer);
//        }
//        
//        return fd;
//    }
//    
//    private void store_from_buffer(FrameDataUnpacked fd_u) {
//        
//        int bitrate= 0;
//        
//        //System.out.println("fs: " + fd_u.getFs());
//        encoder.init_frame(fd_u);
//        encoder.set_side_info(fd_u);
//        
//        while(bitrate != 1)
//            bitrate = encoder.store_data(fd_u.get_data(), fd_u);
//    }
//    
//    
//    private void store_data(FrameData fd){
//        int bitrate= 0;
//        
//        encoder.init_frame(fd);
//        encoder.set_side_info(fd);
//        
//        while(bitrate != 1)
//            bitrate = encoder.store_data(data_def, fd);
//        
//    }
//    
//    private void store_in_buffer(byte [] main_data, FrameData fd) {
//        frame_buffer = new FrameDataUnpacked(fd.getChannels(), fd.getMaxGr());
//        fd.clone(frame_buffer);
//        frame_buffer.set_data(main_data);
//        
//      /*  if(index > 0)
//            System.out.println(frame_buffer[index - 1].esiste());*/
//        
//    }
//}
//
