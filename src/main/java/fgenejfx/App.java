package fgenejfx;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import fgenejfx.controllers.League;
import fgenejfx.controllers.PersistanceController;
import fgenejfx.models.enums.BackgroundSelector;
import fgenejfx.models.enums.Front;
import fgenejfx.models.enums.SideType;
import fgenejfx.models.enums.State;
import fgenejfx.utils.Utils;
import fgenejfx.view.AllPilotsView;
import fgenejfx.view.AllTeamsView;
import fgenejfx.view.ChampionsView;
import fgenejfx.view.DraftView;
import fgenejfx.view.SeasonView;
import fgenejfx.view.engine.Structure;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class App extends Application {
	public static Stage stage;

	private static Structure view;

	public static Map<Front, Object> currentPage;
	public static BackgroundSelector theme;
	public static Map<String, Image> graphics = new HashMap<>();

	public static Boolean prod = true;

	@Override
	public void start(Stage primaryStage) throws Exception {
		Boolean ok = PersistanceController.load();
		if (!ok) {
			League.get();
			Utils.begin();
		}
		App.theme = BackgroundSelector.getRandom();
		App.stage = primaryStage;

		App.view = new Structure();
//    App.view.set(new SeasonView(1), SideType.TEAMSIDE);
		App.navigate(Front.SEASON);

		Scene scene = new Scene(App.view);

		scene.getStylesheets().add(getClass().getResource("/css/text.css").toString());
		scene.getStylesheets().add(getClass().getResource("/css/background.css").toString());
		
		App.graphics.put("goldMedal", new Image(getClass().getResourceAsStream("/graphics/icons/medal1.png")));
		App.graphics.put("silverMedal", new Image(getClass().getResourceAsStream("/graphics/icons/medal2.png")));
		App.graphics.put("bronzeMedal", new Image(getClass().getResourceAsStream("/graphics/icons/medal3.png")));
		
		primaryStage.setScene(scene);
		primaryStage.setMaximized(true);
		primaryStage.show();

		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			public void handle(WindowEvent we) {
				Alert alert = new Alert(AlertType.CONFIRMATION);
				alert.setTitle("Exit");
				alert.setHeaderText("Would you like to save any changes?");
//        alert.setContentText("Choose your option.");

				ButtonType buttonTypeOne = new ButtonType("Yes");
				ButtonType buttonTypeTwo = new ButtonType("No");
				ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);

				alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeTwo, buttonTypeCancel);

				Optional<ButtonType> result = alert.showAndWait();
				if (result.get() == buttonTypeOne) {
					PersistanceController.save();
				} else if (result.get() == buttonTypeTwo) {
				} else {
					we.consume();
				}
			}
		});
	}

	// ============================================================================================
	// Navigation
	// ============================================================================================
	public static void update() {
		navigate(currentPage);
	}

	public static void navigate(Front to) {
		navigate(to, null);
	}

	public static void navigate(Front to, Object o) {
		Map<Front, Object> map = Collections.singletonMap(to, o);
		navigate(map);
	}

	public static void navigate(Map<Front, Object> to) {
		if (!to.isEmpty()) {
			App.currentPage = to;
			Front view = to.keySet().iterator().next();
			switch (view) {
			case SEASON:
				Integer param = (Integer) to.get(Front.SEASON);
				if (param != null) {
					if(League.get().season(param).getState().equals(State.INDRAFT)) {
						App.navigate(Front.DRAFT);
						return;
					}
					App.view.set(new SeasonView(param), SideType.TEAMSIDE);
				} else {
					App.view.set(new SeasonView(League.get().getYear()), SideType.TEAMSIDE);
				}
				break;

			case CHAMPS:
				App.view.set(new ChampionsView(), SideType.TEAMSIDE);
				break;
			case ALL_TEAMS:
				App.view.set(new AllTeamsView(), SideType.TEAMSIDE);
				break;
			case ALL_PILOTS:
				App.view.set(new AllPilotsView(), SideType.TEAMSIDE);
				break;
			case DRAFT:
				Integer paramDraft = (Integer) to.get(Front.DRAFT);
				if (paramDraft != null) {
					App.view.set(new DraftView(paramDraft), SideType.TEAMSIDE);
				} else {
					App.view.set(new DraftView(League.get().getYear()), SideType.TEAMSIDE);
				}
				break;
			default:
				break;
			}
		}
	}

	// Run the launcher
	public static void main(String[] args) {
		launch(args);
	}
}