package controllers;

import java.io.IOException;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
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
import model.Currency;
import model.World;
import utils.FxmlUtils;

public class CurrenciesOverviewController {

	@FXML
	TableView<Currency> currenciesTable;

	@FXML
	TableColumn<Currency, String> nameColumn;

	@FXML
	TableColumn<Currency, Currency> countriesColumn;

	@FXML
	TableColumn<Currency, Currency> deleteColumn;

	@FXML
	public void initialize() {

		this.currenciesTable.setItems(FXCollections.observableArrayList(World.getCurrencies()));
		this.nameColumn.setCellValueFactory(dataCell -> {
			StringProperty name = new SimpleStringProperty(dataCell.getValue().getName());
			return name;
		});
		this.deleteColumn.setCellValueFactory(dataCell -> new SimpleObjectProperty<>(dataCell.getValue()));

		this.deleteColumn.setCellFactory(value -> new TableCell<Currency, Currency>() {

			Button button = new Button();
			{
				button.setGraphic(
						new ImageView(new Image(this.getClass().getResource("/icons/delete.png").toString())));
			}

			@Override
			protected void updateItem(Currency currency, boolean empty) {
				super.updateItem(currency, empty);

				if (empty) {
					button.setGraphic(null);
					return;
				}
				if (!empty) {
					setGraphic(button);
					button.setOnAction(event -> {
						if (currency.getName().equals("USD"))
							return;

						World.getCurrencies().remove(currency);
						currenciesTable.setItems(FXCollections.observableArrayList(World.getCurrencies()));
						initialize();
					});
				}

			};

		});

		this.countriesColumn.setCellValueFactory(dataCell -> new SimpleObjectProperty<>(dataCell.getValue()));

		this.countriesColumn.setCellFactory(value -> new TableCell<Currency, Currency>() {
			Button button = new Button();
			{
				button.setGraphic(
						new ImageView(new Image(this.getClass().getResource("/icons/worldwide.png").toString())));
			}

			@Override
			protected void updateItem(Currency currency, boolean empty) {
				super.updateItem(currency, empty);

				if (empty) {
					button.setGraphic(null);
					return;
				}
				if (!empty) {
					setGraphic(button);
					button.setOnAction(event -> {
						listCountries(currency);

					});
				}
			}

			private void listCountries(Currency currency) {
				Stage stage = new Stage();
				Scene scene = null;

				FXMLLoader loader = FxmlUtils.getLoader("/fxml/ListCountries.fxml");
				try {
					scene = new Scene(loader.load());
				} catch (IOException e) {
					e.printStackTrace();
				}
				ListCountriesController listCountriesController = loader.getController();
				listCountriesController.initCountries(currency);
				stage.setScene(scene);
				stage.showAndWait();
			}
		});

	}
}
