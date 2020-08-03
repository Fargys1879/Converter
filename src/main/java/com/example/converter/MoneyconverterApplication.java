package com.example.converter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.*;
import java.net.URL;
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
		InputStream input = url.openStream();
		byte[] buffer = input.readAllBytes();
		String str = new String(buffer);
		input.close();
		FileOutputStream output = new FileOutputStream("F:/new.xml");
		byte[] buffer1 = str.getBytes();
		output.write(buffer1,0, buffer1.length);
	}

}
