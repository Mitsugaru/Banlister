package net.milkycraft;

import java.io.*;
import java.util.ArrayList;

public class McbanFileReader extends ArrayList<String> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static ArrayList<String> readFile() {
		  String line = "";
		  String fileName = 
		  ArrayList<String> data = new ArrayList<String>();
		  try {
		   FileReader fr = new FileReader();
		   BufferedReader br = new BufferedReader(fr);
		   while((line = br.readLine()) != null) {
		 
		    data.add(line);
		   }
		  }
		  catch(FileNotFoundException fN) {
		   fN.printStackTrace();
		  }
		  catch(IOException e) {
		   System.out.println(e);
		  }
		  return data;
		 }   
}