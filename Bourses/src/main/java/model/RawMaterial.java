package model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Iterator;

public class RawMaterial extends Asset {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1779385234602874298L;

	private String unit;
	private Currency currency;

	private int id;

	public BigDecimal getCurrentPriceInCurrency() {
		return getCurrentPrice().multiply(currency.getCurrentPrice()).setScale(3, RoundingMode.HALF_UP);
	}

	public BigDecimal getMaximumPriceInCurrency() {
		return getMaximumPrice().multiply(currency.getCurrentPrice()).setScale(3, RoundingMode.HALF_UP);
	}

	public BigDecimal getMinimumPriceInCurrency() {
		return getMinimumPrice().multiply(currency.getCurrentPrice()).setScale(3, RoundingMode.HALF_UP);
	}

	public RawMaterial() {

		super();
		setCurrentPrice(new BigDecimal(random.nextInt(100) + 1));
		setMaximumPrice(getCurrentPrice());
		setMinimumPrice(getCurrentPrice());
		setOpeningPrice(getCurrentPrice());
		getHistory().put(World.getDate().withMinute(0).withSecond(0).withNano(0), getCurrentPrice());

	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public Currency getCurrency() {
		if (!World.getCurrencies().contains(currency))
			return World.getCurrencies().get(0);
		return currency;
	}

	public void setCurrency(Currency currency) {
		this.currency = currency;
	}

	@Override
	public void delete() {
		super.delete();
		World.getRawMaterials().remove(this);
		World.getRawMaterialsMarkets().forEach(rmm -> {
			if (rmm.getRawMaterials().contains(this)) {
				rmm.getRawMaterials().remove(this);
			}
		});

	}

}
