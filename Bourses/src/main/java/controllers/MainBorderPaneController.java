package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import utils.FxmlUtils;

public class MainBorderPaneController {

	private final String CREATE_MENU = "/fxml/CreateMenu.fxml";
	private final String INFORMATION_PANEL = "/fxml/InformationPanel.fxml";

	@FXML
	private BorderPane mainBorderPane;

	@FXML
	private ToggleGroup mainMenu;

	@FXML
	private ToggleButton createButton;

	@FXML
	private ToggleButton informationButton;

	@FXML
	private MenuItem close;

	@FXML
	public void initialize() {
		mainBorderPane.setCenter(FxmlUtils.fxmlLoader(CREATE_MENU));
	}

	@FXML
	public void closeAction() {
		javafx.application.Platform.exit();
	}

	@FXML
	public void createButtonAction(ActionEvent event) {
		mainBorderPane.setCenter(null);
		mainBorderPane.setCenter(FxmlUtils.fxmlLoader(CREATE_MENU));
	}

	@FXML
	public void informationButtonAction(ActionEvent event) {
		mainBorderPane.setCenter(FxmlUtils.fxmlLoader(INFORMATION_PANEL));
	}

	@FXML
	public void save() {
		utils.Serialization.saveStateOfSimulation();
	}

	@FXML
	public void load() {
		utils.Serialization.loadStateOfSimulation();
	}
}
