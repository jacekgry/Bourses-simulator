package utils.addDialogs;

import java.util.Optional;
import java.util.Random;
import java.util.ResourceBundle;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import model.Company;
import utils.DB;

public class AddCompanyDialog extends AddDialog<Company> {

	private TextField name = new TextField();

	public Optional<Company> addCompanyDialog() {
		drawCompany();
		dialog.setTitle("Create company");

		dialog.getDialogPane().getButtonTypes().addAll(createButtonType, ButtonType.CANCEL);

		createButton = dialog.getDialogPane().lookupButton(createButtonType);

		setUpGrid(grid);

		name.setPromptText("Name");

		createButton.disableProperty().bind(name.textProperty().isEmpty());

		grid.add(new Label("Name:"), 0, 0);
		grid.add(name, 1, 0);

		randomDataCheckBox.selectedProperty().set(false);
		disableTextFields(true);
		randomDataCheckBox.selectedProperty().addListener((observable, newValue, oldValue) -> {
			if (newValue.booleanValue()) {
				disableTextFields(true);

			} else {
				disableTextFields(false);
			}
		});
		grid.add(randomDataCheckBox, 0, 4);

		dialog.getDialogPane().setContent(grid);

		dialog.setResultConverter(dialogButton -> {
			if (dialogButton == createButtonType) {
				Company company = new Company();
				company.setName(name.getText());
				return company;
			}
			return null;
		});

		Optional<Company> result = dialog.showAndWait();
		if (result.isPresent())
			return result;
		else
			return null;
	}

	protected void disableTextFields(boolean disable) {
		name.disableProperty().set(disable);
	}

	private void drawCompany() {
		name.setText(DB.getCompaniesNames().get(random.nextInt(DB.getCompaniesNames().size())));
	}
}
