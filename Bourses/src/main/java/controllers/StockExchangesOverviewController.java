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
import model.StockExchange;
import model.World;

public class StockExchangesOverviewController {



	@FXML
	private TableView<StockExchange> stockExchangesTableView;

	@FXML
	private TableColumn<StockExchange, String> nameColumn;

	@FXML
	private TableColumn<StockExchange, String> countryColumn;

	@FXML
	private TableColumn<StockExchange, String> cityColumn;

	@FXML
	private TableColumn<StockExchange, StockExchange> deleteColumn;

	@FXML
	private TableColumn<StockExchange, StockExchange> moreInfoColumn;

	@FXML
	public void initialize() {

		try {
			this.stockExchangesTableView
					.setItems(FXCollections.observableArrayList(World.getStockExchanges()));
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

		this.deleteColumn.setCellFactory(value -> new TableCell<StockExchange, StockExchange>() {

			Button button = new Button();
			{
				button.setGraphic(
						new ImageView(new Image(this.getClass().getResource("/icons/delete.png").toString())));
			}

			@Override
			protected void updateItem(StockExchange stockExchange, boolean empty) {
				super.updateItem(stockExchange, empty);

				if (empty) {
					button.setGraphic(null);
					return;
					
				}
				if (!empty) {
					setGraphic(button);
					button.setOnAction(event -> {
						World.getStockExchanges().remove(stockExchange);
						stockExchangesTableView.setItems(FXCollections.observableArrayList(World.getStockExchanges()));
						initialize();
					});
				}

			};

		});

		this.moreInfoColumn.setCellFactory(value -> new TableCell<StockExchange, StockExchange>() {

			Button button = new Button();
			{
				button.setGraphic(
						new ImageView(new Image(this.getClass().getResource("/icons/analysis.png").toString())));
			}

			protected void updateItem(StockExchange stockExchange, boolean empty) {
				if (empty) {
					button.setGraphic(null);
					return;
				}
				if (!empty) {
					setGraphic(button);
					button.setOnAction(event -> {

						stockExchangeInfoDialog(stockExchange);
					});
				}

			}

			private void stockExchangeInfoDialog(StockExchange stockExchange) {
				FXMLLoader loader = utils.FxmlUtils.getLoader("/fxml/StockExchangeInfo2.fxml");
				Scene scene = null;
				try {
					scene = new Scene(loader.load());
				} catch (IOException e) {
					e.printStackTrace();
				}
				StockExchangeInfoController2 controller = loader.getController();
				controller.init(stockExchange);
				Stage stage = new Stage();
				stage.setScene(scene);
				stage.setResizable(false);
				stage.showAndWait();
			}

		});

	}

}
