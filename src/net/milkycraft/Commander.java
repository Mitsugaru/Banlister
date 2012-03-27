package net.milkycraft;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
// import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class Commander implements CommandExecutor
{
	/**
	 * Class variables
	 */
	private Banlisting plugin;
	private Config config;
	private final Map<String, Integer> page = new HashMap<String, Integer>();
	private final Map<String, List<String>> privateSearch = new HashMap<String, List<String>>();
	private final List<String> banned = new ArrayList<String>();
	private final static String bar = "======================";
	

	public Commander(Banlisting plugin)
	{
		this.plugin = plugin;
		config = plugin.getPluginConfig();
		/**
		 * Get banned list and store locally
		 */
		// Generate list of local banned players
		for(OfflinePlayer op : Bukkit.getBannedPlayers())
		{
			banned.add(ChatColor.GOLD + op.getName());
		}
		//If we are using mcbans, grab and add to list
		if(config.mcbans)
		{
			final McbanFileReader mcban = new McbanFileReader(plugin);
			for(String player : mcban.getMcbans())
			{
				banned.add(ChatColor.BLUE + player);
			}
		}
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command,
			String commandlabel, String[] args)
	{
		if (args.length == 0)
		{
			if (sender.hasPermission("banlisting.bans"))
			{
				page.put(sender.getName(), 0);
				this.listBanned(sender, 0);
				return true;
			}
			else
			{
				sender.sendMessage(ChatColor.RED + Banlisting.prefix
						+ " Lack Permission: banlisting.bans");
				return true;
			}
		}
		else
		{
			final String cmd = args[0];
			if (sender.hasPermission("banlisting.bans"))
			{
				if (cmd.equalsIgnoreCase("help"))
				{
					this.displayHelp(sender);
					return true;
				}
				else if (cmd.equalsIgnoreCase("version"))
				{
					this.showVersion(sender);

					return true;
				}
				else if (cmd.equalsIgnoreCase("reload"))
				{
					// Instead of calling plugin.reloadConfig(), we tell our
					// Config class to reload. It does the plugin.reloadConfig()
					// and other stuff. For more info, check the method reload()
					// in the Config class.
					config.reload();
					sender.sendMessage(ChatColor.AQUA + Banlisting.prefix
							+ ChatColor.GREEN
							+ " Config has been sucessfully reloaded!");
					return true;
				}
				else if (cmd.equalsIgnoreCase("prev"))
				{
					this.listBanned(sender, -1);
					return true;
				}
				else if (cmd.equalsIgnoreCase("next"))
				{
					this.listBanned(sender, 1);
					return true;
				}
				else if(cmd.equalsIgnoreCase("end") || cmd.equalsIgnoreCase("stop") || cmd.equalsIgnoreCase("clear"))
				{
					if(privateSearch.containsKey(sender.getName()))
					{
						privateSearch.remove(sender.getName());
						sender.sendMessage(ChatColor.AQUA + Banlisting.prefix
							+ ChatColor.GREEN
							+ " Custom search cleared.");
					}
					else
					{
						sender.sendMessage(ChatColor.AQUA + Banlisting.prefix
								+ ChatColor.YELLOW
								+ " No custom search attached...");
					}
					return true;
				}
				else
				{
					try
					{
						int pageNum = Integer.parseInt(cmd);
						page.put(sender.getName(), pageNum - 1);
						this.listBanned(sender, 0);
					}
					catch (NumberFormatException e)
					{
						//Perhaps they gave a partial name? attempt to search list
						SearchTask search = new SearchTask(sender, cmd);
						plugin.getServer().getScheduler().scheduleAsyncDelayedTask(plugin, search);
						/*sender.sendMessage(ChatColor.RED
								+ " Syntax error make sure you type the command right");
						return false;*/
					}
					return true;
				}
			}
			else
			{
				sender.sendMessage(ChatColor.RED + Banlisting.prefix
						+ " Lack Permission: banlisting.bans");
				return true;
			}
		}

	}

	private void displayHelp(CommandSender sender)
	{
		sender.sendMessage(ChatColor.BLUE + "=====" + ChatColor.RED
				+ "BanListing" + ChatColor.BLUE + "=====");
		if (sender.hasPermission("banlisting.bans"))
		{
			sender.sendMessage(ChatColor.GREEN + "/bp [#]" + ChatColor.YELLOW
					+ " : Show banned players");
			sender.sendMessage(ChatColor.GREEN + "/bp prev" + ChatColor.YELLOW
					+ " : Show previous page");
			sender.sendMessage(ChatColor.GREEN + "/bp next" + ChatColor.YELLOW
					+ " : Show next page");
		}
	}

	/**
	 * Shows plugin version and config
	 * 
	 * @param Player
	 *            that sent the message
	 * @param
	 */
	private void showVersion(CommandSender sender)
	{
		sender.sendMessage(ChatColor.BLUE + bar + "=====");
		sender.sendMessage(ChatColor.RED + "BanListing v"
				+ plugin.getDescription().getVersion());
		sender.sendMessage(ChatColor.GREEN + "Coded by milkywayz");
		sender.sendMessage(ChatColor.GREEN + "Assisted by Mitsugaru");
		sender.sendMessage(ChatColor.GREEN + "Originally by bobbysmithyy");
		sender.sendMessage(ChatColor.BLUE + "===========" + ChatColor.GRAY
				+ "Config" + ChatColor.BLUE + "===========");
		sender.sendMessage(ChatColor.GRAY + "Page entry limit: " + config.limit);
		if (config.log)
		{
			sender.sendMessage(ChatColor.GRAY + "Logging enabled");
		}
	}

	/**
	 * Lists banned players in a paginated way
	 * 
	 * @param player
	 *            to show list to
	 * @param page
	 *            adjustment
	 */
	private void listBanned(CommandSender player, int pageAdjust)
	{
		String[] array = null;
		//Determine whether they are paging through a search or through the general list
		if(privateSearch.containsKey(player.getName()))
		{
			array = privateSearch.get(player.getName()).toArray(new String[0]);
		}
		else
		{
			array = banned.toArray(new String[0]);
		}
		// Add player if they don't exist
		if (!page.containsKey(player.getName()))
		{
			page.put(player.getName(), 0);
		}
		else
		{
			// They already exist, so adjust if necessary
			if (pageAdjust != 0)
			{
				int adj = page.get(player.getName()).intValue() + pageAdjust;
				page.put(player.getName(), adj);
			}
		}
		if (array.length <= 0)
		{
			// No players in ban list
			player.sendMessage(ChatColor.YELLOW + Banlisting.prefix
					+ " No banned players");
			return;
		}
		boolean valid = true;
		final int limit = config.limit;
		// Caluclate amount of pages
		int num = array.length / limit;
		final double rem = (double) array.length % (double) limit;
		if (rem != 0)
		{
			num++;
		}
		if (page.get(player.getName()).intValue() < 0)
		{
			// They tried to use prev when they're on page 0
			player.sendMessage(ChatColor.YELLOW + Banlisting.prefix
					+ " Page does not exist");
			// reset their current page back to 0
			page.put(player.getName(), 0);
			valid = false;
		}
		else if ((page.get(player.getName()).intValue()) * limit > array.length)
		{
			// They tried to use next at the end of the list
			player.sendMessage(ChatColor.YELLOW + Banlisting.prefix
					+ " Page does not exist");
			// Revert to last page
			page.put(player.getName(), num - 1);
			valid = false;
		}
		if (valid)
		{
			// Header with amount of pages
			player.sendMessage(ChatColor.BLUE + "===" + ChatColor.RED
					+ "Banned List" + ChatColor.BLUE + "===" + ChatColor.WHITE
					+ "Page: " + ((page.get(player.getName()).intValue()) + 1)
					+ ChatColor.BLUE + " of " + ChatColor.WHITE + num
					+ ChatColor.BLUE + "===");
			// list
			for (int i = ((page.get(player.getName()).intValue()) * limit); i < ((page
					.get(player.getName()).intValue()) * limit) + limit; i++)
			{
				// Don't try to pull something beyond the bounds
				if (i < array.length)
				{
					player.sendMessage(array[i]);
				}
				else
				{
					break;
				}
			}
		}
	}
	
	public class SearchTask implements Runnable
	{
		private CommandSender sender;
		private String partialName;
		
		public SearchTask(CommandSender sender, String partialName)
		{
			this.sender = sender;
			this.partialName = partialName;
		}

		@Override
		public void run()
		{
			final List<String> search = new ArrayList<String>();
			for(String s : banned)
			{
				if(s.contains(partialName))
				{
					//Add all names that have the partial name in some form
					search.add(s);
				}
			}
			if(!search.isEmpty())
			{
				//Attach list to player
				sender.sendMessage(ChatColor.AQUA + Banlisting.prefix + ChatColor.GREEN + " Found " + ChatColor.BLUE + search.size() + ChatColor.GREEN + " entries for '" + ChatColor.WHITE + partialName + ChatColor.GREEN +"'");
				sender.sendMessage(ChatColor.GREEN + "Attaching list for paging. Detach using: /bp stop");
				privateSearch.put(sender.getName(), search);
				//Reset their page and show the first page of list
				page.put(sender.getName(), 0);
				listBanned(sender, 0);
			}
			else
			{
				//No results for the given entry
				sender.sendMessage(ChatColor.AQUA + Banlisting.prefix + ChatColor.YELLOW + " No names match '" + ChatColor.WHITE + "'");
			}
		}
	}

}
