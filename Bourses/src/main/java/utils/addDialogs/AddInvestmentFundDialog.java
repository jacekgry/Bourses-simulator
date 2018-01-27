package utils.addDialogs;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import model.InvestmentFund;
import utils.DB;
import utils.Validators;

public class AddInvestmentFundDialog extends AddDialog<InvestmentFund> {

	private TextField managerFirstName = new TextField();
	private TextField managerSecondName = new TextField();
	private TextField name = new TextField();
	private TextField budget = new TextField();

	private BooleanProperty invalidManagerFirstName = new SimpleBooleanProperty(false);
	private BooleanProperty invalidManagerSecondName = new SimpleBooleanProperty(false);
	private BooleanProperty invalidName = new SimpleBooleanProperty(false);
	private BooleanProperty invalidBudget = new SimpleBooleanProperty(false);

	private Label invalidManagerFirstNameLabel = new Label(bundle.getString("dialog.invalidFirstName"));
	private Label invalidManagerSecondNameLabel = new Label(bundle.getString("dialog.invalidSecondName"));
	private Label invalidBudgetLabel = new Label(bundle.getString("dialog.invalidBudget"));

	public Optional<InvestmentFund> addInvestmentFundDialog() {

		drawInvestmentFund();
		
		dialog.setTitle(bundle.getString("dialog.createInvestmentFund.title"));
		dialog.getDialogPane().getButtonTypes().addAll(createButtonType, ButtonType.CANCEL);

		setUpGrid(grid);

		managerFirstName.setPromptText(bundle.getString("dialog.managerFirstName"));
		managerSecondName.setPromptText(bundle.getString("dialog.managerSecondName"));
		name.setPromptText(bundle.getString("dialog.managerFirstName"));
		budget.setPromptText(bundle.getString("budget"));

		createButton = dialog.getDialogPane().lookupButton(createButtonType);
		createButton.disableProperty()
				.bind(invalidManagerFirstName.or(invalidManagerSecondName).or(invalidName).or(invalidBudget));

		grid.add(new Label("First name:"), 0, 0);
		grid.add(managerFirstName, 1, 0);
		invalidManagerFirstNameLabel.setVisible(false);
		grid.add(invalidManagerFirstNameLabel, 2, 0);
		managerFirstName.textProperty().addListener((observable, oldValue, newValue) -> {
			invalidManagerFirstName.set(!Validators.isName(newValue));
			invalidManagerFirstNameLabel.setVisible((invalidManagerFirstName.get() && !newValue.isEmpty()));

		});

		grid.add(new Label("Second name:"), 0, 1);
		grid.add(managerSecondName, 1, 1);
		invalidManagerSecondNameLabel.setVisible(false);
		grid.add(invalidManagerSecondNameLabel, 2, 1);
		managerSecondName.textProperty().addListener((observable, oldValue, newValue) -> {
			invalidManagerSecondName.set(!Validators.isName(newValue));
			invalidManagerSecondNameLabel.setVisible(invalidManagerSecondName.get() && !newValue.isEmpty());
		});

		grid.add(new Label("Name:"), 0, 2);
		grid.add(name, 1, 2);
		name.textProperty().addListener((observable, oldValue, newValue) -> {
			invalidName.set(newValue.isEmpty());
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
				InvestmentFund investmentFund = new InvestmentFund();
				investmentFund.setBudget(new BigDecimal(budget.getText()));
				investmentFund.setManagerFirstName(managerFirstName.getText());
				investmentFund.setManagerSecondName(managerSecondName.getText());
				investmentFund.setName(name.getText());
				return investmentFund;
			}
			return null;
		});

		Optional<InvestmentFund> result = dialog.showAndWait();
		if (result.isPresent())
			return result;
		else
			return null;
	}

	protected void disableTextFields(boolean disable) {
		managerFirstName.disableProperty().set(disable);
		managerSecondName.disableProperty().set(disable);
		budget.disableProperty().set(disable);
		name.disableProperty().set(disable);
	}

	private void drawInvestmentFund() {
		String managerFirstName = DB.getFirstNames().get(random.nextInt(DB.getFirstNames().size()));
		this.managerFirstName.setText(managerFirstName);

		String managerSecondName = DB.getLastNames().get(random.nextInt(DB.getLastNames().size()));
		this.managerSecondName.setText(managerSecondName);

		String name = DB.getInvestmentFundsNames().get(random.nextInt(DB.getInvestmentFundsNames().size()));
		this.name.setText(name);

		//String budget = String.format("%.2f", random.nextInt(1000000) + 1000000);
		String budget = new Integer(random.nextInt(1000000) + 1000000).toString();
		this.budget.setText(budget);
	}
}
