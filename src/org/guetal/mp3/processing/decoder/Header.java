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
import java.util.logging.Logger;

import org.guetal.mp3.processing.commons.CommonMethods;
import org.guetal.mp3.processing.commons.data.FrameData;


/**
 * Class for extracting information from a frame header. Contains many informations about mp3 frame, i.e. framerate, framesize..
 * @author     micah
 * @created    DecHeader8, 2001
 */
public final class Header {
    
	private final static Logger LOGGER = Logger.getLogger(Header.class.getName()); 
	
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
    public int getFramesize(){
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
                LOGGER.info("MPEG version: "+h_version);
                if ((h_sample_frequency = ((headerstring >>> 10) & 3)) == 3) {
                    return;
                }
                LOGGER.info("Sampling frequency: " + frequencies[h_version] [h_sample_frequency]);
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
//        if (h_protection_bit == 0) {
//            // frame contains a crc checksum
//            short checksum = (short) stream.readbits(16);
//        }
        // End
    }
    
    public int get_channels(){
        int channels = 2;
        switch (h_mode) {
            case JOINT_STEREO:
                channels = 2; break;
            case DUAL_CHANNEL:
                channels = 2; break;
            case SINGLE_CHANNEL:
                channels = 1; break;
        }
        
        return channels;
    }
    
    public int get_protection_bit(){
        return h_protection_bit;
    }

    
    public void copy_info(FrameData fd){
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
        
        fd.setInfo(h_version, bitrate, fs, mode, h_mode_extension, h_emph, channels, h_mode, h_protection_bit, h_padding_bit);
        fd.setBitrateIndex(h_bitrate_index);
    }

    public int get_max_gr() {
        return 2;
    }
}
