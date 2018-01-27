package controllers;

import java.util.Optional;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import model.Company;
import model.CurrenciesMarket;
import model.Currency;
import model.InvestmentFund;
import model.Investor;
import model.RawMaterial;
import model.RawMaterialsMarket;
import model.StockExchange;
import model.World;
import utils.addDialogs.AddCompanyDialog;
import utils.addDialogs.AddCurrenciesMarketDialog;
import utils.addDialogs.AddCurrencyDialog;
import utils.addDialogs.AddInvestmentFundDialog;
import utils.addDialogs.AddInvestorDialog;
import utils.addDialogs.AddRawMaterialDialog;
import utils.addDialogs.AddRawMaterialsMarketDialog;
import utils.addDialogs.AddStockExchangeDialog;

public class CreateMenuController {


	@FXML
	public void createInvestorButtonAction() {
		Optional<Investor> investor = null;
		try {
			AddInvestorDialog addInvestorDialog = new AddInvestorDialog();
			investor = addInvestorDialog.addInvestorDialog();
			if (investor != null) {
				World.getInvestors().add(investor.get());
				new Thread(investor.get()).start();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@FXML
	public void createInvestmentFundButtonAction() {

		Optional<InvestmentFund> investmentFund = null;

		try {
			AddInvestmentFundDialog addInvestmentFundDialog = new AddInvestmentFundDialog();
			investmentFund = addInvestmentFundDialog.addInvestmentFundDialog();
			if (investmentFund != null) {
				World.getInvestmentFunds().add(investmentFund.get());
				new Thread(investmentFund.get()).start();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@FXML
	public void createCurrencyButtonAction() {

		Optional<Currency> currency = null;
		AddCurrencyDialog addCurrencyDialog = new AddCurrencyDialog();
		currency = addCurrencyDialog.addCurrency();
		if (currency != null)
			World.addCurrency(currency.get());
	}

	@FXML
	public void createCurrenciesMarketButtonAction() {
		Optional<CurrenciesMarket> currenciesMarket = null;
		AddCurrenciesMarketDialog addCurrenciesMarketDialog = new AddCurrenciesMarketDialog();
		currenciesMarket = addCurrenciesMarketDialog.addCurrenciesMarket();
		if (currenciesMarket != null) {
			World.getCurrenciesMarkets().add(currenciesMarket.get());
			new Thread(currenciesMarket.get()).start();
		}
	}

	@FXML
	public void createRawMaterialButtonAction() {
		Optional<RawMaterial> rawMaterial = null;
		AddRawMaterialDialog addRawMaterialDialog = new AddRawMaterialDialog();
		rawMaterial = addRawMaterialDialog.addRawMaterial();
		if (rawMaterial != null) {
			World.addRawMaterial(rawMaterial.get());

		}

	}

	@FXML
	public void createRawMaterialsMarketButtonAction() {
		Optional<RawMaterialsMarket> rawMaterialsMarket = null;
		AddRawMaterialsMarketDialog addRawMaterialsMarketDialog = new AddRawMaterialsMarketDialog();
		rawMaterialsMarket = addRawMaterialsMarketDialog.addRawMaterialsMarket();
		if (rawMaterialsMarket != null) {
			World.getRawMaterialsMarkets().add(rawMaterialsMarket.get());
			new Thread(rawMaterialsMarket.get()).start();
		}
	}

	@FXML
	public void createCompanyButtonAction() {
		Optional<Company> company = null;
		AddCompanyDialog addCompanyDialog = new AddCompanyDialog();
		company = addCompanyDialog.addCompanyDialog();
		if (company != null) {
			World.addCompany(company.get());
			new Thread(company.get()).start();
		}
	}

	@FXML
	public void createStockExchangeButtonAction() {
		AddStockExchangeDialog addStockExchangeDialog = new AddStockExchangeDialog();
		Optional<StockExchange> stockExchange = null;
		stockExchange = addStockExchangeDialog.addStockExchangeDialog();
		if (stockExchange != null) {
			World.getStockExchanges().add(stockExchange.get());
			new Thread(stockExchange.get()).start();

		}
	}
}
