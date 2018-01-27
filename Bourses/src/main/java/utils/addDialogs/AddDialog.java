package utils.addDialogs;

import java.util.Random;
import java.util.ResourceBundle;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.layout.GridPane;
import utils.FxmlUtils;

public class AddDialog<T> {
	protected Random random = new Random();
	protected Dialog<T> dialog = new Dialog<>();
	protected CheckBox randomDataCheckBox = new CheckBox("I want to provide all data by myself");
	protected final ResourceBundle bundle = FxmlUtils.getResourceBundle();
	protected ButtonType createButtonType = new ButtonType(bundle.getString("dialog.create"), ButtonData.OK_DONE);
	protected Node createButton;
	protected GridPane grid = new GridPane();

	protected void setUpGrid(GridPane grid) {
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(20, 150, 10, 10));
	}
	
	protected void disableTextFields(boolean disable) {
		
	}
	
	protected void bindCheckBox() {
	randomDataCheckBox.selectedProperty().set(false);
	disableTextFields(true);
	randomDataCheckBox.selectedProperty().addListener((observable, newValue, oldValue) -> {
		if (newValue.booleanValue()) {
			disableTextFields(true);

		} else {
			disableTextFields(false);
		}
	});
	}
}
