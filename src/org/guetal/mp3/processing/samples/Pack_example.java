package org.guetal.mp3.processing.samples;
///*
// * DeleteExample.java
// *
// * Created on 4 maggio 2007, 15.44
// */
//
//package processing.examples;
//
//import java.io.DataOutputStream;
//import java.io.IOException;
//import java.io.InputStream;
//
//import processing.checker.Checker;
//import processing.effects.Pack;
//
///**
// *
// * @author  Administrator
// * @version
// */
//public class Pack_example {
//    //
//    private String fileName = "/audio/song.mp3";
//    //private String fileName = "/audio/mic.mp3";
//    //private String fileName = "/audio/song.mp3";
//    private Pack effect;
//    private byte [] stream;
//    
//    public void startApp() {
//        InputStream is = getClass().getResourceAsStream(fileName);
//        FileManager file = new FileManager();
//        
//        effect = new Pack();
//        try {
//            long tempo1=System.currentTimeMillis();
//            stream = effect.pack(is);
//            long tempo2=System.currentTimeMillis();
//            System.out.println("tempo (ms): " + (tempo2 - tempo1));
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//        
//        FileConnection fc;
//        
//        //file.saveMedia(stream, "audio/");
//      
//        for(int i = 0; i < stream.length; i++)
//            System.out.println("i: " + stream[i]);
//        
//        try {
//            FileConnection fc1 = (FileConnection) Connector.open("file:///root1/audio/file.mp3");
//            DataOutputStream dos = fc1.openDataOutputStream();
//            dos.write(stream);
//            //FileConnection
//            fc1 = (FileConnection) Connector.open("file:///root1/audio/file.mp3");
//            
//            InputStream is1 = fc1.openInputStream();
//            InputStream is2 = getClass().getResourceAsStream(fileName);
//            InputStream is3 = getClass().getResourceAsStream(fileName);
//            
//            byte str [] = new byte [is2.available()];
//            is3.read(str);
//            
//            
//            
////            System.out.println("stream1: " +is1.available() + " bytes");
////            System.out.println("stream2: " +is2.available() + " bytes");
////            System.out.println("\n\n***************************** CHECKING ************************************");
////            Checker.compareStream(is1, is2,8, 2);
////
//            System.out.println("\n\n***************************** CHECKING ************************************");
//            Checker.compareStream(stream, str);
//            // for(int i = 0; i < str.length; i++)
//            //   System.out.println(""+i+" : " + str[i]);
//        } catch (IOException ex) {
//            ex.printStackTrace();
//        }
//        
//    }
//    
//    public void pauseApp() {
//    }
//    
//    public void destroyApp(boolean unconditional) {
//    }
//}
