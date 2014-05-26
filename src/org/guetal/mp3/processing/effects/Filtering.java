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
            System.out.println("filter["+i+"]: "+filter[i]);
        }
        
        do{
            
            System.out.println("---------------------------------------------(Step 1 - Filtering - Iteration number: " + cont + ")---------------------------------------------");
            
            try{
                FrameDataQuantized fd = (FrameDataQuantized) reader.decodeFrame(opt);
                if((cont >= fStart) && (cont < fEnd)){
                    for(int gr = 0; gr < fd.getMaxGr(); gr++)
                        for(int ch = 0; ch < fd.getChannels(); ch++) {
                        int [] ix = fd.getIx(ch, gr);
                        int data[] = new int[576];
                        
                        // System.out.println("mixed block flag: " + fd.getMixedBlockFlag(ch, gr));
                        
                        if(fd.getBlockType(ch,gr) != 2 ){
                            //System.out.println("ramo A - block type: " + fd.getBlockType(ch, gr));
                            for(int line = 0; line < 576; line++)
                                data[line] = (int)Math.floor(ix[line] * filter[line]);
                        } else {
                            //System.out.println("blocco B - block type: " + fd.getBlockType(ch, gr));
                            //for (int window = 0; window < 3; window++ )
                            for (int line = 0; line < 574; line += 3 ) {
                                data[ line ] = (int)Math.floor(ix[ line ] * filter[line]);
                                data[ line + 1 ] = (int)Math.floor(ix[ line + 1 ] * filter[line]);
                                data[ line + 2 ] = (int)Math.floor(ix[ line + 2 ] * filter[line]);
                                
//                                data[line] = ix [line];
//                                if(line >= 0 && line <= 30)
//                                    data[line] = 0;
//                                if(line >= 192 && line <= 30+192)
//                                    data[line] = 0;
//                                if(line >= 384 && line <= 30+384)
//                                    data[line] = 0;
                            }
//
                            //  for(int i = 0; i < 576; i++)
                            //    System.out.println("data["+i+"]: " + data[i]);
//                            for (int line = 0; line < 192; line ++ )
//                                {
//                                    data[ line] = (int)(ix[ line]* filter[line *3]);
//                            }
//
//                            for (int line = 0; line < 192; line ++ )
//                                {
//                                    data[ line + 192] = (int)(ix[ line + 192] * filter[line*3]);
//                            }
////
//
//                            for (int line = 0; line < 192; line ++ )
//                                {
//                                    data[ line + 384] = (int)(ix[ line + 384] * filter[line*3]);
//                            }
//                            int [] scalefac = Commons.sfBandIndex[3][1];
//                            if ( fd.getMixedBlockFlag(ch,gr) == 0 ) { /* Three short blocks */
//                                /*
//                                Within each scalefactor band, data is given for successive
//                                time windows, beginning with window 0 and ending with window 2.
//                                Within each window, the quantized values are then arranged in
//                                order of increasing frequency...
//                                 */
//                                int sfb, window, line, start, end;
//
//                                for ( sfb = 0; sfb < 13; sfb++ ) {
//                                    start = scalefac[ sfb ];
//                                    end   = scalefac[ sfb+1 ];
//
//                                    System.out.println("star("+sfb+")t: " + start );
//                                    System.out.println("end("+sfb+")t: " + end);
//
//                                     for ( line = start; line < end; line += 2 )
//                                        for ( window = 0; window < 3; window++ ){
//                                        System.out.println(((line)*3+window) + " | filtro pos: " + (line*3)+ " | linea" + line);
//                                        System.out.println(((1+line)*3+window) + " | filtro pos: " + (line*3)+ " | linea" + line);
//                                            data[line * 3 + window] = (int)(ix[line * 3 + window] * filter[(line)* 3]);
//                                            data[(line + 1) * 3 + window] = (int)(ix[(line + 1)* 3 + window] * filter[(line )* 3]);
//                                        }
//
//                                }
//                            }
                            
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
