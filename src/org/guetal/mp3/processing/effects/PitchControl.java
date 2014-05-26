//package org.guetal.mp3.processing.effects;
///*
// * Filtering.java
// *
// * Created on 21 maggio 2007, 10.03
// *
// * To change this template, choose Tools | Template Manager
// * and open the template in the editor.
// */
//
//package processing.effects;
//
//import java.io.InputStream;
//import processing.commons.Constants;
//import processing.commons.data.FrameData;
//import processing.commons.util.MathUtil;
//import processing.commons.ReadAndStore;
//
///**
// *
// * @author Administrator
// */
//public class PitchControl {
//    
//    ReadAndStore reader;
//    
//    /** Creates a new instance of Filtering */
//    public PitchControl() {
//    }
//    
//    public byte [] shift_pitch(InputStream is, int fStart, int fEnd, int shift){
//        byte [] stream;
//        final int opt = Constants.QUANTIZED_DOMAIN;
//        int cont = 0, bytes = 0;
//        
//        double param = (3.0/4.0);
//        
//        reader = new ReadAndStore(is);
//        
//        
//        do{
//            FrameData fd = reader.read_data(opt);
//            System.out.println("---------------------------------------------(Step 1 - Filtering - Iteration number: " + cont + ")---------------------------------------------");
//            
//            if(fd.getFramesize() > 1){
//                
//                if((cont >= fStart) && (cont < fEnd)){
//                    for(int ch = 0; ch < fd.getChannels(); ch++)
//                        for(int gr = 0; gr < fd.getMaxGr(); gr++){
//                        int [] ix = fd.getIx(ch, gr);
//                        
//                        
//                        //fd.setGlobalGain(ch, gr, fd.getGlobalGain(ch, gr)-15);
////                        
////                        for(int i = 0; i<576; i++)
////                            System.out.println("data prima["+i+"]: " + ix[i]);
////                        if(fd.getBlockType(ch,gr) != 2)
//                        int [] data = new int[576];
//                        if(shift > 0){
//                            for(int line = shift; line < 576; line++){
//                                data[line] = ix[line - shift];
//                            }
//                            
//                            for(int line = 0; line < shift; line++)
//                                data[line] = 0;
////                            
////                            for(int i = 0; i<576; i++)
////                                System.out.println("data["+i+"]: " + data[i]);
//                        }
//                        //else
////                                for(int line = 0; line < 192; line++)
////                                    for(int window = 0; window < 3; window++){
////                            ix[line * 3 + window] = (int)(ix[line * 3 + window] * filter[line * 3]);
////                                    }
//                        fd.setIx(data, ch, gr);
//                        }
//                }
//                
//                byte [] data = reader.encode_data(fd);
//                
//                //reader.store_data(fd, data);
//                bytes += fd.getFramesize();
//                
//            } else break;
//            
//            cont++;
//        }
//        while(cont < 3500);
//        // while(true);
//        
//        System.out.println("bytes: " + bytes);
//        stream = reader.getStream(bytes - 1);
//        
//        
//        return stream;
//    }
//    
//}
