package model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Currency extends Asset  {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3619272307014907460L;

	/**
	 * 
	 */

	private List<String> countries = new ArrayList<>();

	public Currency() {

		super();
		this.setCurrentPrice(new BigDecimal(Double.toString(random.nextDouble() * 5)));
		this.setMaximumPrice(getCurrentPrice());
		this.setMinimumPrice(getCurrentPrice());
		setOpeningPrice(getCurrentPrice());
		getHistory().put(World.getDate().withMinute(0).withSecond(0).withNano(0), getCurrentPrice());
	}

	public Currency(int price) {

		super();
		this.setCurrentPrice(new BigDecimal(price));
		this.setMaximumPrice(getCurrentPrice());
		this.setMinimumPrice(getCurrentPrice());
		setOpeningPrice(getCurrentPrice());
		getHistory().put(World.getDate().withMinute(0).withSecond(0).withNano(0), getCurrentPrice());
	}
	
	public List<String> getCountries() {
		return countries;
	}

	public void setCountries(List<String> countries) {
		this.countries = countries;
	}

	@Override
	public String toString() {
		return getName();
	}
	
	@Override
	public void delete() {
		super.delete();
		World.getCurrencies().remove(this);
		World.getCurrenciesMarkets().forEach(cm -> {
			if (cm.getCurrencies().contains(this)) {
				cm.getCurrencies().remove(this);
			}
		});

	}

}
