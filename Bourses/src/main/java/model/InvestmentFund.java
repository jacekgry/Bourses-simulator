package model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;

public class InvestmentFund extends Gambler {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5025078547677967309L;

	private String managerFirstName;
	private String managerSecondName;
	private String name;
	private BigDecimal valueOfUnit;
	private HashMap<Investor, BigDecimal> unitsOwners = new HashMap<>();

	public InvestmentFund() {
		this.valueOfUnit = new BigDecimal(random.nextInt(20) + 1);
	}

	public String getManagerFirstName() {
		return managerFirstName;
	}

	public void setManagerFirstName(String managerFirstName) {
		this.managerFirstName = managerFirstName;
	}

	public String getManagerSecondName() {
		return managerSecondName;
	}

	public void setManagerSecondName(String managerSecondName) {
		this.managerSecondName = managerSecondName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BigDecimal getValueOfUnit() {
		return valueOfUnit.setScale(3, RoundingMode.HALF_UP);
	}

	public void setValueOfUnit(BigDecimal valueOfUnit) {
		this.valueOfUnit = valueOfUnit;
	}

	public HashMap<Investor, BigDecimal> getUnitsOwners() {
		return unitsOwners;
	}

	public void calculateValueOfUnit(BigDecimal previousValueOfAssets) {

		BigDecimal value = BigDecimal.ZERO;
		for (AssetAmount asset : getAssets()) {
			value = value.add(asset.getAmount().multiply(asset.getAsset().getCurrentPrice()));
		}
		value = value.add(getBudget());
		setValueOfUnit(getValueOfUnit().multiply(value.divide(previousValueOfAssets, 3)));
	}

	@Override
	public String toString() {
		return "InvestmentFund [managerFirstName=" + managerFirstName + ", managerSecondName=" + managerSecondName
				+ ", name=" + name + ", valueOfUnit=" + valueOfUnit + ", getBudget()=" + getBudget() + "]";
	}

	@Override
	public void delete() {
		super.delete();
		World.getInvestmentFunds().remove(this);
		unitsOwners.entrySet().stream().forEach(entry -> {
			entry.getKey().getInvestmentFundUnits().remove(this);
		});
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
				Thread.sleep(random.nextInt(3000) + 1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			if (random.nextDouble() < 0.9) {
				sellAssetOnTheMarket();

			}
			if (random.nextDouble() < 0.3) {
				buySharesOnTheStockExchange();

			}
			if (random.nextDouble() < 0.3)
				buyRawMaterials();

			if (random.nextDouble() < 0.3)
				buyCurrency();

		}
	}

}
