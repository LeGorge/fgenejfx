package fgenejfx.view.engine;

import java.util.List;

import org.controlsfx.control.HiddenSidesPane;

import fgenejfx.App;
import fgenejfx.models.enums.BackgroundSelector;
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
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
    dashboardIcon.setFitHeight(25);
    dashboardIcon.setFitWidth(25);
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
      CustomHyperlink h = new CustomHyperlink(s, "myHyperlink");
      bar.getItems().add(h);
    });
    
    return scrollPane;
  }
  
  public void set(Node content, SideType side) {
    HiddenSidesPane pane = new HiddenSidesPane();
    pane.setLeft(this.leftMenu(side.list()));
    content.getStyleClass().add("backgroundtexture");
    content.getStyleClass().add(BackgroundSelector.getRandom().toString());
    
    ScrollPane scrollPane = new ScrollPane();
    scrollPane.setVbarPolicy(ScrollBarPolicy.AS_NEEDED);
    scrollPane.setHbarPolicy(ScrollBarPolicy.NEVER);
    scrollPane.setFitToWidth(true);
    scrollPane.setContent(content);
    final double SPEED = 0.003;
    scrollPane.getContent().setOnScroll(scrollEvent -> {
        double deltaY = scrollEvent.getDeltaY() * SPEED;
        scrollPane.setVvalue(scrollPane.getVvalue() - deltaY);
    });
    
    pane.getStyleClass().add("pane");
    scrollPane.getStyleClass().add("pane");
    this.getStyleClass().add("pane");
    
    pane.setContent(scrollPane);
    this.setCenter(pane);
  }

}
