package utils;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import model.Company;
import model.CurrenciesMarket;
import model.Currency;
import model.InvestmentFund;
import model.Investor;
import model.Market;
import model.RawMaterial;
import model.RawMaterialsMarket;
import model.StockExchange;

public class SerializableWorld implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3107420215750629056L;

	private List<RawMaterialsMarket> rawMaterialsMarkets = new ArrayList<>();
	private List<StockExchange> stockExchanges = new ArrayList<>();
	private List<CurrenciesMarket> currenciesMarkets = new ArrayList<>();
	private List<Company> companies = new ArrayList<>();
	private List<Currency> currencies = new ArrayList<>();
	private List<RawMaterial> rawMaterials = new ArrayList<>();
	private List<Investor> investors = new ArrayList<>();
	private List<InvestmentFund> investmentFunds = new ArrayList<>();
	private int nextAssetId;
	private LocalDateTime date;
	private int numberOfAssets;

	public LocalDateTime getDate() {
		return date;
	}

	public void setDate(LocalDateTime date) {
		this.date = date;
	}

	public int getNextAssetId() {
		return nextAssetId;
	}

	public void setNextAssetId(int nextAssetId) {
		this.nextAssetId = nextAssetId;
	}

	public List<RawMaterialsMarket> getRawMaterialsMarkets() {
		return rawMaterialsMarkets;
	}

	public void setRawMaterialsMarkets(List<RawMaterialsMarket> rawMaterialsMarkets) {
		this.rawMaterialsMarkets = rawMaterialsMarkets;
	}

	public List<StockExchange> getStockExchanges() {
		return stockExchanges;
	}

	public void setStockExchanges(List<StockExchange> stockExchanges) {
		this.stockExchanges = stockExchanges;
	}

	public List<CurrenciesMarket> getCurrenciesMarkets() {
		return currenciesMarkets;
	}

	public void setCurrenciesMarkets(List<CurrenciesMarket> currenciesMarkets) {
		this.currenciesMarkets = currenciesMarkets;
	}

	public List<Company> getCompanies() {
		return companies;
	}

	public void setCompanies(List<Company> companies) {
		this.companies = companies;
	}

	public List<Currency> getCurrencies() {
		return currencies;
	}

	public void setCurrencies(List<Currency> currencies) {
		this.currencies = currencies;
	}

	public List<RawMaterial> getRawMaterials() {
		return rawMaterials;
	}

	public void setRawMaterials(List<RawMaterial> rawMaterials) {
		this.rawMaterials = rawMaterials;
	}

	public List<Investor> getInvestors() {
		return investors;
	}

	public void setInvestors(List<Investor> investors) {
		this.investors = investors;
	}

	public List<InvestmentFund> getInvestmentFunds() {
		return investmentFunds;
	}

	public void setInvestmentFunds(List<InvestmentFund> investmentFunds) {
		this.investmentFunds = investmentFunds;
	}

	public int getNumberOfAssets() {
		return numberOfAssets;
	}

	public void setNumberOfAssets(int numberOfAssets) {
		this.numberOfAssets = numberOfAssets;
	}

}
