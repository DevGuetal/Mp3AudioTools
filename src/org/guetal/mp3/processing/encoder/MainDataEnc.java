/*
 * MainDataEnc.java
 *
 * Created on 19 gennaio 2007, 10.34
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.guetal.mp3.processing.encoder;


/**
 *
 * @author Administrator
 */
public class MainDataEnc {
    private int totlen;
    private int RES_SIZE = 511;
    private FormatStream format;
    
    
    /** Creates a new instance of MainDataEnc */
    //public MainDataEnc(int nSlots)
    public MainDataEnc(int nSlots) {
        format = new FormatStream( nSlots + RES_SIZE );     // da vedere che dimensione dare
        
    }
    
    
    
    
    public void add_entry( int value, int length ) {
        format.store_in_array(value, length);
        totlen += length;
    }
    
    
    public byte [] format_main_data() {
        return format.get_array();
    }
    
    public int get_tolen(){
        return totlen;
    }
}
