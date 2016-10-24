
package com.smm.ctrm.domain.apiClient;

import org.hibernate.cfg.Configuration;

public class ApiServerConfiguration
{
	private static Configuration _config = null;

	public static Configuration getConfig()
	{
		if (_config == null)
		{
				//var config = ConfigurationManager.OpenExeConfiguration(ConfigurationUserLevel.None);
			/*var configPath = System.IO.Path.Combine(System.Environment.CurrentDirectory, "app.config");

			ExeConfigurationFileMap map = new ExeConfigurationFileMap();
			map.ExeConfigFilename = configPath;

			_config = ConfigurationManager.OpenMappedExeConfiguration(map,ConfigurationUserLevel.None);*/
		}
		return _config;
	}


	public static void Set(String settingsKey, String settingsValue)
	{
		//var configPath = System.IO.Path.Combine(System.Environment.CurrentDirectory, "app.config");
		//var config = ConfigurationManager.OpenExeConfiguration(configPath);
		if (getConfig() == null)
		{
			return;
		}

		/*if (getConfig().AppSettings.Settings[settingsKey] == null)
		{
			getConfig().AppSettings.Settings.Add(settingsKey, settingsValue);
		}
		else
		{
			getConfig().AppSettings.Settings[settingsKey].setValue(settingsValue);
		}

		getConfig().Save(ConfigurationSaveMode.Modified);
		ConfigurationManager.RefreshSection("appSettings");*/
	}

	public static String Get(String settingsKey)
	{
		return settingsKey;
		/*if (getConfig().AppSettings.Settings[settingsKey] == null)
		{
			return "";
		}

		return getConfig().AppSettings.Settings[settingsKey].getValue();*/
		//return ConfigurationManager.AppSettings.Get(settingsKey);
	}
}