package utils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class DB {

	private static List<String> countries = new ArrayList<>();
	private static List<String> firstNames = new ArrayList<>();
	private static List<String> lastNames = new ArrayList<>();
	private static List<String> companiesNames = new ArrayList<>();
	private static List<String> cities = new ArrayList<>();
	private static List<String> investmentFundsNames = new ArrayList<>();;
	private static List<String> streets = new ArrayList<>();
	private static List<String> rawMaterials = new ArrayList<>();
	// private static ClassLoader classLoader = DB.class.getClassLoader();

	public static void init() throws IOException {

		try {

			populateList(countries, "/data/countries.txt");
			populateList(firstNames, "/data/firstNames.txt");
			populateList(lastNames, "/data/lastNames.txt");
			populateList(companiesNames, "/data/companiesNames.txt");
			populateList(cities, "/data/cities.txt");
			populateList(investmentFundsNames, "/data/investmentFundsNames.txt");
			populateList(streets, "/data/streets.txt");
			populateList(rawMaterials, "/data/rawMaterials.txt");

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	}

	private static void populateList(List<String> list, String path) throws IOException {
		InputStream inputStream = DB.class.getResourceAsStream(path);
		InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
		BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
		String line;
		while ((line = bufferedReader.readLine()) != null) {
			list.add(line);
		}
	}

	public static List<String> getRawMaterials() {
		return rawMaterials;
	}

	public static List<String> getCities() {
		return cities;
	}

	public static List<String> getInvestmentFundsNames() {
		return investmentFundsNames;
	}

	public static List<String> getStreets() {
		return streets;
	}

	public static List<String> getCompaniesNames() {
		return companiesNames;
	}

	public static void setCompaniesNames(List<String> companiesNames) {
		DB.companiesNames = companiesNames;
	}

	public static void setCountries(List<String> countries) {
		DB.countries = countries;
	}

	public static void setFirstNames(List<String> firstNames) {
		DB.firstNames = firstNames;
	}

	public static void setLastNames(List<String> lastNames) {
		DB.lastNames = lastNames;
	}

	public static List<String> getLastNames() {
		return lastNames;
	}

	public static List<String> getCountries() {
		return countries;
	}

	public static List<String> getFirstNames() {
		return firstNames;
	}
}
