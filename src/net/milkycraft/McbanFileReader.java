package net.milkycraft;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.plugin.java.JavaPlugin;

public class McbanFileReader
{

	//Charset charset = Charset.forName("US-ASCII");
	private List<String> bannedpeople = new ArrayList<String>();

	public McbanFileReader(JavaPlugin plugin)
	{
		try
		{
			//TODO have FileReader point to the list
			BufferedReader reader = new BufferedReader(
					new FileReader("/mcbans/backup.txt"));

			String line;
			while ((line = reader.readLine()) != null)
			{
				//TODO figure out what mcbans puts in the backup text file
				//TODO if necessary, parse through the info to get the player name
				//TODO also, if you want, could have the output include extra info
				bannedpeople.add(line);
			}
			reader.close();
		}
		catch (IOException x)
		{
			System.err.format("IOException: %s%n", x);
		}
	}
	
	public List<String> getMcbans()
	{
		return bannedpeople;
	}
}