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

import java.util.logging.Logger;

import org.guetal.mp3.processing.commons.tables.D16;
import org.guetal.mp3.processing.commons.tables.D17;
import org.guetal.mp3.processing.commons.tables.D18;

final class HuffmanTables {

	private final static Logger LOGGER = Logger.getLogger(HuffmanTables.class.getName()); 

	public static HuffmanTables[] ht;
	private final static int MXOFF = 250;
	private final static int HTN = 34;


	private char				 tablename0 = ' ';      /* string, containing table_description   */
	private char				 tablename1 = ' ';      /* string, containing table_description   */
	private char				 tablename2 = ' ';      /* string, containing table_description   */

	private int				 xlen; 			        /* max. x-index+                          */
	private int				 ylen;	                /* max. y-index+				          */
	private int				 linbits; 		        /* number of linbits   	                  */
	private int 				 linmax;		        /* max number to be stored in linbits	  */
	private int				 ref;			        /* a positive value indicates a reference */
	private int []				 table=null;	        /* pointer to array[xlen][ylen]		      */
	private int []   			 hlen=null;             /* pointer to array[xlen][ylen]		      */
	private short [][]			 val=null;		        /* decoder tree		    	              */
	private int 				 treelen;	            /* length of decoder tree  	              */

	private static int dmask = 1 << ((4 * 8) - 1);
	private static int hs = 4 * 8;
	private static int point,error,level;

	public static class Huffman{
		static int x,y,v,w;
	}

	private HuffmanTables(String S,int XLEN,int YLEN,int LINBITS,int LINMAX,int REF,final short[] [] VAL,int TREELEN) {

		tablename0 = S.charAt(0);
		tablename1 = S.charAt(1);
		tablename2 = S.charAt(2);
		xlen = XLEN;
		ylen = YLEN;
		linbits = LINBITS;
		linmax = LINMAX;
		ref = REF;
		val = VAL;
		treelen = TREELEN;
	}

	/**
	 * Do the huffman-decoding.
	 * note! for counta,countb -the 4 bit value is returned in y,
	 * discard x.
	 */
	public static void decode(final HuffmanTables h,final Huffman huff,final BitReserve br) {
		// array of all huffcodtable headers
		// 0..31 Huffman code table 0..31
		// 32,33 count1-tables

		point = 0;
		level = dmask;
		/*
        HuffmanTables.Huffman.x = 0;
        HuffmanTables.Huffman.y = 0;*/
		short[][] temp = h.val;

		/*   da mettere poi eventualmente per accelerare il codice
        //table 0 needs no bits 
        if ( h.treelen == 0) {
            HuffmanTables.Huffman.x = HuffmanTables.Huffman.y = 0;
            //return 0;
        }*/

		//{{2,1},{0,0},{2,1},{0,16},{2,1},{0,1},{0,17}};

		/*
         Huffman19  = {
	{2,1},{0,0},{8,1},{4,1},{2,1},{0,8},{0,4},{2,1},{0,1},{0,2},
	{8,1},{4,1},{2,1},{0,12},{0,10},{2,1},{0,3},{0,6},{6,1},{2,1},
	{0,9},{2,1},{0,5},{0,7},{4,1},{2,1},{0,14},{0,13},{2,1},{0,15},
	{0,11},
	};

         Huffman20 = {
	{16,1},{8,1},{4,1},{2,1},{0,0},{0,1},{2,1},{0,2},{0,3},{4,1},
	{2,1},{0,4},{0,5},{2,1},{0,6},{0,7},{8,1},{4,1},{2,1},{0,8},
	{0,9},{2,1},{0,10},{0,11},{4,1},{2,1},{0,12},{0,13},{2,1},{0,14},
	{0,15},
	};
		 */

		//decodifica vera e propria
		do {
			/*end of tree*/
			if (temp[point] [0] == 0) {
				HuffmanTables.Huffman.x = h.val[point] [1] >>> 4;
	HuffmanTables.Huffman.y = h.val[point] [1] & 0xf;
	break;
			}

			// MXOFF = 250
			if (br.hget1bit() != 0) {
				while (temp[point] [1] >= MXOFF) {
					point += temp[point] [1];
				}
				point += temp[point] [1];
			} else {
				while (temp[point] [0] >= MXOFF) {
					point += temp[point] [0];
				}
				point += temp[point] [0];
			}
			level >>= 1;
			// MDM: ht[0] is always 0;
		} while ((level != 0) || (point < 0));

		/* Process sign encodings for quadruples tables. */
		//tavole 32 e 33
		if (h.tablename0 == '3' && (h.tablename1 == '2' || h.tablename1 == '3')) {
			/*HuffmanTables.Huffman.v = (HuffmanTables.Huffman.y >>> 3) & 1;
            HuffmanTables.Huffman.w = (HuffmanTables.Huffman.y >>> 2) & 1;
            HuffmanTables.Huffman.x = (HuffmanTables.Huffman.y >>> 1) & 1;
            HuffmanTables.Huffman.y = HuffmanTables.Huffman.y & 1;
			 */
			HuffmanTables.Huffman.x = (HuffmanTables.Huffman.y >>> 2) & 1;
			HuffmanTables.Huffman.w = (HuffmanTables.Huffman.y >>> 1) & 1;
			HuffmanTables.Huffman.v = HuffmanTables.Huffman.y & 1;
			HuffmanTables.Huffman.y = (HuffmanTables.Huffman.y >>> 3) & 1;

			if (HuffmanTables.Huffman.v != 0) {
				if (br.hget1bit() != 0) {
					HuffmanTables.Huffman.v *= -1; //-v[0];
				}
			}
			if (HuffmanTables.Huffman.w != 0) {
				if (br.hget1bit() != 0) {
					HuffmanTables.Huffman.w *= -1; //-w[0];
				}
			}
			if (HuffmanTables.Huffman.x != 0) {
				if (br.hget1bit() != 0) {
					HuffmanTables.Huffman.x *= -1; //-x[0];
				}
			}
			if (HuffmanTables.Huffman.y != 0) {
				if (br.hget1bit() != 0) {
					HuffmanTables.Huffman.y *= -1; //-y[0];
				}
			}



			//            
			//            if (HuffmanTables.Huffman.y != 0) {
			//                if (br.hget1bit() != 0) {
			//                    HuffmanTables.Huffman.y *= -1; //-y[0];
			//                }
			//            }
			//            if (HuffmanTables.Huffman.x != 0) {
			//                if (br.hget1bit() != 0) {
			//                    HuffmanTables.Huffman.x *= -1; //-x[0];
			//                }
			//            }
			//            if (HuffmanTables.Huffman.w != 0) {
			//                if (br.hget1bit() != 0) {
			//                    HuffmanTables.Huffman.w *= -1; //-w[0];
			//                }
			//            }
			//            if (HuffmanTables.Huffman.v != 0) {
			//                if (br.hget1bit() != 0) {
			//                    HuffmanTables.Huffman.v *= -1; //-v[0];
			//                }
			//            }
		} else {

			// linbits != 0 => si legge n = linbits bits dallo stream e si sommano a x ed y
			// Process sign and escape encodings for dual tables.

			if (h.linbits != 0) {
				if ((h.xlen - 1) == HuffmanTables.Huffman.x) {
					HuffmanTables.Huffman.x += br.hgetbits(h.linbits);
				}
			}
			if (HuffmanTables.Huffman.x != 0) {
				if (br.hget1bit() != 0) {
					HuffmanTables.Huffman.x *= -1; //-x[0];
				}
			}
			if (h.linbits != 0) {
				if ((h.ylen - 1) == HuffmanTables.Huffman.y) {
					HuffmanTables.Huffman.y += br.hgetbits(h.linbits);
				}
			}
			if (HuffmanTables.Huffman.y != 0) {
				if (br.hget1bit() != 0) {
					HuffmanTables.Huffman.y *= -1; //-y[0];
				}
			}
		}
	}



	public HuffmanTables(){
		//ObjectInputStream in = null;
		short array[][]=null;

		try{
			//in = new ObjectInputStream(this.getClass().getClassLoader().getResourceAsStream("huffman.dat"));

			//(String S,int XLEN,int YLEN,int LINBITS,int LINMAX,int REF,final short[] [] VAL,int TREELEN)
			ht = new HuffmanTables[HTN];
			array = D16.Huffman1;
			ht[0] = new HuffmanTables("0  ",0,0,0,0,-1, array,0);
			array = D16.Huffman2;
			ht[1] = new HuffmanTables("1  ",2,2,0,0,-1,  array,7);
			array = D16.Huffman3;
			ht[2] = new HuffmanTables("2  ",3,3,0,0,-1,  array,17);
			array = D16.Huffman4;
			ht[3] = new HuffmanTables("3  ",3,3,0,0,-1,  array,17);
			array = D16.Huffman5;
			ht[4] = new HuffmanTables("4  ",0,0,0,0,-1,  array,0);
			array = D16.Huffman6;
			ht[5] = new HuffmanTables("5  ",4,4,0,0,-1,  array,31);
			array = D16.Huffman7;
			ht[6] = new HuffmanTables("6  ",4,4,0,0,-1,  array,31);
			array = D16.Huffman8;
			ht[7] = new HuffmanTables("7  ",6,6,0,0,-1,  array,71);
			array = D16.Huffman9;
			ht[8] = new HuffmanTables("8  ",6,6,0,0,-1,  array,71);
			array = D17.Huffman10;
			ht[9] = new HuffmanTables("9  ",6,6,0,0,-1,  array,71);
			array = D17.Huffman11;
			ht[10] = new HuffmanTables("10 ",8,8,0,0,-1,  array,127);
			array = D17.Huffman12;
			ht[11] = new HuffmanTables("11 ",8,8,0,0,-1,  array,127);
			array = D17.Huffman13;
			ht[12] = new HuffmanTables("12 ",8,8,0,0,-1,  array,127);
			array = D17.Huffman14;
			ht[13] = new HuffmanTables("13 ",16,16,0,0,-1,  array,511);
			array = D17.Huffman15;
			ht[14] = new HuffmanTables("14 ",0,0,0,0,-1, array,0);
			array = D17.Huffman16;
			ht[15] = new HuffmanTables("15 ",16,16,0,0,-1,  array,511);
			array = D18.Huffman17;
			ht[16] = new HuffmanTables("16 ",16,16,1,1,-1,  array,511);
			array = D18.Huffman17;
			ht[17] = new HuffmanTables("17 ",16,16,2,3,16,  array,511);
			array = D18.Huffman17;
			ht[18] = new HuffmanTables("18 ",16,16,3,7,16,  array,511);
			array = D18.Huffman17;
			ht[19] = new HuffmanTables("19 ",16,16,4,15,16,  array,511);
			array = D18.Huffman17;
			ht[20] = new HuffmanTables("20 ",16,16,6,63,16,  array,511);
			array = D18.Huffman17;
			ht[21] = new HuffmanTables("21 ",16,16,8,255,16,  array,511);
			array = D18.Huffman17;
			ht[22] = new HuffmanTables("22 ",16,16,10,1023,16,  array,511);
			array = D18.Huffman17;
			ht[23] = new HuffmanTables("23 ",16,16,13,8191,16,  array,511);
			array = D18.Huffman18;
			ht[24] = new HuffmanTables("24 ",16,16,4,15,-1,  array,512);
			array = D18.Huffman18;
			ht[25] = new HuffmanTables("25 ",16,16,5,31,24,  array,512);
			array = D18.Huffman18;
			ht[26] = new HuffmanTables("26 ",16,16,6,63,24,  array,512);
			array = D18.Huffman18;
			ht[27] = new HuffmanTables("27 ",16,16,7,127,24,  array,512);
			array = D18.Huffman18;
			ht[28] = new HuffmanTables("28 ",16,16,8,255,24,  array,512);
			array = D18.Huffman18;
			ht[29] = new HuffmanTables("29 ",16,16,9,511,24,  array,512);
			array = D18.Huffman18;
			ht[30] = new HuffmanTables("30 ",16,16,11,2047,24,  array,512);
			array = D18.Huffman18;
			ht[31] = new HuffmanTables("31 ",16,16,13,8191,24,  array,512);
			array = D18.Huffman19;
			ht[32] = new HuffmanTables("32 ",1,16,0,0,-1,  array,31);
			array = D18.Huffman20;
			ht[33] = new HuffmanTables("33 ",1,16,0,0,-1,  array,31);
		} catch(Exception e){
			LOGGER.warning("couldn't load the Huffman Tables");
			System.exit(1);
		} finally{
			try{
				//  in.close();
			} catch(Exception e){}
		}
	}

}
