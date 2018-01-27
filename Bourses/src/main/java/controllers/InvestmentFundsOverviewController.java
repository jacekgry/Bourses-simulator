package controllers;

import java.io.IOException;
import java.math.RoundingMode;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import model.InvestmentFund;
import model.Investor;
import model.World;

public class InvestmentFundsOverviewController {

	@FXML
	TableView<InvestmentFund> investmentFundsTable;

	@FXML
	TableColumn<InvestmentFund, String> nameColumn;

	@FXML
	TableColumn<InvestmentFund, String> managerFirstNameColumn;

	@FXML
	TableColumn<InvestmentFund, String> managerSecondNameColumn;

	@FXML
	TableColumn<InvestmentFund, String> budgetColumn;

	@FXML
	TableColumn<InvestmentFund, String> valueOfUnitColumn;

	@FXML
	TableColumn<InvestmentFund, InvestmentFund> deleteColumn;

	@FXML
	TableColumn<InvestmentFund, InvestmentFund> moreInfoColumn;

	@FXML
	public void initialize() {

		System.out.println(World.getInvestmentFunds());
		this.investmentFundsTable.setItems(FXCollections.observableArrayList(World.getInvestmentFunds()));

		this.nameColumn.setCellValueFactory(cellData -> {
			StringProperty name = new SimpleStringProperty(cellData.getValue().getName());
			return name;
		});

		this.managerFirstNameColumn.setCellValueFactory(cellData -> {
			StringProperty firstName = new SimpleStringProperty(cellData.getValue().getManagerFirstName());
			return firstName;
		});

		this.managerSecondNameColumn.setCellValueFactory(cellData -> {
			StringProperty secondName = new SimpleStringProperty(cellData.getValue().getManagerSecondName());
			return secondName;
		});

		this.budgetColumn.setCellValueFactory(cellData -> {
			StringProperty budget = new SimpleStringProperty(
					cellData.getValue().getBudget().setScale(2, RoundingMode.HALF_DOWN).toString());
			return budget;
		});

		this.valueOfUnitColumn.setCellValueFactory(cellData -> {
			StringProperty value = new SimpleStringProperty(cellData.getValue().getValueOfUnit().toString());
			return value;
		});

		this.deleteColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue()));
		this.deleteColumn.setCellFactory(value -> new TableCell<InvestmentFund, InvestmentFund>() {

			Button button = new Button();
			{
				button.setGraphic(
						new ImageView(new Image(this.getClass().getResource("/icons/delete.png").toString())));
			}

			@Override
			protected void updateItem(InvestmentFund inv, boolean empty) {
				super.updateItem(inv, empty);

				if (empty) {
					setGraphic(null);
					return;

				}

				if (!empty) {
					setGraphic(button);
					System.out.println("not empty" + inv);
					button.setOnAction(event -> {
						inv.delete();
						investmentFundsTable.setItems(FXCollections.observableArrayList(World.getInvestmentFunds()));
					});

				}
			}

		});

		this.moreInfoColumn
				.setCellValueFactory(cellData -> new SimpleObjectProperty<InvestmentFund>(cellData.getValue()));
		this.moreInfoColumn.setCellFactory(value -> new TableCell<InvestmentFund, InvestmentFund>() {

			Button button = new Button();
			{
				button.setGraphic(new ImageView(new Image(this.getClass().getResource("/icons/info.png").toString())));
			}

			@Override
			protected void updateItem(InvestmentFund inv, boolean empty) {
				super.updateItem(inv, empty);

				if (empty) {
					setGraphic(null);
					return;

				}

				if (!empty) {
					setGraphic(button);
					button.setOnAction(event -> {
						investmentFundInfoDialog(inv);

					});

				}
			}

		});

	}

	private void investmentFundInfoDialog(InvestmentFund inv) {
		FXMLLoader loader = utils.FxmlUtils.getLoader("/fxml/InvestmentFundInfo.fxml");
		Scene scene = null;
		try {
			scene = new Scene(loader.load());
		} catch (IOException e) {
			e.printStackTrace();
		}
		InvestmentFundInfoController controller = loader.getController();
		controller.init(inv);
		Stage stage = new Stage();
		stage.setScene(scene);
		stage.showAndWait();
	}
}
