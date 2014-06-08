/*
 * Read.java
 *
 * Created on 11 dicembre 2006, 16.59
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.guetal.mp3.processing;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Logger;

import org.guetal.mp3.processing.checker.Checker;
/**
 *
 * @author Tommy
 */
public class Read {
 
    private final static Logger LOGGER = Logger.getLogger(Checker.class.getName()); 
    
    /** Creates a new instance of Read */
    public Read() throws IOException {
        byte [] b = new byte [50000];
        
        String fileName = System.getProperty("user.dir") + "/output/test.mp3";
        File inputFile = new File(fileName);
        InputStream is = new FileInputStream(inputFile);
        
        is.read(b,0,50000);
        
        for(int i = 0; i < b.length; i++)
        {
        	StringBuffer sb = new StringBuffer();
        	sb.append("byte [" + i + "]: " + b[i]);
            if((i % 418) == 25)  sb.append(" <-- begin of frame " + (i / 418 + 1));
            
            if((i % 418) == 29){
                System.out.print(" <-- main_data_beg: ");
                int beg = (b[i] >= 0) ? ((b[i] << 1)+((b[i+1]>>> 7) & 1)): (((b[i] + 256) << 1)+((b[i+1]>>> 7) & 1));
                sb.append(beg);
            }
            
            if((i % 418) == 61)  sb.append(" <-- begin of data in frame");

            LOGGER.info(sb.toString());
        }
        
        is.close();
    }
    
}
