package org.guetal.mp3.processing.decoder;

import org.guetal.mp3.processing.commons.Constants;
import org.guetal.mp3.processing.commons.data.Channel;
import org.guetal.mp3.processing.commons.data.FrameData;
import org.guetal.mp3.processing.commons.data.FrameDataDequantized;
import org.guetal.mp3.processing.commons.data.FrameDataQuantized;
import org.guetal.mp3.processing.commons.data.SideInfo;
import org.guetal.mp3.processing.commons.util.MathUtil;




public final class LayerIIIDecoder {
	private final static int SSLIMIT = 18;
	private final static int SBLIMIT = 32;

	private final static HuffmanTables.Huffman huff = new HuffmanTables.Huffman();
	private static int[][][] ix;
	private static final float[][] prevblck = new float[2][SBLIMIT * SSLIMIT];
	private static int nonzero0,nonzero1;
	private static BitStream stream;
	private static Header header;
	private static BitReserve br;
	private static SideInfo si;
	private static int max_gr;
	private static int frame_start;
	private static int part2_start;
	private static int channels;
	private static int sfreq;

	private static int[] sfBandIndexL;
	private static int[] sfBandIndexS;
	private static HuffmanTables h;

	private static FrameDataQuantized   fd_q;
	private static FrameDataDequantized fd_d;



	public LayerIIIDecoder(BitStream stream0,Header header0) {
		stream = stream0;
		header = header0;
		frame_start = 0;
		channels = (header.mode() == Header.SINGLE_CHANNEL) ? 1 : 2;

		max_gr = (header.version() == Header.MPEG1) ? 2 : 1;
		sfreq = header.sample_frequency() + ((header.version() == Header.MPEG1) ? 3 : 0);

		switch(sfreq){
		case 0:
			sfBandIndexL = new int[]{0,6,12,18,24,30,36,44,54,66,80,96,116,140,168,200,238,284,336,396,464,522,576};
			sfBandIndexS = new int[]{0,4,8,12,18,24,32,42,56,74,100,132,174,192};
			break;
		case 1:
			sfBandIndexL = new int[]{0,6,12,18,24,30,36,44,54,66,80,96,114,136,162,194,232,278,330,394,464,540,576};
			sfBandIndexS = new int[]{0,4,8,12,18,26,36,48,62,80,104,136,180,192};
			break;
		case 2:
			sfBandIndexL = new int[]{0,6,12,18,24,30,36,44,54,66,80,96,116,140,168,200,238,284,336,396,464,522,576};
			sfBandIndexS = new int[]{0,4,8,12,18,26,36,48,62,80,104,134,174,192};
			break;
		case 3:
			sfBandIndexL = new int[]{0,4,8,12,16,20,24,30,36,44,52,62,74,90,110,134,162,196,238,288,342,418,576};
			sfBandIndexS = new int[]{0,4,8,12,16,22,30,40,52,66,84,106,136,192};
			break;
		case 4:
			sfBandIndexL = new int[]{0,4,8,12,16,20,24,30,36,42,50,60,72,88,106,128,156,190,230,276,330,384,576};
			sfBandIndexS = new int[]{0,4,8,12,16,22,28,38,50,64,80,100,126,192};
			break;
		case 5:
			sfBandIndexL = new int[]{0,4,8,12,16,20,24,30,36,44,54,66,82,102,126,156,194,240,296,364,448,550,576};
			sfBandIndexS = new int[]{0,4,8,12,16,22,30,42,58,78,104,138,180,192};
			break;
		}

		for (int ch = 0; ch < channels; ch++) {
			for (int j = 0; j < 576; j++) {
				prevblck[ch][j] = 0.0f;
			}
		}
		nonzero0 = nonzero1 = 576;
		br = new BitReserve();
		h = new HuffmanTables();



	}


	public int[][][] get_ix(){
		return ix;
	}

	// private static final int[][] lr = new int[SBLIMIT][SSLIMIT];
	private static final int[] lr = new int[SBLIMIT * SSLIMIT];

	public final FrameData decodeFrame(final int option) {
		int nSlots = header.slots();

		if( option == Constants.QUANTIZED_DOMAIN ){
			fd_q = new FrameDataQuantized(channels, max_gr);
			ix = fd_q.ix;
			si = fd_q.si;
			read_side_info(fd_q);

			// reads data
			for (int i = 0; i < nSlots; i++) {
				br.hputbuf(stream.readbits(8));
			}

			int main_data_end = BitReserve.totbit >>> 3;
			int flush_main = (BitReserve.totbit & 7);


			if (flush_main != 0) {
				br.hgetbits(8 - flush_main);
				main_data_end++;
			}

			// E.B Fix.
			int bytes_to_discard = frame_start - main_data_end - si.main_data_begin;
			frame_start += nSlots;

			if(bytes_to_discard < 0)
				return null;
			// End of E.B Fix.

			// 4096 = ( 512 * 8 )
			if (main_data_end > 4096) {
				frame_start -= 4096;
				br.rewindNbytes(4096);
			}

			while(bytes_to_discard-- > 0)
				br.hgetbits(8);
			for (int gr = 0; gr < max_gr; gr++) {
				for (int ch = 0; ch < channels; ch++) {
					part2_start = BitReserve.totbit;
					read_scalefactors(si, fd_q.scalefac0L[gr][ch], fd_q.scalefac0S[gr][ch], ch, gr);
					huffman_decode(ch,gr);

					Channel gr_info = (si.gr[gr].ch[ch]);

					if ((gr_info.window_switching_flag != 0) && (gr_info.block_type == 2)){
						System.arraycopy(ix[gr][ch], 0, lr, 0, lr.length);
						reorder(lr, ch, gr, gr_info);
					}

					return fd_q;
				}

			}
		} else {
			fd_d = new FrameDataDequantized(channels, max_gr);
			ro = fd_d.ro;
			ix = new int [max_gr][channels][576];
			si = fd_d.si;
			read_side_info(fd_d);

			// reads data
			for (int i = 0; i < nSlots; i++) {
				br.hputbuf(stream.readbits(8));
			}

			int main_data_end = BitReserve.totbit >>> 3;
				int flush_main = (BitReserve.totbit & 7);


				if (flush_main != 0) {
					br.hgetbits(8 - flush_main);
					main_data_end++;
				}

				// E.B Fix.
				int bytes_to_discard = frame_start - main_data_end - si.main_data_begin;
				frame_start += nSlots;

				if(bytes_to_discard < 0)
					return null;
				// End of E.B Fix.

				// 4096 = ( 512 * 8 )
				if (main_data_end > 4096) {
					frame_start -= 4096;
					br.rewindNbytes(4096);
				}

				while(bytes_to_discard-- > 0)
					br.hgetbits(8);
				for (int gr = 0; gr < max_gr; gr++) {
					for (int ch = 0; ch < channels; ch++) {
						part2_start = BitReserve.totbit;
						read_scalefactors(si, fd_d.scalefac0L[gr][ch], fd_d.scalefac0S[gr][ch], ch, gr);
						huffman_decode(ch,gr);


						dequantize_sample(ch, gr, fd_d.scalefac0L[gr][ch], fd_d.scalefac0S[gr][ch]);

						return fd_d;

					}

				}

		}
		return fd_q;
	}

	/**
	 *  Reads the side info from the stream,assuming the entire. frame has been
	 *  read already. Mono : 136 bits (= 17 bytes) Stereo : 256 bits (= 32
	 *  bytes)
	 *
	 *@return    Description of the Returned Value
	 */
	public final void read_side_info(FrameData fd) {
		int gr;
		int ch;


		if (header.version() == Header.MPEG1) {
			fd.si.main_data_begin = stream.readbits(9);
			if (channels == 1) {
				stream.readbits(5);
			} else {
				stream.readbits(3);
			}

			for (ch = 0; ch < channels; ch++) {
				//int[] scfsi = si.scfsi[ch];
				fd.si.scfsi[ch][0] = stream.readbits(1);
				fd.si.scfsi[ch][1] = stream.readbits(1);
				fd.si.scfsi[ch][2] = stream.readbits(1);
				fd.si.scfsi[ch][3] = stream.readbits(1);
			}

			for (gr = 0; gr < max_gr; gr++) {
				for (ch = 0; ch < channels; ch++) {

					Channel s = fd.si.gr[gr].ch[ch];
					s.part2_3_length = stream.readbits(12);
					s.big_values = stream.readbits(9);
					s.global_gain = stream.readbits(8);
					s.scalefac_compress = stream.readbits(4);
					s.window_switching_flag = stream.readbits(1);
					if ((s.window_switching_flag) != 0) {
						s.block_type = stream.readbits(2);
						s.mixed_block_flag = stream.readbits(1);
						s.table_select[0] = stream.readbits(5);
						s.table_select[1] = stream.readbits(5);
						s.subblock_gain[0] = stream.readbits(3);
						s.subblock_gain[1] = stream.readbits(3);
						s.subblock_gain[2] = stream.readbits(3);
						// Set region_count parameters since they are implicit in this case.
						if (s.block_type == 0) {
							//	 Side info bad: block_type == 0 in split block
							//return false;
						} else if (s.block_type == 2 && s.mixed_block_flag == 0) {
							s.region0_count = 8;
						} else {
							s.region0_count = 7;
						}
						s.region1_count = 20 - s.region0_count;
					} else {
						s.table_select[0] = stream.readbits(5);
						s.table_select[1] = stream.readbits(5);
						s.table_select[2] = stream.readbits(5);
						s.region0_count = stream.readbits(4);
						s.region1_count = stream.readbits(3);
						s.block_type = 0;
					}
					s.preflag = stream.readbits(1);
					s.scalefac_scale = stream.readbits(1);
					s.count1table_select = stream.readbits(1);
				}
			}

		}      

	}

	final static short slen1_tab[] ={0,0,0,0,3,1,1,1,2,2,2,3,3,3,4,4};
	final static short slen2_tab[] ={0,1,2,3,0,1,2,3,1,2,3,1,2,3,2,3};


	//private final void read_scalefactors(final int ch,final int gr) {
	private final void read_scalefactors(SideInfo si, int [] l, int [][] s, int ch, int gr) {
		Channel gr_info = (si.gr[gr].ch[ch]);
		int scale_comp = gr_info.scalefac_compress;
		int length0 = slen1_tab[scale_comp];
		int length1 = slen2_tab[scale_comp];
		//        int l[] = null;
		//        int s[][] = null;
		//        //if(gr==0){
		//        l = fd_q.scalefac0L[gr][ch];
		//        s = fd_q.scalefac0S[gr][ch];
		/*} else{
            l = fd_q.scalefac1L[gr][ch];
            s = fd_q.scalefac1S[gr][ch];
        }*/
		if ((gr_info.window_switching_flag != 0) && (gr_info.block_type == 2)) {
			if ((gr_info.mixed_block_flag) != 0) {

				// MIXED
				for (int sfb = 0; sfb < 8; sfb++) {
					l[sfb] = br.hgetbits(slen1_tab[gr_info.scalefac_compress]);
				}
				for (int sfb = 3; sfb < 6; sfb++) {
					for (int window = 0; window < 3; window++) {
						s[window][sfb] = br.hgetbits(slen1_tab[gr_info.scalefac_compress]);
					}
				}
				for (int sfb = 6; sfb < 12; sfb++) {
					for (int window = 0; window < 3; window++) {
						s[window][sfb] = br.hgetbits(slen2_tab[gr_info.scalefac_compress]);
					}
				}
				for (int sfb = 12,window = 0; window < 3; window++) {
					s[window][sfb] = 0;
				}
			} else {

				// SHORT
				s[0][0] = br.hgetbits(length0);
				s[1][0] = br.hgetbits(length0);
				s[2][0] = br.hgetbits(length0);
				s[0][1] = br.hgetbits(length0);
				s[1][1] = br.hgetbits(length0);
				s[2][1] = br.hgetbits(length0);
				s[0][2] = br.hgetbits(length0);
				s[1][2] = br.hgetbits(length0);
				s[2][2] = br.hgetbits(length0);
				s[0][3] = br.hgetbits(length0);
				s[1][3] = br.hgetbits(length0);
				s[2][3] = br.hgetbits(length0);
				s[0][4] = br.hgetbits(length0);
				s[1][4] = br.hgetbits(length0);
				s[2][4] = br.hgetbits(length0);
				s[0][5] = br.hgetbits(length0);
				s[1][5] = br.hgetbits(length0);
				s[2][5] = br.hgetbits(length0);
				s[0][6] = br.hgetbits(length1);
				s[1][6] = br.hgetbits(length1);
				s[2][6] = br.hgetbits(length1);
				s[0][7] = br.hgetbits(length1);
				s[1][7] = br.hgetbits(length1);
				s[2][7] = br.hgetbits(length1);
				s[0][8] = br.hgetbits(length1);
				s[1][8] = br.hgetbits(length1);
				s[2][8] = br.hgetbits(length1);
				s[0][9] = br.hgetbits(length1);
				s[1][9] = br.hgetbits(length1);
				s[2][9] = br.hgetbits(length1);
				s[0][10] = br.hgetbits(length1);
				s[1][10] = br.hgetbits(length1);
				s[2][10] = br.hgetbits(length1);
				s[0][11] = br.hgetbits(length1);
				s[1][11] = br.hgetbits(length1);
				s[2][11] = br.hgetbits(length1);
				s[0][12] = s[1][12] = s[2][12] = 0;
			}

			// SHORT
		} else {
			// LONG types 0,1,3
			int si_t[] = si.scfsi[ch];
			if (gr == 0) {

				l[0] = br.hgetbits(length0);
				l[1] = br.hgetbits(length0);
				l[2] = br.hgetbits(length0);
				l[3] = br.hgetbits(length0);
				l[4] = br.hgetbits(length0);
				l[5] = br.hgetbits(length0);
				l[6] = br.hgetbits(length0);
				l[7] = br.hgetbits(length0);
				l[8] = br.hgetbits(length0);
				l[9] = br.hgetbits(length0);
				l[10] = br.hgetbits(length0);
				l[11] = br.hgetbits(length1);
				l[12] = br.hgetbits(length1);
				l[13] = br.hgetbits(length1);
				l[14] = br.hgetbits(length1);
				l[15] = br.hgetbits(length1);
				l[16] = br.hgetbits(length1);
				l[17] = br.hgetbits(length1);
				l[18] = br.hgetbits(length1);
				l[19] = br.hgetbits(length1);
				l[20] = br.hgetbits(length1);
			} else{

				if (si_t[0] == 0) {
					l[0] = br.hgetbits(length0);
					l[1] = br.hgetbits(length0);
					l[2] = br.hgetbits(length0);
					l[3] = br.hgetbits(length0);
					l[4] = br.hgetbits(length0);
					l[5] = br.hgetbits(length0);
				}
				if (si_t[1] == 0) {
					l[6] = br.hgetbits(length0);
					l[7] = br.hgetbits(length0);
					l[8] = br.hgetbits(length0);
					l[9] = br.hgetbits(length0);
					l[10] = br.hgetbits(length0);
				}
				if (si_t[2] == 0) {
					l[11] = br.hgetbits(length1);
					l[12] = br.hgetbits(length1);
					l[13] = br.hgetbits(length1);
					l[14] = br.hgetbits(length1);
					l[15] = br.hgetbits(length1);
				}
				if (si_t[3] == 0) {
					l[16] = br.hgetbits(length1);
					l[17] = br.hgetbits(length1);
					l[18] = br.hgetbits(length1);
					l[19] = br.hgetbits(length1);
					l[20] = br.hgetbits(length1);
				}
			}
			l[21] = l[22] = 0;
		}
	}




	private final void huffman_decode(final int ch, final int gr) {
		Channel s = si.gr[gr].ch[ch];
		int part2_3_end = part2_start + s.part2_3_length;
		int num_bits = 0;
		int region1Start = 0;
		int region2Start = 0;
		int index = 0;
		int buf = 0;
		int buf1 = 0;

		// Find region boundary for short block case
		if (((s.window_switching_flag) != 0) && (s.block_type == 2)) {
			region1Start = 36;
			region2Start = 576;
			// No Region2 for short block case
		} else {
			// Find region boundary for long block case
			buf = s.region0_count + 1;
			buf1 = buf + s.region1_count + 1;
			if (buf1 > sfBandIndexL.length - 1) {
				buf1 = sfBandIndexL.length - 1;
			}
			region1Start = sfBandIndexL[buf];
			region2Start = sfBandIndexL[buf1];
		}
		// Read bigvalues area
		int big_val_max,is_1d_max;

		big_val_max = s.big_values << 1;

		is_1d_max = 576;

		for (int i = 0; (i<big_val_max) && (i<is_1d_max); i += 2) {

			if (i < region1Start) {
				h = HuffmanTables.ht[s.table_select[0]];
			} else if (i < region2Start) {
				h = HuffmanTables.ht[s.table_select[1]];
			} else {
				h = HuffmanTables.ht[s.table_select[2]];
			}

			HuffmanTables.decode(h,huff,br);
			ix[gr][ch][index++] = HuffmanTables.Huffman.x;
			ix[gr][ch][index++] = HuffmanTables.Huffman.y;
		}

		//Read count1 area
		h = HuffmanTables.ht[s.count1table_select + 32];
		num_bits = BitReserve.totbit;


		while ((num_bits < part2_3_end) && (index < 576)) {
			HuffmanTables.decode(h,huff,br);
			ix[gr][ch][index++] = HuffmanTables.Huffman.v;
			ix[gr][ch][index++] = HuffmanTables.Huffman.w;
			ix[gr][ch][index++] = HuffmanTables.Huffman.x;
			ix[gr][ch][index++] = HuffmanTables.Huffman.y;
			num_bits = BitReserve.totbit;
		}

		if (num_bits > part2_3_end) {
			br.rewindNbits(num_bits - part2_3_end);
			index -= 4;
		}

		num_bits = BitReserve.totbit;
		// Dismiss stuffing bits
		if (num_bits < part2_3_end) {
			br.hgetbits(part2_3_end - num_bits);
		}

		// Zero out rest
		if (index < 576) {
			if(gr==0)
				nonzero0 = index;
			else
				nonzero1 = index;
		} else {
			if(gr==0)
				nonzero0 = 576;
			else
				nonzero1 = 576;
		}
		if (index < 0) {
			index = 0;
		}

		for (; index<ix[gr][ch].length; index++)
			ix[gr][ch][index] = 0;

	}

	private final void reorder(final int xr[],final int ch,final int gr, Channel gr_info) {


		int freq,freq3,src_line,des_line,sfb_start3;



		if (gr_info.mixed_block_flag != 0) {
			// NO REORDER FOR LOW 2 SUBBANDS
			//                for(int i = 36; --i >= 0;){
			////                    reste = i % SSLIMIT;
			////                    quotien = (int) ((i - reste) / SSLIMIT);
			//                    ix[gr][ch][i] = xr[i];
			//                }
			// REORDERING FOR REST SWITCHED SHORT
			for (int sfb = 3,sfb_start = sfBandIndexS[3],sfb_lines = sfBandIndexS[4] - sfb_start; sfb < 13;
					sfb++,sfb_start = sfBandIndexS[sfb],sfb_lines = sfBandIndexS[sfb + 1] - sfb_start) {
				sfb_start3 = (sfb_start << 2) - sfb_start;
				for (freq = 0,freq3 = 0; freq < sfb_lines; freq++,freq3 += 3) {
					src_line = sfb_start3 + freq;
					des_line = sfb_start3 + freq3;
					ix[gr][ch][des_line] = xr[src_line];
					src_line += sfb_lines;
					des_line++;
					ix[gr][ch][des_line] = xr[src_line];
					src_line += sfb_lines;
					des_line++;
					ix[gr][ch][des_line] = xr[src_line];
				}
			}
		} else {
			int sfb_start = 0, sfb_lines=sfBandIndexS[1];

			for(int sfb=0; sfb < 13; sfb++){
				sfb_start=sfBandIndexS[sfb];
				sfb_lines=sfBandIndexS[sfb+1] - sfb_start;
				for(int window=0; window<3; window++)
					for(freq=0;freq < sfb_lines;freq++) {
						src_line = sfb_start*3 + window*sfb_lines + freq;
						des_line = (sfb_start*3) + window + (freq*3);
						ix[gr][ch][des_line] = xr[src_line];
					}
			}
		}

	}

	private final static float t_43[];

	/*  eventualmente in futuro sostiture questa tabella con i valori precalcolati */
	static{
		t_43 = new float[8192];                 // nel materiale c'è scritto che i valori sono 8206 e non 8192
		float d43 = (float)(4.0 / 3.0);
		for (int i = 0; i < 8192; i++)

			t_43[i] = (float)MathUtil.pow((double)i,(double)d43);
		//t_43[i] = (float)pow(i,(int)d43);
	}


	private static float [][][] ro;

	public float[][][] get_ro(){
		return this.ro;
	}

	private final static float two_to_negative_half_pow[] =
		{1.0000000000E+00f,7.0710678119E-01f,5.0000000000E-01f,3.5355339059E-01f,
		2.5000000000E-01f,1.7677669530E-01f,1.2500000000E-01f,8.8388347648E-02f,
		6.2500000000E-02f,4.4194173824E-02f,3.1250000000E-02f,2.2097086912E-02f,
		1.5625000000E-02f,1.1048543456E-02f,7.8125000000E-03f,5.5242717280E-03f,
		3.9062500000E-03f,2.7621358640E-03f,1.9531250000E-03f,1.3810679320E-03f,
		9.7656250000E-04f,6.9053396600E-04f,4.8828125000E-04f,3.4526698300E-04f,
		2.4414062500E-04f,1.7263349150E-04f,1.2207031250E-04f,8.6316745750E-05f,
		6.1035156250E-05f,4.3158372875E-05f,3.0517578125E-05f,2.1579186438E-05f,
		1.5258789062E-05f,1.0789593219E-05f,7.6293945312E-06f,5.3947966094E-06f,
		3.8146972656E-06f,2.6973983047E-06f,1.9073486328E-06f,1.3486991523E-06f,
		9.5367431641E-07f,6.7434957617E-07f,4.7683715820E-07f,3.3717478809E-07f,
		2.3841857910E-07f,1.6858739404E-07f,1.1920928955E-07f,8.4293697022E-08f,
		5.9604644775E-08f,4.2146848511E-08f,2.9802322388E-08f,2.1073424255E-08f,
		1.4901161194E-08f,1.0536712128E-08f,7.4505805969E-09f,5.2683560639E-09f,
		3.7252902985E-09f,2.6341780319E-09f,1.8626451492E-09f,1.3170890160E-09f,
		9.3132257462E-10f,6.5854450798E-10f,4.6566128731E-10f,3.2927225399E-10f
		};

	private final static int pretab[] = {0,0,0,0,0,0,0,0,0,0,0,1,1,1,1,2,2,3,3,3,2,0};

	private final void dequantize_sample(final int ch,final int gr, int [] l, int [][] s) {
		int [] is_1d = ix[gr][ch];
		Channel gr_info = (si.gr[gr].ch[ch]);
		int cb = 0;
		int next_cb_boundary =0;
		int cb_begin = 0;
		int cb_width = 0;
		float g_gain = 0.0f;

		// choose correct scalefactor band per block type,initalize boundary
		if ((gr_info.window_switching_flag != 0) && (gr_info.block_type == 2)) {
			if (gr_info.mixed_block_flag != 0) {
				next_cb_boundary = sfBandIndexL[1];
			}
			// LONG blocks: 0,1,3
			else {
				cb_width = sfBandIndexS[1];
				next_cb_boundary = (cb_width << 2) - cb_width;
				cb_begin = 0;
			}
		} else {
			next_cb_boundary = sfBandIndexL[1];
			// LONG blocks: 0,1,3
		}

		// more precise
		g_gain = (float)MathUtil.pow(2.0,(0.25 * (gr_info.global_gain - 210.0)));
		int maxNonZero = (gr == 0) ? nonzero0 : nonzero1;
		for (int j = 0; j < maxNonZero; j++) {
			int abv = is_1d[j];

			if (abv == 0) {
				ro[gr][ch][j] = 0.0f;
			} else {
				ro[gr][ch][j] = abv > 0 ? g_gain * t_43[abv]: -g_gain * t_43[-abv];
			}
		}


		// apply formula per block type
		for (int i=0,j = 0; j < maxNonZero; j++,i++) {
			//int reste = j % SSLIMIT;
			//int quotien = (int) ((j - reste) / SSLIMIT);
			if (i == next_cb_boundary) {
				/*
				 *  Adjust critical band boundary
				 */
				if ((gr_info.window_switching_flag != 0) && (gr_info.block_type == 2)) {
					if (gr_info.mixed_block_flag != 0) {
						if (i == sfBandIndexL[8]) {
							next_cb_boundary = sfBandIndexS[4];
							next_cb_boundary = (next_cb_boundary << 2) - next_cb_boundary;
							cb = 3;
							cb_width = sfBandIndexS[4] - sfBandIndexS[3];
							cb_begin = sfBandIndexS[3];
							cb_begin = (cb_begin << 2) - cb_begin;
						} else if (i < sfBandIndexL[8]) {
							next_cb_boundary = sfBandIndexL[(++cb) + 1];
						} else {
							next_cb_boundary = sfBandIndexS[(++cb) + 1];
							next_cb_boundary = (next_cb_boundary << 2) - next_cb_boundary;
							cb_begin = sfBandIndexS[cb];
							cb_width = sfBandIndexS[cb + 1] - cb_begin;
							cb_begin = (cb_begin << 2) - cb_begin;
						}
					} else {
						next_cb_boundary = sfBandIndexS[(++cb) + 1];
						next_cb_boundary = (next_cb_boundary << 2) - next_cb_boundary;
						cb_begin = sfBandIndexS[cb];
						cb_width = sfBandIndexS[cb + 1] - cb_begin;
						cb_begin = (cb_begin << 2) - cb_begin;
					}
				} else {
					// long blocks
					next_cb_boundary = sfBandIndexL[(++cb) + 1];
				}
			}


			// Do long/short dependent scaling operations
			if ((gr_info.window_switching_flag != 0) && (((gr_info.block_type == 2) && (gr_info.mixed_block_flag == 0)) || ((gr_info.block_type == 2) && (gr_info.mixed_block_flag != 0) && (j >= 36)))) {
				int t_index = (i - cb_begin) / cb_width;
				//int idx = s[t_index][cb] << gr_info.scalefac_scale;
				//idx += (gr_info.subblock_gain[t_index] << 2);
				int idx = s[t_index][cb] * gr_info.scalefac_scale;

				idx += (gr_info.subblock_gain[t_index] << 1);
				ro[gr][ch][j] *= two_to_negative_half_pow[idx];


			} else {
				// LONG block types 0,1,3 & 1st 2 subbands of switched blocks
				int idx = l[cb] * gr_info.scalefac_scale;
				if (gr_info.preflag != 0) {
					idx += pretab[cb];
				}
				//idx = idx * gr_info.scalefac_scale;
				ro[gr][ch][j] *= two_to_negative_half_pow[idx];



			}
		}

		//int reste;
		//int quotien;
		for(int j = maxNonZero; j < 576; j++) {
			//reste = j % SSLIMIT;
			//quotien = (int) ((j - reste) / SSLIMIT);
			ro[gr][ch][j] = 0.0f;
		}


	}


	public FrameData getFrameInfo(){
		return this.fd_q;
	}

	public int getChannels(){
		return this.channels;
	}

	public void copyData(FrameDataQuantized fd){
		fd.ix = this.ix;

	}

}