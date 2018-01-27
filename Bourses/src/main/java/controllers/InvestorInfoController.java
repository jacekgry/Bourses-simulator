package controllers;

import java.io.IOException;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.AssetAmount;
import model.Company;
import model.Currency;
import model.Investor;
import model.RawMaterial;

public class InvestorInfoController {

	@FXML
	private Label firstNameLabel;

	@FXML
	private Label secondNameLabel;

	@FXML
	private Label budgetLabel;

	@FXML
	private Label PESELLabel;

	private List<AssetAmount> currencies = new ArrayList<>();
	private List<AssetAmount> shares = new ArrayList<>();
	private List<AssetAmount> rawMaterials = new ArrayList<>();

	private Investor investor;

	private final String SHARES_FXML = "/fxml/SharesInfo.fxml";
	private final String RAW_MATERIALS_FXML = "/fxml/RawMaterialsInfo.fxml";
	private final String INVESTMENT_FUNDS_UNITS_FXML = "/fxml/UnitsOfInvestmentFundsInfo.fxml";
	private final String CURRENCIES_FXML = "/fxml/CurrenciesInfo.fxml";


	@FXML
	public void sharesButtonAction() {

		Scene scene = null;
		FXMLLoader loader = utils.FxmlUtils.getLoader(SHARES_FXML);
		try {
			scene = new Scene(loader.load());
		} catch (IOException e) {
			e.printStackTrace();
		}
		SharesInfoController sharesInfoController = loader.getController();

		sharesInfoController.bindings(investor.getFirstName()+ " " + investor.getSecondName(), shares);

		Stage stage = new Stage();
		stage.setScene(scene);
		stage.showAndWait();

	}

	@FXML
	public void investmentFundsButtonAction() {

		Scene scene = null;
		FXMLLoader loader = utils.FxmlUtils.getLoader(INVESTMENT_FUNDS_UNITS_FXML);
		try {
			scene = new Scene(loader.load());
		} catch (IOException e) {
			e.printStackTrace();
		}
		UnitsOfInvestmentmentFundsInfoController unitsOfInvestmentmentFundsInfoController = loader.getController();

		unitsOfInvestmentmentFundsInfoController.bindings(investor);

		Stage stage = new Stage();
		stage.setScene(scene);
		stage.showAndWait();
		
	}

	@FXML
	public void rawMaterialsButtonAction() {

		Scene scene = null;
		FXMLLoader loader = utils.FxmlUtils.getLoader(RAW_MATERIALS_FXML);
		try {
			scene = new Scene(loader.load());
		} catch (IOException e) {
			e.printStackTrace();
		}
		RawMaterialsInfoController rawMaterialsInfoController = loader.getController();

		rawMaterialsInfoController.bindings(investor.getFirstName() + " " + investor.getSecondName(), rawMaterials);

		Stage stage = new Stage();
		stage.setScene(scene);
		stage.showAndWait();
	}

	@FXML
	public void currenciesButtonAction() {

		Scene scene = null;
		FXMLLoader loader = utils.FxmlUtils.getLoader(CURRENCIES_FXML);
		try {
			scene = new Scene(loader.load());
		} catch (IOException e) {
			e.printStackTrace();
		}
		CurrenciesInfoController currenciesInfoController = loader.getController();

		currenciesInfoController.bindings(investor.getFirstName() + " " + investor.getSecondName(), currencies);

		Stage stage = new Stage();
		stage.setScene(scene);
		stage.showAndWait();
	}

	public void setFirstNameLabel(String firstName) {
		firstNameLabel.setText(firstName);
	}

	public void init(Investor inv) {
		this.investor = inv;
		this.firstNameLabel.setText(inv.getFirstName());
		this.secondNameLabel.setText(inv.getSecondName());
		this.PESELLabel.setText(inv.getPESEL());
		this.budgetLabel.setText(inv.getBudget().setScale(3, RoundingMode.HALF_DOWN).toString());

		inv.getAssets().stream().forEach(asset -> {
			if (asset.getAsset() instanceof Company)
				this.shares.add(asset);
			else if (asset.getAsset() instanceof Currency)
				this.currencies.add(asset);
			else if (asset.getAsset() instanceof RawMaterial)
				this.rawMaterials.add(asset);
		});
	}

}
