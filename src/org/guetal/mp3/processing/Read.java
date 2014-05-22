/*
 * Read.java
 *
 * Created on 11 dicembre 2006, 16.59
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.guetal.mp3.processing;
import java.io.IOException;
import java.io.InputStream;
/**
 *
 * @author Tommy
 */
public class Read {
    private InputStream is;
    
    /** Creates a new instance of Read */
    public Read() throws IOException {
        byte [] b = new byte [50000];
        //String fileName = "/audio/440-128-CBR.mp3";     //2446
        String fileName = "/audio/trombe.mp3";     //2446
        //String fileName = "/audio/shine.mp3";         //0?
        //String fileName = "/audio/vari.mp3";
        is = getClass().getResourceAsStream(fileName);
        
        is.read(b,0,50000);
        
        for(int i = 0; i < b.length; i++)
        {
            System.out.print("byte [" + i + "]: " + b[i]);
            if((i % 418) == 25)  System.out.print(" <-- begin of frame " + (i / 418 + 1));
            
            if((i % 418) == 29){
                System.out.print(" <-- main_data_beg: ");
                int beg = (b[i] >= 0) ? ((b[i] << 1)+((b[i+1]>>> 7) & 1)): (((b[i] + 256) << 1)+((b[i+1]>>> 7) & 1));
                System.out.print(beg);
            }
            
            if((i % 418) == 61)  System.out.print(" <-- begin of data in frame");
            System.out.print("\n");       
        }
    }
    
}
