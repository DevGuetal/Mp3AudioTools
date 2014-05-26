/*
 * Init.java
 *
 * Created on 12 novembre 2006, 18.39
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.guetal.mp3.processing.decoder;

public final class ByteReserve {
    private final int BUF_SIZE = 511 * 20;
    private byte [] byteBuf = new byte[BUF_SIZE];  // circular buffer containing bytes of bit reservoir
    private int index = 0;
    
    public ByteReserve(){
    }
   
    /**
     */
    public void store_in_reserve(byte [] data_buf){
        int len = data_buf.length;
        
        for(int i = 0; i < len; i++ )
            set_byte_buf(data_buf[i]);
    }
    
    /** Store byte value in circular byte buffer byte_buf
     *
     *  @param  value   byte containing the value we want to set in byte_buf
     */
    private void set_byte_buf(byte value){
        index = (index + 1) % BUF_SIZE;
        byteBuf[index] = value;
    }

    /**
     *  Returns bytes array containing bytes of byte_buf, starting from index - main_data_begin to index.
     */
    public byte[] read_from_reserve(int main_data_beg) {
        //int main_data_beg = sideinfo.get_main_data_begin();
        int start = 0;
        byte [] res_buf = new byte[main_data_beg];

        if(main_data_beg > index){
            start = BUF_SIZE - main_data_beg + index + 1; 

            System.arraycopy(byteBuf, start, res_buf, 0, BUF_SIZE - start);

            int off_arr =  main_data_beg - index - 1;   

            System.arraycopy(byteBuf, 0, res_buf, off_arr, index + 1);

        } else {
            start = index - main_data_beg + 1;
            System.arraycopy(byteBuf, start, res_buf, 0, (index - start + 1));
        }

        return res_buf;
    }
}
