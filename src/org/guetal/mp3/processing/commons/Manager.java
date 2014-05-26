/*
 * Manager.java
 *
 * Created on 13 May 2007, 11.50
 *
 */

package org.guetal.mp3.processing.commons;

import java.io.IOException;
import java.io.InputStream;

import org.guetal.mp3.processing.commons.data.FrameData;
import org.guetal.mp3.processing.commons.data.FrameDataDequantized;
import org.guetal.mp3.processing.commons.data.FrameDataQuantized;
import org.guetal.mp3.processing.decoder.BitStream;
import org.guetal.mp3.processing.decoder.ByteReserve;
import org.guetal.mp3.processing.decoder.Header;
import org.guetal.mp3.processing.decoder.LayerIIIDecoder;
import org.guetal.mp3.processing.encoder.LayerIIIEnc;


/**
 *
 * @author DevGuetal
 */
public class Manager {


	private static BitStream bitstream;
	private LayerIIIEnc encoder;
	private ByteReserve reserve = new ByteReserve();

	private Header header;

	private LayerIIIDecoder decoder;

	private final int N_FRAMES = 1000;
	private final int[] DEFAULT_SIZE = {10};

	private int cont;
	private int size_buffer;
	private byte [] data_def;

	private byte [] stream;
	private byte [] temp;
	private int position = 0;
	private boolean first_call = true;

	/**
	 * Creates a new instance of Manager
	 */
	 public Manager(InputStream is) {
		 bitstream = new BitStream(is);

		 try {
			 header =  bitstream.readFrame();
			 stream = new byte[header.getFramesize()*N_FRAMES];
			 size_buffer = DEFAULT_SIZE[0];
		 } catch (IOException ex) {
			 ex.printStackTrace();
		 }

		 decoder = new LayerIIIDecoder(bitstream, header);
		 encoder = new LayerIIIEnc();
	 }

	 /**
	  * Creates a new instance of Manager
	  */
	 public Manager(InputStream is, int size_buffer) {
		 bitstream = new BitStream(is);

		 try {
			 header =  bitstream.readFrame();
			 stream = new byte[header.getFramesize()*size_buffer];
			 this.size_buffer = size_buffer;
		 } catch (IOException ex) {
			 ex.printStackTrace();
		 }

		 decoder = new LayerIIIDecoder(bitstream, header);
		 encoder = new LayerIIIEnc(size_buffer);
	 }

	 public FrameData decodeFrame(final int mode){
		 FrameData fd = null;

		 if (mode == Constants.HUFFMAN_DOMAIN){
			 int mdb = 0;
			 fd = new FrameData(header.get_channels(), header.get_max_gr());

			 decoder.read_side_info(fd);

			 byte [] buffer = bitstream.get_main_data();

			 if( (mdb = fd.getMainDataBegin()) != 0 ){

				 byte[] data_res = reserve.read_from_reserve(mdb);

				 int totlen = fd.getMdLen() ;
				 int main_data_beg = fd.getMainDataBegin();

				 data_def = new byte[totlen];
				 int end = (totlen > main_data_beg)? main_data_beg: totlen;

				 for(int i = 0; i < end; i++){
					 data_def[i] = data_res[i];
				 }

				 if(totlen > main_data_beg){
					 int offset = end;
					 for(int i = offset; i < totlen; i++){
						 data_def[i] = buffer[i - end];
					 }
				 }

				 reserve.store_in_reserve(buffer);
			 } else {
				 int totlen = fd.getMdLen();

				 data_def = new byte[totlen];

				 for(int i = 0; i < totlen; i++)
					 data_def[i] = buffer[i];

				 reserve.store_in_reserve(buffer);
			 }
		 } else {

			 if(mode == Constants.QUANTIZED_DOMAIN){
				 fd = (FrameDataQuantized)   decoder.decodeFrame(mode);
			 } else{
				 fd = (FrameDataDequantized)    decoder.decodeFrame(mode);
			 }
		 }

		 header.copy_info(fd);


		 try {
			 header = bitstream.readFrame();
		 } catch (IOException ex) {
			 ex.printStackTrace();
		 }

		 return fd;
	 }

	 public byte[] getMainData(){
		 return data_def;
	 }


	 public byte[] getDataDef() {
		 return data_def;
	 }

	 public void encodeFrame(FrameDataQuantized fd){
		 int bitrate = 0;
		 encoder.init_frame(fd);
		 encoder.set_side_info(fd);

		 byte [] data = encoder.encode_main_data(fd);

		 while(bitrate != 1)
			 bitrate = encoder.store_data(data, fd);

		 int f_back = size_buffer - 1;
		 if(cont >= f_back)
			 formatStream((cont - f_back) % size_buffer);

		 cont++;
	 }

	 public void storeData(FrameData fd, byte [] data){
		 int bitrate= 0;

		 encoder.init_frame(fd);
		 encoder.set_side_info(fd);

		 while(bitrate != 1)
			 bitrate = encoder.store_data(data, fd);

		 int f_back = size_buffer - 1;
		 if(cont >= f_back)
			 formatStream((cont - f_back) % size_buffer);

		 cont++;
	 }

	 public byte [] getIdV2(){
		 return bitstream.get_id_v2();
	 }

	 public void formatStream(int n){
		 if(first_call){
			 first_call = false;
			 writeIdV2();
		 }

		 byte[] frame_data = encoder.get_frame_data(n);

		 if(frame_data.length > 1){
			 if(frame_data.length + position + 1 <= stream.length)
				 System.arraycopy(frame_data,0,stream, position, frame_data.length);
			 else{
				 temp = new byte[(frame_data.length * N_FRAMES + position)];
				 System.arraycopy(stream,0,temp, 0, position);
				 System.arraycopy(frame_data,0,temp, position, frame_data.length );
				 stream = temp;
			 }

			 position += frame_data.length;
		 }
	 }

	 public byte[] getStream(){
		 int end;
		 int f_back = size_buffer - 1;

		 for(int i = 0; i < f_back; i ++)
			 formatStream((cont - f_back + i ) % size_buffer);

		 for(end = stream.length - 1; end > 0; end--)
			 if (stream[end] != 0)
				 break;

		 byte [] temp = new byte[end];
		 System.arraycopy(stream, 0, temp, 0, temp.length);

		 return temp;
	 }

	 private void writeIdV2() {

		 byte [] id = bitstream.get_id_v2();
		 if (id.length > stream.length){
			 temp = new byte[stream.length + id.length];

			 System.arraycopy(id,0,temp, 0, id.length);
			 stream = temp;
		 } else System.arraycopy(id,0,stream, 0, id.length);

		 position += id.length;
	 }
}
