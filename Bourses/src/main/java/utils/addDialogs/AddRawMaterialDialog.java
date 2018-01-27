package utils.addDialogs;

import java.util.Optional;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import model.Currency;
import model.RawMaterial;
import model.World;
import utils.DB;

public class AddRawMaterialDialog extends AddDialog<RawMaterial> {

	private TextField name = new TextField();
	private TextField unit = new TextField();
	private ComboBox<Currency> currencies = new ComboBox<>(FXCollections.observableArrayList(World.getCurrencies()));

	public Optional<RawMaterial> addRawMaterial() {

		dialog.setTitle("Create raw material");

		dialog.getDialogPane().getButtonTypes().addAll(createButtonType, ButtonType.CANCEL);

		setUpGrid(grid);

		name.setPromptText("Name");
		grid.add(new Label("Name:"), 0, 0);
		grid.add(name, 1, 0);

		unit.setPromptText("Unit");
		grid.add(new Label("Unit"), 0, 1);
		grid.add(unit, 1, 1);

		grid.add(new Label("Currency:"), 0, 2);
		grid.add(currencies, 1, 2);

		grid.add(randomDataCheckBox, 0, 3);
		createButton = dialog.getDialogPane().lookupButton(createButtonType);
		createButton.disableProperty().bind(name.textProperty().isEmpty()
				.or(unit.textProperty().isEmpty().or(currencies.getSelectionModel().selectedItemProperty().isNull())));

		dialog.getDialogPane().setContent(grid);

		drawRawMaterial();
		bindCheckBox();

		dialog.setResultConverter(dialogButton -> {
			if (dialogButton == createButtonType) {
				RawMaterial rawMaterial = new RawMaterial();
				rawMaterial.setName(name.getText());
				rawMaterial.setUnit(unit.getText());
				rawMaterial.setCurrency(currencies.getSelectionModel().getSelectedItem());
				return rawMaterial;
			}
			return null;
		});

		Optional<RawMaterial> result = dialog.showAndWait();
		if (result.isPresent())
			return result;
		else
			return null;
	}

	private void drawRawMaterial() {
		this.name.setText(DB.getRawMaterials().get(random.nextInt(DB.getRawMaterials().size())));
		this.unit.setText("kg");
		this.currencies.getSelectionModel().select(random.nextInt(currencies.getItems().size()));
	}

	protected void disableTextFields(boolean disable) {
		this.name.disableProperty().set(disable);
		this.unit.disableProperty().set(disable);
		this.currencies.disableProperty().set(disable);
	}

}
