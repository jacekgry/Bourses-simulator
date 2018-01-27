package controllers;

import java.io.IOException;
import java.math.RoundingMode;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
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
import model.Investor;
import model.World;

public class InvestorsOverviewController {

	@FXML
	private InvestorInfoController investorInfoVBoxController;

	@FXML
	private TableView<Investor> investorsTable;

	@FXML
	private TableColumn<Investor, String> firstNameColumn;

	@FXML
	private TableColumn<Investor, String> secondNameColumn;

	@FXML
	private TableColumn<Investor, String> budgetColumn;

	@FXML
	private TableColumn<Investor, String> PESELColumn;

	@FXML
	private TableColumn<Investor, Investor> deleteColumn;

	@FXML
	private TableColumn<Investor, Investor> moreInfoColumn;

	@FXML
	public void initialize() {

		try {
			this.investorsTable.setItems(FXCollections.observableArrayList(World.getInvestors()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.firstNameColumn.setCellValueFactory(cellData -> {
			SimpleStringProperty firstName = new SimpleStringProperty(cellData.getValue().getFirstName());
			return firstName;
		});
		this.secondNameColumn.setCellValueFactory(cellData -> {
			SimpleStringProperty secondName = new SimpleStringProperty(cellData.getValue().getSecondName());
			return secondName;
		});
		this.PESELColumn.setCellValueFactory(cellData -> {
			SimpleStringProperty PESEL = new SimpleStringProperty(cellData.getValue().getPESEL());
			return PESEL;
		});
		this.budgetColumn.setCellValueFactory(cellData -> {
			SimpleStringProperty budget = new SimpleStringProperty(
					cellData.getValue().getBudget().setScale(2, RoundingMode.HALF_DOWN).toString());
			return budget;
		});
		this.deleteColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue()));
		this.moreInfoColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue()));

		this.deleteColumn.setCellFactory(value -> new TableCell<Investor, Investor>() {

			Button button = new Button();
			{
				button.setGraphic(
						new ImageView(new Image(this.getClass().getResource("/icons/delete.png").toString())));
			}

			@Override
			protected void updateItem(Investor inv, boolean empty) {
				super.updateItem(inv, empty);

				if (empty) {
					button.setGraphic(null);
					return;
				}
				if (!empty) {
					setGraphic(button);
					button.setOnAction(event -> {
						inv.delete();
						investorsTable.setItems(FXCollections.observableArrayList(World.getInvestors()));
						initialize();
					});
				}

			};

		});

		this.moreInfoColumn.setCellFactory(value -> new TableCell<Investor, Investor>() {

			Button button = new Button();
			{
				button.setGraphic(
						new ImageView(new Image(this.getClass().getResource("/icons/businessman.png").toString())));
			}

			protected void updateItem(Investor inv, boolean empty) {
				if (empty) {
					button.setGraphic(null);
					return;
				}
				if (!empty) {
					setGraphic(button);
					button.setOnAction(event -> {
						investorInfoDialog(inv);
					});
				}

			}

			private void investorInfoDialog(Investor inv) {
				FXMLLoader loader = utils.FxmlUtils.getLoader("/fxml/InvestorInfo.fxml");
				Scene scene = null;
				try {
					scene = new Scene(loader.load());
				} catch (IOException e) {
					e.printStackTrace();
				}
				InvestorInfoController controller = loader.getController();
				controller.init(inv);
				Stage stage = new Stage();
				stage.setScene(scene);
				stage.showAndWait();
			}

		});

	}

}
