package controllers;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.CurrenciesMarket;
import model.Currency;
import utils.FxmlUtils;

public class CurrenciesMarketInfoController {

	@FXML
	private TableView<Currency> currenciesTableView;

	@FXML
	private TableColumn<Currency, String> nameColumn;

	@FXML
	private TableColumn<Currency, String> buyPriceColumn;

	@FXML
	private TableColumn<Currency, String> sellPriceColumn;

	@FXML
	private TableColumn<Currency, Boolean> addToChartColumn;

	@FXML
	private Button okButton;

	@FXML
	private Button rawMaterialsButton;

	@FXML
	private Label nameLabel;

	@FXML
	private Label countryLabel;

	@FXML
	private Label cityLabel;

	@FXML
	private Label addressLabel;

	@FXML
	private Label profitMarginLabel;

	@FXML
	private Label priceLabel;

	@FXML
	private ComboBox<Currency> currency1ComboBox;

	@FXML
	private ComboBox<Currency> currency2ComboBox;

	@FXML
	private Label currency1Label;

	@FXML
	private Label currency2Label;

	private List<Currency> currencies = new ArrayList<>();

	private CurrenciesMarket currenciesMarket;

	public void init(CurrenciesMarket currenciesMarket) {
		this.currenciesMarket = currenciesMarket;
		this.nameLabel.setText(currenciesMarket.getName());
		this.countryLabel.setText(currenciesMarket.getCountry());
		this.addressLabel.setText(currenciesMarket.getAddress());
		this.cityLabel.setText(currenciesMarket.getCity().toString());
		this.currencies = currenciesMarket.getCurrencies();
		this.profitMarginLabel.setText(currenciesMarket.getProfitMargin().toString());
		this.currenciesTableView.setItems(FXCollections.observableArrayList(this.currenciesMarket.getCurrencies()));

		this.currenciesTableView.getItems().stream().forEach(currency -> {
			currency.setAddProperty(false);
		});

		this.nameColumn.setCellValueFactory(cellData -> {
			StringProperty name = new SimpleStringProperty(cellData.getValue().getName());
			return name;
		});

		this.sellPriceColumn.setCellValueFactory(cellData -> {
			StringProperty price = new SimpleStringProperty(cellData.getValue().getCurrentPrice().toString());
			return price;
		});

		this.buyPriceColumn.setCellValueFactory(cellData -> {
			StringProperty price = new SimpleStringProperty(cellData.getValue().getCurrentPrice()
					.multiply(BigDecimal.ONE.add(this.currenciesMarket.getProfitMargin().divide(new BigDecimal(100))))
					.toString());
			return price;
		});

		this.nameColumn.setCellValueFactory(cellData -> {
			StringProperty name = new SimpleStringProperty(cellData.getValue().getName());
			return name;
		});

		this.currency1ComboBox.setItems(FXCollections.observableArrayList(this.currencies));
		this.currency2ComboBox.setItems(FXCollections.observableArrayList(this.currencies));

		this.currency1ComboBox.getSelectionModel().select(0);
		this.currency2ComboBox.getSelectionModel().select(0);
		this.currency1ComboBox.setUserData(currencies.get(0));
		this.currency2ComboBox.setUserData(currencies.get(0));
		this.currency1Label.setText("1 " + currencies.get(0).getName());
		this.currency2Label.setText(currencies.get(0).getName());
		this.priceLabel.setText(
				this.currenciesMarket.getCurrenciesPrice(this.currencies.get(0), this.currencies.get(0)).toString());

		this.currency2ComboBox.getSelectionModel().selectedItemProperty()
				.addListener((observable, oldValue, newValue) -> {
					this.currency2ComboBox.setUserData(newValue);
					this.currency1Label.setText("1 " + newValue.getName());
					this.priceLabel.setText(
							this.currenciesMarket.getCurrenciesPrice((Currency) currency1ComboBox.getUserData(),
									(Currency) currency2ComboBox.getUserData()).toString());
				});

		this.currency1ComboBox.getSelectionModel().selectedItemProperty()
				.addListener((observable, oldValue, newValue) -> {
					this.currency1ComboBox.setUserData(newValue);

					this.currency2Label.setText(newValue.getName());

					this.priceLabel.setText(
							this.currenciesMarket.getCurrenciesPrice((Currency) currency1ComboBox.getUserData(),
									(Currency) currency2ComboBox.getUserData()).toString());
				});

		this.addToChartColumn.setCellValueFactory(new PropertyValueFactory<Currency, Boolean>("add"));
		this.addToChartColumn.setCellFactory(dataCell -> new CheckBoxTableCell<>());
		this.currenciesTableView.setEditable(true);

	}

	@FXML
	private void drawChart() {
		List<Currency> selectedCurrencies = this.currenciesTableView.getItems().stream().filter(company -> {
			return company.addProperty().get();
		}).collect(Collectors.toList());

		Stage stage = new Stage();
		Scene scene = null;

		FXMLLoader loader = FxmlUtils.getLoader("/fxml/StockExchangeChart.fxml");
		try {
			scene = new Scene(loader.load());
		} catch (IOException e) {
			e.printStackTrace();
		}
		ChartController stockExchangeChartController = loader.getController();
		stockExchangeChartController.init(selectedCurrencies);
		stage.setScene(scene);
		stage.showAndWait();
	}

	@FXML
	private void refresh() {
		this.currenciesTableView.setItems(FXCollections.observableArrayList(this.currencies));
		this.currenciesTableView.refresh();
	}

}
