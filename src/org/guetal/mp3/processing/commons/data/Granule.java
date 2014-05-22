/*
 * Granule.java
 *
 * Created on 24 aprile 2007, 20.26
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.guetal.mp3.processing.commons.data;

import org.guetal.mp3.processing.decoder.*;

/**
 *
 * @author Administrator
 */


public class Granule {
    public final Channel[] ch;
    //public int [][] scfsi;
    
    public Granule(final int CHANNELS) {
        ch = new Channel[CHANNELS];

        for(int i = 0; i<CHANNELS; i++)
            ch[i] = new Channel();
    }
    
}
