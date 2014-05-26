/*
 * FrameData.java
 *
 * Created on 24 April 2007, 19.59
 */

package org.guetal.mp3.processing.commons.data;


/**
 *
 * @author DevGuetal
 */
public class FrameDataUnpacked extends FrameData {
    
    private byte [] data;
    
    /** Creates a new instance of FrameData */
    public FrameDataUnpacked(int channels, int max_gr) {
        super(channels, max_gr);
    }
    
    public void set_data(byte [] dataToCopy){
        data = new byte [ dataToCopy.length ];              
        System.arraycopy(dataToCopy, 0, this.data, 0, dataToCopy.length);
    }
    
    public byte[] get_data(){       
        return this.data;
    }
}
