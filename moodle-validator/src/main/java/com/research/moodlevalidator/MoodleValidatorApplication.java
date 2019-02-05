package com.research.moodlevalidator;

import javax.annotation.Resource;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.research.moodlevalidator.services.StorageService;



@SpringBootApplication
public class MoodleValidatorApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(MoodleValidatorApplication.class, args);
	}
	
	@Resource
	StorageService storageService;
	
	@Override
	public void run(String... arg) throws Exception {
		storageService.deleteAll();
		storageService.init();
	}
}
