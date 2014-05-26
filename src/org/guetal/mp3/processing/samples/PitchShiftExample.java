//package org.guetal.mp3.processing.samples;
///*
// * EqualizationExample.java
// *
// * Created on 21 maggio 2007, 12.12
// */
//
//
//import java.io.InputStream;
//
//import org.guetal.fileManager.FileManager;
//
///**
// *
// * @author  Administrator
// * @version
// */
//public class PitchShiftExample  {
//    //private String fileName = "/audio/spettro equispaziato.mp3";
//    //private String fileName = "/audio/100-1000-15000.mp3";
//    private String fileName = "/audio/impulsi.mp3";
//    private PitchControl effect;
//    private byte [] stream;
//    
//    public void startApp() {
//        InputStream is = getClass().getResourceAsStream(fileName);
//        FileManager file = new FileManager();
//        
//        
//        effect = new PitchControl();
//        
//        stream = effect.shift_pitch(is, 0, 800, 5);
//        
//        if (stream.length > 1000)
//            file.saveMedia(stream, "audio/");
//    }
//    
//    public void pauseApp() {
//    }
//    
//    public void destroyApp(boolean unconditional) {
//    }
//}
