package controllers;

import java.util.ArrayList;
import java.util.List;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import model.Company;
import model.MarketIndex;

public class ListCompaniesController {
	@FXML
	private TableView<Company> companiesTableView;

	@FXML
	private TableColumn<Company, String> companiesColumn;

	private List<Company> companies = new ArrayList<>();


	public void initCompanies(MarketIndex marketIndex) {
		this.companies = marketIndex.getCompanies();

		companiesTableView.setItems(FXCollections.observableArrayList(companies));

		this.companiesColumn.setCellValueFactory(dataCell -> {
			SimpleStringProperty name = new SimpleStringProperty(dataCell.getValue().getName());
			return name;
		});
	}

}
