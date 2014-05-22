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
public class FrameDataDequantized extends FrameData{
    
    public float [][][] ro;
    public int[][][] scalefac0L;
    public int[][][][] scalefac0S;
    /** Creates a new instance of FrameData */
    public FrameDataDequantized(int channels, int max_gr) {
        super(channels, max_gr);
        ro = new float[max_gr][channels][Constants.SBLIMIT * Constants.SSLIMIT];
        scalefac0L = new int[max_gr][channels][23];
        scalefac0S = new int[max_gr][channels][3][13];
    }
    
    
    public float [] getRo(int ch, int gr){
        return this.ro[gr][ch];
    }
    
    public void setRo(float [] ix, int ch, int gr){
        System.arraycopy(ro, 0, this.ro[gr][ch], 0, ro.length);
    }
    
    
}
