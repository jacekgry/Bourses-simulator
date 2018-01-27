package controllers;

import java.util.List;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import model.AssetAmount;
import model.RawMaterial;

public class RawMaterialsInfoController {

	@FXML
	private TableView<AssetAmount> rawMaterialsTableView;

	@FXML
	private Label titleLabel;

	@FXML
	private TableColumn<AssetAmount, String> rawMaterialColumn;

	@FXML
	private TableColumn<AssetAmount, String> amountOfRawMaterialColumn;

	private List<AssetAmount> rawMaterials;


	public void bindings(String name, List<AssetAmount> listOfRawMaterials) {

		this.rawMaterials = listOfRawMaterials;

		rawMaterialsTableView.setItems(FXCollections.observableArrayList(rawMaterials));

		rawMaterialColumn.setCellValueFactory(cellData -> {
			SimpleStringProperty rawMaterialName = new SimpleStringProperty(cellData.getValue().getAsset().getName());
			return rawMaterialName;
		});

		amountOfRawMaterialColumn.setCellValueFactory(cellData -> {
			RawMaterial rm = (RawMaterial) cellData.getValue().getAsset();
			SimpleStringProperty number = new SimpleStringProperty(
					cellData.getValue().getAmount().toString() + " " + rm.getUnit() + "s");
			return number;
		});

		titleLabel.setText("Raw materials of " + name);
	}



}
