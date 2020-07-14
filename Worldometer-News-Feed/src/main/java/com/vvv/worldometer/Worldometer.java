package com.vvv.worldometer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.Set;
import java.util.TimeZone;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

@Component
public class Worldometer extends TelegramLongPollingBot {

	private static final String BOT_TOKEN = "xxx";
	private static final String BOT_USER_NAME = "Covid19_Feed_Bot";

	private final String url = "https://www.worldometers.info/coronavirus";
	private Map<Long, List<String>> chatIdMap = new HashMap<Long, List<String>>();
	private Set<String> todayNews = new LinkedHashSet<String>();
	private String lastNews;
	private Element cases;
	private Element deaths;
	private Element recovered;

	@Scheduled(fixedDelay = 20000)
	public void checkNews() {
		try {
			Document html = Jsoup.connect(url).userAgent(
					"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.132 Safari/537.36")
					.timeout(15000).referrer("http://google.com").get();

			TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
			Calendar gmt = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			sdf.setTimeZone(TimeZone.getTimeZone("UTC"));

			Element newsBlock = html.getElementById("newsdate" + sdf.format(gmt.getTime()));

			if (newsBlock == null) {
				gmt.add(Calendar.DAY_OF_MONTH, -1);
				newsBlock = html.getElementById("newsdate" + sdf.format(gmt.getTime()));
			}

			if (newsBlock != null) {
				Elements newsPosts = newsBlock.getElementsByClass("news_post");
				if (!newsPosts.isEmpty()) {

					if (todayNews.size() > 0) {
						Set<String> tempTodayNews = new LinkedHashSet<String>();
						for (Element e : newsPosts) {
							tempTodayNews.add(e.text().replace("[source]", "").trim());
						}
						tempTodayNews.removeAll(todayNews);

						if (!tempTodayNews.isEmpty()) {
							boolean getFirst = true;
							for (String element : tempTodayNews) {
								if (getFirst) {
									lastNews = element;
									getFirst = false;
								}
								String tempNews = element;
								if (!tempNews.isEmpty()) {
									if (tempNews.length() > 2000)
										tempNews = tempNews.substring(0, 2000) + "...";
									broadcastMessage(true, tempNews);
								}
							}
						}
					}
					todayNews.clear();
					for (Element e : newsPosts) {
						todayNews.add(e.text().replace("[source]", "").trim());
					}
					if (lastNews == null || lastNews.isEmpty())
						lastNews = newsPosts.get(0).text().replace("[source]", "").trim();
					System.out.println("*****" + new Date() + " - " + chatIdMap.size());
				}
			}

			Elements stats = html.select("div[id=maincounter-wrap]");
			if (stats.size() == 3) {
				if (cases == null || !cases.text().equals(stats.get(0).text())) {

					cases = stats.get(0);
					deaths = stats.get(1);
					recovered = stats.get(2);
//
//					StringBuilder sb = new StringBuilder();
//					sb.append("Current Status : \n\n");
//					sb.append(cases.text() + "\n");
//					sb.append(deaths.text() + "\n");
//					sb.append(recovered.text() + "\n");
//
//					broadcastMessage(false, sb.toString());
				}
			}

			System.out.println(lastNews);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void onUpdateReceived(Update update) {
		saveTelegramUpdate(update);
		if (update.hasMessage() && update.getMessage().hasText()) {

			String query = update.getMessage().getText();
			Long chatId = update.getMessage().getChatId();
			System.out.println("query : " + query);
			System.out.println("chatId : " + chatId);
			if (query.equals("/start")) {
				String messageText = "";
				if (!chatIdMap.containsKey(chatId)) {
					chatIdMap.put(chatId, new ArrayList<>());
					messageText = "Welcome to Covid-19 feed bot!\n" + "use /start to subscribe news feed\n"
							+ "use /help for more commands.";
				} else {
					messageText = "You have already subscribed. Please use /help for available commands.";
				}
				sendMessage(chatId, messageText);

			} else if (query.equals("/stop")) {

				if (chatIdMap.containsKey(chatId)) {
					chatIdMap.remove(chatId);
					String messageText = "Successfully Unsubscribed from the News Feed!";
					sendMessage(chatId, messageText);
				}
			} else if (query.equals("/last")) {
				if (lastNews != null & !lastNews.isEmpty())
					sendMessage(chatId, lastNews);
			} else if (query.startsWith("/addfilter")) {
				String filter = query.replace("/addfilter", "").trim();
				if (filter.length() > 0) {
					if (!chatIdMap.containsKey(chatId))
						chatIdMap.put(chatId, new ArrayList<String>());
					List<String> filterList = chatIdMap.get(chatId);
					filterList.add(filter);
					chatIdMap.put(chatId, filterList);
					String messageText = "Successfully subscribed to " + filter + " news";
					sendMessage(chatId, messageText);
				}
			} else if (query.equals("/stat")) {
				if (cases != null & !cases.text().isEmpty()) {
					StringBuilder sb = new StringBuilder();
					sb.append(cases.text() + "\n");
					sb.append(deaths.text() + "\n");
					sb.append(recovered.text() + "\n");

					sendMessage(chatId, sb.toString());
				}
			} else if (query.equals("/help")) {

				StringBuilder builder = new StringBuilder();
				builder.append("Available commands : \n");
				builder.append("/start - Subscribe to News Feed\n");
				builder.append("/stop - Unsubscribe from News Feed\n");
				builder.append("/last - Latest News\n");
				builder.append("/stat - Case Statistics\n");
				builder.append(
						"/addfilter filter - Filter News. You can add as many as you want. They will be combined with 'OR' condition. Ex: /addfilter Turkey\n");
				builder.append("/help - For Available Commands");
				sendMessage(chatId, builder.toString());

			}
		}

	}

	public String getBotUsername() {
		return BOT_USER_NAME;
	}

	@Override
	public String getBotToken() {
		return BOT_TOKEN;
	}

	@PostConstruct
	public void init() {
		chatIdMap = getChatIdList();
//		lastNews = getLastNews();
		broadcastMessage(false,
				"I'm here again. You can use /start to subscribe news feed if you have not registered. Use /help for more commands.");
	}

	@PreDestroy
	public void destroy() {
		saveChatIdList();
//		saveLastNews();
		broadcastMessage(false, "Going offline. See you soon!");
	}

	private void sendMessage(Long chatId, String messageText) {
		SendMessage message = new SendMessage().setChatId(chatId).setText(messageText);

		try {
			execute(message);
		} catch (TelegramApiException e) {
			e.printStackTrace();
		}
	}

	private void broadcastMessage(boolean useFilter, String messageText) {
		for (Entry<Long, List<String>> entry : chatIdMap.entrySet()) {
			if (useFilter && !entry.getValue().isEmpty()) {
				boolean skip = true;
				for (String filter : entry.getValue()) {
					if (messageText.contains(filter)) {
						skip = false;
						break;
					}
				}

				if (skip)
					continue;
			}
			sendMessage(entry.getKey(), messageText);
		}
//		chatIdMap.forEach(t -> sendMessage(t, messageText));
	}

	private void saveTelegramUpdate(Update update) {

		try {
			FileWriter writer = new FileWriter("telegramMessages.txt", true);
			writer.write(update.toString());
			writer.write(System.lineSeparator());
			writer.write("*************************");
			writer.write(System.lineSeparator());

			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void saveChatIdList() {
		FileWriter writer;
		try {
			writer = new FileWriter("chatIdList.txt");
			for (Entry<Long, List<String>> entry : chatIdMap.entrySet()) {
				try {
					writer.write(entry.getKey().toString());
					if (!entry.getValue().isEmpty()) {
						writer.write(" ");
						for (String filter : entry.getValue().subList(0, entry.getValue().size() - 1)) {
							writer.write(filter + " ");
						}
						writer.write(entry.getValue().get(entry.getValue().size() - 1));
					}
					writer.write(System.lineSeparator());

				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

//	private void saveLastNews() {
//		FileWriter writer;
//		try {
//			writer = new FileWriter("lastNews.txt");
//			writer.write(lastNews + System.lineSeparator());
//			writer.close();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
//
//	@SuppressWarnings("resource")
//	private String getLastNews() {
//		Scanner scanner;
//		String temp = "";
//		try {
//			scanner = new Scanner(new File("lastNews.txt"));
//			while (scanner.hasNext()) {
//				temp = scanner.next();
//			}
//			return temp;
//		} catch (FileNotFoundException e) {
//			return "";
//		}
//	}

	@SuppressWarnings("resource")
	private Map<Long, List<String>> getChatIdList() {
		Scanner scanner;
		try {
			scanner = new Scanner(new File("chatIdList.txt"));
			Map<Long, List<String>> tempMap = new HashMap<Long, List<String>>();
			while (scanner.hasNext()) {
				String line = scanner.nextLine();
				System.out.println("line : " + line);
				List<String> filter = new ArrayList<>();
				Long chatId = 0L;
				if (line.contains(" ")) {
					String[] split = line.split(" ");
					chatId = Long.valueOf(split[0].trim());
					System.out.println("split.length : " + split.length);
					for (int i = 1; i < split.length; i++) {
						filter.add(split[i]);
					}
					System.out.println("filter : " + filter);
				} else {
					chatId = Long.valueOf(line.trim());
				}
				System.out.println("chatId : " + chatId);
				tempMap.put(chatId, filter);
			}
			return tempMap;
		} catch (FileNotFoundException e) {
			return new HashMap<Long, List<String>>();
		}
	}
}
