package utils.addDialogs;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.Currency;
import model.StockExchange;
import model.World;
import utils.DB;
import utils.Validators;

public class AddStockExchangeDialog extends AddDialog<StockExchange> {

	private BooleanProperty invalidName = new SimpleBooleanProperty(false);
	private BooleanProperty invalidCity = new SimpleBooleanProperty(false);
	private BooleanProperty invalidAddress = new SimpleBooleanProperty(false);
	private BooleanProperty invalidProfitMargin = new SimpleBooleanProperty(false);
	private TextField name = new TextField();
	private TextField city = new TextField();
	private TextField address = new TextField();
	private TextField profitMargin = new TextField();

	private Label invalidNameLabel = new Label("Please provide valid name");
	private Label invalidCityLabel = new Label("Please provide valid city");
	private Label invalidProfitMarginLabel = new Label("Please provide valid profit margin");
	private Label invalidAddressLabel = new Label("Please provide valid address");

	private ComboBox<Currency> currencies = new ComboBox<>(FXCollections.observableArrayList(World.getCurrencies()));
	private ComboBox<String> countries = new ComboBox<>(FXCollections.observableArrayList(DB.getCountries()));

	public Optional<StockExchange> addStockExchangeDialog() {

		drawStockExchange();
		dialog.setTitle("Create stock exchange");

		dialog.getDialogPane().getButtonTypes().addAll(createButtonType, ButtonType.CANCEL);

		createButton = dialog.getDialogPane().lookupButton(createButtonType);

		setUpGrid(grid);

		name.setPromptText("Name");

		city.setPromptText("City");

		address.setPromptText("Adress");

		profitMargin.setPromptText("Profit margin");

		createButton.disableProperty().bind(invalidName.or(invalidCity).or(invalidAddress).or(invalidProfitMargin)
				.or(countries.getSelectionModel().selectedItemProperty().isNull()));

		grid.add(new Label("Name:"), 0, 0);
		grid.add(name, 1, 0);
		grid.add(invalidNameLabel, 2, 0);
		invalidNameLabel.setVisible(false);
		name.textProperty().addListener((observable, oldValue, newValue) -> {
			invalidName.set(!Validators.isName(newValue));
			invalidNameLabel.setVisible(invalidName.get() && !newValue.isEmpty());
		});

		grid.add(new Label("Country:"), 0, 1);
		grid.add(countries, 1, 1);

		grid.add(new Label("City:"), 0, 2);
		grid.add(city, 1, 2);
		grid.add(invalidCityLabel, 2, 2);
		invalidCityLabel.setVisible(false);
		city.textProperty().addListener((observable, oldValue, newValue) -> {
			invalidCity.set(!Validators.isName(newValue));
			invalidCityLabel.setVisible(invalidCity.get() && !newValue.isEmpty());
		});

		grid.add(new Label("Address:"), 0, 3);
		grid.add(address, 1, 3);
		grid.add(invalidAddressLabel, 2, 3);
		invalidAddressLabel.setVisible(false);
		address.textProperty().addListener((observable, oldValue, newValue) -> {
			invalidAddress.set(!Validators.isAddress(newValue));
			invalidAddressLabel.setVisible(invalidAddress.get() && !newValue.isEmpty());
		});

		grid.add(new Label("Profit margin[%]:"), 0, 4);
		grid.add(profitMargin, 1, 4);
		grid.add(invalidProfitMarginLabel, 2, 4);
		invalidProfitMarginLabel.setVisible(false);
		profitMargin.textProperty().addListener((observable, oldValue, newValue) -> {
			invalidProfitMargin.set(!Validators.isNumber(newValue));
			invalidProfitMarginLabel.setVisible(invalidProfitMargin.get() && !newValue.isEmpty());
		});

		grid.add(new Label("Currency:"), 0, 5);
		grid.add(currencies, 1, 5);

		bindCheckBox();
		grid.add(randomDataCheckBox, 0, 6);

		dialog.getDialogPane().setContent(grid);

		dialog.setResultConverter(dialogButton -> {
			if (dialogButton == createButtonType) {
				StockExchange stockExchange = new StockExchange();
				stockExchange.setName(name.getText());
				stockExchange.setCountry(countries.getSelectionModel().getSelectedItem());
				stockExchange.setAddress(address.getText());
				stockExchange.setCurrency(currencies.getSelectionModel().getSelectedItem());
				stockExchange.setCity(city.getText());
				stockExchange.setProfitMargin(new BigDecimal(profitMargin.getText()));
				return stockExchange;
			}
			return null;
		});

		Optional<StockExchange> result = dialog.showAndWait();
		if (result.isPresent())
			return result;
		else
			return null;
	}

	private void drawStockExchange() {
		this.city.setText(DB.getCities().get(random.nextInt(DB.getCities().size())));

		String[] stockExchangeSuffixes = { "Stock Exchange", "Global Markets", "Market System", "Board of Trade",
				"Exchange", "Securities Exchange" };

		this.name.setText(
				this.city.getText() + " " + stockExchangeSuffixes[random.nextInt(stockExchangeSuffixes.length)]);

		this.countries.getSelectionModel().select(random.nextInt(countries.getItems().size()));
		this.profitMargin.setText(String.format("%.2f", random.nextDouble() * 10));
		this.address.setText(Integer.toString(random.nextInt(100) + 1) + " "
				+ DB.getStreets().get(random.nextInt(DB.getStreets().size())) + " Street");
		this.currencies.getSelectionModel().select(random.nextInt(currencies.getItems().size()));

	}

	@Override
	protected void disableTextFields(boolean disable) {
		name.disableProperty().set(disable);
		city.disableProperty().set(disable);
		address.disableProperty().set(disable);
		countries.disableProperty().set(disable);
		profitMargin.disableProperty().set(disable);
		currencies.disableProperty().set(disable);

	}

}
