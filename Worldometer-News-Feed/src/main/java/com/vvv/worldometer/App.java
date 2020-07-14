package com.vvv.worldometer;

import java.util.Scanner;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.telegram.telegrambots.ApiContextInitializer;

@SpringBootApplication
@EnableScheduling
public class App {
	@SuppressWarnings("resource")
	public static void main(String[] args) {
		ApiContextInitializer.init();
		SpringApplication.run(App.class, args);

	}
}
