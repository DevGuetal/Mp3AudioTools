package org.guetal.mp3.processing;

import org.guetal.mp3.processing.samples.TremoloExample;

public class Main {

	
	public static void main(String[] args) {
		TremoloExample test = new TremoloExample();
		try {
			test.startApp();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
