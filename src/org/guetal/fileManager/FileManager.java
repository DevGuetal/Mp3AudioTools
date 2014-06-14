package org.guetal.fileManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class FileManager {

	private static final String INPUT_DIR  = "/input/";
	private static final String OUTPUT_DIR = "/output/test.mp3";

	public static InputStream getInputStream(String filename)  {
		File inputFile = new File(System.getProperty("user.dir") + INPUT_DIR + filename);
        InputStream is = null;
		try {
			is = new FileInputStream(inputFile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
        return is;
	}
	
	public void saveMedia(byte[] stream, String string) {
		File f = new File(System.getProperty("user.dir") + OUTPUT_DIR);
		
		try {
			FileOutputStream fos = new FileOutputStream(f);
			
			fos.write(stream, 0, stream.length);
			fos.flush();
			fos.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

}
