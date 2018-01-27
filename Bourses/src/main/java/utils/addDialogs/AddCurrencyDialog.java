package utils.addDialogs;

import java.util.Optional;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.TextField;
import model.Currency;
import model.World;
import utils.DB;
import utils.Validators;

public class AddCurrencyDialog extends AddDialog<Currency> {

	private BooleanProperty invalidName = new SimpleBooleanProperty(false);
	private BooleanProperty unUniqueName = new SimpleBooleanProperty(false);
	private ButtonType createButtonType = new ButtonType(bundle.getString("dialog.create"), ButtonData.OK_DONE);
	private TextField name = new TextField();
	private MenuButton countriesMenu = new MenuButton();
	private Label countriesLabel = new Label(bundle.getString("countries"));

	public Optional<Currency> addCurrency() {

		Dialog<Currency> dialog = new Dialog<>();
		dialog.setTitle(bundle.getString("dialog.createCurrency.title"));

		dialog.getDialogPane().getButtonTypes().addAll(createButtonType, ButtonType.CANCEL);

		createButton = dialog.getDialogPane().lookupButton(createButtonType);
		createButton.disableProperty().bind(invalidName.or(unUniqueName));

		setUpGrid(grid);

		name.setPromptText(bundle.getString("name"));

		grid.add(new Label("Name:"), 0, 0);
		grid.add(name, 1, 0);
		Label invalidNameLabel = new Label(bundle.getString("dialog.invalidName"));
		Label unUniqueNameLabel = new Label("Currency with this name already exists");

		invalidNameLabel.setVisible(false);
		unUniqueNameLabel.setVisible(false);

		grid.add(invalidNameLabel, 2, 0);
		grid.add(unUniqueNameLabel, 2, 0);

		name.textProperty().addListener((observable, oldValue, newValue) -> {
			invalidName.set(!Validators.isName(newValue));
			invalidNameLabel.setVisible(invalidName.get() && !newValue.isEmpty());

		});

		name.textProperty().addListener((observable, oldValue, newValue) -> {
			unUniqueName.set(World.getCurrencies().stream().anyMatch(currency -> {
				return currency.getName().equals(newValue);
			}));
			unUniqueNameLabel.setVisible(unUniqueName.get());
		});

		DB.getCountries().stream().forEach(country -> {
			CheckMenuItem checkCountry = new CheckMenuItem();
			checkCountry.setText(country);
			countriesMenu.getItems().add(checkCountry);
		});
		countriesMenu.setText("Countries");

		bindCheckBox();
		drawCurrency();

		grid.add(countriesLabel, 0, 1);
		grid.add(countriesMenu, 1, 1);
		grid.add(randomDataCheckBox, 0, 2);

		dialog.getDialogPane().setContent(grid);

		dialog.setResultConverter(dialogButton -> {
			if (dialogButton == createButtonType) {

				Currency currency = new Currency();
				currency.setName(name.getText());
				countriesMenu.getItems().stream().forEach(country -> {
					CheckMenuItem checkCountry = (CheckMenuItem) country;
					if (checkCountry.isSelected())
						currency.getCountries().add(checkCountry.getText());

				});
				return currency;
			}
			return null;
		});

		Optional<Currency> result = dialog.showAndWait();
		if (result.isPresent())
			return result;
		else
			return null;
	}

	private void drawCurrency() {

		boolean unique = false;
		String name = "";

		while (!unique) {
			name = "";
			name = name + (char) (random.nextInt(26) + 'A') + (char) (random.nextInt(26) + 'A')
					+ (char) (random.nextInt(26) + 'A');
			unique = true;
			for (Currency c : World.getCurrencies()) {
				if (c.getName().equals(name)) {
					unique = false;
					break;
				}
			}
		}

		this.name.setText(name);

		countriesMenu.getItems().stream().forEach(country -> {
			if (random.nextDouble() < 0.1) {
				CheckMenuItem checkCountry = (CheckMenuItem) country;
				checkCountry.selectedProperty().set(true);
			}
		});
	}

	protected void disableTextFields(boolean disable) {
		name.disableProperty().set(disable);
		countriesMenu.disableProperty().set(disable);

	}
}
