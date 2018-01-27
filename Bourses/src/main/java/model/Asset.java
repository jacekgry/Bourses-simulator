package model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Random;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

public abstract class Asset implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4669047138544222214L;
	/**
	 * 
	 */
	private String name;
	private transient BooleanProperty add;
	private int id;
	protected Random random = new Random();
	private BigDecimal demand = BigDecimal.ONE;
	private BigDecimal supply = BigDecimal.ONE;
	private LinkedHashMap<LocalDateTime, BigDecimal> history = new LinkedHashMap<>();
	private BigDecimal currentPrice;
	private BigDecimal maximumPrice;
	private BigDecimal minimumPrice;
	private BigDecimal openingPrice;

	public Asset() {
		setId(World.getNextAssetId());
		World.incrementAssetId();
		World.incrementNumberOfAssets();
	}

	public synchronized BigDecimal getCurrentPrice() {
		return currentPrice.setScale(3, RoundingMode.HALF_UP);
	}

	public void setCurrentPrice(BigDecimal currentPrice) {
		this.currentPrice = currentPrice;
	}

	public BigDecimal getMaximumPrice() {
		return maximumPrice.setScale(3, RoundingMode.HALF_UP);
	}

	public void setMaximumPrice(BigDecimal maximumPrice) {
		this.maximumPrice = maximumPrice.setScale(3, RoundingMode.HALF_UP);
	}

	public BigDecimal getMinimumPrice() {
		return minimumPrice;
	}

	public void setMinimumPrice(BigDecimal minimumPrice) {
		this.minimumPrice = minimumPrice;
	}

	public LinkedHashMap<LocalDateTime, BigDecimal> getHistory() {
		return history;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public synchronized BigDecimal getDemand() {
		return demand;
	}

	public synchronized void setDemand(BigDecimal demand) {
		this.demand = demand;
	}

	public synchronized BigDecimal getSupply() {
		return supply;
	}

	public synchronized void setSupply(BigDecimal supply) {
		this.supply = supply;
	}

	public void setAddProperty(boolean bool) {
		this.add = new SimpleBooleanProperty(bool);
	}

	public BooleanProperty addProperty() {
		return add;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public BigDecimal getOpeningPrice() {
		return openingPrice;
	}
	

	public void setOpeningPrice(BigDecimal openingPrice) {
		this.openingPrice = openingPrice;
	}

	public void delete() {
		World.getInvestmentFunds().forEach(investmentFund -> {
			investmentFund.getAssets().removeIf(asset -> {
				return asset.getAsset().equals(this);
			});
		});
		World.getInvestors().forEach(investor -> {
			investor.getAssets().removeIf(asset -> {
				return asset.getAsset().equals(this);
			});
		});
	}

	public void giveItToGamblers(int max) {
		for (Investor investor : World.getInvestors()) {
			if (random.nextDouble() < 0.3)
				investor.addAsset(this, new BigDecimal(random.nextInt(max)));
		}
		for (InvestmentFund investmentFund : World.getInvestmentFunds()) {
			if (random.nextDouble() < 0.3)
				investmentFund.addAsset(this, new BigDecimal(random.nextInt(max)));
		}
	}

}
