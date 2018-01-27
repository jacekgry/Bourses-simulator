package controllers;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import model.Company;
import model.Investor;
import model.MarketIndex;
import model.StockExchange;
import utils.FxmlUtils;
import utils.addDialogs.AddMarketIndexDialog;

public class StockExchangeInfoController2 {

	private List<Company> companies;

	@FXML
	private TableView<Company> companiesTableView;

	@FXML
	private TableColumn<Company, String> nameColumn;

	@FXML
	private TableColumn<Company, String> currentPriceColumn;

	@FXML
	private TableColumn<Company, String> minimumPriceColumn;

	@FXML
	private TableColumn<Company, String> maximumPriceColumn;

	@FXML
	private TableColumn<Company, String> volumeColumn;

	@FXML
	private TableColumn<Company, String> turnoverColumn;

	@FXML
	private TableColumn<Company, Company> moreColumn;
	
	@FXML
	private TableColumn<Company, Boolean> addToChartColumn;

	@FXML
	private TableView<MarketIndex> indexesTableView;

	@FXML
	private TableColumn<MarketIndex, String> indexNameColumn;

	@FXML
	private TableColumn<MarketIndex, String> indexCurrentValueColumn;

	@FXML
	private TableColumn<MarketIndex, MarketIndex> indexCompaniesColumn;

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

	private StockExchange stockExchange;
	
	private String currency;

	public void init(StockExchange stockExchange) {
		this.stockExchange = stockExchange;
		this.currency = stockExchange.getCurrency().getName();
		this.nameLabel.setText(stockExchange.getName());
		this.countryLabel.setText(stockExchange.getCountry());
		this.cityLabel.setText(stockExchange.getCity());
		this.addressLabel.setText(stockExchange.getAddress());
		this.profitMarginLabel.setText(stockExchange.getProfitMargin().toString());
		this.companies = stockExchange.getCompanies();
		this.companiesTableView.setItems(FXCollections.observableArrayList(companies));
		this.nameColumn.setCellValueFactory(cellData -> {
			StringProperty name = new SimpleStringProperty(cellData.getValue().getName());
			return name;
		});

		this.companiesTableView.getItems().stream().forEach(company -> {
			company.setAddProperty(false);
		});

		this.currentPriceColumn.setCellValueFactory(cellData -> {
			StringProperty price = new SimpleStringProperty(
					stockExchange.getCurrentPriceInCurrency(cellData.getValue()).toString() + currency);
			return price;
		});

		this.maximumPriceColumn.setCellValueFactory(cellData -> {
			StringProperty price = new SimpleStringProperty(
					stockExchange.getMaximumPriceInCurrency(cellData.getValue()).toString() + currency);
			return price;
		});

		this.minimumPriceColumn.setCellValueFactory(cellData -> {
			StringProperty price = new SimpleStringProperty(
					stockExchange.getMinimumPriceInCurrency(cellData.getValue()).toString() + currency);
			return price;
		});

		this.turnoverColumn.setCellValueFactory(cellData -> {
			StringProperty price = new SimpleStringProperty(
					stockExchange.getTurnoverInCurrency(cellData.getValue()).toString() + currency);
			return price;
		});
		
		this.moreColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue()));


		this.moreColumn.setCellFactory(value -> new TableCell<Company, Company>() {

			Button button = new Button();
			{
				button.setGraphic(
						new ImageView(new Image(this.getClass().getResource("/icons/companyIcon.jpg").toString())));
			}

			protected void updateItem(Company company, boolean empty) {
				if (empty) {
					button.setGraphic(null);
					return;
				}
				if (!empty) {
					setGraphic(button);
					button.setOnAction(event -> {
						companyInfoDialog(company);
					});
				}

			}

			private void companyInfoDialog(Company company) {
				FXMLLoader loader = utils.FxmlUtils.getLoader("/fxml/CompanyInfo.fxml");
				Scene scene = null;
				try {
					scene = new Scene(loader.load());
				} catch (IOException e) {
					e.printStackTrace();
				}
				CompanyInfoController controller = loader.getController();
				controller.init(company);
				Stage stage = new Stage();
				stage.setScene(scene);
				stage.showAndWait();
			}

		});

		
		
		
		
		
		this.volumeColumn.setCellValueFactory(cellData -> {
			StringProperty volume = new SimpleStringProperty(cellData.getValue().getVolume().toString());
			return volume;
		});

		this.addToChartColumn.setCellValueFactory(new PropertyValueFactory<Company, Boolean>("add"));
		this.addToChartColumn.setCellFactory(dataCell -> new CheckBoxTableCell<>());
		this.companiesTableView.setEditable(true);

		this.indexesTableView.setItems(FXCollections.observableArrayList(this.stockExchange.getMarketIndexes()));
		this.indexNameColumn.setCellValueFactory(cellData -> {
			StringProperty name = new SimpleStringProperty(cellData.getValue().getName().toString());
			return name;
		});
		this.indexCurrentValueColumn.setCellValueFactory(cellData -> {
			StringProperty value = new SimpleStringProperty(cellData.getValue().getValue().toString());
			return value;
		});

		this.indexCompaniesColumn.setCellValueFactory(dataCell -> new SimpleObjectProperty<>(dataCell.getValue()));

		this.indexCompaniesColumn.setCellFactory(value -> new TableCell<MarketIndex, MarketIndex>() {
			Button button = new Button();
			{
				button.setGraphic(
						new ImageView(new Image(this.getClass().getResource("/icons/companiesIcon.jpg").toString())));
			}

			@Override
			protected void updateItem(MarketIndex marketIndex, boolean empty) {
				super.updateItem(marketIndex, empty);

				if (empty) {
					button.setGraphic(null);
					return;
				}
				if (!empty) {
					setGraphic(button);
					button.setOnAction(event -> {
						listCompanies(marketIndex);
					});
				}
			}

		});
	}

	public void drawChart() {
		for (Company company : this.companiesTableView.getItems()) {
			if (company.addProperty().get())
				System.out.println(company.getName());
		}
		List<Company> selectedCompanies = this.companiesTableView.getItems().stream().filter(company -> {
			return company.addProperty().get();
		}).collect(Collectors.toList());

		System.out.println(selectedCompanies);
		Stage stage = new Stage();
		Scene scene = null;

		FXMLLoader loader = FxmlUtils.getLoader("/fxml/StockExchangeChart.fxml");
		try {
			scene = new Scene(loader.load());
		} catch (IOException e) {
			e.printStackTrace();
		}
		ChartController stockExchangeChartController = loader.getController();
		stockExchangeChartController.init(selectedCompanies);
		stage.setScene(scene);
		stage.showAndWait();
	}

	private void listCompanies(MarketIndex marketIndex) {
		Stage stage = new Stage();
		Scene scene = null;

		FXMLLoader loader = FxmlUtils.getLoader("/fxml/ListCompanies.fxml");
		try {
			scene = new Scene(loader.load());
		} catch (IOException e) {
			e.printStackTrace();
		}
		ListCompaniesController listCompaniesController = loader.getController();
		listCompaniesController.initCompanies(marketIndex);
		stage.setScene(scene);
		stage.showAndWait();
	}

	public void refresh() {

		companiesTableView.setItems(FXCollections.observableArrayList(companies));
		companiesTableView.refresh();
		indexesTableView.setItems(FXCollections.observableArrayList(stockExchange.getMarketIndexes()));
		indexesTableView.refresh();
	}

	public void addIndex() {
		try {
			AddMarketIndexDialog addMarketIndexDialog = new AddMarketIndexDialog();
			MarketIndex marketIndex = addMarketIndexDialog.addMarketIndex(stockExchange).get();
			if (marketIndex != null)
				this.stockExchange.addMarketIndex(marketIndex);
		} catch (Exception e) {
		}

		indexesTableView.setItems(FXCollections.observableArrayList(stockExchange.getMarketIndexes()));
		indexesTableView.refresh();

	}

}
