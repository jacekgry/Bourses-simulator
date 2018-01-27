package model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Random;
import java.util.Map.Entry;

public abstract class Market implements Serializable, Runnable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5314836459741358986L;
	private String name;
	private String country;
	private String city;
	private String address;
	private BigDecimal profitMargin;
	private volatile boolean running = true;
	protected Random random = new Random();
	private LinkedHashMap<Gambler, AssetAmount> offersOfBuy = new LinkedHashMap<>();
	private LinkedHashMap<Gambler, AssetAmount> offersOfSell = new LinkedHashMap<>();

	public LinkedHashMap<Gambler, AssetAmount> getOffersOfBuy() {
		return offersOfBuy;
	}

	public LinkedHashMap<Gambler, AssetAmount> getOffersOfSell() {
		return offersOfSell;
	}

	public boolean isRunning() {
		return running;
	}

	public void setRunning(boolean running) {
		this.running = running;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String adress) {
		this.address = adress;
	}

	public BigDecimal getProfitMargin() {
		return profitMargin;
	}

	public void setProfitMargin(BigDecimal profitMargin) {
		this.profitMargin = profitMargin;
	}

	public <T extends Asset> void recalculatePrice(T asset) {

		synchronized (asset) {
			if (asset.getDemand().compareTo(asset.getSupply()) > 0) {
				if (asset.getDemand().divide(asset.getSupply(), 3).compareTo(new BigDecimal(100)) > 0) {
					asset.setCurrentPrice(asset.getCurrentPrice().multiply(new BigDecimal(2)));
				} else {
					asset.setCurrentPrice(asset.getCurrentPrice().multiply(BigDecimal.ONE
							.add(new BigDecimal(0.01).multiply((asset.getDemand().divide(asset.getSupply(), 3))))));
				}
			} else if (asset.getDemand().compareTo(asset.getSupply()) < 0) {

				if (asset.getSupply().divide(asset.getDemand(), 3).compareTo(new BigDecimal(50)) > 0) {
					asset.setCurrentPrice(asset.getCurrentPrice().multiply(new BigDecimal(0.5)));
				} else {
					asset.setCurrentPrice(asset.getCurrentPrice().multiply(BigDecimal.ONE.subtract(
							new BigDecimal(0.01).multiply((asset.getSupply().divide(asset.getDemand(), 3))))));
				}

				if (asset.getCurrentPrice().compareTo(new BigDecimal(0.01)) <= 0)
					asset.setCurrentPrice(new BigDecimal(0.01));

			}

			synchronized (asset) {
				if (asset.getCurrentPrice().compareTo(asset.getMaximumPrice()) > 0)
					asset.setMaximumPrice(asset.getCurrentPrice());
				if (asset.getCurrentPrice().compareTo(asset.getMinimumPrice()) < 0)
					asset.setMinimumPrice(asset.getCurrentPrice());
			}

			LocalDateTime date = World.getDate();

			asset.getHistory().put(date.withMinute(0).withSecond(0).withNano(0), asset.getCurrentPrice());

			asset.setDemand(BigDecimal.ONE);
			asset.setSupply(BigDecimal.ONE);
		}
	}

	protected void realizeDeals() {


		synchronized (this) {
			for (Entry<Gambler, AssetAmount> offer : getOffersOfBuy().entrySet()) {
				Gambler buyer = offer.getKey();
				synchronized (buyer) {
					Asset asset = offer.getValue().getAsset();
					synchronized (asset) {

						BigDecimal numberOfRequestedUnits = offer.getValue().getAmount().divide(asset.getCurrentPrice(),
								2);
						BigDecimal sellPrice = asset.getCurrentPrice()
								.divide(BigDecimal.ONE.add(getProfitMargin().divide(new BigDecimal(100), 2)), 2);
						Iterator<Entry<Gambler, AssetAmount>> it = getOffersOfSell().entrySet().iterator();
						while (it.hasNext()) {
							Entry<Gambler, AssetAmount> offerOfSell = it.next();
							if (!offerOfSell.getKey().equals(buyer)
									&& offerOfSell.getValue().getAsset().equals(asset)) {
								Gambler seller = offerOfSell.getKey();
								if (offerOfSell.getValue().getAmount().compareTo(numberOfRequestedUnits) <= 0) {
									BigDecimal numOfUnits = offerOfSell.getValue().getAmount();
									buyer.addAsset(asset, numOfUnits);


									synchronized (seller) {
										seller.setBudget(seller.getBudget().add(numOfUnits.multiply(sellPrice)));
									}
									offer.getValue().setAmount(offer.getValue().getAmount()
											.subtract(numberOfRequestedUnits.multiply(asset.getCurrentPrice())));
									numberOfRequestedUnits = numberOfRequestedUnits.subtract(numOfUnits);
									it.remove();
								}

								else {

									synchronized (seller) {
										seller.setBudget(
												seller.getBudget().add(numberOfRequestedUnits.multiply(sellPrice)));
									}
									offer.getValue().setAmount(offer.getValue().getAmount()
											.subtract(numberOfRequestedUnits.multiply(asset.getCurrentPrice())));

									offerOfSell.getValue().setAmount(
											offerOfSell.getValue().getAmount().subtract(numberOfRequestedUnits));

									numberOfRequestedUnits = BigDecimal.ZERO;

								}

							}

						}
					}

					buyer.setBudget(buyer.getBudget().add(offer.getValue().getAmount()));
				}

			}
			for (Entry<Gambler, AssetAmount> offerOfSell : getOffersOfSell().entrySet()) {
				offerOfSell.getKey().addAsset(offerOfSell.getValue().getAsset(), offerOfSell.getValue().getAmount());
			}

			this.getOffersOfBuy().clear();
			this.getOffersOfSell().clear();
		}

	}

}
