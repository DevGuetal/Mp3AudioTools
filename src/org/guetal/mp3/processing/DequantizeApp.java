package org.guetal.mp3.processing;
//    /*
// * HuffmanApp.java
// *
// * Created on 3 gennaio 2007, 17.07
// */
//
//package processing;
//
//import java.io.ByteArrayInputStream;
//import java.io.InputStream;
//import javax.microedition.io.Connector;
//import javax.microedition.io.file.FileConnection;
//import javax.microedition.media.Manager;
//import javax.microedition.media.Player;
//import javax.microedition.midlet.*;
//import javax.microedition.lcdui.*;
//import processing.effects.ProvaDequantizzazione;
//import processing.effects.ProvaHuffman;
//import fileManager.FileManager;
//import processing.checker.Checker;
///**
// *
// * @author  Tommy
// * @version
// */
//public class DequantizeApp extends MIDlet implements CommandListener {
//    //File List
//    
//    //private String fileName = "/audio/bip128CBR.mp3";
////    private String fileName = "/audio/440-128-CBR.mp3";     //2446
//    private String fileName = "/audio/trombe.mp3";     //   0
//        //private String fileName = "/audio/stan.mp3";     //   0
//    //private String fileName = "/audio/shine.mp3";
//    //private String fileName = "/audio/fields.mp3";    
//    //private String fileName = "/audio/jingle.mp3";    
//    //private String fileName = "/audio/vari.mp3";    
//    //private String fileName = "/audio/song.mp3";   
//    //private String fileName = "/audio/mic.mp3";   
//
//    private InputStream is1, is2;
//    private ProvaDequantizzazione prova;
//    private byte [] stream;
//    private FileManager file = new FileManager();
//    
//    private Display display;
//    private StringItem Text = new StringItem("initialization",null);
//    private List selectFilesForm =new List("Open a Mp3 file",Choice.IMPLICIT);
//    
//    private Command exitCommand = new Command("Exit",Command.EXIT,1);
//    
//    public void startApp() {
//        
//        try {
//            selectFilesForm.addCommand(exitCommand); // add the commands
//            selectFilesForm.setCommandListener(this);
//            
//            selectFilesForm.append("wait.. we are",null);
//            selectFilesForm.append("working for you..",null);// add some audio files ...
//            display  = Display.getDisplay(this);
//            display.setCurrent(selectFilesForm);
//            
//            System.out.println("DirectProcessing file: " + fileName);
//            prova = new ProvaDequantizzazione(fileName);
//
//            //Checker.printStream(is2);
//            
//           // createPlayer();
//            
//        } catch (Exception e){
//            String error = e.toString();
//            
//            String riga = "";
//            
//            for(int j = 0; j < error.length(); j = j+20){
//                for(int i = 0; i < 20; i++)
//                    if( i + j < error.length() )
//                        riga += error.charAt(i+j);
//                selectFilesForm.append(riga,null);
//                riga = "";
//            }
//            
//            System.err.println("\nerror: " + e);
//            
//        }
//    }
//    
//    public void pauseApp() {
//    }
//    
//    public void destroyApp(boolean unconditional) {
//    }
//    
//    
//    private void createPlayer(){
//    try {
//                Player m_player = Manager.createPlayer(new ByteArrayInputStream(stream), "audio/mpeg");
//                //Player m_player = Manager.createPlayer(is, "audio/midi");
//                m_player.prefetch();
//                System.out.println("Trying execute sound.......");
//                selectFilesForm.append("player created",null);
//                selectFilesForm.append("trying execute",null); 
//                m_player.start();
//                selectFilesForm.append("does it work??",null);
//            } catch(Exception e) {
//                String error = e.toString();
//                
//                String riga = "";
//
//                for(int j = 0; j < error.length(); j = j+20){
//                    for(int i = 0; i<20; i++)
//                        if(i+j < error.length() )
//                            riga += error.charAt(i+j);
//                        selectFilesForm.append(riga,null);
//                        riga = "";
//                }
//
//                System.out.println("Error while playing file: " + e);
//            }
//    }
//    
////--------------------------------------------------------------------------------------------
//// Listeners (Commands and Items)
////--------------------------------------------------------------------------------------------
//    
//    
//    public void itemStateChanged(Item item) {
//        
//    }
//    
//    public void commandAction(Command c,Displayable s) {
//        try{
//            if (c == exitCommand) {
//                destroyApp(true);
//                notifyDestroyed();
//            }
//        } catch(Exception e){}
//        
//    }
//}
