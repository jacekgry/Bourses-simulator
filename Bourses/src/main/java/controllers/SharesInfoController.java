package controllers;

import java.util.List;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import model.AssetAmount;

public class SharesInfoController {

	
	@FXML
	private TableView<AssetAmount> sharesTableView;
	
	@FXML
	private Label titleLabel;
	
	@FXML
	private TableColumn<AssetAmount, String> companyColumn;
	
	@FXML
	private TableColumn<AssetAmount, String> numberOfSharesColumn;
	
	private List<AssetAmount> shares;
	
	@FXML
	public void initialize() {
		
		
	}


	public void bindings(String name, List<AssetAmount> listOfShares) {
		
		this.shares = listOfShares;
		
		sharesTableView.setItems(FXCollections.observableArrayList(shares));
		
		companyColumn.setCellValueFactory(cellData -> {
			SimpleStringProperty company = new SimpleStringProperty(cellData.getValue().getAsset().getName());
			return company;
		});
			
		numberOfSharesColumn.setCellValueFactory(cellData -> {
			SimpleStringProperty number = new SimpleStringProperty(cellData.getValue().getAmount().toString());
			return number;
		});
		
		titleLabel.setText("Shares of " + name);
	}

	

}
