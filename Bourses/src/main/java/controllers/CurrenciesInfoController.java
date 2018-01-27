package controllers;

import java.util.List;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import model.AssetAmount;

public class CurrenciesInfoController {

	
	@FXML
	private TableView<AssetAmount> currenciesTableView;
	
	@FXML
	private Label titleLabel;
	
	@FXML
	private TableColumn<AssetAmount, String> currencyColumn;
	
	@FXML
	private TableColumn<AssetAmount, String> amountOfCurrencyColumn;
	
	private List<AssetAmount> currencies;
	
	public void bindings(String name, List<AssetAmount> listOfCurrencies) {
		
		this.currencies = listOfCurrencies;
		
		currenciesTableView.setItems(FXCollections.observableArrayList(currencies));
		
		currencyColumn.setCellValueFactory(cellData -> {
			System.out.println(cellData);
			SimpleStringProperty currency = new SimpleStringProperty(cellData.getValue().getAsset().getName());
			return currency;
		});
			
		amountOfCurrencyColumn.setCellValueFactory(cellData -> {
			SimpleStringProperty number = new SimpleStringProperty(cellData.getValue().getAmount().toString());
			return number;
		});
		
		titleLabel.setText("Currencies of " + name);
	}

	

}
