package com.example.converter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
public class MoneyconverterApplication {

	public static void main(String[] args) throws Exception  {

		SpringApplication.run(MoneyconverterApplication.class, args);
		try {
			getXML();
		} catch (Exception e) {
			System.out.println(e);
		}

	}
	public static void getXML() throws IOException {
		URL url = new URL("http://www.cbr.ru/scripts/XML_daily.asp");
		File file = new File("new.xml");
		InputStream in = url.openStream();
		Files.copy(in, Paths.get(file.getName()), StandardCopyOption.REPLACE_EXISTING);
	}
}
