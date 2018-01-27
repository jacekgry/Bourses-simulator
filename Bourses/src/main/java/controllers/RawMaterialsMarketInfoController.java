package controllers;

import java.io.IOException;
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
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.RawMaterial;
import model.RawMaterialsMarket;
import utils.FxmlUtils;

public class RawMaterialsMarketInfoController {

	@FXML
	private Button okButton;

	@FXML
	private TableView<RawMaterial> rawMaterialsTableView;

	@FXML
	private TableColumn<RawMaterial, String> nameColumn;

	@FXML
	private TableColumn<RawMaterial, String> unitColumn;
	
	@FXML
	private TableColumn<RawMaterial, String> currentValueColumn;

	@FXML
	private TableColumn<RawMaterial, String> minimumValueColumn;

	@FXML
	private TableColumn<RawMaterial, String> maximumValueColumn;

	@FXML
	private TableColumn<RawMaterial, Boolean> addToChartColumn;

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

	private List<RawMaterial> rawMaterials = new ArrayList<>();

	private RawMaterialsMarket rawMaterialsMarket;

	public void setFirstNameLabel(String firstName) {
		nameLabel.setText(firstName);
	}

	public void init(RawMaterialsMarket rawMaterialsMarket) {
		this.rawMaterialsMarket = rawMaterialsMarket;
		this.nameLabel.setText(rawMaterialsMarket.getName());
		this.countryLabel.setText(rawMaterialsMarket.getCountry());
		this.addressLabel.setText(rawMaterialsMarket.getAddress());
		this.cityLabel.setText(rawMaterialsMarket.getCity().toString());

		this.rawMaterials = rawMaterialsMarket.getRawMaterials();

		this.rawMaterialsTableView.setItems(FXCollections.observableArrayList(this.rawMaterials));

		this.rawMaterialsTableView.getItems().stream().forEach(rawMaterial -> {
			rawMaterial.setAddProperty(false);
		});
		
		this.nameColumn.setCellValueFactory(cellData -> {
			StringProperty name = new SimpleStringProperty(cellData.getValue().getName());
			return name;
		});
		
		this.unitColumn.setCellValueFactory(cellData -> {
			StringProperty unit = new SimpleStringProperty(cellData.getValue().getUnit());
			return unit;
		});

		this.minimumValueColumn.setCellValueFactory(cellData -> {
			StringProperty price = new SimpleStringProperty(
					cellData.getValue().getMinimumPriceInCurrency().toString() + cellData.getValue().getCurrency().getName());
			return price;
		});

		this.maximumValueColumn.setCellValueFactory(cellData -> {
			StringProperty price = new SimpleStringProperty(
					cellData.getValue().getMaximumPriceInCurrency().toString() + cellData.getValue().getCurrency().getName());
			return price;
		});

		this.currentValueColumn.setCellValueFactory(cellData -> {
			StringProperty price = new SimpleStringProperty(
					cellData.getValue().getCurrentPriceInCurrency().toString() + cellData.getValue().getCurrency().getName());
			return price;
		});

		this.addToChartColumn.setCellValueFactory(new PropertyValueFactory<RawMaterial, Boolean>("add"));
		this.addToChartColumn.setCellFactory(dataCell -> new CheckBoxTableCell<>());
		this.rawMaterialsTableView.setEditable(true);
	}


	@FXML
	public void refresh() {
		rawMaterialsTableView.setItems(FXCollections.observableArrayList(this.rawMaterialsMarket.getRawMaterials()));
		rawMaterialsTableView.refresh();
	}

	public void drawChart() {

		List<RawMaterial> selectedCompanies = this.rawMaterialsTableView.getItems().stream().filter(company -> {
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
		stockExchangeChartController.init(selectedCompanies);
		stage.setScene(scene);
		stage.showAndWait();
	}
	
	
}
