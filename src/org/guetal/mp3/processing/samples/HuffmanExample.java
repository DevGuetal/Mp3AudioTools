package org.guetal.mp3.processing.samples;
///*
// * HuffmanExample.java
// *
// * Created on 3 gennaio 2007, 17.07
// */
//
//package processing.examples;
//
//import java.awt.Choice;
//import java.io.ByteArrayInputStream;
//import java.io.InputStream;
//import java.util.List;
//
//import processing.checker.Checker;
//import processing.commons.Manager;
//import processing.effects.ProvaHuffman;
///**
// *
// * @author  Tommy
// * @version
// */
//public class HuffmanExample  {
//    //File List
//    
//    //private String fileName = "/audio/bip128CBR.mp3";
////    private String fileName = "/audio/440-128-CBR.mp3";     //2446
//    // private String fileName = "/audio/trombe.mp3";     //   0
//    //private String fileName = "/audio/stan.mp3";     //   0
//    //private String fileName = "/audio/shine.mp3";
//    //private String fileName = "/audio/fields.mp3";
//    //private String fileName = "/audio/jingle.mp3";
//    //private String fileName = "/audio/vari.mp3";
//    //private String fileName = "/audio/wave.mp3";
//    //private String fileName = "/audio/song.mp3";
//    private String fileName = "/audio/440.mp3";     //   0
//    
//    private InputStream is1, is2, is3;
//    private ProvaHuffman prova;
//    private byte [] stream;
//    private FileManager file = new FileManager();
//    
//    private Display display;
//    private StringItem Text = new StringItem("initialization",null);
//    private List selectFilesForm =new List("Open a Mp3 file",Choice.IMPLICIT);
//    
//    private Command exitCommand = new Command("Exit",Command.EXIT,1);
//    
//    public void startApp()  {
//        
//        
//        selectFilesForm.addCommand(exitCommand); // add the commands
//        selectFilesForm.setCommandListener(this);
//        
//        selectFilesForm.append("wait.. we are",null);
//        selectFilesForm.append("working for you..",null);// add some audio files ...
//        display  = Display.getDisplay(this);
//        display.setCurrent(selectFilesForm);
//        
//        System.out.println("DirectProcessing file: " + fileName);
//        long tempo1=System.currentTimeMillis();
//        prova = new ProvaHuffman(fileName);
//        
//        stream = prova.get_stream();
//        long tempo2=System.currentTimeMillis();
//        
//        System.out.println("tempo (ms): " + (tempo2 - tempo1));
//        try {
//            file.saveMedia(stream, "audio/");
//            System.out.println("FILE SAVED");
//            
//            FileConnection fc = (FileConnection) Connector.open("file:///root1/audio/file.mp3");
//            FileConnection fc1 = (FileConnection) Connector.open("file:///root1/audio/file.mp3");
//            
//            is1 = fc.openInputStream();
//            is2 = getClass().getResourceAsStream(fileName);
//            is3 = getClass().getResourceAsStream(fileName);
//            
//            System.out.println("n bytes stream 1: " + is1.available());
//            System.out.println("n bytes stream 2: " + is2.available());
//            byte [] stream2 = new byte [is3.available()];
//            is3.read(stream2);
////
////            for(int i = 0; i < stream2.length; i++)
// //               System.out.println(i + ": " + stream2[i]);
//
//            System.out.println("\n\n***************************** CHECKING ************************************");
//           // Checker.compareStream(is1, is2, 30, 20);
//            
//            
//            System.out.println("\n\n***************************** COMPARING STREAM ************************************");
//            Checker.compareStream(stream, stream2);
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
//        try {
//            Player m_player = Manager.createPlayer(new ByteArrayInputStream(stream), "audio/mpeg");
//            //Player m_player = Manager.createPlayer(is, "audio/midi");
//            m_player.prefetch();
//            System.out.println("Trying execute sound.......");
//            selectFilesForm.append("player created",null);
//            selectFilesForm.append("trying execute",null);
//            m_player.start();
//            selectFilesForm.append("does it work??",null);
//        } catch(Exception e) {
//            String error = e.toString();
//            
//            String riga = "";
//            
//            for(int j = 0; j < error.length(); j = j+20){
//                for(int i = 0; i<20; i++)
//                    if(i+j < error.length() )
//                        riga += error.charAt(i+j);
//                selectFilesForm.append(riga,null);
//                riga = "";
//            }
//            
//            System.out.println("Error while playing file: " + e);
//        }
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
