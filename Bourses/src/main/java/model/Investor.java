package model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class Investor extends Gambler {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4979145740686829380L;

	private int id;

	private String firstName;
	private String secondName;
	private String PESEL;
	private HashMap<InvestmentFund, BigDecimal> investmentFundUnits = new HashMap<>();

	public Investor() {
		if (World.getInvestors().isEmpty())
			this.id = 1;
		else
			this.id = World.getInvestors().get(World.getInvestors().size() - 1).getId() + 1;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getSecondName() {
		return secondName;
	}

	public void setSecondName(String secondName) {
		this.secondName = secondName;
	}

	public String getPESEL() {
		return PESEL;
	}

	public void setPESEL(String PESEL) {
		this.PESEL = PESEL;
	}

	public HashMap<InvestmentFund, BigDecimal> getInvestmentFundUnits() {
		return investmentFundUnits;
	}

	public void setInvestmentFundUnits(HashMap<InvestmentFund, BigDecimal> investmentFundUnits) {
		this.investmentFundUnits = investmentFundUnits;
	}

	public void buyInvestmentFundUnits() {
		if (World.getInvestmentFunds().isEmpty())
			return;
		InvestmentFund investmentFund;
		investmentFund = World.getInvestmentFunds().get(random.nextInt(World.getInvestmentFunds().size()));
		synchronized (this) {
			BigDecimal valueOfUnit;

			synchronized (investmentFund) {
				valueOfUnit = investmentFund.getValueOfUnit();
			}

			BigDecimal moneyToBeSpent = new BigDecimal(0.05 * random.nextDouble() * this.getBudget().doubleValue())
					.setScale(0, RoundingMode.HALF_DOWN);

			BigDecimal numOfUnits = moneyToBeSpent.divide(valueOfUnit, 3);
			this.setBudget(this.getBudget().subtract(numOfUnits.multiply(valueOfUnit)));
			investmentFund.setBudget(investmentFund.getBudget().add(numOfUnits.multiply(valueOfUnit)));
			if (!this.investmentFundUnits.containsKey(investmentFund)) {
				this.investmentFundUnits.put(investmentFund, numOfUnits);
				investmentFund.getUnitsOwners().put(this, numOfUnits);
			} else {
				this.investmentFundUnits.put(investmentFund,
						this.investmentFundUnits.get(investmentFund).add(numOfUnits));
				investmentFund.getUnitsOwners().put(this, investmentFund.getUnitsOwners().get(this).add(numOfUnits));
			}
		}
	}

	public void sellInvestmentFundUnits() {
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "Investor [firstName=" + firstName + ", secondName=" + secondName + ", PESEL=" + PESEL + ", getBudget()="
				+ getBudget() + ", getAssets()=" + getAssets() + "]";
	}

	public synchronized void increaseBudget() {
		BigDecimal value = new BigDecimal(random.nextInt(200000));
		this.setBudget(this.getBudget().add(value));
	}

	@Override
	public void delete() {
		super.delete();
		World.getInvestors().remove(this);
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
				Investor inv = (Investor) this;
				sellAssetOnTheMarket();

			}
			if (random.nextDouble() < 0.3) {
				Investor inv = (Investor) this;
				buySharesOnTheStockExchange();

			}
			if (random.nextDouble() < 0.4) {
				increaseBudget();
			}

			if (random.nextDouble() < 0.3) {
				buyInvestmentFundUnits();
			}

			if (random.nextDouble() < 0.3)
				buyRawMaterials();

			if (random.nextDouble() < 0.3)
				buyCurrency();
		}
	}

}
