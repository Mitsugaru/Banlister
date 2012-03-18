package net.milkycraft;

import java.io.*;
import java.util.ArrayList;

public class McbanFileReader extends ArrayList<String> {
	
	Charset charset = Charset.forName("US-ASCII");
	String bannedpeople = new String[5000]; //syntax might not be right
	
	
	try (BufferedReader reader = Files.newBufferedReader("/mcbans/backup.txt", charset)) {
		
		String line;
		int counter = 0;
		
		while((line = reader.readline()) != null) {
			 bannedpeople[counter++] = line;	
		}
	} catch (IOException x) {
		System.err.format("IOException: %s%n", x);
	}

}