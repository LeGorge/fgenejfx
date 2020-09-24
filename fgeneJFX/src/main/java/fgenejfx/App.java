package fgenejfx;

import java.util.Collections;
import java.util.Map;

import fgenejfx.controllers.League;
import fgenejfx.models.enums.BackgroundSelector;
import fgenejfx.models.enums.Front;
import fgenejfx.models.enums.SideType;
import fgenejfx.utils.Utils;
import fgenejfx.view.SeasonView;
import fgenejfx.view.engine.Structure;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {
  public static Stage stage;
  
  private static Structure view;
  
  public static Map<Front,Object> currentPage;
  public static BackgroundSelector theme;

  @Override
  public void start(Stage primaryStage) throws Exception {
    League.get();
    Utils.begin();
    App.theme = BackgroundSelector.getRandom();
    App.stage = primaryStage;
    
    App.view = new Structure();
//    App.view.set(new SeasonView(1), SideType.TEAMSIDE);
    App.navigate(Front.SEASON);
    
    Scene scene = new Scene(App.view);
    
    scene.getStylesheets().add(getClass().getResource("/css/text.css").toString());
    scene.getStylesheets().add(getClass().getResource("/css/background.css").toString());
    primaryStage.setScene(scene);
    primaryStage.setMaximized(true);
    primaryStage.show();
  }
  
  //============================================================================================
 // Navigation
 // ============================================================================================
  public static void navigate() {
    navigate(currentPage);
  }
  public static void navigate(Front to) {
    navigate(to, null);
  }
  public static void navigate(Front to, Object o) {
    Map<Front, Object> map = Collections.singletonMap(to, o);
    navigate(map);
  }
//  public static void navigate(Front view, year) {
  public static void navigate(Map<Front, Object> to) {
    if(!to.isEmpty()) {
      Front view = to.keySet().iterator().next();
      switch (view) {
      case SEASON:
        App.currentPage = to;
        Integer param = (Integer)to.get(Front.SEASON);
        if(param != null) {
          App.view.set(new SeasonView(param), SideType.TEAMSIDE);
        }else {
          App.view.set(new SeasonView(League.get().getYear()), SideType.TEAMSIDE);
        }
        break;
        
      default:
        break;
      }
    }
  }
  
  public static void main(String[] args) {
    Application.launch(args);
  }
}