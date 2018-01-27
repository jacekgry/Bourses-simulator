package model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class Gambler implements Serializable, Runnable {

	private static final long serialVersionUID = -8476233808673983631L;
	protected Random random = new Random();
	private BigDecimal budget;
	private List<AssetAmount> assets = new ArrayList<>();
	protected boolean running = true;

	public String getName() {
		if (this instanceof Investor) {
			Investor inv = (Investor) this;
			return inv.getFirstName();
		} else {
			InvestmentFund inv = (InvestmentFund) this;
			return inv.getName();
		}

	}

	public Gambler() {
		for (Asset asset : World.getRawMaterials()) {
			if (random.nextDouble() < 0.3)
				addAsset(asset, new BigDecimal(random.nextInt(1000)));
		}
		for (Asset asset : World.getCurrencies()) {
			if (random.nextDouble() < 0.3)
				addAsset(asset, new BigDecimal(random.nextInt(1000)));
		}
	}

	public boolean isRunning() {
		return running;
	}

	public void setRunning(boolean running) {
		this.running = running;
	}

	public synchronized BigDecimal getBudget() {
		return budget;
	}

	public synchronized void setBudget(BigDecimal budget) {
		this.budget = budget;
	}

	public List<AssetAmount> getAssets() {
		return assets;
	}

	public void setAssets(List<AssetAmount> assets) {
		this.assets = assets;
	}

	public void sellAssetOnTheMarket() {
		if (this.getAssets().isEmpty())
			return;

		AssetAmount assetAmount = this.getAssets().get(random.nextInt(this.getAssets().size()));

		BigDecimal toBeSold = assetAmount.getAmount().multiply(new BigDecimal(random.nextDouble())).setScale(3,
				RoundingMode.HALF_DOWN);
		if (assetAmount.getAsset() instanceof Company) {
			toBeSold = toBeSold.setScale(0, RoundingMode.HALF_DOWN);
			Company company = (Company) assetAmount.getAsset();
			StockExchange stockExchange = company.getStockExchange();
			AssetAmount offer = new AssetAmount(assetAmount.getAsset(), toBeSold);
			synchronized (stockExchange) {
				if (!stockExchange.getOffersOfSell().containsKey(this)) {
					stockExchange.getOffersOfSell().put(this, offer);
				}
			}
			company.setSupply(company.getSupply().add(toBeSold));
			this.removeAsset(company, toBeSold);

		} else if (assetAmount.getAsset() instanceof RawMaterial) {
			RawMaterial rawMaterial = (RawMaterial) assetAmount.getAsset();
			List<RawMaterialsMarket> markets = World.getRawMaterialsMarkets().stream().filter(rmm -> {
				return rmm.getRawMaterials().contains(rawMaterial);
			}).collect(Collectors.toList());
			if (markets.isEmpty())
				return;
			RawMaterialsMarket rawMaterialsMarket = markets.get(random.nextInt(markets.size()));
			AssetAmount offer = new AssetAmount(rawMaterial, toBeSold);
			synchronized (rawMaterialsMarket) {
				if (!rawMaterialsMarket.getOffersOfSell().containsKey(this)) {
					rawMaterialsMarket.getOffersOfSell().put(this, offer);
				}
			}
			rawMaterial.setSupply(rawMaterial.getSupply().add(toBeSold));

			removeAsset(rawMaterial, toBeSold);

		}

		else if (assetAmount.getAsset() instanceof Currency) {

			Currency currency = (Currency) assetAmount.getAsset();
			List<CurrenciesMarket> markets = World.getCurrenciesMarkets().stream().filter(cm -> {
				return cm.getCurrencies().contains(currency);
			}).collect(Collectors.toList());
			if (markets.isEmpty())
				return;
			CurrenciesMarket currenciesMarket = markets.get(random.nextInt(markets.size()));
			AssetAmount offer = new AssetAmount(currency, toBeSold);
			synchronized (currenciesMarket) {
				if (!currenciesMarket.getOffersOfSell().containsKey(this)) {
					currenciesMarket.getOffersOfSell().put(this, offer);
				}
			}
			currency.setSupply(currency.getSupply().add(toBeSold));
			removeAsset(currency, toBeSold);

		}

	}

	public void buyRawMaterials() {
		if (World.getRawMaterialsMarkets().isEmpty())
			return;

		RawMaterialsMarket rawMaterialsMarket = World.getRawMaterialsMarkets()
				.get(random.nextInt(World.getRawMaterialsMarkets().size()));

		if (rawMaterialsMarket.getRawMaterials().isEmpty())
			return;

		RawMaterial rawMaterial = rawMaterialsMarket.getRawMaterials()
				.get(random.nextInt(rawMaterialsMarket.getRawMaterials().size()));
		BigDecimal moneyToBeSpent;

		moneyToBeSpent = new BigDecimal(0.2 * random.nextDouble() * this.getBudget().doubleValue());
		BigDecimal currentPrice = rawMaterial.getCurrentPrice();

		BigDecimal unitsToBeBought = moneyToBeSpent.divide(currentPrice, 3, RoundingMode.HALF_DOWN);
		AssetAmount offer = new AssetAmount(rawMaterial, moneyToBeSpent);

		rawMaterial.setDemand(rawMaterial.getDemand().add(unitsToBeBought));

		this.setBudget(this.getBudget().subtract(moneyToBeSpent));

		synchronized (rawMaterialsMarket) {
			if (!rawMaterialsMarket.getOffersOfBuy().containsKey(this)) {
				rawMaterialsMarket.getOffersOfBuy().put(this, offer);

			}
		}

	}

	public void buySharesOnTheStockExchange() {
		if (World.getStockExchanges().isEmpty())
			return;

		StockExchange stockExchange = World.getStockExchanges().get(random.nextInt(World.getStockExchanges().size()));

		if (stockExchange.getCompanies().isEmpty())
			return;

		Company company = stockExchange.getCompanies().get(random.nextInt(stockExchange.getCompanies().size()));
		BigDecimal moneyToBeSpent = new BigDecimal(0.2 * random.nextDouble() * this.budget.doubleValue());

		BigDecimal currentPrice = company.getCurrentPrice();

		BigDecimal sharesToBeBought = moneyToBeSpent.divide(currentPrice, 0, RoundingMode.HALF_DOWN);
		AssetAmount offer = new AssetAmount(company, moneyToBeSpent);

		company.setDemand(company.getDemand().add(sharesToBeBought));

		this.setBudget(this.getBudget().subtract(moneyToBeSpent));

		synchronized (stockExchange) {
			if (!stockExchange.getOffersOfBuy().containsKey(this)) {
				stockExchange.getOffersOfBuy().put(this, offer);
			}
		}

	}

	public void buyCurrency() {
		if (World.getCurrenciesMarkets().isEmpty())
			return;

		CurrenciesMarket currenciesMarket = World.getCurrenciesMarkets()
				.get(random.nextInt(World.getCurrenciesMarkets().size()));

		if (currenciesMarket.getCurrencies().isEmpty())
			return;

		Currency currency = currenciesMarket.getCurrencies()
				.get(random.nextInt(currenciesMarket.getCurrencies().size()));
		BigDecimal moneyToBeSpent;

		moneyToBeSpent = new BigDecimal(0.2 * random.nextDouble() * this.budget.doubleValue());

		BigDecimal currentPrice = currency.getCurrentPrice();

		BigDecimal toBeBought = moneyToBeSpent.divide(currentPrice, 2, RoundingMode.HALF_DOWN);
		AssetAmount offer = new AssetAmount(currency, moneyToBeSpent);

		currency.setDemand(currency.getDemand().add(toBeBought));

		this.setBudget(this.getBudget().subtract(moneyToBeSpent));

		synchronized (currenciesMarket) {
			if (!currenciesMarket.getOffersOfBuy().containsKey(this)) {
				currenciesMarket.getOffersOfBuy().put(this, offer);
			}
		}

	}

	public void delete() {
		this.setRunning(false);
	}

	public synchronized void addAsset(Asset asset, BigDecimal amount) {
		for (AssetAmount assetAmount : this.getAssets()) {
			if (assetAmount.getAsset().equals(asset)) {
				assetAmount.setAmount(assetAmount.getAmount().add(amount));
				return;
			}
		}
		AssetAmount assetAmount = new AssetAmount(asset, amount);
		this.getAssets().add(assetAmount);
		if (assetAmount.getAsset() instanceof Company) {
			Company company = (Company) assetAmount.getAsset();
			company.getShareholders().put(this, assetAmount.getAmount());
		}
	}

	public synchronized void removeAsset(Asset asset, BigDecimal amount) {
		for (AssetAmount assetAmount : this.getAssets()) {
			if (assetAmount.getAsset().equals(asset)) {
				if (amount.compareTo(assetAmount.getAmount()) == 0) {
					this.getAssets().remove(assetAmount);
					return;
				} else {
					assetAmount.setAmount(assetAmount.getAmount().subtract(amount));
					return;
				}
			}
		}
	}

	@Override
	public void run() {

	}

}