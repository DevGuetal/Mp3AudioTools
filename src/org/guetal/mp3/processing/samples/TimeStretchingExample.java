package org.guetal.mp3.processing.samples;
///*
// * TimeStretchingExample.java
// *
// * Created on 9 maggio 2007, 15.44
// */
//
//package processing.examples;
//
//import fileManager.FileManager;
//import java.io.IOException;
//import java.io.InputStream;
//import javax.microedition.midlet.*;
//import javax.microedition.lcdui.*;
//import processing.commons.data.FrameDataUnpacked;
//import processing.effects.TimeStretching;
//
///**
// *
// * @author  Administrator
// * @version
// */
//public class TimeStretchingExample extends MIDlet {
//   // private String fileName = "/audio/trombe.mp3";
//    //private String fileName = "/audio/mic.mp3";
//    private String fileName = "/audio/440_norm.mp3";
//    //private String fileName = "/audio/440-128-CBR.mp3";
//    private TimeStretching effect;
//    private byte [] stream;
//    
//    public void startApp() {
//        InputStream is = getClass().getResourceAsStream(fileName);
//
//        FileManager file = new FileManager();
// 
//        effect = new TimeStretching();
//        try {
//            //System.out.println("buff len: " + buffer.length+"\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
//            stream = effect.stretch(is, 1.5F, 20, 200);
//        } catch (IOException ex) {
//            ex.printStackTrace();
//        }
//        
//        if (stream.length > 100000)
//            file.saveMedia(stream, "audio/");
//    }
//    
//    public void pauseApp() {
//    }
//    
//    public void destroyApp(boolean unconditional) {
//    }
//}
