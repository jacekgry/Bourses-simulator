package controllers;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import model.Currency;
import model.InvestmentFund;
import model.Investor;
import model.RawMaterial;
import model.RawMaterialsMarket;
import model.StockExchange;
import utils.FxmlUtils;

public class InformationPanelController {

	@FXML
	private Pane mainPane;

	@FXML
	private TabPane mainTab;

	@FXML
	private Tab investorsTab;

	@FXML
	private Tab currenciesTab;

	@FXML
	private Tab investmentFundsTab;

	@FXML
	private Tab companiesTab;

	@FXML
	private Tab stockExchangesTab;

	@FXML
	private Tab currenciesMarketsTab;

	@FXML
	private Tab rawMaterialsMarketsTab;

	@FXML
	private Tab rawMaterialsTab;

	@FXML
	private AnchorPane investorsAnchorPane;

	@FXML
	private AnchorPane investmentFundsAnchorPane;

	@FXML
	private AnchorPane companiesAnchorPane;

	@FXML
	private AnchorPane exchangeMarketsAnchorPane;

	@FXML
	private AnchorPane stockExchangesAnchorPane;

	@FXML
	private AnchorPane currenciesAnchorPane;

	@FXML
	private AnchorPane currenciesMarketsAnchorPane;

	@FXML
	private AnchorPane rawMaterialsAnchorPane;

	@FXML
	private AnchorPane rawMaterialsMarketsAnchorPane;

	@FXML
	public void initialize() {

		initInvestorsTab();

		mainTab.setPrefWidth(1055.0);
		mainTab.setPrefHeight(705.0);
		investmentFundsTab.selectedProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue.booleanValue())
				initInvestmentFundsTab();
		});

		currenciesTab.selectedProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue.booleanValue())
				initCurrenciesTab();
		});

		rawMaterialsTab.selectedProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue.booleanValue())
				initRawMaterialsTab();
		});

		rawMaterialsMarketsTab.selectedProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue.booleanValue())
				initRawMaterialsMarketsTab();
		});

		companiesTab.selectedProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue.booleanValue())
				initCompaniesTab();
		});

		stockExchangesTab.selectedProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue.booleanValue())
				initStockExchangesTab();
		});

		currenciesMarketsTab.selectedProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue.booleanValue())
				initCurrenciesMarketsTab();
		});
	}

	private void initCurrenciesMarketsTab() {
		TableView<StockExchange> currenciesMarketsTable = null;
		try {
			currenciesMarketsTable = FXMLLoader.load(getClass().getResource("/fxml/CurrenciesMarketsOverview.fxml"),
					FxmlUtils.getResourceBundle());
		} catch (IOException e) {
			e.printStackTrace();
		}

		setUpAnchorPane(currenciesMarketsTable);
		currenciesMarketsAnchorPane.getChildren().add(currenciesMarketsTable);
	}

	private void initStockExchangesTab() {
		TableView<StockExchange> stockExchangesTable = null;
		try {
			stockExchangesTable = FXMLLoader.load(getClass().getResource("/fxml/StockExchangesOverview.fxml"),
					FxmlUtils.getResourceBundle());
		} catch (IOException e) {
			e.printStackTrace();
		}

		setUpAnchorPane(stockExchangesTable);
		stockExchangesAnchorPane.getChildren().add(stockExchangesTable);
	}

	private void initRawMaterialsTab() {
		TableView<RawMaterial> rawMaterialsTable = null;
		try {
			rawMaterialsTable = FXMLLoader.load(getClass().getResource("/fxml/RawMaterialsOverview.fxml"),
					FxmlUtils.getResourceBundle());
		} catch (IOException e) {
			e.printStackTrace();
		}
		setUpAnchorPane(rawMaterialsTable);
		rawMaterialsAnchorPane.getChildren().add(rawMaterialsTable);
	}

	private void initRawMaterialsMarketsTab() {
		TableView<RawMaterialsMarket> rawMaterialsMarketsTable = null;
		try {
			rawMaterialsMarketsTable = FXMLLoader.load(getClass().getResource("/fxml/RawMaterialsMarketsOverview.fxml"),
					FxmlUtils.getResourceBundle());
		} catch (IOException e) {
			e.printStackTrace();
		}
		setUpAnchorPane(rawMaterialsMarketsTable);
		rawMaterialsMarketsAnchorPane.getChildren().add(rawMaterialsMarketsTable);
	}

	private void initInvestmentFundsTab() {

		TableView<InvestmentFund> investmentFundsTable = null;
		try {
			investmentFundsTable = FXMLLoader.load(getClass().getResource("/fxml/InvestmentFundsOverview.fxml"),
					FxmlUtils.getResourceBundle());
		} catch (IOException e) {
			e.printStackTrace();
		}
		setUpAnchorPane(investmentFundsTable);
		investmentFundsAnchorPane.getChildren().add(investmentFundsTable);

	}

	private void initInvestorsTab() {
		TableView<Investor> investorsTable = null;
		try {
			investorsTable = FXMLLoader.load(getClass().getResource("/fxml/InvestorsOverview.fxml"),
					FxmlUtils.getResourceBundle());
		} catch (IOException e) {
			e.printStackTrace();
		}
		setUpAnchorPane(investorsTable);
		investorsAnchorPane.getChildren().add(investorsTable);

	}

	private void initCurrenciesTab() {
		TableView<Currency> currenciesTable = null;
		try {
			currenciesTable = FXMLLoader.load(getClass().getResource("/fxml/CurrenciesOverview.fxml"),
					FxmlUtils.getResourceBundle());
		} catch (IOException e) {
			e.printStackTrace();
		}
		setUpAnchorPane(currenciesTable);
		currenciesAnchorPane.getChildren().add(currenciesTable);

	}

	private void initCompaniesTab() {
		TableView<Currency> companiesTable = null;
		try {
			companiesTable = FXMLLoader.load(getClass().getResource("/fxml/CompaniesOverview.fxml"),
					FxmlUtils.getResourceBundle());
		} catch (IOException e) {
			e.printStackTrace();
		}
		setUpAnchorPane(companiesTable);
		companiesAnchorPane.getChildren().add(companiesTable);
	}

	private <T> void setUpAnchorPane(TableView<T> table) {
		AnchorPane.setTopAnchor(table, 0.0);
		AnchorPane.setLeftAnchor(table, 10.0);
		AnchorPane.setRightAnchor(table, 0.0);
		AnchorPane.setBottomAnchor(table, 0.0);
	}
}
