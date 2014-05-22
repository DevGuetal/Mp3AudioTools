/*
 * Constants.java
 *
 * Created on 15 giugno 2007, 0.03
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.guetal.mp3.processing.commons;

/**
 *
 * @author Administrator
 */
public final class Constants {
    
    /** Constant for LayerI version */
    public final static int LayerI = 1;
    
    /** Constant for LayerII version */
    public final static int LayerII = 2;
    
    /** Constant for LayerIII version */
    public final static int LayerIII = 3;
    
    /** Constant for MPEG-1 version */
    public final static int MPEG1 = 1;
    
    /** Description of the Field */
    public final static int STEREO = 0;
    
    /** Description of the Field */
    public final static int JOINT_STEREO = 1;
    
    /** Description of the Field */
    public final static int DUAL_CHANNEL = 2;
    
    /** Description of the Field */
    public final static int SINGLE_CHANNEL = 3;
    
    /** Description of the Field */
    public final static int FOURTYFOUR_POINT_ONE = 0;
    
    /** Description of the Field */
    public final static int FOURTYEIGHT = 1;
    
    /** Description of the Field */
    public final static int THIRTYTWO = 2;
    
    public final static int SBLIMIT = 32;
    public final static int SSLIMIT = 18;
    
    public final static int FREQ_LINES = SBLIMIT * SSLIMIT;
    
    
    /* OPTIONS */
    public static final int HUFFMAN_DOMAIN      = 0;
    public static final int READ_SCALEFACTORS   = 1;
    public static final int QUANTIZED_DOMAIN    = 2;
    public static final int DEQUANTIZED_DOMAIN  = 3;
    
}
