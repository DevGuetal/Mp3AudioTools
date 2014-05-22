/**
 *-----------------------------------------------------------------------
 *   This program is free software; you can redistribute it and/or modify
 *   it under the terms of the GNU Library General Public License as published
 *   by the Free Software Foundation; either version 2 of the License,or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU Library General Public License for more details.
 *
 *   You should have received a copy of the GNU Library General Public
 *   License along with this program; if not,write to the Free Software
 *   Foundation,Inc.,675 Mass Ave,Cambridge,MA 02139,USA.
 *----------------------------------------------------------------------
 */
package org.guetal.mp3.processing.decoder;


import java.io.IOException;

import org.guetal.mp3.processing.commons.CommonMethods;
import org.guetal.mp3.processing.commons.data.FrameData;
import org.guetal.mp3.processing.encoder.LayerIIIEnc;


/**
 * Class for extracting information from a frame header. Contains many informations about mp3 frame, i.e. framerate, framesize..
 * @author     micah
 * @created    DecHeader8, 2001
 */
public final class Header {
    
    public final static int[] [] frequencies = CommonMethods.frequencies;
    
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
    
    /** Description of the Field */
    public final static int bitrates[] [] [] = CommonMethods.bitrates;
//    {
//        {{0
//    /*
//     *  free format
//     */
//                 ,32000,48000,56000,64000,80000,96000,
//                 112000,128000,144000,160000,176000,192000,224000,256000,0},
//                 {0
//    /*
//     *  free format
//     */
//                          ,8000,16000,24000,32000,40000,48000,
//                          56000,64000,80000,96000,112000,128000,144000,160000,0},
//                          {0
//    /*
//     *  free format
//     */
//                                   ,8000,16000,24000,32000,40000,48000,
//                                   56000,64000,80000,96000,112000,128000,144000,160000,0}},
//                                   {{0
//    /*
//     *  free format
//     */
//                                            ,32000,64000,96000,128000,160000,192000,
//                                            224000,256000,288000,320000,352000,384000,416000,448000,0},
//                                            {0
//    /*
//     *  free format
//     */
//                                                     ,32000,48000,56000,64000,80000,96000,
//                                                     112000,128000,160000,192000,224000,256000,320000,384000,0},
//                                                     {0
//    /*
//     *  free format
//     */
//                                                              ,32000,40000,48000,56000,64000,80000,
//                                                              96000,112000,128000,160000,192000,224000,256000,320000,0}}
//    };
    
    /** Description of the Field */
   /* public final static String bitrate_str[] [] [] = {
        {{"free format","32 kbit/s","48 kbit/s","56 kbit/s","64 kbit/s",
                 "80 kbit/s","96 kbit/s","112 kbit/s","128 kbit/s","144 kbit/s",
                 "160 kbit/s","176 kbit/s","192 kbit/s","224 kbit/s","256 kbit/s",
                 "forbidden"},
                 {"free format","8 kbit/s","16 kbit/s","24 kbit/s","32 kbit/s",
                          "40 kbit/s","48 kbit/s","56 kbit/s","64 kbit/s","80 kbit/s",
                          "96 kbit/s","112 kbit/s","128 kbit/s","144 kbit/s","160 kbit/s",
                          "forbidden"},
                          {"free format","8 kbit/s","16 kbit/s","24 kbit/s","32 kbit/s",
                                   "40 kbit/s","48 kbit/s","56 kbit/s","64 kbit/s","80 kbit/s",
                                   "96 kbit/s","112 kbit/s","128 kbit/s","144 kbit/s","160 kbit/s",
                                   "forbidden"}},
                                   {{"free format","32 kbit/s","64 kbit/s","96 kbit/s","128 kbit/s",
                                            "160 kbit/s","192 kbit/s","224 kbit/s","256 kbit/s","288 kbit/s",
                                            "320 kbit/s","352 kbit/s","384 kbit/s","416 kbit/s","448 kbit/s",
                                            "forbidden"},
                                            {"free format","32 kbit/s","48 kbit/s","56 kbit/s","64 kbit/s",
                                                     "80 kbit/s","96 kbit/s","112 kbit/s","128 kbit/s","160 kbit/s",
                                                     "192 kbit/s","224 kbit/s","256 kbit/s","320 kbit/s","384 kbit/s",
                                                     "forbidden"},
                                                     {"free format","32 kbit/s","40 kbit/s","48 kbit/s","56 kbit/s",
                                                              "64 kbit/s","80 kbit/s","96 kbit/s","112 kbit/s","128 kbit/s",
                                                              "160 kbit/s","192 kbit/s","224 kbit/s","256 kbit/s","320 kbit/s",
                                                              "forbidden"}}
    };*/
    
    
    public static int framesize;
    public static int nSlots;
    private static int h_layer,h_protection_bit,h_bitrate_index,h_padding_bit,h_mode_extension, h_emph;
    private static int h_version;
    private static int h_mode;
    private static int h_sample_frequency;
    private static int h_number_of_subbands,h_intensity_stereo_bound;
    private static byte syncmode = BitStream.INITIAL_SYNC;
    
    // Functions to query header contents:
    /**
     * Returns version.
     */
    public int version() {
        return h_version;
    }
    
    /**
     * Returns Layer ID.
     */
    public int layer() {
        return h_layer;
    }
    
    /**
     * Returns bitrate index.
     */
    public int bitrate_index() {
        return h_bitrate_index;
    }
    
    /**
     * Returns Sample Frequency.
     */
    public int sample_frequency() {
        return h_sample_frequency;
    }
    
    /**
     * Returns Frequency.
     */
    public int frequency() {
        return frequencies[h_version] [h_sample_frequency];
    }
    
    /**
     * Returns Mode.
     */
    public int mode() {
        return h_mode;
    }
    
    /**
     * Returns an int containig framesize
     */
    public int framesize(){
        return framesize;
    }
    
    /**
     * Returns Layer III Padding bit.
     */
    public boolean padding() {
        return (h_padding_bit != 0);
    }
    
    /**
     * Returns Slots.
     */
    public int slots() {
        return nSlots;
    }
    
    /**
     * Returns Mode Extension.
     */
    public int mode_extension() {
        return h_mode_extension;
    }

    
    public int number_of_subbands() {
        return h_number_of_subbands;
    }
    
    public int intensity_stereo_bound() {
        return h_intensity_stereo_bound;
    }
    
    /**
     * Read a 32-bit header from the bitstream.
     */
    final void read_header(BitStream stream) throws IOException {
        int headerstring;
        int channel_bitrate;
        boolean sync = false;
        do {
            headerstring = stream.syncHeader(syncmode);
            if (syncmode == BitStream.INITIAL_SYNC) {
                h_version = ((headerstring >>> 19) & 1);
                System.out.println("MPEG version: "+h_version);
                if ((h_sample_frequency = ((headerstring >>> 10) & 3)) == 3) {
                    return;
                }
                System.out.println("Sampling frequency: " + frequencies[h_version] [h_sample_frequency]);
            }
            h_layer = 4 - (headerstring >>> 17) & 3;
            // E.B Fix.
            //h_protection_bit = 0;
            h_protection_bit = (headerstring >>> 16) & 1;
            // End.
            h_bitrate_index = (headerstring >>> 12) & 0xF;
            h_padding_bit = (headerstring >>> 9) & 1;
            h_mode = ((headerstring >>> 6) & 3);
            h_mode_extension = (headerstring >>> 4) & 3;
            h_emph = headerstring & 3;
            if (h_mode == JOINT_STEREO) {
                h_intensity_stereo_bound = (h_mode_extension << 2) + 4;
            } else {
                h_intensity_stereo_bound = 0;
            }
            if (h_layer == 1) {
                h_number_of_subbands = 32;
            } else {
                channel_bitrate = h_bitrate_index;
                // calculate bitrate per channel:
                if (h_mode != SINGLE_CHANNEL) {
                    if (channel_bitrate == 4) {
                        channel_bitrate = 1;
                    } else {
                        channel_bitrate -= 4;
                    }
                }
                if ((channel_bitrate == 1) || (channel_bitrate == 2)) {
                    if (h_sample_frequency == THIRTYTWO) {
                        h_number_of_subbands = 12;
                    } else {
                        h_number_of_subbands = 8;
                    }
                } else if ((h_sample_frequency == FOURTYEIGHT) || ((channel_bitrate >= 3) && (channel_bitrate <= 5))) {
                    h_number_of_subbands = 27;
                } else {
                    h_number_of_subbands = 30;
                }
            }
            if (h_intensity_stereo_bound > h_number_of_subbands) {
                h_intensity_stereo_bound = h_number_of_subbands;
            }
            // calculate framesize and nSlots
            framesize = CommonMethods.calFrameSize(h_bitrate_index, h_padding_bit, frequencies[h_version] [h_sample_frequency]);
            
            if(h_version == MPEG1)
                nSlots = CommonMethods.calNSlots(framesize, h_mode, h_protection_bit);
            
            else{
                System.out.println("sorry,doesn't support mpeg2 frames - only LayerIII is supported");
                System.exit(1);
            }
            
            // read framedata:
            stream.read_frame_data(framesize - 4);
            if (stream.isSyncCurrentPosition(syncmode)) {
                if (syncmode == BitStream.INITIAL_SYNC) {
                    syncmode = BitStream.STRICT_SYNC;
                    stream.set_syncword(headerstring & 0xFFF80CC0);
                }
                sync = true;
            } else {
                stream.unreadFrame();
            }
        } while (!sync);
        stream.parse_frame();
        
        // E.B Fix
        if (h_protection_bit == 0) {
            // frame contains a crc checksum
            short checksum = (short) stream.readbits(16);
        }
        // End
    }
    
    /**
     * Calculates framesize in bytes excluding Header Size.
     * nSlots is framesize excluding Header, CRC and Side Info
     */
    /* only supports mpeg1 frames; stripped out mpeg2 checking */
//    private final void calFrameSize() {
//   /*     framesize = (144 * bitrates[h_version] [h_layer - 1] [h_bitrate_index]) / frequencies[h_version] [h_sample_frequency];
//        if(h_padding_bit != 0)
//            framesize++;*/
//        framesize = CommonMethods.calFrameSize(h_bitrate_index, h_padding_bit, frequencies[h_version] [h_sample_frequency]);
//        
//        if(h_version == MPEG1)
//            nSlots = CommonMethods.calNSlots(framesize, h_mode, h_protection_bit);
//        // E.B Fix
//        //nSlots = framesize - ((h_mode == SINGLE_CHANNEL) ? 17 : 32) -  ((h_protection_bit!=0) ? 0 : 2) - 4;
//        //nSlots = framesize - ((h_mode == SINGLE_CHANNEL) ? 17 : 32) - 4;
//        // End.
//        
//        else{
//            System.out.println("sorry,doesn't support mpeg2 frames - only LayerIII is supported");
//            System.exit(1);
//        }
//        
//        framesize -= 4;
//    }
//    
    public int get_channels(){
        int channels = 2;
        String mode = "stereo";
        switch (h_mode) {
            case JOINT_STEREO:
                mode = "joint_stereo"; channels = 2; break;
            case DUAL_CHANNEL:
                mode = "dual_channel"; channels = 2; break;
            case SINGLE_CHANNEL:
                mode = "single_channel"; channels = 1; break;
        }
        
        return channels;
    }
    
    public int get_protection_bit(){
        return h_protection_bit;
    }
    
    public int get_framesize(){
        return this.framesize;
    }
    //
    /** */
    
//    public void create_main_info(LayerIIIEnc mp3){
//        // modificare in futuro: va uniformata rispetto al resto delle classi
//        int channels = 2;
//
//        String mode = "stereo";
//        System.out.println(h_mode);
//        switch (h_mode) {
//            case JOINT_STEREO:
//                mode = "joint_stereo"; channels = 2; break;
//            case DUAL_CHANNEL:
//                mode = "dual_channel"; channels = 2; break;
//            case SINGLE_CHANNEL:
//                mode = "single_channel"; channels = 1; break;
//        }
//
//        channels = 2;
//        boolean MS_stereo = false;
//        boolean intensity_stereo = false;
//
//        switch (h_mode_extension) {
//            case 1:
//                MS_stereo = true; break;
//            case 2:
//                intensity_stereo = true; break;
//            case 3:
//                intensity_stereo = true; MS_stereo = true; break;
//        }
//        //System.out.print("[ok]\nheader: setting main_info.......");
//        int fs = frequencies[h_version] [h_sample_frequency];
//        int bitrate = bitrates[h_version] [h_layer - 1] [h_bitrate_index];
//        mp3.init_frame(1, bitrate, fs, mode, intensity_stereo, MS_stereo, 0, channels);
//    }
//
//
//    public void create_main_info(LayerIIIEnc mp3, int channels){
//        // modificare in futuro: va uniformata rispetto al resto delle classi
//
//        channels = 2;
//        String mode = "stereo";
//        switch (h_mode) {
//            case JOINT_STEREO:
//                mode = "joint_stereo"; channels = 2; break;
//            case DUAL_CHANNEL:
//                mode = "dual_channel"; channels = 2; break;
//            case SINGLE_CHANNEL:
//                mode = "single_channel"; channels = 1; break;
//        }
//
//
//        boolean MS_stereo = false;
//        boolean intensity_stereo = false;
//
//        switch (h_mode_extension) {
//            case 1:
//                MS_stereo = true; break;
//            case 2:
//                intensity_stereo = true; break;
//            case 3:
//                intensity_stereo = true; MS_stereo = true; break;
//        }
//        //System.out.print("[ok]\nheader: setting main_info.......");
//        int fs = frequencies[h_version] [h_sample_frequency];
//        int bitrate = bitrates[h_version] [h_layer - 1] [h_bitrate_index];
//        mp3.init_frame(1, bitrate, fs, mode, intensity_stereo, MS_stereo, 0, channels);
//    }
    
    public void copy_info(FrameData fd){
        // modificare in futuro: va uniformata rispetto al resto delle classi
        
         /*public void setInfo(int version, int bitrate, int fs, String mode, boolean intensity_stereo,
            boolean MS_stereo, int emphasis, int channels, int h_mode, int h_protection_bit, int h_padding_bit) {*/
        int fs = frequencies[h_version] [h_sample_frequency];
        int bitrate = bitrates[h_version] [h_layer - 1] [h_bitrate_index];
        int channels = 2;
        
        String mode = "stereo";
        switch (h_mode) {
            case JOINT_STEREO:
                mode = "joint_stereo"; channels = 2; break;
            case DUAL_CHANNEL:
                mode = "dual_channel"; channels = 2; break;
            case SINGLE_CHANNEL:
                mode = "single_channel"; channels = 1; break;
        }
        
        
        boolean MS_stereo = false;
        boolean intensity_stereo = false;
        
        
        fd.setInfo(h_version, bitrate, fs, mode, h_mode_extension, h_emph, channels, h_mode, h_protection_bit, h_padding_bit);
        fd.setBitrateIndex(h_bitrate_index);
    }

    public int get_max_gr() {
        return 2;
    }
}
