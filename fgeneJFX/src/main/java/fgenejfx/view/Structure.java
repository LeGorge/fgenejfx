package fgenejfx.view;

import java.util.List;

import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ToolBar;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Structure extends BorderPane {
  
  public Structure(Stage stage, List<Hyperlink> links) {
    super();
    this.setTop(topMenu(stage));
    if(links != null) {
      this.setLeft(leftMenu(stage, links));
    }
  }
  
  private MenuBar topMenu(Stage stage) {
    MenuBar bar = new MenuBar();
    bar.prefWidthProperty().bind(stage.widthProperty());
    
    ImageView dashboardIcon = new ImageView(
        "file:src\\main\\resources\\graphics\\generally-icon.png");
    dashboardIcon.setFitHeight(50);
    dashboardIcon.setFitWidth(50);
    Menu dashboardMenu = new Menu("", dashboardIcon);
    
    bar.getMenus().add(dashboardMenu);
    
    return bar;
  }
  
  private Node leftMenu(Stage stage, List<Hyperlink> list) {
    ToolBar bar = new ToolBar();
    bar.setOrientation(Orientation.VERTICAL);
    
    ScrollPane scrollPane = new ScrollPane();
    scrollPane.setVbarPolicy(ScrollBarPolicy.AS_NEEDED);
    scrollPane.prefHeightProperty().bind(stage.heightProperty());
    scrollPane.setContent(bar);
    
    list.stream().forEachOrdered(h -> {
      h.setBorder(null);
      h.getStyleClass().add("hyperlink");
      bar.getItems().add(h);
    });
    
    return scrollPane;
  }
  
  public void set(Pane pane) {
    this.setCenter(pane);
  }

}
