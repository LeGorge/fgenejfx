package fgenejfx.view.components.tableViews;

import fgenejfx.dtos.CardDto;
import fgenejfx.dtos.ChampionsDto;
import fgenejfx.models.enums.LeagueTime;
import fgenejfx.view.components.CustomTableView;
import fgenejfx.view.components.cards.ChampionsCard;
import fgenejfx.view.engine.ViewUtils;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;

public class ChampsTableView extends CustomTableView<ChampionsDto> {

	public ChampsTableView(int year) {
		super(year);
	}
	
	public ChampsTableView addYearColumn() {
		TableColumn<ChampionsDto, Number> yearCol = new TableColumn<>("Year");
		ViewUtils.tooltip(yearCol);
		yearCol.setStyle("-fx-alignment: CENTER;");
		yearCol.setCellValueFactory(new PropertyValueFactory<>("year"));
		this.getColumns().add(yearCol);
		return this;
	}

	public ChampsTableView addCardColumn(String colTitle, String field, LeagueTime time) {
		TableColumn<ChampionsDto, CardDto> cardCol = new TableColumn<>(colTitle);
		ViewUtils.tooltip(cardCol);
		cardCol.setCellValueFactory(new PropertyValueFactory<>(field));
		cardCol.setCellFactory(col -> new TableCell<ChampionsDto, CardDto>() {
			@Override
			protected void updateItem(CardDto item, boolean empty) {
				super.updateItem(item, empty);
				if (empty) {
					setGraphic(null);
				} else {
					setGraphic(new ChampionsCard(item, time));
				}
			}
		});
		this.getColumns().add(cardCol);
		return this;
	}

}
