package utils;

import java.math.BigDecimal;
import java.util.Optional;

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

public class RepurchaseSharesDialog {

	public static Optional<BigDecimal> repurchaseSharesDialog() {


		BooleanProperty invalidPrice = new SimpleBooleanProperty(false);

		Dialog<BigDecimal> dialog = new Dialog<>();
		dialog.setTitle("Repurchase shares");

		ButtonType okButtonType = new ButtonType("Repurchase", ButtonData.OK_DONE);
		dialog.getDialogPane().getButtonTypes().addAll(okButtonType, ButtonType.CANCEL);

		Node okButton = dialog.getDialogPane().lookupButton(okButtonType);

		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(20, 150, 10, 10));

		TextField price = new TextField();
		price.setPromptText("Price");
		
		Label invalidPriceLabel = new Label("Please provide valid price");
		invalidPriceLabel.setVisible(false);
		
		okButton.disableProperty().bind(price.textProperty().isEmpty().or(invalidPrice));

		grid.add(new Label("Price:"), 0, 0);
		grid.add(price, 1, 0);
		grid.add(invalidPriceLabel, 2, 0);
		price.textProperty().addListener((observable, oldValue, newValue) -> {
			invalidPrice.set(!Validators.isNumber(newValue));
			invalidPriceLabel.setVisible(invalidPrice.get() && !newValue.isEmpty());
		});
		


		dialog.getDialogPane().setContent(grid);

		dialog.setResultConverter(dialogButton -> {
			if (dialogButton == okButtonType) {
				return new BigDecimal(price.getText());
			}
			return null;
		});

		Optional<BigDecimal> result = dialog.showAndWait();
		if (result.isPresent())
			return result;
		else
			return null;
	}
}
