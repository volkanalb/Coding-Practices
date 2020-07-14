package com.vvv.worldometer;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.exceptions.TelegramApiException;

@Configuration
public class WorldometerConfig {

	@Autowired
	private Worldometer worldometer;
	
	
	@PostConstruct
	public void init() {
		TelegramBotsApi botsApi = new TelegramBotsApi();
        try {
            botsApi.registerBot(worldometer);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
	}

}
