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
import model.CurrenciesMarket;
import model.World;

public class CurrenciesMarketsOverviewController {

	@FXML
	private TableView<CurrenciesMarket> currenciesMarketsTableView;

	@FXML
	private TableColumn<CurrenciesMarket, String> nameColumn;

	@FXML
	private TableColumn<CurrenciesMarket, String> countryColumn;

	@FXML
	private TableColumn<CurrenciesMarket, String> cityColumn;

	@FXML
	private TableColumn<CurrenciesMarket, CurrenciesMarket> deleteColumn;

	@FXML
	private TableColumn<CurrenciesMarket, CurrenciesMarket> moreInfoColumn;

	@FXML
	public void initialize() {

		try {
			this.currenciesMarketsTableView.setItems(FXCollections.observableArrayList(World.getCurrenciesMarkets()));
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

		this.deleteColumn.setCellFactory(value -> new TableCell<CurrenciesMarket, CurrenciesMarket>() {

			Button button = new Button();
			{
				button.setGraphic(
						new ImageView(new Image(this.getClass().getResource("/icons/delete.png").toString())));
			}

			@Override
			protected void updateItem(CurrenciesMarket currenciesMarket, boolean empty) {
				super.updateItem(currenciesMarket, empty);

				if (empty) {
					button.setGraphic(null);
					return;
				}
				if (!empty) {
					setGraphic(button);
					button.setOnAction(event -> {
						World.getCurrenciesMarkets().remove(currenciesMarket);
						currenciesMarketsTableView
								.setItems(FXCollections.observableArrayList(World.getCurrenciesMarkets()));
						initialize();
					});
				}

			};

		});

		this.moreInfoColumn.setCellFactory(value -> new TableCell<CurrenciesMarket, CurrenciesMarket>() {

			Button button = new Button();
			{
				button.setGraphic(
						new ImageView(new Image(this.getClass().getResource("/icons/analysis.png").toString())));
			}

			protected void updateItem(CurrenciesMarket currenciesMarket, boolean empty) {
				if (empty) {
					button.setGraphic(null);
					return;
				}
				if (!empty) {
					setGraphic(button);
					button.setOnAction(event -> {

						currenciesMarketInfoDialog(currenciesMarket);

					});
				}

			}

			private void currenciesMarketInfoDialog(CurrenciesMarket currenciesMarket) {
				FXMLLoader loader = utils.FxmlUtils.getLoader("/fxml/CurrenciesMarketInfo.fxml");
				Scene scene = null;
				try {
					scene = new Scene(loader.load());
				} catch (IOException e) {
					e.printStackTrace();
				}
				CurrenciesMarketInfoController controller = loader.getController();
				controller.init(currenciesMarket);
				Stage stage = new Stage();
				stage.setScene(scene);
				stage.showAndWait();
			}

		});

	}

}
