package controllers;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import model.Company;

public class CompanyInfoController {

	@FXML
	private Label nameLabel;

	@FXML
	private Label stockExchangeLabel;

	@FXML
	private Label firstValuationDateLabel;

	@FXML
	private Label openingPriceLabel;

	@FXML
	private Label currentPriceLabel;

	@FXML
	private Label minimumPriceLabel;

	@FXML
	private Label maximumPriceLabel;

	@FXML
	private Label revenueLabel;

	@FXML
	private Label profitLabel;

	@FXML
	private Label equityCapitalLabel;

	@FXML
	private Label shareCapitalLabel;

	@FXML
	private Label volumeLabel;

	@FXML
	private Label turnoverLabel;

	@FXML
	private Label numberOfSharesLabel;

	public void init(Company company) {

		nameLabel.setText(company.getName());
		stockExchangeLabel.setText(company.getStockExchange().getName());
		firstValuationDateLabel.setText(company.getFirstValuationDate().toString());
		openingPriceLabel.setText(company.getOpeningPrice().toString());
		currentPriceLabel.setText(company.getCurrentPrice().toString());
		minimumPriceLabel.setText(company.getMinimumPrice().toString());
		maximumPriceLabel.setText(company.getMaximumPrice().toString());
		revenueLabel.setText(company.getRevenue().toString());
		profitLabel.setText(company.getProfit().toString());
		equityCapitalLabel.setText(company.getEquityCapital().toString());
		shareCapitalLabel.setText(company.getShareCapital().toString());
		volumeLabel.setText(company.getVolume().toString());
		turnoverLabel.setText(company.getTurnover().toString());
		numberOfSharesLabel.setText(company.getNumberOfShares().toString());
	}

}
