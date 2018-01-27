package main;

import java.util.Locale;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import model.Clock;
import model.Currency;
import model.World;
import utils.DB;
import utils.FxmlUtils;

public class Main extends Application {

	public static void main(String... args) {

		launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {


		DB.init();
		
		Clock clock = new Clock();
		new Thread(clock).start();;
				
		Currency usd = new Currency(1);
		usd.setCountries(DB.getCountries());
		usd.setName("USD");
		World.getCurrencies().add(usd);

		Locale.setDefault(new Locale("en", "EN"));
		Pane mainBorderPane = FxmlUtils.fxmlLoader("/fxml/MainBorderPane.fxml");
		Scene scene = new Scene(mainBorderPane);

		stage.setHeight(800.0);
		stage.setWidth(1200.0);
		stage.setResizable(false);
		stage.setScene(scene);
		stage.setTitle("Bourses");
		stage.show();

	}
}
