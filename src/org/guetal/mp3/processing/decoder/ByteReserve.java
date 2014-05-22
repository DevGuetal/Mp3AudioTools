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
    private byte [] byte_buf = new byte[BUF_SIZE];  // circular buffer containing bytes of bit reservoir
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
        //index = (index < BUF_SIZE - 1)? 0: index++;
        index = (index + 1) % BUF_SIZE;
        byte_buf[index] = value;
    }
    
    /**
     *  Returns N-th byte of byte_buf
     *  @param  N   position of byte we need
     */
    
    private byte get_byte_buf(int N){
        return byte_buf[N];
    }
  
    
    
    /**
     *  Returns byte array containing bytes of byte_buf, from the position 
     *  indicated by start till the position indicated by end
     *
     *  @param  start   start position
     *  @param  end     end position
     */
    private byte[] get_byte_buf(int start, int end){
        int len = end - start;
        byte [] temp = new byte[len];
          
    /*    for(int i = 0; i < len; i++)
            temp[i] = byte_buf[i+start];*/
        System.arraycopy(byte_buf, start, temp, 0, len);
        
        return temp;
    }
    


    /**
     *  Returns bytes array containing bytes of byte_buf, starting from index - main_data_begin to index.
     */
    public byte[] read_from_reserve(int main_data_beg) {
        //int main_data_beg = sideinfo.get_main_data_begin();
        int start = 0;
        byte [] res_buf = new byte[main_data_beg];

        if(main_data_beg > index){
//System.out.println("A");
            start = BUF_SIZE - main_data_beg + index + 1; 
            int i = 0;
            
           /* for(i = start; i < BUF_SIZE; i++)
                res_buf[i - start] = byte_buf[i];*/
            System.arraycopy(byte_buf, start, res_buf, 0, BUF_SIZE - start);

            int off_arr =  main_data_beg - index - 1;   

          /*  for(i = 0; i <= index; i++)
                res_buf[i + off_arr] = byte_buf[i];*/
            System.arraycopy(byte_buf, 0, res_buf, off_arr, index + 1);

        } else {
//System.out.println("B");
            start = index - main_data_beg + 1;
            /*for(int i = start; i <= index; i++)
                res_buf[i - start] = byte_buf[i];*/
            System.arraycopy(byte_buf, start, res_buf, 0, (index - start + 1));
        }

//        for(int i = 0; i < main_data_beg; i++)
//            System.out.println("res_buf["+i+"]: " + res_buf[i]);
        return res_buf;
    }
}
