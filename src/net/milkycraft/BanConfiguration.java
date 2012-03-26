package net.milkycraft;

import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.configuration.MemoryConfiguration;
import org.bukkit.plugin.Plugin;

/*
 * This config was made with lots of help from antishare source code. Thanks to
 * fieldmaster!
 */
public class BanConfiguration extends
		org.bukkit.configuration.file.YamlConfiguration
{
	private final File file;
	private final Plugin plugin;
	private Exception exception;
	private Map<String, Object> cache = new HashMap<String, Object>();
	private boolean modified = false;
	private long last_modified = -1L;

	public BanConfiguration(Plugin plugin)
	{
		this("config.yml", plugin);
	}

	public BanConfiguration(String file, Plugin plugin)
	{
		this(new File(plugin.getDataFolder(), file), plugin);
	}

	public BanConfiguration(File file)
	{
		this(file, null);
	}

	public BanConfiguration(File file, Plugin plugin)
	{
		this.file = file;
		this.plugin = plugin;
		options = new BanConfigurationOptions(this);
		load();
	}

	public final boolean load()
	{
		if (last_modified != -1L && !isFileModified())
		{
			return true;
		}
		try
		{
			load(file);
			clearCache();
			last_modified = file.lastModified();
			return true;
		}
		catch (Exception ex)
		{
			exception = ex;
			return false;
		}
	}

	public final boolean save()
	{
		try
		{
			save(file);
			modified = false;
			return true;
		}
		catch (Exception ex)
		{
			exception = ex;
			return false;
		}
	}

	public Exception getLastException()
	{
		return exception;
	}

	public boolean loadDefaults()
	{
		try
		{
			return loadDefaults(file.getName());
		}
		catch (Exception ex)
		{
			exception = ex;
			return false;
		}
	}

	public boolean loadDefaults(String filename)
	{
		try
		{
			return loadDefaults(plugin.getResource(filename));
		}
		catch (Exception ex)
		{
			exception = ex;
			return false;
		}
	}

	public boolean loadDefaults(InputStream filestream)
	{
		try
		{
			setDefaults(loadConfiguration(filestream));
			clearCache();
			return true;
		}
		catch (Exception ex)
		{
			exception = ex;
			return false;
		}
	}

	public boolean saveDefaults()
	{
		options().copyDefaults(true); // These stay so future saves continue to
										// copy over.
		options().copyHeader(true);
		return save();
	}

	public boolean checkDefaults()
	{
		if (getDefaults() == null)
		{
			return true;
		}
		return getKeys(true).containsAll(getDefaults().getKeys(true));
	}

	public final void clearDefaults()
	{
		setDefaults(new MemoryConfiguration());
	}

	public boolean needsUpdate()
	{
		return !fileExists() || !checkDefaults() || isModified();
	}

	public boolean fileExists()
	{
		try
		{
			return file.exists();
		}
		catch (Exception ex)
		{
			exception = ex;
			return false;
		}
	}

	@Override
	public BanConfigurationOptions options()
	{
		return (BanConfigurationOptions) options;
	}

	@Override
	public Object get(String path, Object def)
	{
		Object value = cache.get(path);
		if (value != null)
		{
			return value;
		}

		value = super.get(path, def);
		if (value != null)
		{
			cache.put(path, value);
		}

		return value;
	}

	@Override
	public void set(String path, Object value)
	{
		if (value == null && cache.containsKey(path))
		{
			cache.remove(path);
			modified = true;
		}
		else if (value != null)
		{
			if (!value.equals(get(path)))
			{
				modified = true;
			}
			cache.put(path, value);

		}
		super.set(path, value);
	}

	public void unset(String path)
	{
		set(path, null);
	}

	public void clearCache()
	{
		cache.clear();
	}

	public Plugin getPlugin()
	{
		return plugin;
	}

	protected File getFile()
	{
		return file;
	}

	public boolean isModified()
	{
		return modified;
	}

	public boolean isFileModified()
	{
		try
		{
			return last_modified != file.lastModified();
		}
		catch (Exception e)
		{
			this.exception = e;
			return false;
		}
	}
}