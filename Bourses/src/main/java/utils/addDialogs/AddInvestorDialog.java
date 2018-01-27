package utils.addDialogs;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.Random;
import java.util.ResourceBundle;

import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import model.Investor;
import utils.DB;
import utils.Validators;

public class AddInvestorDialog extends AddDialog<Investor> {

	private TextField firstName = new TextField();
	private TextField secondName = new TextField();
	private TextField PESEL = new TextField();
	private TextField budget = new TextField();

	private BooleanProperty invalidFirstName = new SimpleBooleanProperty(false);
	private BooleanProperty invalidSecondName = new SimpleBooleanProperty(false);
	private BooleanProperty invalidPESEL = new SimpleBooleanProperty(false);
	private BooleanProperty invalidBudget = new SimpleBooleanProperty(false);

	private Label invalidFirstNameLabel = new Label(bundle.getString("dialog.invalidFirstName"));
	private Label invalidSecondNameLabel = new Label(bundle.getString("dialog.invalidSecondName"));
	private Label invalidPESELLabel = new Label(bundle.getString("dialog.invalidPESEL"));
	private Label invalidBudgetLabel = new Label(bundle.getString("dialog.invalidBudget"));

	public Optional<Investor> addInvestorDialog() {

		drawInvestor();

		dialog.setTitle(bundle.getString("dialog.createInvestor.title"));
		dialog.getDialogPane().getButtonTypes().addAll(createButtonType, ButtonType.CANCEL);

		createButton = dialog.getDialogPane().lookupButton(createButtonType);
		createButton.disableProperty().bind(invalidFirstName.or(invalidSecondName).or(invalidPESEL).or(invalidBudget));

		setUpGrid(grid);
		
		firstName.setPromptText(bundle.getString("firstName"));
		secondName.setPromptText(bundle.getString("secondName"));
		PESEL.setPromptText(bundle.getString("PESEL"));
		budget.setPromptText(bundle.getString("budget"));

		grid.add(new Label("First name:"), 0, 0);
		grid.add(firstName, 1, 0);
		invalidFirstNameLabel.setVisible(false);
		grid.add(invalidFirstNameLabel, 2, 0);
		firstName.textProperty().addListener((observable, oldValue, newValue) -> {
			invalidFirstName.set(!Validators.isName(newValue));
			invalidFirstNameLabel.setVisible(invalidFirstName.get() && !newValue.isEmpty());

		});

		grid.add(new Label("Second name:"), 0, 1);
		grid.add(secondName, 1, 1);
		invalidSecondNameLabel.setVisible(false);
		grid.add(invalidSecondNameLabel, 2, 1);
		secondName.textProperty().addListener((observable, oldValue, newValue) -> {
			invalidSecondName.set(!Validators.isName(newValue));
			invalidSecondNameLabel.setVisible(invalidSecondName.get() && !newValue.isEmpty());
		});

		grid.add(new Label("PESEL:"), 0, 2);
		grid.add(PESEL, 1, 2);
		grid.add(invalidPESELLabel, 2, 2);
		invalidPESELLabel.setVisible(false);
		PESEL.textProperty().addListener((observable, oldValue, newValue) -> {
			invalidPESEL.set(!Validators.isPESEL(newValue));
			invalidPESELLabel.setVisible(invalidPESEL.get() && !newValue.isEmpty());
		});

		grid.add(new Label("Budget:"), 0, 3);
		grid.add(budget, 1, 3);
		grid.add(invalidBudgetLabel, 2, 3);
		invalidBudgetLabel.setVisible(false);
		budget.textProperty().addListener((observable, oldValue, newValue) -> {
			invalidBudget.set(!Validators.isNumber(newValue));
			invalidBudgetLabel.setVisible(invalidBudget.get() && !newValue.isEmpty());
		});


		bindCheckBox();
		grid.add(randomDataCheckBox, 0, 4);

		dialog.getDialogPane().setContent(grid);

		dialog.setResultConverter(dialogButton -> {
			if (dialogButton == createButtonType) {
				Investor investor = new Investor();
				investor.setBudget(new BigDecimal(budget.getText()));
				investor.setFirstName(firstName.getText());
				investor.setSecondName(secondName.getText());
				investor.setPESEL(PESEL.getText());
				return investor;
			}
			return null;
		});

		Optional<Investor> result = dialog.showAndWait();
		if (result.isPresent())
			return result;
		else
			return null;
	}

	protected void disableTextFields(boolean disable) {
		firstName.disableProperty().set(disable);
		secondName.disableProperty().set(disable);
		budget.disableProperty().set(disable);
		PESEL.disableProperty().set(disable);
	}

	private void drawInvestor() {
		String firstName = DB.getFirstNames().get(random.nextInt(DB.getFirstNames().size()));
		this.firstName.setText(firstName);

		String lastName = DB.getLastNames().get(random.nextInt(DB.getLastNames().size()));
		this.secondName.setText(lastName);

		String PESEL = Integer.toString(random.nextInt(999999999) + 100000000)
				+ Integer.toString(random.nextInt(99) + 10);
		this.PESEL.setText(PESEL);

		String budget = String.format("%.2f", random.nextDouble() * 1000000);

		this.budget.setText(budget);
	}
}
