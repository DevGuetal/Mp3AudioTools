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
    private int entries = 1;
    private int totlen;
    private int nSlots;
    private int RES_SIZE = 511;
    
    private Entry entry, cur_entry;
    private FormatStream format;
    
    
    /** Creates a new instance of MainDataEnc */
    //public MainDataEnc(int nSlots)
    public MainDataEnc(int nSlots) {
        this.nSlots = nSlots + 1; 
        //   System.out.println("main data enc -> creo new entry");
        //   entry = new Entry();
        format = new FormatStream( nSlots + RES_SIZE );     // da vedere che dimensione dare
        
    }
    
    
 /*   public void add_entry( int value, int length )
    {
        if(cur_entry != null)
            cur_entry = cur_entry.create_entry();
        else cur_entry = entry.create_entry();
        cur_entry.set_entry( value, length );
  
        entries++;
        totlen += length;
  
        //System.out.println("main data enc -> totlen: " + totlen);
    }*/
    
    
/*    public byte [] format_main_data()
    {
        totlen = ((totlen % 8) > 0)? (totlen/8 + 1): (totlen / 8);
        System.out.println("main data enc -> totlen: " + totlen);
        //format = new FormatStream(totlen);
 
        format = new FormatStream(totlen);
        while( entry.next_entry != null ){
            format.store_in_array(entry.value, entry.length);
            entry = entry.next_entry;
        }
        //System.out.println("main data enc -> prima di tornare");
        return format.get_array();
    }*/
    
    
    public void add_entry( int value, int length ) {
        //System.out.println("main data -> value: " + value);
        //System.out.println("main data -> length: " + length);
        format.store_in_array(value, length);
        totlen += length;
        
        //System.out.println("main data enc -> totlen: " + totlen);
    }
    
    
    public byte [] format_main_data() {
       /* byte [] array = new byte[totlen];
        for (int i = 0; i< totlen; i++)*/
        //System.out.println("prima di get array");
        
        //int len = ((totlen % 8) > 0)? (totlen/8 + 1): (totlen / 8);
        
        return format.get_array();
    }
    
    public int get_tolen(){
        return totlen;
    }


    /** Inner class */
    private class Entry{
        private int value;
        private int length;
        
        private Entry next_entry;
        
        public void set_entry( int value, int length ) {
            this.value = value;
            this.length = length;
        }
        
        public Entry create_entry() {
            this.next_entry = new Entry();
            
            return next_entry;
        }
        
        public Entry get_next_entry() {
            return this.next_entry;
        }
    }
}
