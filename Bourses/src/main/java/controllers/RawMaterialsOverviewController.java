package controllers;

import java.io.IOException;
import java.util.ResourceBundle;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import model.Investor;
import model.RawMaterial;
import model.World;
import utils.FxmlUtils;

public class RawMaterialsOverviewController {

	@FXML
	private TableView<RawMaterial> rawMaterialsTable;

	@FXML
	private TableColumn<RawMaterial, String> nameColumn;

	@FXML
	private TableColumn<RawMaterial, String> unitColumn;

	@FXML
	private TableColumn<RawMaterial, String> currencyColumn;

	@FXML
	private TableColumn<RawMaterial, RawMaterial> deleteColumn;

	@FXML
	public void initialize() {

		try {
			this.rawMaterialsTable.setItems(FXCollections.observableArrayList(World.getRawMaterials()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.nameColumn.setCellValueFactory(cellData -> {
			SimpleStringProperty name = new SimpleStringProperty(cellData.getValue().getName());
			return name;
		});
		this.currencyColumn.setCellValueFactory(cellData -> {
			SimpleStringProperty currency = new SimpleStringProperty(cellData.getValue().getCurrency().getName());
			return currency;
		});
		this.unitColumn.setCellValueFactory(cellData -> {
			SimpleStringProperty unit = new SimpleStringProperty(cellData.getValue().getUnit());
			return unit;
		});

		this.deleteColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue()));

		this.deleteColumn.setCellFactory(value -> new TableCell<RawMaterial, RawMaterial>() {

			Button button = new Button();
			{
				button.setGraphic(
						new ImageView(new Image(this.getClass().getResource("/icons/delete.png").toString())));
			}

			@Override
			protected void updateItem(RawMaterial rawMaterial, boolean empty) {
				super.updateItem(rawMaterial, empty);

				if (empty) {
					button.setGraphic(null);
					return;
				}
				if (!empty) {
					setGraphic(button);
					button.setOnAction(event -> {
						rawMaterial.delete();
						rawMaterialsTable.setItems(FXCollections.observableArrayList(World.getRawMaterials()));

						initialize();
					});
				}

			};

		});

	}

}
