/*
 * Filtering.java
 *
 * Created on 21 maggio 2007, 10.03
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.guetal.mp3.processing.effects;

import java.io.InputStream;

import org.guetal.mp3.processing.commons.Constants;
import org.guetal.mp3.processing.commons.Manager;
import org.guetal.mp3.processing.commons.data.FrameDataQuantized;
import org.guetal.mp3.processing.commons.util.MathUtil;

/**
 *
 * @author Administrator
 */
public class Filtering {
    
    Manager reader;
    byte [] stream;
    /** Creates a new instance of Filtering */
    public Filtering() {
    }
    
    public byte [] filter(InputStream is, int fStart, int fEnd, double [] filter) throws Exception{
        
        
        int cont = 0, bytes = 0;
        final int opt = Constants.QUANTIZED_DOMAIN;
        double param = (3.0/4.0);
        
        if( fStart > fEnd ){
            throw new Exception("fStart can't be greater than fEnd!!");
        }
        
        reader = new Manager(is);
        
        for(int i = 0; i < filter.length; i++){
            filter[i] = (float) MathUtil.pow((double)filter[i], param);
        }
        
        do{
            try{
                FrameDataQuantized fd = (FrameDataQuantized) reader.decodeFrame(opt);
                if((cont >= fStart) && (cont < fEnd)){
                    for(int gr = 0; gr < fd.getMaxGr(); gr++)
                        for(int ch = 0; ch < fd.getChannels(); ch++) {
                        int [] ix = fd.getIx(ch, gr);
                        int data[] = new int[576];
                        
                        if(fd.getBlockType(ch,gr) != 2 ){
                            for(int line = 0; line < 576; line++)
                                data[line] = (int)Math.floor(ix[line] * filter[line]);
                        } else {
                            for (int line = 0; line < 574; line += 3 ) {
                                data[ line ] = (int)Math.floor(ix[ line ] * filter[line]);
                                data[ line + 1 ] = (int)Math.floor(ix[ line + 1 ] * filter[line]);
                                data[ line + 2 ] = (int)Math.floor(ix[ line + 2 ] * filter[line]);
                            }

                            
                        }
                        
                        fd.setIx(data, ch, gr);
                        }
                }
                
                
                reader.encodeFrame(fd);
                
                bytes += fd.getFramesize();
                
            } catch (Exception ex){
                System.out.println("End of file");
                break;
            }
            
            cont++;
        }while(cont < 2000);
       // while(true);
        
        stream = reader.getStream();
        
        
        return stream;
    }
    
    public byte [] filter(InputStream is, int fStart, double [] filter){
        return stream;
    }
    
    public byte [] filter(InputStream is, double [] filter){
        return stream;
    }
}
