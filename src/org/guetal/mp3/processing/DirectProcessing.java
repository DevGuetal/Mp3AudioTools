package org.guetal.mp3.processing;
/*
 * DirectProcessing.java
 *
 * Created on 13 novembre 2006, 09.00
 */
//
//package processing;
//
//import javax.microedition.media.Manager;
//import javax.microedition.midlet.*;
//import javax.microedition.lcdui.*;
//import java.io.InputStream;
//import java.io.ByteArrayInputStream;
//import processing.directProc.Mp3Manager;
//import javax.microedition.media.*;
//
///**
// *
// * @author  Tommy
// * @version
// */
//public class DirectProcessing extends MIDlet implements CommandListener {
//    
//    //private String fileName = "/audio/bip128CBR.mp3";
//    //private String fileName = "/audio/440-128-CBR.mp3";
//    //private String fileName = "/audio/fields.mp3";    //name of mp3 file we want open
//    //private String fileName = "/audio/jingle.mp3";    //name of mp3 file we want open
//    private String fileName = "/audio/vari.mp3";    //name of mp3 file we want open
//    
//    private Mp3Manager prova;
//    private byte [] stream;
//    
//    private Display display;
//    private StringItem Text = new StringItem("initialization",null);
//    private List selectFilesForm =new List("Open a Mp3 file",Choice.IMPLICIT);
//    
//    private Command exitCommand = new Command("Exit",Command.EXIT,1);
//    
//    public void startApp(){
//        try
//        {
//            selectFilesForm.addCommand(exitCommand); // add the commands
//            selectFilesForm.setCommandListener(this);
//            
//            selectFilesForm.append("wait.. we are",null);
//            selectFilesForm.append("working for you..",null);// add some audio files ...
//            display  = Display.getDisplay(this);
//            display.setCurrent(selectFilesForm);
//            
//            System.out.println("DirectProcessing file: " + fileName);
//            prova = new Mp3Manager(fileName);
//
//            stream = prova.get_byte_stream();
//            selectFilesForm.append("stream created",null);
//            createPlayer();
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
//                    selectFilesForm.append(riga,null);
//                    riga = "";
//            }
//
//            System.err.println("error: " + e);
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
//            
//            
//    }
//    
////--------------------------------------------------------------------------------------------
//// Listeners (Commands and Items)
////--------------------------------------------------------------------------------------------
//    
//    public void playerUpdate(Player p, String event, Object eventData) {
//        if (event==PlayerListener.CLOSED) {
//            display.setCurrent(selectFilesForm);
//        }
//    }
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
