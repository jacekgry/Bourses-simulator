package controllers;

import java.io.IOException;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import model.RawMaterialsMarket;
import model.World;

public class RawMaterialsMarketsOverviewController {

	@FXML
	private TableView<RawMaterialsMarket> rawMaterialsMarketsTableView;

	@FXML
	private TableColumn<RawMaterialsMarket, String> nameColumn;

	@FXML
	private TableColumn<RawMaterialsMarket, String> countryColumn;

	@FXML
	private TableColumn<RawMaterialsMarket, String> cityColumn;

	@FXML
	private TableColumn<RawMaterialsMarket, RawMaterialsMarket> deleteColumn;

	@FXML
	private TableColumn<RawMaterialsMarket, RawMaterialsMarket> moreInfoColumn;

	@FXML
	public void initialize() {

		try {
			this.rawMaterialsMarketsTableView
					.setItems(FXCollections.observableArrayList(World.getRawMaterialsMarkets()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.nameColumn.setCellValueFactory(cellData -> {
			SimpleStringProperty name = new SimpleStringProperty(cellData.getValue().getName());
			return name;
		});
		this.countryColumn.setCellValueFactory(cellData -> {
			SimpleStringProperty country = new SimpleStringProperty(cellData.getValue().getCountry());
			return country;
		});
		this.cityColumn.setCellValueFactory(cellData -> {
			SimpleStringProperty city = new SimpleStringProperty(cellData.getValue().getCity().toString());
			return city;
		});
		this.deleteColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue()));
		this.moreInfoColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue()));
		this.deleteColumn.setCellFactory(value -> new TableCell<RawMaterialsMarket, RawMaterialsMarket>() {

			Button button = new Button();
			{
				button.setGraphic(
						new ImageView(new Image(this.getClass().getResource("/icons/delete.png").toString())));
			}

			@Override
			protected void updateItem(RawMaterialsMarket rawMaterialsMarket, boolean empty) {
				super.updateItem(rawMaterialsMarket, empty);

				if (empty) {
					button.setGraphic(null);
					return;
				}
				if (!empty) {
					setGraphic(button);
					button.setOnAction(event -> {
						World.getRawMaterialsMarkets().remove(rawMaterialsMarket);
						rawMaterialsMarketsTableView.setItems(FXCollections.observableArrayList(World.getRawMaterialsMarkets()));
						initialize();
					});
				}

			};

		});

		this.moreInfoColumn.setCellFactory(value -> new TableCell<RawMaterialsMarket, RawMaterialsMarket>() {

			Button button = new Button();
			{
				button.setGraphic(
						new ImageView(new Image(this.getClass().getResource("/icons/analysis.png").toString())));
			}

			protected void updateItem(RawMaterialsMarket rawMaterialsMarket, boolean empty) {
				if (empty) {
					button.setGraphic(null);
					return;
				}
				if (!empty) {
					setGraphic(button);
					button.setOnAction(event -> {
						rawMaterialsMarketInfoDialog(rawMaterialsMarket);
					});
				}

			}

			private void rawMaterialsMarketInfoDialog(RawMaterialsMarket rawMaterialsMarket) {
				FXMLLoader loader = utils.FxmlUtils.getLoader("/fxml/RawMaterialsMarketInfo.fxml");
				Scene scene = null;
				try {
					scene = new Scene(loader.load());
				} catch (IOException e) {
					e.printStackTrace();
				}
				RawMaterialsMarketInfoController controller = loader.getController();
				controller.init(rawMaterialsMarket);
				Stage stage = new Stage();
				stage.setScene(scene);
				stage.showAndWait();
			}
		});

	}

}
