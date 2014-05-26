/*
 * Checker.java
 *
 * Created on 2 may 2007, 16.10
 *
 */

package org.guetal.mp3.processing.checker;

import java.io.InputStream;
import java.util.logging.Logger;

import org.guetal.mp3.processing.commons.Constants;
import org.guetal.mp3.processing.commons.Manager;
import org.guetal.mp3.processing.commons.data.FrameBuffer;
import org.guetal.mp3.processing.commons.data.FrameData;
import org.guetal.mp3.processing.commons.data.FrameDataUnpacked;


/**
 *
 * @author DevGuetal
 */
public class Checker {

	private final static Logger LOGGER = Logger.getLogger(Checker.class.getName()); 

	public static void compareStream(InputStream is1, InputStream is2, int start, int num_frames){
		FrameData fd1 = null, fd2 = null;
		int cont = 0;

		Manager reader1 = new Manager(is1);

		FrameBuffer buffer1 = new FrameBuffer(num_frames);
		FrameBuffer buffer2 = new FrameBuffer(num_frames);

		final int opt = Constants.HUFFMAN_DOMAIN;

		LOGGER.info("\nReading stream1");
		do{
			LOGGER.info("frame " + cont);
			try{
				fd1 = reader1.decodeFrame(opt);
			} catch (Exception e){
				LOGGER.warning("\nEnd of file 1. If end is unexpeted at this frame, probably main_data_begin or part2_3_length is wrong");
				break;
			}
			byte [] data = reader1.getMainData();

			if(cont >= start && (cont <= start + num_frames)){
				LOGGER.info(" - buffering...");
				buffer1.add_entry(data, fd1);
			}

			cont++;
		} while(cont < start + num_frames);

		LOGGER.info("\n\nReading stream2");
		cont = 0;
		Manager reader2 = new Manager(is2);

		do{
			LOGGER.info("frame " + cont);
			try{
				fd2 = reader2.decodeFrame(opt);
			} catch (Exception e){
				LOGGER.info("\nEnd of file 2. If end is unexpeted in at this frame, probably main_data_begin or part2_3_length is wrong");
				break;
			}
			byte [] data = reader2.getMainData();

			if( cont >= start && (cont <= start + num_frames)){
				LOGGER.info(" - buffering");
				buffer2.add_entry(data, fd2);
			}

			cont++;
		}
		while(cont < start + num_frames);

		cont = 0;

		LOGGER.info("\nComparing...\n");

		do{

			try{
				FrameDataUnpacked fd_u1 = buffer1.get_entry();
				FrameDataUnpacked fd_u2 = buffer2.get_entry();

				LOGGER.info("\n\n---------------------------------------------(Frame number: " + (cont + start) + ")---------------------------------------------");
				LOGGER.info("\n  **** HEADER (stream1, stream2) ****");
				printHeaderInfo(fd_u1, fd_u2);

				LOGGER.info("\n  **** SIDE INFO (stream1, stream2) ****");
				printSideInfo(fd_u1, fd_u2);
				LOGGER.info("\n  **** MAIN DATA (stream1, stream2) ****");
				printMainDataAligned(fd_u1, fd_u2);



			} catch (Exception e){
				LOGGER.info("End of file");
				break;
			}

			cont++;
		}
		while(cont < start + num_frames);
	}

	public static void printHeaderInfo(FrameData fd1, FrameData fd2){
		StringBuffer sb = new StringBuffer();
		
		sb.append("framesize: " + fd1.getFramesize());
		sb.append(" | " + fd2.getFramesize());

		if(fd1.getFramesize() != fd2.getFramesize())
			sb.append(" <--- DIFFERENT!!");
		sb.append("\n");

		sb.append("framesize: " + fd1.getFs());
		sb.append(" | " + fd2.getFs());

		if(fd1.getFs() != fd2.getFs())
			sb.append(" <--- DIFFERENT!!");
		sb.append("\n");
		
		LOGGER.info(sb.toString());
	}

	public static void printSideInfo(FrameData fd){

		LOGGER.info("side_info_enc -> main_data_beg: "+ fd.getMainDataBegin());
		for(int gr = 0; gr < fd.getMaxGr(); gr++)
			for(int ch = 0; ch < fd.getChannels(); ch++) {
				//Channel t = fd.
				LOGGER.info("");
				LOGGER.info("side_info_enc -> ch["+ch+"].gr["+gr+"].window_switching_flag: "+ fd.getWindowSwitchingFlag(ch, gr) );

				// important info
				LOGGER.info("side_info_enc -> ch["+ch+"].gr["+gr+"].part2_3_length: "+ fd.getPart23Length(ch, gr) );
				LOGGER.info("side_info_enc -> ch["+ch+"].gr["+gr+"].block type: "+ fd.getBlockType(ch, gr) );

				// bigvalues
				LOGGER.info("side_info_enc -> ch["+ch+"].gr["+gr+"].big_values: "+ fd.getBigValues(ch, gr) );
				int [] table_select = fd.getTableSelect(ch, gr);
				LOGGER.info("side_info_enc -> ch["+ch+"].gr["+gr+"].table_select[0: "+ table_select[0] );
				LOGGER.info("side_info_enc -> ch["+ch+"].gr["+gr+"].table_select[1: "+ table_select[1] );
				LOGGER.info("side_info_enc -> ch["+ch+"].gr["+gr+"].table_select[2: "+ table_select[2] );


				LOGGER.info("side_info_enc -> ch["+ch+"].gr["+gr+"].region0_count: "+ fd.getRegion0Count(ch, gr));

				// count1
				LOGGER.info("side_info_enc -> ch["+ch+"].gr["+gr+"].region1_count: "+ fd.getRegion1Count(ch, gr) );
				LOGGER.info("side_info_enc -> ch["+ch+"].gr["+gr+"]count1table_select: "+ fd.getCount1TableSelect(ch, gr) );
				LOGGER.info("side_info_enc -> ch["+ch+"].gr["+gr+"].scalefac_compress: "+ fd.getScalefactCompress(ch, gr) );
				LOGGER.info("side_info_enc -> ch["+ch+"].gr["+gr+"].scalefac_scale: "+ fd.getScalefacScale(ch, gr) );

				LOGGER.info("side_info_enc -> ch["+ch+"].gr["+gr+"].global_gain: "+ fd.getGlobalGain(ch, gr) );
				LOGGER.info("side_info_enc -> ch["+ch+"].gr["+gr+"].preflag: "+ fd.getPreflag(ch, gr) );
				LOGGER.info("side_info_enc -> ch["+ch+"].gr["+gr+"].mixed_block_flag: "+ fd.getMixedBlockFlag(ch, gr) );

				int [] subblock_gain = fd.getSubblockGain(ch, gr);
				LOGGER.info("side_info_enc -> ch["+ch+"].gr["+gr+"][0].subblock_gain: "+ subblock_gain[0] );
				LOGGER.info("side_info_enc -> ch["+ch+"].gr["+gr+"][1].subblock_gain: "+ subblock_gain[1] );
				LOGGER.info("side_info_enc -> ch["+ch+"].gr["+gr+"][2].subblock_gain: "+ subblock_gain[2] );

			}
	}


	public static void printSideInfo(FrameData fd1, FrameData fd2){

		StringBuffer sb = new StringBuffer();
		sb.append("side_info_enc -> main_data_beg: "+ fd1.getMainDataBegin());
		sb.append(" | "+ fd2.getMainDataBegin());
		if(fd1.getMainDataBegin() != fd2.getMainDataBegin())
			sb.append(" <--- DIFFERENT!!");
		sb.append("\n");


		for(int gr = 0; gr < fd1.getMaxGr(); gr++)
			for(int ch = 0; ch < fd1.getChannels(); ch++){
				//Channel t = fd.
				LOGGER.info("");
				sb.append("side_info_enc -> ch["+ch+"].gr["+gr+"].window_switching_flag: "+ fd1.getWindowSwitchingFlag(ch, gr) );
				sb.append(" | "+ fd2.getWindowSwitchingFlag(ch, gr));
				if(fd1.getWindowSwitchingFlag(ch, gr) != fd2.getWindowSwitchingFlag(ch, gr))
					sb.append(" <--- DIFFERENT!!");
				sb.append("\n");

				// important info
				sb.append("side_info_enc -> ch["+ch+"].gr["+gr+"].part2_3_length: "+ fd1.getPart23Length(ch, gr) );
				sb.append(" | "+ fd2.getPart23Length(ch, gr));
				if(fd1.getPart23Length(ch, gr) != fd2.getPart23Length(ch, gr))
					sb.append(" <--- DIFFERENT!!");
				sb.append("\n");


				sb.append("side_info_enc -> ch["+ch+"].gr["+gr+"].block type: "+ fd1.getBlockType(ch, gr) );
				sb.append(" | "+ fd2.getBlockType(ch, gr));
				if(fd1.getBlockType(ch, gr) != fd2.getBlockType(ch, gr))
					sb.append(" <--- DIFFERENT!!");
				sb.append("\n");


				// bigvalues
				sb.append("side_info_enc -> ch["+ch+"].gr["+gr+"].big_values: "+ fd1.getBigValues(ch, gr) );
				sb.append(" | "+ fd2.getBigValues(ch, gr));
				if(fd1.getBigValues(ch, gr) != fd2.getBigValues(ch, gr))
					sb.append(" <--- DIFFERENT!!");
				sb.append("\n");


				int [] table_select1 = fd1.getTableSelect(ch, gr);
				int [] table_select2 = fd2.getTableSelect(ch, gr);

				sb.append("side_info_enc -> ch["+ch+"].gr["+gr+"].table_select[0: "+ table_select1[0] );
				sb.append(" | "+ table_select2[0]);
				if(table_select1[0] != table_select2[0])
					sb.append(" <--- DIFFERENT!!");
				sb.append("\n");

				sb.append("side_info_enc -> ch["+ch+"].gr["+gr+"].table_select[1: "+ table_select1[1] );
				sb.append(" | "+ table_select2[1]);
				if(table_select1[1] != table_select2[1])
					sb.append(" <--- DIFFERENT!!");
				sb.append("\n");

				sb.append("side_info_enc -> ch["+ch+"].gr["+gr+"].table_select[2: "+ table_select1[2] );
				sb.append(" | "+ table_select2[2]);
				if(table_select1[2] != table_select2[2])
					sb.append(" <--- DIFFERENT!!");
				sb.append("\n");


				sb.append("side_info_enc -> ch["+ch+"].gr["+gr+"].region0_count: "+ fd1.getRegion0Count(ch, gr));
				sb.append(" | "+ fd2.getRegion0Count(ch, gr));
				if(fd1.getRegion0Count(ch, gr) != fd2.getRegion0Count(ch, gr))
					sb.append(" <--- DIFFERENT!!");
				sb.append("\n");

				// count1
				sb.append("side_info_enc -> ch["+ch+"].gr["+gr+"].region1_count: "+ fd1.getRegion1Count(ch, gr) );
				sb.append(" | "+ fd2.getRegion1Count(ch, gr));
				if(fd1.getRegion1Count(ch, gr) != fd2.getRegion1Count(ch, gr))
					sb.append(" <--- DIFFERENT!!");
				sb.append("\n");


				sb.append("side_info_enc -> ch["+ch+"].gr["+gr+"]count1table_select: "+ fd1.getCount1TableSelect(ch, gr) );
				sb.append(" | "+ fd2.getCount1TableSelect(ch, gr));
				if(fd1.getCount1TableSelect(ch, gr) != fd2.getCount1TableSelect(ch, gr))
					sb.append(" <--- DIFFERENT!!");
				sb.append("\n");

				sb.append("side_info_enc -> ch["+ch+"].gr["+gr+"].scalefac_compress: "+ fd1.getScalefactCompress(ch, gr) );
				sb.append(" | "+ fd2.getScalefactCompress(ch, gr));
				if(fd1.getScalefactCompress(ch, gr) != fd2.getScalefactCompress(ch, gr))
					sb.append(" <--- DIFFERENT!!");
				sb.append("\n");

				sb.append("side_info_enc -> ch["+ch+"].gr["+gr+"].scalefac_scale: "+ fd1.getScalefacScale(ch, gr) );
				sb.append(" | "+ fd2.getScalefacScale(ch, gr));
				if(fd1.getScalefacScale(ch, gr) != fd2.getScalefacScale(ch, gr))
					sb.append(" <--- DIFFERENT!!");
				sb.append("\n");

				sb.append("side_info_enc -> ch["+ch+"].gr["+gr+"].global_gain: "+ fd1.getGlobalGain(ch, gr) );
				sb.append(" | "+ fd2.getGlobalGain(ch, gr));
				if(fd1.getGlobalGain(ch, gr) != fd2.getGlobalGain(ch, gr))
					sb.append(" <--- DIFFERENT!!");
				sb.append("\n");

				sb.append("side_info_enc -> ch["+ch+"].gr["+gr+"].preflag: "+ fd1.getPreflag(ch, gr) );
				sb.append(" | "+ fd2.getPreflag(ch, gr));
				if(fd1.getPreflag(ch, gr) != fd2.getPreflag(ch, gr))
					sb.append(" <--- DIFFERENT!!");
				sb.append("\n");

				sb.append("side_info_enc -> ch["+ch+"].gr["+gr+"].mixed_block_flag: "+ fd1.getMixedBlockFlag(ch, gr) );
				sb.append(" | "+ fd2.getMixedBlockFlag(ch, gr));
				if(fd1.getMixedBlockFlag(ch, gr) != fd2.getMixedBlockFlag(ch, gr))
					sb.append(" <--- DIFFERENT!!");
				sb.append("\n");

				int [] subblock_gain1 = fd1.getSubblockGain(ch, gr);
				int [] subblock_gain2 = fd2.getSubblockGain(ch, gr);
				sb.append("side_info_enc -> ch["+ch+"].gr["+gr+"][0].subblock_gain: "+ subblock_gain1[0] );
				sb.append(" | "+ subblock_gain2[0]);
				if(subblock_gain1[0] != subblock_gain2[0])
					sb.append(" <--- DIFFERENT!!");
				sb.append("\n");

				sb.append("side_info_enc -> ch["+ch+"].gr["+gr+"][1].subblock_gain: "+ subblock_gain1[1] );
				sb.append(" | "+ subblock_gain2[1]);
				if(subblock_gain1[1] != subblock_gain2[1])
					sb.append(" <--- DIFFERENT!!");
				sb.append("\n");

				sb.append("side_info_enc -> ch["+ch+"].gr["+gr+"][2].subblock_gain: "+ subblock_gain1[2] );
				sb.append(" | "+ subblock_gain2[2]);
				if(subblock_gain1[2] != subblock_gain2[2])
					sb.append(" <--- DIFFERENT!!");
				sb.append("\n");

			}

		LOGGER.info(sb.toString());
	}

	public static void printMainData(FrameDataUnpacked fd_u1, FrameDataUnpacked fd_u2){
		int st1_end_gr_1 = 0, st1_end_gr_2 = 0, st1_end_gr_3 = 0, st1_end_gr_4 = 0;     //  end of main data (stream 1)
		int st2_end_gr_1 = 0, st2_end_gr_2 = 0, st2_end_gr_3 = 0, st2_end_gr_4 = 0;     //  end of main data (stream 1)

		int scf1_end_1 = 0, scf1_end_2 = 0, scf1_end_3 = 0, scf1_end_4 = 0;     //end of scalefactors (stream 1)
		int scf2_end_1 = 0, scf2_end_2 = 0, scf2_end_3 = 0, scf2_end_4 = 0;     //end of scalefactors (stream 2)

		byte [] data1 = fd_u1.get_data();
		byte [] data2 = fd_u2.get_data();
		int i = 0;

		st1_end_gr_1 = fd_u1.getPart23Length(0,0)/8;
		st1_end_gr_2 = (int)((float)(fd_u1.getPart23Length(0,1))/8 + (float)(fd_u1.getPart23Length(0,0))/8);
		st1_end_gr_3 = (int)((float)(fd_u1.getPart23Length(0,1))/8 + (float)(fd_u1.getPart23Length(0,0))/8 +
				(float)(fd_u1.getPart23Length(1,0))/8);

		st1_end_gr_4 = (int)((float)(fd_u1.getPart23Length(0,1))/8 + (float)(fd_u1.getPart23Length(0,0))/8 +
				(float)(fd_u1.getPart23Length(1,0))/8 + (float)(fd_u1.getPart23Length(1,1))/8);

		scf1_end_1 = (part2_length(0,0, fd_u1) /8 );
		scf1_end_2 = st1_end_gr_1 + ( part2_length(0, 1, fd_u1) /8 );
		scf1_end_3 = st1_end_gr_2 + ( part2_length(1, 0, fd_u1) /8 );
		scf1_end_4 = st1_end_gr_3 + ( part2_length(1, 1, fd_u1) /8 );


		st2_end_gr_1 = fd_u2.getPart23Length(0,0)/8;
		st2_end_gr_2 = (int)((float)(fd_u2.getPart23Length(0,1))/8 + (float)(fd_u2.getPart23Length(0,0))/8);
		st2_end_gr_3 = (int)((float)(fd_u2.getPart23Length(0,1))/8 + (float)(fd_u2.getPart23Length(0,0))/8 +
				(float)(fd_u2.getPart23Length(1,0))/8);

		st2_end_gr_4 = (int)((float)(fd_u2.getPart23Length(0,1))/8 + (float)(fd_u2.getPart23Length(0,0))/8 +
				(float)(fd_u2.getPart23Length(1,0))/8 + (float)(fd_u2.getPart23Length(1,1))/8);

		scf2_end_1 = (part2_length(0,0, fd_u2) /8 );
		scf2_end_2 = st1_end_gr_1 + ( part2_length(0, 1, fd_u2) /8 );
		scf2_end_3 = st1_end_gr_2 + ( part2_length(1, 0, fd_u2) /8 );
		scf2_end_4 = st1_end_gr_3 + ( part2_length(1, 1, fd_u2) /8 );



		int end = fd_u1.getMdLen() > fd_u2.getMdLen() ? fd_u2.getMdLen(): fd_u1.getMdLen();

		for(; i < end; i++){
			StringBuffer sb = new StringBuffer();
			sb.append("data["+i+"]: "+ data1[i]);
			sb.append(" | " + data2[i]);


			if (i == 0)                sb.append(" - begin of block 1 of stream 1 (block_type " + fd_u1.getBlockType(0, 0) +")");
			if (st1_end_gr_1 - 1 == i) sb.append(" - end of block 1 of stream 1 - begin of block 2 of stream 1 (block_type " + fd_u1.getBlockType(0, 1) +")");
			if (st1_end_gr_2 - 1 == i) sb.append(" - end of block 2 of stream 1 - begin of block 3 of stream 1 (block_type " + fd_u1.getBlockType(1, 0) +")");
			if (st1_end_gr_3 - 1 == i) sb.append(" - end of block 3 of stream 1 - begin of block 4 of stream 1 (block_type " + fd_u1.getBlockType(1, 1) +")");
			if (st1_end_gr_4 - 1 == i) sb.append(" - end of block 4 of stream 1");

			if( (i == scf1_end_1 && scf1_end_1 != 0) || (i == scf1_end_2 && scf1_end_2 != st1_end_gr_1) ||
					(i == scf1_end_3 && scf1_end_3 != st1_end_gr_2) || (i == scf1_end_4 && scf1_end_4 != st1_end_gr_3))
				sb.append(" - end of scalefators stream 1");

			if (i == 0)                sb.append(" - begin of block 1 of stream 2 (block_type " + fd_u2.getBlockType(0, 0) +")");
			if (st2_end_gr_1 - 1 == i) sb.append(" - end of block 1 of stream 2 - begin of block 2 of stream 2 (block_type " + fd_u2.getBlockType(0, 1) +")");
			if (st2_end_gr_2 - 1 == i) sb.append(" - end of block 2 of stream 2 - begin of block 3 of stream 2 (block_type " + fd_u2.getBlockType(1, 0) +")");
			if (st2_end_gr_3 - 1 == i) sb.append(" - end of block 3 of stream 2 - begin of block 4 of stream 2 (block_type " + fd_u2.getBlockType(1, 1) +")");
			if (st2_end_gr_4 - 1 == i) sb.append(" - end of block 4 of stream 2");


			if( (i == scf2_end_1 && scf2_end_1 != 0) || (i == scf2_end_2 && scf2_end_2 != st1_end_gr_1) ||
					(i == scf2_end_3 && scf2_end_3 != st2_end_gr_2) || (i == scf2_end_4 && scf2_end_4 != st2_end_gr_3))
				sb.append(" - end of scalefators stream 2");

			if(data1[i] != data2[i])
				sb.append(" <--- DIFFERENT!!");

			sb.append("\n");
			
			LOGGER.info(sb.toString());
		}

		if(fd_u1.getMdLen() > fd_u2.getMdLen()){
			for(; i < fd_u1.getMdLen(); i++){
				StringBuffer sb = new StringBuffer();
				sb.append("data of stream 1 ["+i+"]: "+ data1[i]);

				if (i == 0)                sb.append(" - begin of block 1 of stream 1 (block_type " + fd_u1.getBlockType(0, 0) +")");
				if (st1_end_gr_1 - 1 == i) sb.append(" - end of block 1 of stream 1 - begin of block 2 of stream 1 (block_type " + fd_u1.getBlockType(0, 1) +")");
				if (st1_end_gr_2 - 1 == i) sb.append(" - end of block 2 of stream 1 - begin of block 3 of stream 1 (block_type " + fd_u1.getBlockType(1, 0) +")");
				if (st1_end_gr_3 - 1 == i) sb.append(" - end of block 3 of stream 1 - begin of block 4 of stream 1 (block_type " + fd_u1.getBlockType(1, 1) +")");
				if (st1_end_gr_4 - 1 == i) sb.append(" - end of block 4 of stream 1");

				sb.append("\n");
				LOGGER.info(sb.toString());
			}
		}

		if(fd_u1.getMdLen() < fd_u2.getMdLen()){
			for(; i < fd_u2.getMdLen(); i++){
				StringBuffer sb = new StringBuffer();
				sb.append("data of stream 2 ["+i+"]: "+ data2[i]);

				if (i == 0)                sb.append(" - begin of block 1 of stream 2 (block_type " + fd_u2.getBlockType(0, 0) +")");
				if (st2_end_gr_1 - 1 == i) sb.append(" - end of block 1 of stream 2 - begin of block 2 of stream 2 (block_type " + fd_u2.getBlockType(0, 1) +")");
				if (st2_end_gr_2 - 1 == i) sb.append(" - end of block 2 of stream 2 - begin of block 3 of stream 2 (block_type " + fd_u2.getBlockType(1, 0) +")");
				if (st2_end_gr_3 - 1 == i) sb.append(" - end of block 3 of stream 2 - begin of block 4 of stream 2 (block_type " + fd_u2.getBlockType(1, 1) +")");
				if (st2_end_gr_4 - 1 == i) sb.append(" - end of block 4 of stream 2");

				sb.append("\n");
				LOGGER.info(sb.toString());
			}
		}

	}


	public static void printMainDataAligned(FrameDataUnpacked fd_u1, FrameDataUnpacked fd_u2){
		int [] begin_block = {0, 0};
		int [] offset = {0, 0};
		for(int gr = 0; gr < fd_u1.getMaxGr(); gr++){
			for(int ch = 0; ch < fd_u1.getChannels(); ch++){

				LOGGER.info("\nBLOCK (ch: " + ch + "| gr:" + gr +" - block type: "+ fd_u1.getBlockType(ch, gr) +")");
				LOGGER.info("");
				begin_block = print_block(fd_u1, fd_u2, ch, gr, offset);

				offset[0] += begin_block[0];
				offset[1] += begin_block[1];
				LOGGER.info("");
				LOGGER.info("being next block: " + offset[0]);
				LOGGER.info("being next block: " + offset[1]);

			}
		}
	}

	private static int[] print_block(FrameDataUnpacked fd_u1, FrameDataUnpacked fd_u2, int ch, int gr, int[] begin_block){
		int [] end_block = new int[2];

		byte [] data1 = fd_u1.get_data();
		byte [] data2 = fd_u2.get_data();

		end_block[0] = fd_u1.getPart23Length(ch, gr)/8;
		end_block[1] = fd_u2.getPart23Length(ch, gr)/8;

		int end_loop = (end_block[0] > end_block[1]) ? end_block[1]: end_block[0];
		int i = 0;

		int end_scf1 = (part2_length(ch, gr, fd_u1) /8 );
		int end_scf2 = (part2_length(ch, gr, fd_u2) /8 );


		for(; i < end_loop; i++) {
			StringBuffer sb = new StringBuffer();
			sb.append("data["+i+"]: "+ data1[begin_block[0] + i]);
			sb.append(" | " + data2[begin_block[1] + i]);

			if(i == end_block[0]) sb.append(" - end of block stream 1");
			if(i == end_block[1]) sb.append(" - end of block stream 2");

			if(i == end_scf1) sb.append(" - end of scalefactors stream 1");
			if(i == end_scf2) sb.append(" - end of scalefactors stream 2");

			if(data2[begin_block[1] + i] != data1[begin_block[0] + i]) sb.append(" <-- DIFFERENT !!");

			sb.append("\n");
			LOGGER.info(sb.toString());
		}

		if(end_block[0] > end_block[1])
			for(; i < end_block[0]; i++){
				StringBuffer sb = new StringBuffer();
				sb.append("data["+i+"] (stream 1 only): "+ data1[begin_block[0] + i]);
				if(i == end_block[0]) sb.append(" - end of block stream 1");
				if(i == end_scf1) sb.append(" - end of scalefactors stream 1");

				sb.append("\n");
				LOGGER.info(sb.toString());
			}

		if(end_block[0] < end_block[1])
			for(; i < end_block[1]; i++){
				StringBuffer sb = new StringBuffer();
				
				sb.append("data["+i+"] (stream 2 only): " + data2[begin_block[1] + i]);
				if(i == end_block[1]) sb.append(" - end of block stream 2");
				if(i == end_scf2) sb.append(" - end of scalefactors stream 2");

				sb.append("\n");
				
				LOGGER.info(sb.toString());
			}

		return end_block;
	}


	public static boolean compareStream(byte[] st, byte [] original_st){
		boolean good_mp3 = true;
		
		int offset = 0;
		int n_frame = 0;
		int framesize = 418;

		for(int i = 0; i < st.length; i++) {


			int main_data_begin = 0;
			
			StringBuffer sb = new StringBuffer();

			sb.append("byte " + i + ": " + st[i]);
			sb.append(" | " + original_st[i + offset]);



			if((i % framesize) == 0)  sb.append(" <-- begin of frame " + n_frame++);
			if((i % framesize) == 4){
				sb.append(" <-- main_data_beg: ");
				main_data_begin = (st[i] >= 0) ? ((st[i] << 1)+((st[i+1]>>> 7) & 1)):
					(((st[i] + 256) << 1)+((st[i+1]>>> 7) & 1));

				sb.append(main_data_begin);

				main_data_begin = (original_st[i+offset] >= 0) ? ((original_st[i+offset] << 1) +
						((original_st[i+1+offset]>>> 7) & 1)): (((original_st[i+offset] + 256) << 1) +
								((original_st[offset+i+1]>>> 7) & 1));

				sb.append(" | "+main_data_begin);
			}

			if((i % framesize) == 36)  sb.append(" <-- begin of data in frame");

			if(original_st[i+offset]!= st[i] ){
				int byte_number = i - main_data_begin;
				sb.append("<------------- DIFFERENT ("+byte_number+") !!");

				good_mp3 = false;
			}

			sb.append("\n");

			LOGGER.info(sb.toString());
		}

		LOGGER.info("end mp3");
	
		return good_mp3;
	}

	private static void printMainData(FrameDataUnpacked fd_u){
		int end_gr_1 = 0, end_gr_2 = 0, end_gr_3 = 0, end_gr_4 = 0;
		byte [] data = fd_u.get_data();

		end_gr_1 = fd_u.getPart23Length(0,0)/8;
		end_gr_2 = (int)((float)(fd_u.getPart23Length(0,1))/8 + (float)(fd_u.getPart23Length(0,0))/8);
		end_gr_3 = (int)((float)(fd_u.getPart23Length(0,1))/8 + (float)(fd_u.getPart23Length(0,0))/8 +
				(float)(fd_u.getPart23Length(1,0))/8);

		end_gr_4 = (int)((float)(fd_u.getPart23Length(0,1))/8 + (float)(fd_u.getPart23Length(0,0))/8 +
				(float)(fd_u.getPart23Length(1,0))/8 + (float)(fd_u.getPart23Length(1,1))/8);


		for(int i = 0; i < fd_u.getMdLen(); i++){
			StringBuffer sb = new StringBuffer(); 
			sb.append("data["+i+"]: "+ data[i]);

			if (i == 0)            sb.append (" - begin of block 1 (block_type " + fd_u.getBlockType(0, 0) +")");
			if (end_gr_1 - 1 == i) sb.append (" - end of block 1 - begin of block 2 (block_type " + fd_u.getBlockType(0, 1) +")");
			if (end_gr_2 - 1 == i) sb.append (" - end of block 2 - begin of block 3 (block_type " + fd_u.getBlockType(1, 0) +")");
			if (end_gr_3 - 1 == i) sb.append (" - end of block 3 - begin of block 4 (block_type " + fd_u.getBlockType(1, 1) +")");
			if (end_gr_4 - 1 == i) sb.append (" - end of block 4");

			LOGGER.info(sb.toString());
		}
	}

	public static void print_info(FrameDataUnpacked fd_u){
		LOGGER.info("\n  **** HEADER ****");
		fd_u.printHeaderInfo();
		LOGGER.info("\n  **** SIDE INFO ****");
		printSideInfo(fd_u);
		LOGGER.info("\n  **** MAIN DATA ****");
		printMainData(fd_u);

	}

	/** calculates the number of bits needed to encode the scalefacs in the
	 * main data block                                                         */

	private static int part2_length(int gr, int ch,   FrameData fd) {
		int slen1, slen2, bits;
		//EChannel gi = si.ch[ch].gr[gr];

		bits = 0;

		slen1 = slen1_tab[ fd.getScalefactCompress(ch, gr) ];
		slen2 = slen2_tab[ fd.getScalefactCompress(ch, gr) ];

		if ( (fd.getWindowSwitchingFlag(ch, gr) == 1) && (fd.getBlockType(ch, gr) == 2) ) {
			if ( fd.getMixedBlockFlag(ch, gr) != 0 ) {
				bits += (8 * slen1) + (9 * slen1) + (18 * slen2);
			} else {
				bits += (18 * slen1) + (18 * slen2);
			}
		} else {
			int [] scfsi = fd.getScfsi(ch);
			if ( gr != 0 || (scfsi[0] == 0 ) )
				bits += (6 * slen1);

			if ( gr != 0 || (scfsi[1] == 0 ) )
				bits += (5 * slen1);

			if ( gr != 0 || (scfsi[2] == 0 ) )
				bits += (5 * slen2);

			if ( gr != 0 || (scfsi[3] == 0 ) )
				bits += (5 * slen2);
		}

		return bits;
	}


	final static short slen1_tab[] ={0,0,0,0,3,1,1,1,2,2,2,3,3,3,4,4};
	final static short slen2_tab[] ={0,1,2,3,0,1,2,3,1,2,3,1,2,3,2,3};

}
