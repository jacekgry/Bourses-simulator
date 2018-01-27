package controllers;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map.Entry;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import model.InvestmentFund;
import model.Investor;

public class UnitsOfInvestmentmentFundsInfoController {
	@FXML
	private TableView<Entry<InvestmentFund, BigDecimal>> unitsTableView;
	
	@FXML
	private Label titleLabel;
	
	@FXML
	private TableColumn<Entry<InvestmentFund, BigDecimal>, String> investmentFundColumn;
	
	@FXML
	private TableColumn<Entry<InvestmentFund, BigDecimal>, String> unitsColumn;

	public void bindings(Investor investor) {
		
		
		unitsTableView.setItems(FXCollections.observableArrayList(investor.getInvestmentFundUnits().entrySet()));
		
		investmentFundColumn.setCellValueFactory(cellData -> {
			System.out.println(cellData);
			SimpleStringProperty investmentFund = new SimpleStringProperty(cellData.getValue().getKey().getName());
			return investmentFund;
		});
			
		unitsColumn.setCellValueFactory(cellData -> {
			SimpleStringProperty num = new SimpleStringProperty(cellData.getValue().getValue().toString());
			return num;
		});
		
		titleLabel.setText("Investment funds units of " + investor.getFirstName() + " " + investor.getSecondName());
	}
	
}
