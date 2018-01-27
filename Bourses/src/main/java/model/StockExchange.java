package model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class StockExchange extends Market {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2624901771478044992L;
	private Currency currency;
	private List<Company> companies = new ArrayList<>();
	private List<MarketIndex> marketIndexes = new ArrayList<>();
	private Map<Company, BigDecimal> newSharesGeneratedByCompany = new LinkedHashMap<>();

	public Currency getCurrency() {
		return currency;
	}

	public void setCurrency(Currency currency) {
		this.currency = currency;
	}

	public List<Company> getCompanies() {
		return companies;
	}

	public void setCompanies(List<Company> stocks) {
		this.companies = stocks;
	}

	public List<MarketIndex> getMarketIndexes() {
		return marketIndexes;
	}

	public void setMarketIndexes(List<MarketIndex> marketIndexes) {
		this.marketIndexes = marketIndexes;
	}

	public void addMarketIndex(MarketIndex marketIndex) {
		marketIndexes.add(marketIndex);
	}

	public void deleteMarketIndex(MarketIndex marketIndex) {
		marketIndexes.remove(marketIndex);
	}

	public void delete() {
		World.getStockExchanges().remove(this);
		World.getCompanies().stream().forEach(company -> {
			if (company.getStockExchange().equals(this)) {
				company.setStockExchange(null);
			}
		});
		this.setRunning(false);
	}

	@Override
	protected void realizeDeals() {

		synchronized (this) {

			for (Entry<Gambler, AssetAmount> offer : getOffersOfBuy().entrySet()) {
				Gambler buyer = offer.getKey();
				synchronized (buyer) {
					Asset asset = offer.getValue().getAsset();
					synchronized (asset) {

						BigDecimal numberOfRequestedShares = offer.getValue().getAmount()
								.divide(asset.getCurrentPrice(), 3).setScale(0, RoundingMode.HALF_DOWN);
						BigDecimal sellPrice = asset.getCurrentPrice()
								.divide(BigDecimal.ONE.add(getProfitMargin().divide(new BigDecimal(100), 3)), 3);

						Iterator<Entry<Gambler, AssetAmount>> it = getOffersOfSell().entrySet().iterator();

						while (it.hasNext()) {
							Entry<Gambler, AssetAmount> offerOfSell = it.next();
							if (!offerOfSell.getKey().equals(buyer)
									&& offerOfSell.getValue().getAsset().equals(asset)) {
								Gambler seller = offerOfSell.getKey();
								if (offerOfSell.getValue().getAmount().compareTo(numberOfRequestedShares) <= 0) {
									BigDecimal numOfShares = offerOfSell.getValue().getAmount();
									buyer.addAsset(asset, numOfShares);


									synchronized (seller) {
										seller.setBudget(seller.getBudget().add(numOfShares.multiply(sellPrice)));
									}
									offer.getValue().setAmount(offer.getValue().getAmount()
											.subtract(numberOfRequestedShares.multiply(asset.getCurrentPrice())));
									numberOfRequestedShares = numberOfRequestedShares.subtract(numOfShares);
									it.remove();
								}

								else {
									buyer.addAsset(asset, numberOfRequestedShares);


									synchronized (seller) {
										seller.setBudget(
												seller.getBudget().add(numberOfRequestedShares.multiply(sellPrice)));
									}

									offer.getValue().setAmount(offer.getValue().getAmount()
											.subtract(numberOfRequestedShares.multiply(asset.getCurrentPrice())));

									offerOfSell.getValue().setAmount(
											offerOfSell.getValue().getAmount().subtract(numberOfRequestedShares));

									numberOfRequestedShares = BigDecimal.ZERO;

								}

							}

						}

						Iterator<Entry<Company, BigDecimal>> it2 = getNewSharesGeneratedByCompany().entrySet()
								.iterator();
						while (it2.hasNext()) {
							Entry<Company, BigDecimal> offerFromCompany = it2.next();

							Company company = offerFromCompany.getKey();
							BigDecimal numOfSharesOfferedByCompany = offerFromCompany.getValue();

							if (offer.getValue().getAsset().equals(company)) {
								if (numOfSharesOfferedByCompany.compareTo(numberOfRequestedShares) <= 0) {
									buyer.addAsset(company, numOfSharesOfferedByCompany);

									company.updateAfterTransaction(numOfSharesOfferedByCompany);

									offer.getValue().setAmount(offer.getValue().getAmount()
											.subtract(numberOfRequestedShares.multiply(asset.getCurrentPrice())));
									numberOfRequestedShares = numberOfRequestedShares
											.subtract(numOfSharesOfferedByCompany);
									it2.remove();
								}

								else {
									buyer.addAsset(asset, numberOfRequestedShares);

									company.updateAfterTransaction(numberOfRequestedShares);

									offer.getValue().setAmount(offer.getValue().getAmount()
											.subtract(numberOfRequestedShares.multiply(asset.getCurrentPrice())));
									offerFromCompany
											.setValue((numOfSharesOfferedByCompany.subtract(numberOfRequestedShares)));
									numberOfRequestedShares = BigDecimal.ZERO;

								}
							}
						}
						buyer.setBudget(buyer.getBudget().add(offer.getValue().getAmount()));
					}
				}
			}

			Iterator<Entry<Gambler, AssetAmount>> it = getOffersOfSell().entrySet().iterator();

			while (it.hasNext()) {
				Entry<Gambler, AssetAmount> offerOfSell = it.next();
				Gambler gambler = offerOfSell.getKey();
				gambler.addAsset(offerOfSell.getValue().getAsset(), offerOfSell.getValue().getAmount());
			}

			this.getOffersOfBuy().clear();
			this.getOffersOfSell().clear();

		}

	}

	@Override
	public void run() {

		try {
			Thread.sleep(random.nextInt(1000));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		while (isRunning()) {

			try {
				Thread.sleep(6000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			realizeDeals();
			recalculatePrices();
		}

	}

	private void recalculatePrices() {
		for (Company company : this.getCompanies()) {
			recalculatePrice(company);
		}
	}

	public Map<Company, BigDecimal> getNewSharesGeneratedByCompany() {
		return newSharesGeneratedByCompany;
	}

	public BigDecimal getCurrentPriceInCurrency(Company company) {
		return company.getCurrentPrice().multiply(currency.getCurrentPrice()).setScale(3, RoundingMode.HALF_UP);
	}

	public BigDecimal getMaximumPriceInCurrency(Company company) {
		return company.getMaximumPrice().multiply(currency.getCurrentPrice()).setScale(3, RoundingMode.HALF_UP);
	}

	public BigDecimal getMinimumPriceInCurrency(Company company) {
		return company.getMinimumPrice().multiply(currency.getCurrentPrice()).setScale(3, RoundingMode.HALF_UP);
	}

	public BigDecimal getOpeningPriceInCurrency(Company company) {
		return company.getOpeningPrice().multiply(currency.getCurrentPrice()).setScale(3, RoundingMode.HALF_UP);
	}

	public BigDecimal getEquityCapitalInCurrency(Company company) {
		return company.getEquityCapital().multiply(currency.getCurrentPrice()).setScale(3, RoundingMode.HALF_UP);
	}

	public BigDecimal getShareCapitalInCurrency(Company company) {
		return company.getShareCapital().multiply(currency.getCurrentPrice()).setScale(3, RoundingMode.HALF_UP);
	}

	public BigDecimal getVolumeInCurrency(Company company) {
		return company.getVolume().multiply(currency.getCurrentPrice()).setScale(3, RoundingMode.HALF_UP);
	}

	public BigDecimal getTurnoverCapitalInCurrency(Company company) {
		return company.getTurnover().multiply(currency.getCurrentPrice()).setScale(3, RoundingMode.HALF_UP);
	}

	public BigDecimal getProfitInCurrency(Company company) {
		return company.getProfit().multiply(currency.getCurrentPrice()).setScale(3, RoundingMode.HALF_UP);
	}

	public BigDecimal getRevenueInCurrency(Company company) {
		return company.getRevenue().multiply(currency.getCurrentPrice()).setScale(3, RoundingMode.HALF_UP);
	}

	public BigDecimal getTurnoverInCurrency(Company company) {
		return company.getTurnover().multiply(currency.getCurrentPrice()).setScale(3, RoundingMode.HALF_UP);

	}
}
