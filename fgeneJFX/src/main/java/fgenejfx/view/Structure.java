package fgenejfx.view;

import java.util.List;

import org.controlsfx.control.HiddenSidesPane;

import fgenejfx.App;
import fgenejfx.models.enums.SideType;
import javafx.beans.property.DoubleProperty;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.ToolBar;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;

public class Structure extends BorderPane {
  
  public Structure() {
    super();
    this.setTop(topMenu());
  }
  
  private MenuBar topMenu() {
    MenuBar bar = new MenuBar();
    bar.prefWidthProperty().bind(App.stage.widthProperty());
    
    ImageView dashboardIcon = new ImageView(
        "file:src\\main\\resources\\graphics\\generally-icon.png");
    dashboardIcon.setFitHeight(50);
    dashboardIcon.setFitWidth(50);
    Menu dashboardMenu = new Menu("", dashboardIcon);
    
    bar.getMenus().add(dashboardMenu);
    return bar;
  }
  
  private Node leftMenu(List<String> list) {
    ToolBar bar = new ToolBar();
    bar.setOrientation(Orientation.VERTICAL);
    bar.setStyle("-fx-padding: 0 10 0 5;");
    
    ScrollPane scrollPane = new ScrollPane();
    scrollPane.setVbarPolicy(ScrollBarPolicy.AS_NEEDED);
    scrollPane.setHbarPolicy(ScrollBarPolicy.NEVER);
    scrollPane.setContent(bar);
    
    list.stream().forEachOrdered(s -> {
      Hyperlink h = new Hyperlink(s);
      h.setBorder(null);
      h.getStyleClass().add("hyperlink");
      bar.getItems().add(h);
    });
    
    return scrollPane;
  }
  
  public void set(Node content, SideType side) {
    HiddenSidesPane pane = new HiddenSidesPane();
    pane.setLeft(this.leftMenu(side.list()));
    pane.setContent(content);
    this.setCenter(pane);
  }

}