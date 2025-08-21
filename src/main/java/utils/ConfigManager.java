package utils;
import java.util.Properties;

import properties.PropertiesReader;

public class ConfigManager {
	private static ConfigManager instance;
	private final PropertiesReader propsreader;
	
	private ConfigManager(){
		this.propsreader= new PropertiesReader();
	}
	
	public static synchronized ConfigManager getInstance() {
		if(instance==null) {
			instance=new ConfigManager();
		}
		return instance;
	}
	
	public String get(String property) {
		return propsreader.get(property);
	}
}
