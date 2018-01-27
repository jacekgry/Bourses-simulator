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
import model.Company;
import model.Currency;
import model.MarketIndex;
import model.StockExchange;
import utils.Validators;

public class AddMarketIndexDialog extends AddDialog<MarketIndex> {

	private BooleanProperty invalidName = new SimpleBooleanProperty(false);
	private ButtonType createButtonType = new ButtonType(bundle.getString("dialog.create"), ButtonData.OK_DONE);
	private TextField name = new TextField();
	private MenuButton companiesMenu = new MenuButton();
	private Label companiesLabel = new Label("Companies");

	public Optional<MarketIndex> addMarketIndex(StockExchange stockExchange) {

		Dialog<MarketIndex> dialog = new Dialog<>();
		dialog.setTitle("Create index");

		dialog.getDialogPane().getButtonTypes().addAll(createButtonType, ButtonType.CANCEL);

		createButton = dialog.getDialogPane().lookupButton(createButtonType);
		createButton.disableProperty().bind(invalidName);

		setUpGrid(grid);

		name.setPromptText(bundle.getString("name"));

		grid.add(new Label("Name:"), 0, 0);
		grid.add(name, 1, 0);
		Label invalidNameLabel = new Label(bundle.getString("dialog.invalidName"));
		invalidNameLabel.setVisible(false);
		grid.add(invalidNameLabel, 2, 0);
//		name.textProperty().addListener((observable, oldValue, newValue) -> {
//			invalidName.set(!Validators.isName(newValue));
//			invalidNameLabel.setVisible(invalidName.get() && !newValue.isEmpty());
//
//		});

		stockExchange.getCompanies().stream().forEach(company -> {
			CheckMenuItem checkCompany = new CheckMenuItem();
			checkCompany.setUserData(company);
			checkCompany.setText(company.getName());
			companiesMenu.getItems().add(checkCompany);
		});

		companiesMenu.setText("Companies");

		bindCheckBox();

		grid.add(companiesLabel, 0, 1);
		grid.add(companiesMenu, 1, 1);

		dialog.getDialogPane().setContent(grid);

		dialog.setResultConverter(dialogButton -> {
			if (dialogButton == createButtonType) {

				MarketIndex marketIndex = new MarketIndex();
				marketIndex.setName(name.getText());
				companiesMenu.getItems().stream().forEach(company -> {
					CheckMenuItem checkCompany = (CheckMenuItem) company;
					if (checkCompany.isSelected()) {
						marketIndex.addStock((Company) company.getUserData());
					}

				});
				return marketIndex;
			}
			return null;
		});

		Optional<MarketIndex> result = dialog.showAndWait();
		if (result.isPresent())
			return result;
		else
			return null;
	}

}
