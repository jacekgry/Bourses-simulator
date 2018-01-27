package model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import exceptions.NotEnoughMoneyException;

public class Company extends Asset implements Runnable, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5948545096893458719L;
	private LocalDateTime firstValuationDate;
	private BigDecimal openingPrice;
	private Integer numberOfShares;
	private BigDecimal revenue;
	private BigDecimal profit;
	private BigDecimal volume;
	private BigDecimal turnover;
	private BigDecimal equityCapital; // wlasny
	private BigDecimal shareCapital; // zakladowy
	private boolean running = true;
	private BigDecimal profitToBePaidToShareholders = new BigDecimal(0);
	private int timeLeftToPayShareholders = 10;
	private Map<Gambler, BigDecimal> shareholders = new HashMap<>();
	private StockExchange stockExchange;
	private Integer numberOfSharesNotYetSold;
	private BigDecimal paid;

	public Company() {
		super();
		setShareCapital(new BigDecimal(random.nextInt(200000) + 100000));
		setRevenue(BigDecimal.ZERO);
		setProfit(BigDecimal.ZERO);
		setEquityCapital(shareCapital.add(new BigDecimal(random.nextInt(50000) + 20000)));
		setNumberOfShares(random.nextInt(100000) + 100000);
		setNumberOfSharesNotYetSold(getNumberOfShares());
	}

	public Map<Gambler, BigDecimal> getShareholders() {
		return shareholders;
	}

	public Integer getNumberOfSharesNotYetSold() {
		return numberOfSharesNotYetSold;
	}

	public void setNumberOfSharesNotYetSold(Integer numberOfSharesNotYetSold) {
		this.numberOfSharesNotYetSold = numberOfSharesNotYetSold;
	}

	public void setRunning(boolean running) {
		this.running = running;
	}

	@Override
	public void run() {

		try {
			Thread.sleep(random.nextInt(1000));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		while (running) {

			while (stockExchange == null) {
				try {
					Thread.sleep(200);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				if (World.getStockExchanges().size() > 0) {
					StockExchange stockExchange = World.getStockExchanges()
							.get(random.nextInt(World.getStockExchanges().size()));
					this.stockExchange = stockExchange;
					debutOnStockMarket(stockExchange);
				}
			}

			BigDecimal generatedRevenue = generateRevenue();
			BigDecimal generatedCost = generateCost();
			generateProfit(generatedRevenue, generatedCost);
			if (random.nextDouble() < 0.1) {
				generateNewShares();
			}

			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}
	}

	public void delete() {
		super.delete();
		this.running = false;
		World.getStockExchanges().forEach(stockExchange -> {
			if (stockExchange.getCompanies().contains(this))
				stockExchange.getCompanies().remove(this);
		});
		World.getCompanies().remove(this);
	}

	public void repurchaseShares(BigDecimal price) throws NotEnoughMoneyException {
		BigDecimal toBePaid = price.multiply(new BigDecimal(this.numberOfShares - this.numberOfSharesNotYetSold));
		this.numberOfSharesNotYetSold = this.numberOfShares;
		if (toBePaid.compareTo(this.equityCapital) == 1)
			throw new NotEnoughMoneyException();
		this.setEquityCapital(this.getEquityCapital().subtract(toBePaid));
		this.shareholders.entrySet().forEach(shareholder -> {
			synchronized (shareholder) {
				BigDecimal budget = shareholder.getKey().getBudget();
				shareholder.getKey().setBudget(budget.add(shareholder.getValue().multiply(price)));
				shareholder.getKey().getAssets().removeIf(assetAmount -> {
					return assetAmount.getAsset().equals(this);
				});
			}
		});
	}

	public void offerShares(int numberOfOfferedShares) {
		synchronized (stockExchange) {
			this.stockExchange.getNewSharesGeneratedByCompany().put(this, new BigDecimal(numberOfSharesNotYetSold));
		}

	}

	public void debutOnStockMarket(StockExchange stockExchange) {
		synchronized (this) {
			setFirstValuationDate(World.getDate());
			setTurnover(BigDecimal.ZERO);
			setVolume(BigDecimal.ZERO);
			setOpeningPrice(shareCapital.divide(new BigDecimal(numberOfShares), 2, RoundingMode.HALF_UP));
			setCurrentPrice(openingPrice);
			setMinimumPrice(openingPrice);
			setMaximumPrice(openingPrice);
		}
		getHistory().clear();
		getHistory().put(firstValuationDate.withMinute(0).withSecond(0).withNano(0), getCurrentPrice());
		stockExchange.getCompanies().add(this);
		offerShares(numberOfShares);
	}

	private BigDecimal generateRevenue() {
		BigDecimal generatedRevenue = new BigDecimal(random.nextInt(10000));
		this.setRevenue(this.getRevenue().add(generatedRevenue));
		return generatedRevenue;
	}

	private BigDecimal generateCost() {
		BigDecimal generatedCost = new BigDecimal(random.nextInt(5000));
		return generatedCost;
	}

	private void generateProfit(BigDecimal generatedRevenue, BigDecimal generatedCost) {
		synchronized (this) {
			BigDecimal generatedProfit = generatedRevenue.subtract(generatedCost);

			if (generatedProfit.compareTo(new BigDecimal(0)) == 1) {
				this.setCurrentPrice(this.getCurrentPrice().multiply(new BigDecimal(1.05)));
			} else if (generatedProfit.compareTo(new BigDecimal(0)) == -1) {
				this.setCurrentPrice(this.getCurrentPrice().multiply(new BigDecimal(0.95)));
			}

			this.setProfit(this.getProfit().add(generatedProfit));
			setProfitToBePaidToShareholders(getProfitToBePaidToShareholders().add(generatedProfit));
		}
		timeLeftToPayShareholders--;
		if (timeLeftToPayShareholders == 0) {
			timeLeftToPayShareholders = 10;
			payShareholders();
		}

	}

	private void payShareholders() {
		paid = BigDecimal.ZERO;
		if (profitToBePaidToShareholders.compareTo(BigDecimal.ZERO) > 0) {
			shareholders.entrySet().forEach(shareholder -> {
				BigDecimal toPay = shareholder.getValue().divide(new BigDecimal(this.numberOfShares), 5)
						.multiply(profitToBePaidToShareholders);
				synchronized (shareholder) {
					shareholder.getKey().setBudget(shareholder.getKey().getBudget().add(toPay));
				}
				paid = paid.add(toPay);
			});
		}
		setEquityCapital(getEquityCapital().add(profitToBePaidToShareholders.subtract(paid)));
		setProfitToBePaidToShareholders(BigDecimal.ZERO);

	}

	public void generateNewShares() {
		int numOfNewShares = random.nextInt(10000);
		synchronized (this) {
			this.numberOfShares += numOfNewShares;
			this.numberOfSharesNotYetSold += numOfNewShares;
			this.equityCapital = this.equityCapital.add(openingPrice.multiply(new BigDecimal(numOfNewShares)));
		}
		synchronized (stockExchange) {
			this.stockExchange.getNewSharesGeneratedByCompany().put(this, new BigDecimal(numberOfSharesNotYetSold));
		}
	}

	public synchronized void updateAfterTransaction(BigDecimal numOfShares) {
		setNumberOfSharesNotYetSold(getNumberOfSharesNotYetSold() - numOfShares.intValue());
		setProfit(getProfit().add(numOfShares.multiply(getCurrentPrice())));
		setVolume(getVolume().add(numOfShares));
		setTurnover(getTurnover().add(numOfShares.multiply(getCurrentPrice())));
		setProfitToBePaidToShareholders(getProfitToBePaidToShareholders().add(numOfShares.multiply(getCurrentPrice())));
	}

	public BigDecimal getEquityCapital() {
		return equityCapital;
	}

	public void setEquityCapital(BigDecimal equityCapital) {
		this.equityCapital = equityCapital;
	}

	public BigDecimal getShareCapital() {
		return shareCapital;
	}

	public void setShareCapital(BigDecimal shareCapital) {
		this.shareCapital = shareCapital;
	}

	public LocalDateTime getFirstValuationDate() {
		return firstValuationDate;
	}

	public void setFirstValuationDate(LocalDateTime firstValuationDate) {
		this.firstValuationDate = firstValuationDate;
	}

	public BigDecimal getOpeningPrice() {
		return openingPrice;
	}

	public void setOpeningPrice(BigDecimal openingPrice) {
		this.openingPrice = openingPrice;
	}

	public Integer getNumberOfShares() {
		return numberOfShares;
	}

	public void setNumberOfShares(int numberOfShares) {
		this.numberOfShares = numberOfShares;
	}

	public BigDecimal getRevenue() {
		return revenue;
	}

	public void setRevenue(BigDecimal revenue) {
		this.revenue = revenue;
	}

	public BigDecimal getProfit() {
		return profit;
	}

	public void setProfit(BigDecimal profit) {
		this.profit = profit;
	}

	public BigDecimal getVolume() {
		return volume.setScale(0, RoundingMode.HALF_DOWN);
	}

	public void setVolume(BigDecimal volume) {
		this.volume = volume;
	}

	public BigDecimal getTurnover() {
		return turnover;
	}

	public void setTurnover(BigDecimal turnover) {
		this.turnover = turnover;
	}

	public StockExchange getStockExchange() {
		return stockExchange;
	}

	public void setStockExchange(StockExchange stockExchange) {
		this.stockExchange = stockExchange;
	}

	public BigDecimal getProfitToBePaidToShareholders() {
		return profitToBePaidToShareholders;
	}

	public void setProfitToBePaidToShareholders(BigDecimal profitToBePaidToShareholders) {
		this.profitToBePaidToShareholders = profitToBePaidToShareholders;
	}

}
