package controllers;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ComboBox;
import model.Asset;
import model.World;

public class ChartController {

	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm", Locale.US);

	@FXML
	private LineChart<String, BigDecimal> chart;

	@FXML
	private ComboBox<Periods> periodsComboBox;

	@FXML
	private List<? extends Asset> selectedAssets;

	private ChartType chartType = ChartType.USD;
	private LocalDateTime date = null;

	private enum Periods {
		DAY, WEEK, MONTH, YEAR, ALL
	}

	private enum ChartType {
		USD, PERCENTAGE
	}

	public void init(List<? extends Asset> selectedAssets) {

		this.selectedAssets = selectedAssets;
		periodsComboBox.getItems().addAll(Periods.values());

		periodsComboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue == Periods.ALL) {
				date = null;
				draw();
			}

			else if (newValue == Periods.YEAR) {
				date = World.getDate().minusYears(1);
				draw();
			}

			else if (newValue == Periods.MONTH) {
				date = World.getDate().minusMonths(1);
				draw();
			}

			else if (newValue == Periods.WEEK) {
				date = World.getDate().minusDays(7);
				draw();

			}

			else if (newValue == Periods.DAY) {
				date = World.getDate().minusDays(1);
				draw();
			}
		});

		periodsComboBox.getSelectionModel().select(Periods.ALL);

	}

	public void draw() {

		chart.getData().clear();
		for (Asset asset : selectedAssets) {

			XYChart.Series<String, BigDecimal> series = new XYChart.Series<>();
			List<Entry<LocalDateTime, BigDecimal>> historyInPeriod;

			if (date != null)
				historyInPeriod = asset.getHistory().entrySet().stream().filter(record -> {
					return record.getKey().isAfter(date);
				}).collect(Collectors.toList());
			else
				historyInPeriod = asset.getHistory().entrySet().stream().collect(Collectors.toList());

			if (chartType == ChartType.USD) {
				for (Entry<LocalDateTime, BigDecimal> record : historyInPeriod) {
					series.getData()
							.add(new XYChart.Data<String, BigDecimal>(
									DateTimeFormatter.ofPattern("yyyy-MM-dd HH:MM")
											.format(LocalDateTime.parse(record.getKey().toString(), formatter)),
									record.getValue()));
				}
				chart.getYAxis().setLabel("USD");

			} else if (chartType == ChartType.PERCENTAGE) {
				for (Entry<LocalDateTime, BigDecimal> record : historyInPeriod) {
					series.getData().add(new XYChart.Data<String, BigDecimal>(
							DateTimeFormatter.ofPattern("yyyy-MM-dd HH:MM")
									.format(LocalDateTime.parse(record.getKey().toString(), formatter)),
							new BigDecimal(100).multiply(
									record.getValue().divide(asset.getOpeningPrice(), 3).subtract(BigDecimal.ONE))));
				}
				chart.getYAxis().setLabel("%");

			}
			series.setName(asset.getName());
			chart.getData().add(series);
		}

	}

	@FXML
	public void usdChart() {
		chartType = ChartType.USD;
		draw();
	}

	@FXML
	public void percentageChart() {
		chartType = ChartType.PERCENTAGE;
		draw();
	}

}
