/*
 * SideInfo.java
 *
 * Created on 24 aprile 2007, 20.25
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.guetal.mp3.processing.commons.data;

/**
 *
 * @author Administrator
 */
public class SideInfo {
    
    //public final int[] scfsi = new int[4];
    public int main_data_begin;
    // public int public_bits;  ?????
    public final Granule[] gr;
    public int [][] scfsi;// = new int[4];             // determines weather the same scalefactors are transferred for both granules or not.
    
    /**
     * Creates a new instance of SideInfo
     */
    public SideInfo(final int MAX_GR, final int CHANNELS) {
        gr = new Granule[MAX_GR];
        
        for(int i = 0; i < MAX_GR; i ++)
            gr[i] = new Granule(CHANNELS);
        
        scfsi = new int [CHANNELS][4];
    }
    
    public void setMainDataBegin( int main_data_begin){
        this.main_data_begin = main_data_begin;
    }
    
    
    public int getMainDataBegin() {
        return this.main_data_begin;
    }
    
    
    
}
