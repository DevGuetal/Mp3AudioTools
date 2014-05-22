/*
 * FrameBuffer.java
 *
 * Created on 14 maggio 2007, 16.15
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.guetal.mp3.processing.commons.data;

/**
 *
 * @author Administrator
 */
public class FrameBuffer {
    
    private FrameDataUnpacked [] frame_buffer;
    
    private Entry entry, last_entry, cur_entry;

    private int direction = 1;
    private int entries = 0;
    
    /** Creates a new instance of FrameBuffer */
    public FrameBuffer(int length) {
        frame_buffer = new FrameDataUnpacked[length];
        
    }
    
    public FrameBuffer() {
        cur_entry = last_entry = entry = new Entry(null);
    }
    
    private int index = 0;
    
 /*   public void add_entry(byte [] main_data, FrameData fd) {
        frame_buffer[index] = new FrameDataUnpacked(fd.getChannels(), fd.getMaxGr());
        fd.clone(frame_buffer[index]);
        frame_buffer[index].set_data(main_data);
        index++;
    }*/
    
  /*  public FrameDataUnpacked [] get_frame_buffer(){
        return this.frame_buffer;
    }*/
    
    public int get_buffer_len(){
        //return frame_buffer.length;
        return this.entries;
    }
       
  /*  public byte[] get_data(int index){
        return this.frame_buffer[index].get_data();
    }*/
    
    /**/
    public void add_entry( byte [] main_data, FrameData fd ) {
        last_entry = last_entry.create_entry( main_data, fd );
        
        last_entry.setnum(entries);
        entries++;
    }
    
    public void set_direction_reading(int direction){
        this.direction = direction;
        
        switch(direction){
            case  1: cur_entry =      entry; break;
            case -1: cur_entry = last_entry; break;
        }
        
    }
    
    public FrameDataUnpacked get_entry() {
        
        switch(direction){
            case  1: cur_entry = cur_entry.get_next_entry(); break;
            case -1: cur_entry =  cur_entry.get_prevEntry(); break;
        }
        
        return cur_entry.get_frame();
    }
    
    
    /* Inner Class */
    private class Entry{
        private FrameDataUnpacked frame;
        
        private Entry next_entry, prev_entry;
        private int num;
        
        protected Entry(Entry prev_entry){
            this.prev_entry = prev_entry;
        }
        
        protected Entry create_entry( byte [] data, FrameData fd ) {
            this.next_entry = new Entry(this);
            frame = new FrameDataUnpacked(fd.getChannels(), fd.getMaxGr());
            fd.clone(frame);
            frame.set_data(data);
            return next_entry;
        }
        
        protected void setnum(int num){
            this.num = num;
        }
        
        protected Entry get_next_entry() {
            return this.next_entry;
        }
        
        protected FrameDataUnpacked get_frame(){
            return this.frame;
        }
        
        protected Entry get_prevEntry(){
            return this.prev_entry;
        }
        
        public int getnum() {
            return this.num;
        }
    }
}
