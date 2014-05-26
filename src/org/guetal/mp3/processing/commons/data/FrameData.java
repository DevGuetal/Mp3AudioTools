/*
 * FrameData.java
 *
 * Created on 24 April 2007, 19.59
 *
 */

package org.guetal.mp3.processing.commons.data;

import org.guetal.mp3.processing.commons.CommonMethods;

/**
 *
 * @author DevGuetal
 */
public class FrameData {
	public SideInfo si;

	private int MAXGR;
	private int CHANNELS;

	private int nSlots = 0;
	private int framesize = 0;

	/* Header Info  */
	private int hBitrateIndex = 0;
	private int version, fs = 44100, emphasis, hMode, hModeExtension;
	private String mode;
	private int bitrate;
	private int hProtectionBit;
	private int hPaddingBit;


	public void setVersion(int version) {
		this.version = version;
	}
	
	/** Creates a new instance of FrameData */
	public FrameData(final int channels, final int max_gr) {
		si = new SideInfo(max_gr, channels);
		CHANNELS = channels;
		MAXGR = max_gr;
	}


	public int getFsIndex(){
		switch (fs){
		case 48000: return 4;
		case 32000: return 5;
		}

		return 3;
	}

	public void clone(FrameData fd){
		fd.setInfo(version, bitrate, fs, mode, hModeExtension, emphasis, CHANNELS, hMode, hProtectionBit, hPaddingBit);
		fd.setBitrateIndex(hBitrateIndex);

		SideInfo si_u = fd.getSi();

		si_u.setMainDataBegin(si.getMainDataBegin());


		for( int gr = 0; gr < MAXGR; gr++ ){
			for (int ch = 0; ch < CHANNELS; ch++ ){
				Channel t = si.gr[gr].ch[ch];

				if( t.window_switching_flag != 0)
					fd.set_granule_info(ch, gr, t.big_values, t.count1table_select, t.global_gain, t.part2_3_length,
							t.preflag, t.block_type, t.scalefac_compress, t.table_select, t.mixed_block_flag,
							t.scalefac_scale, t.subblock_gain);
				else
					fd.set_granule_info(ch, gr, t.big_values, t.count1table_select, t.global_gain, t.part2_3_length, t.preflag,
							t.region0_count, t.region1_count, t.scalefac_compress, t.table_select, t.block_type,
							t.scalefac_scale);
			}
		}

	}


	public int [] getScfsi(int ch){
		return this.si.scfsi[ch];
	}

	public SideInfo getSi(){
		return this.si;
	}

	public Channel getGrInfo(int ch, int gr){
		return si.gr[gr].ch[ch];
	}

	public void setInfo(int version, int bitrate, int fs, String mode, int h_mode_extension, int emphasis, int channels, int h_mode, int h_protection_bit, int h_padding_bit) {
		this.version = version;
		this.bitrate = bitrate;
		this.fs = fs;
		this.mode = mode;
		this.hModeExtension = h_mode_extension;
		this.emphasis = emphasis;
		this.hMode = h_mode;
		this.hProtectionBit = h_protection_bit;
		this.hPaddingBit = h_padding_bit;

		updateFrameLen();
	}


	public int getPart23Length(int ch, int gr){
		return si.gr[gr].ch[ch].part2_3_length;
	}

	public int getBlockType(int ch, int gr){
		return si.gr[gr].ch[ch].block_type;
	}

	public int getHPaddingBit(){
		return hPaddingBit;
	}

	/** window_switching_flag = 1 */
			public void set_granule_info(int ch, int gr, int big_values,
					int count1_table_select, int global_gain, int part2_3_length,
					int preflag, int block_type,
					int scalefact_compress, int [] table_select,
					int mixed_block_flag, int scalefac_scale, int [] subblock_gain) {
		Channel gi = si.gr[gr].ch[ch];

		gi.window_switching_flag = 1;
		gi.big_values = big_values;
		gi.count1table_select = count1_table_select;
		gi.global_gain = global_gain;
		gi.part2_3_length = part2_3_length;
		gi.preflag = preflag;
		gi.scalefac_compress = scalefact_compress;
		gi.block_type = block_type;
		gi.mixed_block_flag = mixed_block_flag;
		gi.scalefac_scale = scalefac_scale;
		gi.table_select[0] = table_select[0];
		gi.table_select[1] = table_select[1];
		gi.subblock_gain[0] = subblock_gain[0];
		gi.subblock_gain[1] = subblock_gain[1];
		gi.subblock_gain[2] = subblock_gain[2];

	}


	/** window_switching_flag = 0*/
	public void set_granule_info(int ch, int gr, int big_values,
			int count1_table_select, int global_gain, int part2_3_length,
			int preflag, int region0_count, int region1_count,
			int scalefact_compress, int [] table_select, int block_type,
			int scalefac_scale) {
		Channel gi = si.gr[gr].ch[ch];
		gi.window_switching_flag = 0;
		gi.big_values = big_values;
		gi.count1table_select = count1_table_select;
		gi.global_gain = global_gain;
		gi.part2_3_length = part2_3_length;
		gi.preflag = preflag;
		gi.region0_count = region0_count;
		gi.region1_count = region1_count;
		gi.scalefac_compress = scalefact_compress;
		gi.scalefac_scale = scalefac_scale;
		gi.table_select[0] = table_select[0];
		gi.table_select[1] = table_select[1];
		gi.table_select[2] = table_select[2];

	}


	public void printHeaderInfo(){

	}

	public void updateFrameLen(){
		framesize = CommonMethods.calFrameSize(hBitrateIndex, hPaddingBit, fs);
		nSlots = CommonMethods.calNSlots(framesize, hMode, 1);
	}


	public void setBitrate(int bitrate_index, int bitrate){
		this.hBitrateIndex = bitrate_index;
		this.bitrate = bitrate;
	}

	public int getFramesize() {
		return framesize;
	}

	public int getNSlots() {
		return nSlots;
	}


	public void set_nSlots(int nSlots){
		this.nSlots = nSlots;
	}

	public int getMdLen(){
		int len = 0;
		for( int gr = 0; gr < MAXGR; gr++ )
			for (int ch = 0; ch < CHANNELS; ch++) {
				len += si.gr[gr].ch[ch].get_part2_3_length();
			}

		if (len % 8 != 0)
			len = len / 8 + 1;
		else len /= 8;
		return len;
	}

	public int getMainDataBegin(){
		return si.main_data_begin;
	}


	public int getBitrateIndex() {
		return this.hBitrateIndex;
	}

	public void setBitrateIndex(int h_bitrate_index) {
		this.hBitrateIndex = h_bitrate_index;
		updateFrameLen();
	}

	public int getBitrate() {
		return this.bitrate;
	}

	public String getMode() {
		return this.mode;
	}

	public int getEmphasis() {
		return this.emphasis;
	}

	public int getHModeExtension(){
		return this.hModeExtension;
	}

	public int getChannels() {
		return this.CHANNELS;
	}

	public int getFs() {
		return fs;
	}

	public int getMaxGr() {
		return this.MAXGR;
	}

	public int getWindowSwitchingFlag(int ch, int gr) {
		return this.si.gr[gr].ch[ch].window_switching_flag;
	}

	public int getBigValues(int ch, int gr) {
		return this.si.gr[gr].ch[ch].big_values;
	}

	public int[] getSubblockGain(int ch, int gr) {
		return this.si.gr[gr].ch[ch].subblock_gain;
	}

	public int getMixedBlockFlag(int ch, int gr) {
		return this.si.gr[gr].ch[ch].mixed_block_flag;
	}

	public int[] getTableSelect(int ch, int gr) {
		return this.si.gr[gr].ch[ch].table_select;
	}

	public int getScalefacScale(int ch, int gr) {
		return this.si.gr[gr].ch[ch].scalefac_scale;
	}

	public int getScalefactCompress(int ch, int gr) {
		return this.si.gr[gr].ch[ch].scalefac_compress;
	}

	public int getRegion1Count(int ch, int gr) {
		return this.si.gr[gr].ch[ch].region1_count;
	}

	public int getRegion0Count(int ch, int gr) {
		return this.si.gr[gr].ch[ch].region0_count;
	}

	public int getPreflag(int ch, int gr) {
		return this.si.gr[gr].ch[ch].preflag;
	}

	public int getGlobalGain(int ch, int gr) {
		return this.si.gr[gr].ch[ch].global_gain;
	}

	public int getCount1TableSelect(int ch, int gr) {
		return this.si.gr[gr].ch[ch].count1table_select;
	}

	public void setGlobalGain(int ch, int gr, int gain) {
		Channel gi = si.gr[gr].ch[ch];
		gi.global_gain = gain;
	}

	public int getHmode(){
		return hMode;
	}
}
