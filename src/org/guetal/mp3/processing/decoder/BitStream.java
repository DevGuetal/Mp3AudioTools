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
import java.io.InputStream;

/**
 *  The <code>Bistream</code> class is responsible for parsing an MPEG audio bitstream. <b>REVIEW:</b> much of the parsing currently occurs in the
 * various decoders. This should be moved into this class and associated inner classes.
 *@author     micah
 *@created    December 8,2001
 */
public final class BitStream {
    
    static final byte INITIAL_SYNC = 0;
    static final byte STRICT_SYNC = 1;
    private final static int BUFFER_INT_SIZE = 433;
    private final static int bitmask[] = {0,
    0x00000001,0x00000003,0x00000007,0x0000000F,
    0x0000001F,0x0000003F,0x0000007F,0x000000FF,
    0x000001FF,0x000003FF,0x000007FF,0x00000FFF,
    0x00001FFF,0x00003FFF,0x00007FFF,0x0000FFFF,
    0x0001FFFF};
    
    private static PushBackStream source;
    private static final int[] framebuffer = new int[BUFFER_INT_SIZE];
    private static int framesize;
    private static final byte[] frame_bytes = new byte[BUFFER_INT_SIZE * 4];
    private static int wordpointer;
    private static int bitindex;
    private static int syncword;
    private static boolean single_ch_mode;
    private final static Header header = new Header();
    private final static byte syncbuf[] = new byte[4];
    
    
    public BitStream(InputStream in) {
        source = new PushBackStream(in,512);
        closeFrame();
    }
    
    static int read;
    
    public final boolean isSyncCurrentPosition(int syncmode) throws IOException {
        if ((read = source.read(syncbuf,0,4)) > 0) {  //if(read>=0){
            source.unread(syncbuf,0,read);
            if (read == 4){
                headerstring = ((syncbuf[0] << 24) & 0xFF000000) | ((syncbuf[1] << 16) & 0x00FF0000) | ((syncbuf[2] << 8) & 0x0000FF00) | ((syncbuf[3] << 0) & 0x000000FF);
                return isSyncMark(headerstring,syncmode,syncword);
            }
        }
        
        return true;
    }
    
    /**
     *  Gets the syncMark attribute of the Bitstream object
     *@param  headerstring  Description of Parameter
     *@param  syncmode      Description of Parameter
     *@param  word          Description of Parameter
     *@return               The syncMark value
     */
    static boolean sync;
    
    public final boolean isSyncMark(int headerstring,int syncmode,int word) {
        if (syncmode == INITIAL_SYNC) {
            sync = ((headerstring & 0xFFF00000) == 0xFFF00000);
        } else {
            sync = ((headerstring & 0xFFF80C00) == word) && (((headerstring & 0x000000C0) == 0x000000C0) == single_ch_mode);
        }
        // filter out invalid sample rate
        if(sync)
            if(sync = (((headerstring >>> 10) & 3) != 3))
                if(sync = (((headerstring >>> 17) & 3) != 0))
                    sync = (((headerstring >>> 19) & 3) != 1);
        
        if(sync) first_sync = true;
        
        return sync;
    }
    
    
    static int sum,returnvalue;
    
    public final int readbits(int num) {
        sum = bitindex + num;
        if (sum <= 32) {
            returnvalue = (framebuffer[wordpointer] >>> (32 - sum)) & bitmask[num];
            if ((bitindex += num) == 32) {
                bitindex = 0;
                wordpointer++;
            }
            
            return returnvalue;
        }
        
        returnvalue = (((framebuffer[wordpointer++] & 0x0000FFFF) << 16) & 0xFFFF0000) | (((framebuffer[wordpointer] & 0xFFFF0000) >>> 16) & 0x0000FFFF);
        returnvalue >>>= 48 - sum;
        returnvalue &= bitmask[num];
        bitindex = sum - 32;
        return returnvalue;
    }
    
    /**
     *  Description of the Method
     *@exception  IOException  Description of Exception
     */
    public void close() throws IOException {
        source.close();
    }
    
    /**
     *  Reads and parses the next frame from the input source.
     *@return                  the Header describing details of the frame read,or null if the end of the stream has been reached.
     *@exception  IOException  Description of Exception
     */
    public Header readFrame() throws IOException {
        //if (framesize == -1) {
        header.read_header(this);
        //}
        return header;
    }
    
    /**
     *  Unreads the bytes read from the frame.
     *@exception  IOException      Description of Exception
     *@throws  BitstreamException
     */
    // REVIEW: add new error codes for this.
    public final void unreadFrame() throws IOException {
        if (wordpointer == -1 && bitindex == -1 && (framesize > 0)) {
            source.unread(frame_bytes,0,framesize);
        }
    }
    
    /** Description of the Method */
    public void closeFrame() {
        framesize = wordpointer = bitindex = -1;
    }
    
    /**
     *  Set the word we want to sync the header to. In Big-Endian byte order
     *@param  syncword0  Description of Parameter
     */
    final void set_syncword(int syncword0) {
        syncword = syncword0 & 0xFFFFFF3F;
        single_ch_mode = ((syncword0 & 0x000000C0) == 0x000000C0);
    }
    
    static int bytesread,headerstring;
    
    byte [] id_v2 = new byte [5000];
    byte [] temp;
    int off = 3;
    boolean first_sync = false;
    boolean first_call = true;
    
    int syncHeader(byte syncmode) throws IOException {
        if((bytesread = source.read(syncbuf,0,3))!= 3)
            return -1;
        // return 0;
        
        headerstring = syncbuf[0] << 16 & 0xff0000 | syncbuf[1] << 8 & 0xff00 | syncbuf[2] << 0 & 0xff;
        
        if(first_call){
            id_v2 [0] = syncbuf[0];
            id_v2 [1] = syncbuf[1];
            id_v2 [2] = syncbuf[2];
            first_call = false;
        }
        //id_v2 [3] = syncbuf[3];
        
        // off = 4;
        
        do{
            headerstring <<= 8;
            if(source.read(syncbuf,3,1) != 1)
                //return 0;
                return -1;
            
            headerstring |= syncbuf[3] & 0xff;
            if(!first_sync){
                if(off+1 > id_v2.length){
                    temp = new byte [off + 4];
                    
                    System.arraycopy(id_v2, 0, temp, 0, id_v2.length);
                    id_v2 = temp;
                }
                
                id_v2 [off] = syncbuf[3];
                off ++;
            }
            
        }while(!isSyncMark(headerstring,syncmode,syncword));
        
        return headerstring;
    }
    
    
    public byte[] get_id_v2(){
        temp = new byte [off-4];
        System.arraycopy(id_v2, 0, temp, 0, temp.length);
        
        return temp;
    }
    /**
     *  Reads the data for the next frame. The frame is not parsed until parse frame is called.
     *@param  bytesize         Description of Parameter
     *@exception  IOException  Description of Exception
     */
    public final void read_frame_data(int bytesize) throws IOException {
        if(bytesize >= 0){
            framesize = bytesize;
            wordpointer = bitindex = -1;
            source.read(frame_bytes,0,bytesize);
        }
    }
    
    static int b,k;
    static byte b0,b1,b2,b3;
    
    /** Parses the data previously read with read_frame_data(). */
    final void parse_frame() {
        // Convert Bytes read to int
        for (k = 0,b=0; k < framesize; k += 4) {
            b0 = frame_bytes[k];
            if (k + 3 < framesize) {
                b3 = frame_bytes[k + 3];
                b2 = frame_bytes[k + 2];
                b1 = frame_bytes[k + 1];
            } else if (k + 2 < framesize) {
                b3 = 0;
                b2 = frame_bytes[k + 2];
                b1 = frame_bytes[k + 1];
            } else if (k + 1 < framesize) {
                b2 = b3 = 0;
                b1 = frame_bytes[k + 1];
            }
            // 4 bytes are stored in a int array
            framebuffer[b++] = ((b0 << 24) & 0xFF000000) | ((b1 << 16) & 0x00FF0000) | ((b2 << 8) & 0x0000FF00) | (b3 & 0x000000FF);
        }
        
        wordpointer = bitindex = 0;
    }
    
    public int[] get_frame_buffer(){
        return framebuffer;
    }
    
    public byte[] get_main_data(){
        int offset = framesize - header.nSlots;
        int end = header.nSlots + offset;
        byte [] ans = new byte[header.nSlots];
        
        System.arraycopy(frame_bytes, offset, ans, 0, ans.length);
        
        return ans;
    }
    
    public byte[] get_side_info(){
        int channels = header.get_channels();
        int protection_bit = header.get_protection_bit();
        
        int len = (channels == 1) ? 17: 32;
        int start = (protection_bit == 0) ? 2: 0;
        
        byte [] ans = new byte[len];
        
        /*for(int i = start; i < len + start; i++)
            ans[i - start] = frame_bytes[i];*/
        System.arraycopy(frame_bytes, start, ans, 0, len);
        
        return ans;
    }
}
