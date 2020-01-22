package fgenejfx;

import java.util.Arrays;

import fgenejfx.view.SeasonView;
import fgenejfx.view.Structure;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Hyperlink;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class App extends Application {
  private static Structure view;

  @Override
  public void start(Stage primaryStage) throws Exception {
    
    App.view = new Structure(primaryStage, Arrays.asList(
        new Hyperlink("test-one"),
        new Hyperlink("test-two"),
        new Hyperlink("test-three")));
    App.view.set(new SeasonView());
    Scene scene = new Scene(App.view);
    
    scene.getStylesheets().add(getClass().getResource("/css/text.css").toString());
    primaryStage.setScene(scene);
    primaryStage.setMaximized(true);
    primaryStage.show();
  }
  
  public static void navigate(Pane to) {
    App.view.set(null);
  }
  
  public static void main(String[] args) {
    Application.launch(args);
  }
}