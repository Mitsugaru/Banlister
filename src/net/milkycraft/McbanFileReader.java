package net.milkycraft;

import java.io.*;
import java.util.ArrayList;

public class McbanFileReader extends ArrayList<String> {
	
	Charset charset = Charset.forName("US-ASCII");
	
	try (BufferedReader reader = Files.newBufferedReader(file, charset)) {
		String line = null;
		while((line = reader.readline()) != null) {
			System.out.println(line); //Prints out the line, which you need to change to the .txt file, the entire filepath:
			//ex: String line = "C:/Users/Erik/Documents/Data.txt"
			//if not, try withoug the quotes
		}
	} catch (IOException x) {
		System.err.format("IOException: %s%n", x);
	}

}

/* or you can try using
regular files which
are created, then updated on
each method call, which I'll look
up mcbans method for banning, 
and i'll call that to write a string
in the file
*/

Path file = ...;
try {
	//create the empty file with default permissions
	Files.createFile(file);
} catch (FileAlreadyExistsException x) {
	System.err.format("file named %s" + " already exists%n", file);
} catch (IOException x) {
	//some other sort of fail
	System.err.format("createFile error: %s%n", x);;
}
