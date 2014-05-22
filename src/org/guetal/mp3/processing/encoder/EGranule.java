package org.guetal.mp3.processing.encoder;

/**
 *  Data rapresentation of side info field
 *  @author Thomas Gualino
 */


public class EGranule {
    
    
    protected EChannel[] ch; // each granule is splitted in 2 part
    //public int [] scfsi;


    
    public EGranule(int channels) {
        ch= new EChannel[channels];
        
        for(int i = 0; i < channels; i++)
            ch[i] = new EChannel();
    
    }
}
