package controllers;

import java.util.ArrayList;
import java.util.List;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import model.Currency;

public class ListCountriesController {

	@FXML
	private TableView<String> countriesTableView;

	@FXML
	private TableColumn<String, String> countryColumn;

	private List<String> countries = new ArrayList<>();

	public void initCountries(Currency currency) {
		this.countries = currency.getCountries();

		countriesTableView.setItems(FXCollections.observableArrayList(countries));

		this.countryColumn.setCellValueFactory(dataCell -> {
			SimpleStringProperty country = new SimpleStringProperty(dataCell.getValue());
			return country;
		});

	}

}
