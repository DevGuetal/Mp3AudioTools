package org.guetal.mp3.processing;
///*
// * Mp3Player.java
// *
// * Created on 12 novembre 2006, 23.20
// */
//
//package processing;
//
//import javax.microedition.midlet.*;
//import javax.microedition.media.*;
//import java.io.*;
//
//
///**
// *
// * @author  Tommy
// * @version
// */
//public class Mp3Player extends MIDlet {
//    private InputStream is;
//    //private FileInputStream mp3Stream;
//    //private String fileName = "/audio/bip128CBR.mp3";
////    private String fileName = "/audio/440-128-CBR.mp3";     //2446
//    private String fileName = "/audio/trombe.mp3";     //   0
//    //private String fileName = "/audio/shine.mp3";
//    //private String fileName = "/audio/fields.mp3";    
//    //private String fileName = "/audio/jingle.mp3";    
//    //private String fileName = "/audio/vari.mp3";    
//    //private String fileName = "/audio/song.mp3";  
//    
//    public void startApp() {
//        System.out.print("Opening file.......");
//        openFile(fileName);
//        System.out.print("[ok]\nTrying create player.......");
//        createPlayer();
//    }
//    
//    public void pauseApp() {
//    }
//    
//    public void destroyApp(boolean unconditional) {
//    }
//    
//    private void openFile(String fileName){
//        is = getClass().getResourceAsStream(fileName);        
//    }
//    
//    private void createPlayer(){
//        
//            try {
//                Player m_player = Manager.createPlayer(is, "audio/mpeg");
//                //Player m_player = Manager.createPlayer(is, "audio/midi");
//                m_player.prefetch();
//                System.out.print("[ok]\nTrying execute sound.......");
//                m_player.start();
//                System.out.print("[ok]\n");
//            } catch(Exception ex) {
//                System.out.println("Error while playing file: " + ex);
//            }
//            
//            
//    }
//}
