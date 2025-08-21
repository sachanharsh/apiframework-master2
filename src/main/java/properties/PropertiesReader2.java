package properties;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesReader2 {
	private static PropertiesReader2 instance;
    private Properties Prop;

    private PropertiesReader2() {
        Prop = new Properties();
        try {
            String env = "environment";
            String propertiesFilePath = env + ".properties";
            InputStream inputStream = getResourceAsStream(propertiesFilePath);
            Prop.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static synchronized PropertiesReader2 getInstance() {
    	if(instance==null)
    		instance=new PropertiesReader2();
    	return instance;
    }

    private InputStream getResourceAsStream(String path) {
        return this.getClass().getClassLoader().getResourceAsStream(path);
    }

    public String getHost() {
        return Prop.getProperty("host");
    }
    
    public String get(String property) {
        return Prop.getProperty(property);
    }
}
