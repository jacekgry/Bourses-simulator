package utils.addDialogs;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import model.RawMaterial;
import model.RawMaterialsMarket;
import model.World;
import utils.DB;
import utils.Validators;

public class AddRawMaterialsMarketDialog extends AddDialog<RawMaterialsMarket> {

	private BooleanProperty invalidName = new SimpleBooleanProperty(false);
	private BooleanProperty invalidCity = new SimpleBooleanProperty(false);
	private BooleanProperty invalidAddress = new SimpleBooleanProperty(false);
	private BooleanProperty invalidProfitMargin = new SimpleBooleanProperty(false);

	private ComboBox<String> countries = new ComboBox<>(FXCollections.observableArrayList(DB.getCountries()));

	private TextField name = new TextField();
	private TextField city = new TextField();
	private TextField address = new TextField();
	private TextField profitMargin = new TextField();

	private Label invalidCityLabel = new Label("Please provide valid city");
	private Label invalidNameLabel = new Label("Please provide valid name");
	private Label invalidAddressLabel = new Label("Please provide valid address");
	private Label invalidProfitMarginLabel = new Label("Please provide valid profit margin");

	MenuButton rawMaterials = new MenuButton();

	public Optional<RawMaterialsMarket> addRawMaterialsMarket() {

		dialog.setTitle("Create raw materials market");

		dialog.getDialogPane().getButtonTypes().addAll(createButtonType, ButtonType.CANCEL);

		setUpGrid(grid);

		name.setPromptText("Name");

		city.setPromptText("City");

		address.setPromptText("Adress");

		profitMargin.setPromptText("Profit margin");

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

		World.getRawMaterials().stream().forEach(rawMaterial -> {
			CheckMenuItem checkCurrency = new CheckMenuItem();
			checkCurrency.setText(rawMaterial.getName());
			rawMaterials.getItems().add(checkCurrency);
			checkCurrency.setUserData(rawMaterial);
		});
		grid.add(new Label("Raw materials:"), 0, 5);
		grid.add(rawMaterials, 1, 5);

		grid.add(randomDataCheckBox, 0, 6);
		
		createButton = dialog.getDialogPane().lookupButton(createButtonType);
		createButton.disableProperty().bind(invalidName.or(invalidCity).or(invalidAddress).or(invalidProfitMargin)
				.or(countries.getSelectionModel().selectedItemProperty().isNull()));

		
		drawRawMaterialsMarket();
		bindCheckBox();
		
		dialog.getDialogPane().setContent(grid);

		dialog.setResultConverter(dialogButton -> {
			if (dialogButton == createButtonType) {
				RawMaterialsMarket rawMaterialsMarket = new RawMaterialsMarket();
				rawMaterialsMarket.setAddress(address.getText());
				rawMaterialsMarket.setCity(city.getText());
				rawMaterialsMarket.setCountry(countries.getSelectionModel().getSelectedItem());
				rawMaterialsMarket.setName(name.getText());
				rawMaterialsMarket.setProfitMargin(new BigDecimal(profitMargin.getText()));
				rawMaterials.getItems().stream().forEach(rawMaterial -> {
					CheckMenuItem checkRawMaterial = (CheckMenuItem) rawMaterial;
					if (checkRawMaterial.isSelected()) {
						RawMaterial rawMaterialFromMenu = (RawMaterial) checkRawMaterial.getUserData();
						rawMaterialsMarket.getRawMaterials().add(rawMaterialFromMenu);
					}
				});

				return rawMaterialsMarket;
			}
			return null;
		});

		Optional<RawMaterialsMarket> result = dialog.showAndWait();
		if (result.isPresent())
			return result;
		else
			return null;
	}

	protected void disableTextFields(boolean disable) {
		name.disableProperty().set(disable);
		countries.disableProperty().set(disable);
		address.disableProperty().set(disable);
		city.disableProperty().set(disable);
		profitMargin.disableProperty().set(disable);
		rawMaterials.disableProperty().set(disable); 
	}

	private void drawRawMaterialsMarket() {
		this.city.setText(DB.getCities().get(random.nextInt(DB.getCities().size())));

		this.countries.getSelectionModel().select(random.nextInt(countries.getItems().size()));
		this.profitMargin.setText(String.format("%.2f", random.nextDouble() * 10));
		this.address.setText(Integer.toString(random.nextInt(100) + 1) + " "
				+ DB.getStreets().get(random.nextInt(DB.getStreets().size())) + " Street");
		this.name.setText(this.city.getText() + "Raw Materials Market");
		
		this.rawMaterials.getItems().stream().forEach(rawMaterial -> {
			if(random.nextDouble() < 0.5) {
				CheckMenuItem rm = (CheckMenuItem) rawMaterial;
				rm.selectedProperty().set(true);
			}
		});
	}
}
