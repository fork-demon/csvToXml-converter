package com.sapient.csvtoxml;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.sapient.csvtoxml.xgen.app.CXClassGenerator;

@SpringBootApplication
public class CsvToXmlApplication {

	public static void main(String[] args) {
		SpringApplication.run(CsvToXmlApplication.class, args);
	}
	
	@Bean
	public CommandLineRunner runner() {
	    return new CommandLineRunner() {
	        @Override
	        public void run(String... args) throws Exception {
	        	CXClassGenerator.init();
	        }
	    };
	}
}
