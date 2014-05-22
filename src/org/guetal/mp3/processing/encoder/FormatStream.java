package org.guetal.mp3.processing.encoder;

import org.guetal.mp3.processing.commons.util.MathUtil;


/**
 *  Class providing methods for formatting data in array
 *  @author Thomas Gualino
 */

public final class FormatStream {
    private int pos_bit = 0;    // actual bit in array[index]
    private int index = 0;      // actual position
    
    private byte [] array;      // array containing informations
    
    /** Creates a new instance of FormatStream */
    public FormatStream(int len) {
        array = new byte[len];
    }
    
    /** Provide simple management of side_info_array. Param is stored in array using len bits*/
    public void store_in_array( int param, int len ){
        final int SIZE = 8;
        int free_space = SIZE - pos_bit;
        
        if(len >= free_space){
            int mask = (MathUtil.pow(2, free_space) - 1) << (len - free_space);
            int shift = len - free_space;
            
            array[index] |= ( (param & mask) >>> shift );
            pos_bit = 0;
            param -= ( param & mask );
            len -= free_space;
            index ++;
            
            if(len > 0)
                store_in_array( param, len );
        } else {
            
            int mask = MathUtil.pow( 2, len ) - 1;
            int shift = free_space - len;
            
            array[ index ] |= ( ( param & mask ) << shift );
            pos_bit = SIZE - shift;
            
        }
    }
    
    
    /*
    public int get_pos(){
        return pos_bit;
    }*/
    
    
    /** Getter for array
     * @return  array   the entire array containg data previously stored
     */
    public byte [] get_array(){
        return array;
    }
    
    
    /** Getter for array
     * @param   len     desired length in bytes
     * @return  array   first len bytes containg data previously stored
     */
    public byte [] get_array( int len ){
        byte[] ans = new byte[len];
        
        for(int i = 0; i < len; i++)
            ans[i] = array[i];
        
        return ans;
    }
}
