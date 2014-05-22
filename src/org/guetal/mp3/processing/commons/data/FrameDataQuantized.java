/*
 * FrameData.java
 *
 * Created on 24 aprile 2007, 19.59
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.guetal.mp3.processing.commons.data;

import org.guetal.mp3.processing.commons.Constants;

/**
 *
 * @author Administrator
 */
public class FrameDataQuantized extends FrameData{
    
    public int [][][] ix;

    public int[][][] scalefac0L;
    public int[][][][] scalefac0S;
   
    
    /** Creates a new instance of FrameData */
    public FrameDataQuantized(int channels, int max_gr) {
        super(channels, max_gr);
        ix = new int[max_gr][channels][Constants.SBLIMIT * Constants.SSLIMIT + 4];
        scalefac0L = new int[max_gr][channels][23];
        scalefac0S = new int[max_gr][channels][3][13];
    }
    
    
    public int [] getIx(int ch, int gr){
        return this.ix[gr][ch];
    }
    
    public void setIx(int [] ix, int ch, int gr){
        System.arraycopy(ix, 0, this.ix[gr][ch], 0, ix.length);
    }
    
    
}
