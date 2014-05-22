/*
 * CommonMethods.java
 *
 * Created on 8 maggio 2007, 16.38
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.guetal.mp3.processing.commons;

/**
 *
 * @author Administrator
 */
public final class CommonMethods {
    
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
    
    public final static int bitrates[] [] [] = {
        {{0
    /*
     *  MPEG 2
     */
                 ,32000,48000,56000,64000,80000,96000,
                 112000,128000,144000,160000,176000,192000,224000,256000,0},
                 {0
    /*
     *  MPEG 2
     */
                          ,8000,16000,24000,32000,40000,48000,
                          56000,64000,80000,96000,112000,128000,144000,160000,0},
                          {0
    /*
     *  MPEG 2
     */
                                   ,8000,16000,24000,32000,40000,48000,
                                   56000,64000,80000,96000,112000,128000,144000,160000,0}}, 
                                   {{0
    /*
     *  MPEG 1
     */
                                            ,32000,64000,96000,128000,160000,192000,
                                            224000,256000,288000,320000,352000,384000,416000,448000,0},
                                            {0
    /*
     *  MPEG 1
     */
                                                     ,32000,48000,56000,64000,80000,96000,
                                                     112000,128000,160000,192000,224000,256000,320000,384000,0},
                                                     {0
    /*
     *  MPEG 1
     */
                                                              ,32000,40000,48000,56000,64000,80000,
                                                              96000,112000,128000,160000,192000,224000,256000,320000,0}}
    };
    
    
    
    public final static int[] [] frequencies =
    {{22050,24000,16000,1},
     {44100,48000,32000,1}};
    
    
   public final static int [][][] sfBandIndex = {
        { /* Table B.2.b: 22.05 kHz */
            {0,6,12,18,24,30,36,44,54,66,80,96,116,140,168,200,238,284,336,396,464,522,576},
            {0,4,8,12,18,24,32,42,56,74,100,132,174,192}
        },
        { /* Table B.2.c: 24 kHz */
            {0,6,12,18,24,30,36,44,54,66,80,96,114,136,162,194,232,278,330,394,464,540,576},
            {0,4,8,12,18,26,36,48,62,80,104,136,180,192}
        },
        { /* Table B.2.a: 16 kHz */
            {0,6,12,18,24,30,36,44,45,66,80,96,116,140,168,200,238,248,336,396,464,522,576},
            {0,4,8,12,18,26,36,48,62,80,104,134,174,192}
        },
        { /* Table B.8.b: 44.1 kHz */
            {0,4,8,12,16,20,24,30,36,44,52,62,74,90,110,134,162,196,238,288,342,418,576},
            {0,4,8,12,16,22,30,40,52,66,84,106,136,192}
        },
        { /* Table B.8.c: 48 kHz */
            {0,4,8,12,16,20,24,30,36,42,50,60,72,88,106,128,156,190,230,276,330,384,576},
            {0,4,8,12,16,22,28,38,50,64,80,100,126,192}
        },
        { /* Table B.8.a: 32 kHz */
            {0,4,8,12,16,20,24,30,36,44,54,66,82,102,126,156,194,240,296,364,448,550,576},
            {0,4,8,12,16,22,30,42,58,78,104,138,180,192}
        }
    };
    
    
    /* OPTIONS */
    public static final int UNPACK = 0;
    public static final int READ_SCALEFACTORS = 1;
    public static final int HUFFMAN_DECODING = 2;
    public static final int DEQUANTIZE = 3;
    
    
    /**
     * Calculates framesize in bytes excluding Header Size.
     * nSlots is framesize excluding Header, CRC and Side Info
     */
    /* only supports mpeg1 frames; stripped out mpeg2 checking */
    public static final int calFrameSize(int h_bitrate_index, int h_padding_bit, int h_sample_frequency) {
        int framesize = (144 * bitrates[1] [2] [h_bitrate_index]) / h_sample_frequency;

        if(h_padding_bit != 0)
            framesize++;

        return framesize;
    }
    
    public static final int calFrameSize(int h_bitrate_index, int h_sample_frequency) {
        int fs_index = 3;      
        int framesize = (144 * bitrates[1] [2] [h_bitrate_index]) / h_sample_frequency;

        if( ((144 * bitrates[1] [2] [h_bitrate_index]) % h_sample_frequency)!= 0)
            framesize++;
        return framesize;
    }
    
    /**
     * Calculates framesize in bytes excluding Header Size.
     * nSlots is framesize excluding Header, CRC and Side Info
     */
    /* only supports mpeg1 frames; stripped out mpeg2 checking */
    public static final int calNSlots(int framesize, int h_mode, int h_protection_bit) {
       
        int nSlots = framesize - ((h_mode == SINGLE_CHANNEL) ? 17 : 32) -  ((h_protection_bit!=0) ? 0 : 2) - 4;
        return nSlots;
    }
    
    
    
    
}
