package org.guetal.fileManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileManager {
	
	private final String OUTPUT_DIR = "/output/test.mp3";

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
