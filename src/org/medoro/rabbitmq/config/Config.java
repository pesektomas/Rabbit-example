package org.medoro.rabbitmq.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


public class Config {

	private static Config instance;
	
	private final Properties properties = new Properties();
	
	private Config(){}
	
	public static Config getInstance(){
		if(instance == null){
			instance = new Config();
			try {
				instance.init();
			} catch (IOException e){
				e.printStackTrace();
			}
		}
		
		return instance;
	}
	
	private void init() throws IOException{
		InputStream inputStream = new FileInputStream(new File("config/config.properties"));
		properties.load(inputStream);
	}
	
	public String getProperty(String property)  {
		 return properties.getProperty(property);
	}
}
