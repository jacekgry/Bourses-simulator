package model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public class CurrenciesMarket extends Market {

	private static final long serialVersionUID = -2678429694516142073L;

	private List<Currency> currencies = new ArrayList<>();

	public List<Currency> getCurrencies() {
		return currencies;
	}

	public void setCurrencies(List<Currency> currencies) {
		this.currencies = currencies;
	}

	public void delete() {
		World.getCurrenciesMarkets().remove(this);
		setRunning(false);
	}

	public BigDecimal getCurrenciesPrice(Currency c1, Currency c2) {
		BigDecimal price = c2.getCurrentPrice().divide(c1.getCurrentPrice(), 5)
				.multiply(BigDecimal.ONE.add(getProfitMargin().divide(new BigDecimal(100))))
				.setScale(3, RoundingMode.HALF_UP);
		return price;
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
		for (Currency c : this.getCurrencies()) {
			if (!c.getName().equals("USD")) {
				recalculatePrice(c);
			} else
				c.getHistory().put(World.getDate().withMinute(0).withSecond(0).withNano(0), BigDecimal.ONE);
		}
	}
}
