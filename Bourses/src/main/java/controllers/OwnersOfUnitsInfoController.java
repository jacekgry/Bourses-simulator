package controllers;

import java.math.BigDecimal;
import java.util.Map.Entry;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import model.InvestmentFund;
import model.Investor;

public class OwnersOfUnitsInfoController {

	@FXML
	private TableView<Entry<Investor, BigDecimal>> investorsTableView;

	@FXML
	private TableColumn<Entry<Investor, BigDecimal>, String> investorColumn;

	@FXML
	private TableColumn<Entry<Investor, BigDecimal>, String> unitsColumn;

	@FXML
	private Label titleLabel;

	public void bindings(InvestmentFund investmentFund) {

		this.titleLabel.setText("Investors owning units of " + investmentFund.getName());

		this.investorsTableView.setItems(FXCollections.observableArrayList(investmentFund.getUnitsOwners().entrySet()));

		this.investorColumn.setCellValueFactory(cellData -> {
			StringProperty investor = new SimpleStringProperty(
					cellData.getValue().getKey().getFirstName() + cellData.getValue().getKey().getSecondName());
			return investor;
		});

		this.unitsColumn.setCellValueFactory(cellData -> {
			StringProperty units = new SimpleStringProperty(cellData.getValue().getValue().toString());
			return units;
		});
	}

}
