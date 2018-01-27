package model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import utils.DB;

public class World {

	/**
	 * 
	 */
	private static Random random = new Random();
	private static List<RawMaterialsMarket> rawMaterialsMarkets = new ArrayList<>();
	private static List<StockExchange> stockExchanges = new ArrayList<>();
	private static List<CurrenciesMarket> currenciesMarkets = new ArrayList<>();
	private static List<Company> companies = new ArrayList<>();
	private static List<Currency> currencies = new ArrayList<>();
	private static List<RawMaterial> rawMaterials = new ArrayList<>();
	private static List<Investor> investors = new ArrayList<>();
	private static List<InvestmentFund> investmentFunds = new ArrayList<>();
	private static int nextAssetId = 0;
	private static LocalDateTime date = LocalDateTime.now();
	private static int numberOfAssets = 0;


	public static int getNumberOfAssets() {
		return numberOfAssets;
	}

	public static void incrementNumberOfAssets() {
		World.numberOfAssets++;
		if (investmentFunds.size()  < (numberOfAssets / 4 )) {
			addRandomInvestmentFund();
		}
		if (investors.size()  < (numberOfAssets / 3))
			addRandomInvestor();
	}

	private static void addRandomInvestmentFund() {
		InvestmentFund investmentFund = new InvestmentFund();
		String firstName = DB.getFirstNames().get(random.nextInt(DB.getFirstNames().size()));
		investmentFund.setManagerFirstName(firstName);
		String lastName = DB.getLastNames().get(random.nextInt(DB.getLastNames().size()));
		investmentFund.setManagerSecondName(lastName);

		String name = DB.getInvestmentFundsNames().get(random.nextInt(DB.getInvestmentFundsNames().size()));
		investmentFund.setName(name);

		BigDecimal budget = new BigDecimal(random.nextDouble() * 1000000);
		investmentFund.setBudget(budget);
		investmentFunds.add(investmentFund);
		new Thread(investmentFund).start();
	}

	private static void addRandomInvestor() {
		Investor investor = new Investor();
		String firstName = DB.getFirstNames().get(random.nextInt(DB.getFirstNames().size()));
		investor.setFirstName(firstName);
		String lastName = DB.getLastNames().get(random.nextInt(DB.getLastNames().size()));
		investor.setSecondName(lastName);
		String PESEL = Integer.toString(random.nextInt(999999999) + 100000000)
				+ Integer.toString(random.nextInt(99) + 10);
		investor.setPESEL(PESEL);
		BigDecimal budget = new BigDecimal(random.nextDouble() * 1000000);
		investor.setBudget(budget);
		investors.add(investor);
		new Thread(investor).start();
	}

	public static void addCompany(Company company) {
		companies.add(company);
		incrementNumberOfAssets();
	}

	public static void addCurrency(Currency currency) {
		currencies.add(currency); 
		currency.giveItToGamblers(1000000);
		incrementNumberOfAssets();
	}

	public static void addRawMaterial(RawMaterial rawMaterial) {
		rawMaterials.add(rawMaterial);
		rawMaterial.giveItToGamblers(100000);
		incrementNumberOfAssets();
	}

	public static void decrementNumberOfAssets() {
		World.numberOfAssets--;
	}

	public static synchronized void setDate(LocalDateTime date) {
			World.date = date;
	}

	public static synchronized LocalDateTime getDate() {
			return date;
	}

	public static int getNextAssetId() {
		return nextAssetId;
	}

	public static void setNextAssetId(int lastAssetId) {
		World.nextAssetId = lastAssetId;
	}

	public static void incrementAssetId() {
		nextAssetId++;
	}

	public static List<RawMaterialsMarket> getRawMaterialsMarkets() {
		return rawMaterialsMarkets;

	}

	public static void setRawMaterialsMarkets(List<RawMaterialsMarket> rawMaterialsMarkets) {
		World.rawMaterialsMarkets = rawMaterialsMarkets;
	}

	public static List<StockExchange> getStockExchanges() {
		return stockExchanges;
	}

	public static void setStockExchanges(List<StockExchange> stockExchanges) {
		World.stockExchanges = stockExchanges;
	}

	public static List<CurrenciesMarket> getCurrenciesMarkets() {
		return currenciesMarkets;
	}

	public static void setCurrenciesMarkets(List<CurrenciesMarket> currenciesMarkets) {
		World.currenciesMarkets = currenciesMarkets;
	}

	public static List<Company> getCompanies() {
		return companies;
	}

	public static void setCompanies(List<Company> companies) {
		World.companies = companies;
	}

	public static List<Currency> getCurrencies() {
		return currencies;
	}

	public static void setCurrencies(List<Currency> currencies) {
		World.currencies = currencies;
	}

	public static List<RawMaterial> getRawMaterials() {
		return rawMaterials;
	}

	public static void setRawMaterials(List<RawMaterial> rawMaterials) {
		World.rawMaterials = rawMaterials;
	}

	public static List<Investor> getInvestors() {
		return investors;
	}

	public static void setInvestors(List<Investor> investors) {
		World.investors = investors;
	}

	public static List<InvestmentFund> getInvestmentFunds() {
		return investmentFunds;
	}

	public static void setInvestmentFunds(List<InvestmentFund> investmentFunds) {
		World.investmentFunds = investmentFunds;
	}

	public static void setNumberOfAssets(int numberOfAssets) {
		World.numberOfAssets = numberOfAssets;
	}
	
	

}
