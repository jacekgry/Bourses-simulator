package controllers;

import java.io.IOException;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import model.AssetAmount;
import model.Company;
import model.Currency;
import model.InvestmentFund;
import model.RawMaterial;

public class InvestmentFundInfoController {

	private final String INVESTMENT_FUNDS_UNITS_FXML = "/fxml/OwnersOfUnits.fxml";
	private final String SHARES_FXML = "/fxml/SharesInfo.fxml";
	private final String RAW_MATERIALS_FXML = "/fxml/RawMaterialsInfo.fxml";
	private final String CURRENCIES_FXML = "/fxml/CurrenciesInfo.fxml";
	@FXML
	private Label nameLabel;
	
	@FXML
	private Label firstNameLabel;
	
	@FXML
	private Label secondNameLabel;
	
	@FXML
	private Label budgetLabel;
	
	@FXML
	private Button sharesButton;

	@FXML
	private Button investorButton;
	
	@FXML
	private Button currenciesButton;

	@FXML
	private Button rawMaterialsButton;

	@FXML
	private Button investmentFundsButton;
	
	private InvestmentFund investmentFund;
	
	private List<AssetAmount> currencies = new ArrayList<>();
	private List<AssetAmount> shares = new ArrayList<>();
	private List<AssetAmount> rawMaterials = new ArrayList<>();
	

	
	public void init(InvestmentFund inv) {
		this.investmentFund = inv;
		this.firstNameLabel.setText(inv.getManagerFirstName());
		this.secondNameLabel.setText(inv.getManagerSecondName());
		this.nameLabel.setText(inv.getName());
		this.budgetLabel.setText(inv.getBudget().setScale(2, RoundingMode.HALF_DOWN).toString());

		inv.getAssets().stream().forEach(asset -> {
			if (asset.getAsset() instanceof Company)
				this.shares.add(asset);
			else if (asset.getAsset() instanceof Currency)
				this.currencies.add(asset);
			else if (asset.getAsset() instanceof RawMaterial)
				this.rawMaterials.add(asset);
		});
	
	
		
		
	}

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

		sharesInfoController.bindings(investmentFund.getName(), shares);

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

		rawMaterialsInfoController.bindings(investmentFund.getName(), rawMaterials);

		Stage stage = new Stage();
		stage.setScene(scene);
		stage.showAndWait();
	}
	
	@FXML
	public void investorsButtonAction() {
		Scene scene = null;
		FXMLLoader loader = utils.FxmlUtils.getLoader(INVESTMENT_FUNDS_UNITS_FXML);
		try {
			scene = new Scene(loader.load());
		} catch (IOException e) {
			e.printStackTrace();
		}
		OwnersOfUnitsInfoController ownersOfUnitsInfoController = loader.getController();

		ownersOfUnitsInfoController.bindings(investmentFund);

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

		currenciesInfoController.bindings(investmentFund.getName(), currencies);

		Stage stage = new Stage();
		stage.setScene(scene);
		stage.showAndWait();
	}
	

}
