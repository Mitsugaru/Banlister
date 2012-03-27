package net.milkycraft;

import java.io.*;
//import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.bukkit.plugin.java.JavaPlugin;

public class McbanFileReader
{

	//Charset charset = Charset.forName("US-ASCII");
	private List<String> bannedpeople = new ArrayList<String>();

	//TODO maybe also give it our plugin as well so it can log to our logger
	public McbanFileReader(JavaPlugin plugin)
	{
		try
		{
			//TODO have FileReader point to the list
			Scanner reader = new Scanner(new FileReader(plugin.getDataFolder().getParent() + "/mcbans/backup.txt"));
			//The bans file is a simple CSV of names
			reader.useDelimiter(",");
			while (reader.hasNext())
			{
				bannedpeople.add(reader.next());
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