package fgenejfx.view.engine;

import java.util.Arrays;
import java.util.List;

import org.controlsfx.control.HiddenSidesPane;
import org.kordamp.ikonli.javafx.FontIcon;

import fgenejfx.App;
import fgenejfx.models.enums.BackgroundSelector;
import fgenejfx.models.enums.Front;
import fgenejfx.models.enums.SideType;
import fgenejfx.view.components.CustomHyperlink;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.BorderPane;

public class Structure extends BorderPane {
	private MenuBar bar = new MenuBar();

	public Structure() {
		super();
		bar.prefWidthProperty().bind(App.stage.widthProperty());
		this.setTop(topMenu());
	}

	private MenuBar topMenu() {
		addTopBarButton(Front.SEASON, "fa-automobile");
		addTopBarButton(Front.CHAMPS, "fa-trophy");
		addTopBarButton(Front.DRAFT, "fa-random");

		bar.getMenus().add(themesMenu());
		return bar;
	}

	private BorderPane menuPane(Label label) {
		BorderPane p = new BorderPane(label);
		p.setPrefSize(30, 25);
		return p;
	}

	private Menu menu(Node n) {
		Menu m = new Menu("", n);
		m.setStyle("-fx-padding : 0 0 0 0");
		return m;
	}

//  private Menu actionsMenu() {
//    Menu menu = new Menu("Actions");
//    MenuItem item = new MenuItem("Publish News");
//    item.setOnAction(e ->{
//      
//      App.update();
//    });
//    menu.getItems().add(item);
//    return menu;
//  }

	private void addTopBarButton(Front front, String icon) {
		var p = menuPane(new Label("",new FontIcon(icon)));
	    p.setOnMouseClicked(m -> App.navigate(front));
	    bar.getMenus().add(menu(p));
	}

	private Menu themesMenu() {
		Menu menu = new Menu("Themes");
		Arrays.stream(BackgroundSelector.values()).forEach(back -> {
			MenuItem item = new MenuItem(back.toString());
			item.setOnAction(e -> {
				App.theme = back;
				App.update();
			});
			menu.getItems().add(item);
		});
		return menu;
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
		content.getStyleClass().add(App.theme.toString());

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
