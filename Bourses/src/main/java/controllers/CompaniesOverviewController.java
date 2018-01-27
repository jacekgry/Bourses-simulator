package controllers;

import java.io.IOException;
import java.math.BigDecimal;

import exceptions.NotEnoughMoneyException;
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
import model.Company;
import model.World;
import utils.ErrorDialog;
import utils.RepurchaseSharesDialog;

public class CompaniesOverviewController {

	@FXML
	private TableView<Company> companiesTable;

	@FXML
	private TableColumn<Company, String> nameColumn;

	@FXML
	private TableColumn<Company, Company> deleteColumn;

	@FXML
	private TableColumn<Company, Company> repurchaseSharesColumn;

	@FXML
	private TableColumn<Company, Company> moreColumn;

	@FXML
	public void initialize() {

		System.out.println(World.getInvestors());
		try {
			this.companiesTable.setItems(FXCollections.observableArrayList(World.getCompanies()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.nameColumn.setCellValueFactory(cellData -> {
			SimpleStringProperty name = new SimpleStringProperty(cellData.getValue().getName());
			return name;
		});

		this.deleteColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue()));
		this.deleteColumn.setCellFactory(value -> new TableCell<Company, Company>() {

			Button button = new Button();
			{
				button.setGraphic(
						new ImageView(new Image(this.getClass().getResource("/icons/delete.png").toString())));
			}

			@Override
			protected void updateItem(Company company, boolean empty) {
				super.updateItem(company, empty);

				if (empty) {
					button.setGraphic(null);
					return;
				}
				if (!empty) {
					setGraphic(button);
					button.setOnAction(event -> {
						company.delete();
						companiesTable.setItems(FXCollections.observableArrayList(World.getCompanies()));
						initialize();
					});
				}

			};

		});

		this.repurchaseSharesColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue()));
		this.repurchaseSharesColumn.setCellFactory(value -> new TableCell<Company, Company>() {

			Button button = new Button();
			{
				button.setGraphic(
						new ImageView(new Image(this.getClass().getResource("/icons/repurchase.png").toString())));
			}

			protected void updateItem(Company company, boolean empty) {
				if (empty) {
					button.setGraphic(null);
					return;
				}
				if (!empty) {

					setGraphic(button);
					button.setOnAction(event -> {

						BigDecimal price = null;
						try {
							price = RepurchaseSharesDialog.repurchaseSharesDialog().get();
						} catch (Exception e) {

						}
						if (price != null) {
							System.out.println(price);
							try {
								company.repurchaseShares(price);
							} catch (NotEnoughMoneyException e) {
								ErrorDialog.errorDialog(
										"Company does not have enough money to repurchase shares for this price!");
							}
						}
					});
				}

			}

		});

	}

}
