package net.milkycraft;

import java.io.*;
//import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.plugin.java.JavaPlugin;

public class McbanFileReader
{

	//Charset charset = Charset.forName("US-ASCII");
	//TODO If we have details, will use a custom object to hold all details, in place of string
	private List<String> bannedpeople = new ArrayList<String>();

	//TODO maybe also give it our plugin as well so it can log to our logger
	public McbanFileReader(JavaPlugin plugin)
	{
		try
		{
			//TODO have FileReader point to the list
			BufferedReader reader = new BufferedReader(
					new FileReader(plugin.getDataFolder().toString() + "/backup.txt"));
			String line;
			while ((line = reader.readLine()) != null)
			{
				//TODO figure out what mcbans puts in the backup text file
				//TODO if necessary, parse through the info to get the player name
				//TODO also, if you want, could have the output include extra info
				bannedpeople.add(line);
			}
			//Close reader
			reader.close();
		}
		catch (IOException x)
		{
			//TODO make this a log entry
			System.err.format("IOException: %s%n", x);
		}
	}
	
	/**
	 * Get the resulting list from reading the MCBans file
	 * 
	 * @return An ArrayList of the names
	 */
	public List<String> getMcbans()
	{
		return bannedpeople;
	}
}