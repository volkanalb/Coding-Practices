package com.vvv.stack04;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {
	public static void main(String[] args) {
		List<Api> apis = new ArrayList<>();
		Api r1 = new Api();
		List<Config> configs1 = new ArrayList<>();
		Config c1 = new Config();
		c1.setCountry("M1");
		c1.setUsersegment("5|6|10");
		configs1.add(c1);
		Config c2 = new Config();
		c2.setCountry("M2");
		c2.setUsersegment("10|11");
		configs1.add(c2);
		r1.setConfigs(configs1);
		apis.add(r1);
		Api r2 = new Api();
		List<Config> configs2 = new ArrayList<>();

		Config c21 = new Config();
		c21.setCountry("M1");
		c21.setUsersegment("5|6|10|11");
		configs2.add(c21);
		Config c22 = new Config();
		c22.setCountry("M2");
		c22.setUsersegment("10|11");
		configs2.add(c22);

		r2.setConfigs(configs2);
		apis.add(r2);
		long count = apis.stream().filter(api -> (api.getConfigs().stream().filter(
				singleConfig -> singleConfig.usersegment.contains("5") && singleConfig.country.equals("M1"))
				.count() > 0)).count();

		System.out.println(count);

		List<Api> collect = apis
				.stream().filter(
						api -> (api.getConfigs().stream()
								.filter(singleConfig -> singleConfig.getUsersegment().contains("11")
										&& singleConfig.country.equals("M1"))
								.count() > 0))
				.collect(Collectors.toList());

		System.out.println(collect.size());
	}

	private static class Api {
		private List<Config> configs;

		public Api() {
			setConfigs(new ArrayList<>());
		}

		public List<Config> getConfigs() {
			return configs;
		}

		public void setConfigs(List<Config> configs) {
			this.configs = configs;
		}
	}

	private static class Config {
		private String usersegment;
		private String country;

		public String getUsersegment() {
			return usersegment;
		}

		public void setUsersegment(String usersegment) {
			this.usersegment = usersegment;
		}

		public String getCountry() {
			return country;
		}

		public void setCountry(String country) {
			this.country = country;
		}
	}
}
