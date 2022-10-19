package fgenejfx.view.components;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

import fgenejfx.controllers.ContractsController;
import fgenejfx.controllers.League;
import fgenejfx.models.Powers;
import fgenejfx.models.Team;
import fgenejfx.models.enums.LeagueTime;
import fgenejfx.models.enums.MethodSelector;
import fgenejfx.utils.Utils;
import fgenejfx.view.engine.Callbacks;
import fgenejfx.view.engine.MethodMapper;
import fgenejfx.view.engine.ViewUtils;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.SortType;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;

public class CustomTableView<A> extends TableView<A> {

	private int year;

	public CustomTableView(int year) {
		this.year = year;
		setColumnResizePolicy(CONSTRAINED_RESIZE_POLICY);
	}

	// ============================================================================================
	// Row
	// ============================================================================================
	public void colorRow(Map<Integer, String> map) {
		this.setRowFactory(table -> {
			TableRow<A> row = new TableRow<A>() {
				@Override
				protected void updateItem(A item, boolean empty) {
					super.updateItem(item, empty);
					for (Integer i : map.keySet()) {
						if (getIndex() == i) {
							setStyle("-fx-background-color: " + map.get(i) + ";");
//              System.out.println(getIndex() + " - "+ getStyle());
						}
					}
				}
			};
//      row.setStyle("-fx-background-color: red;");

			return row;
		});
	}

	// ============================================================================================
	// Info
	// ============================================================================================
	public CustomTableView<A> addNameColumn() {
		return addNameColumn("Name", "name");
	}

	public CustomTableView<A> addNameColumn(String colTitle, String field) {
		TableColumn<A, String> nameCol = getSimpleCol(colTitle, colTitle, field);
		nameCol.setMinWidth(70);
		nameCol.setCellFactory(col -> new TableCell<A, String>() {
			@Override
			protected void updateItem(String item, boolean empty) {
				super.updateItem(item, empty);
				if (empty) {
					setGraphic(null);
				} else {
					setGraphic(new CustomHyperlink(item, "name"));
				}
			}
		});
		this.getColumns().add(nameCol);
		return this;
	}

	public CustomTableView<A> addTeamColumn() {
		TableColumn<A, Team> teamCol = new TableColumn<>("Team");
		ViewUtils.tooltip(teamCol);
		teamCol.setMinWidth(70);
		teamCol.setCellValueFactory(
				new Callbacks<A, Team>().stringCol(League.get(), MethodMapper.teamOf(year), null)
			);
		teamCol.setCellFactory(col -> new TableCell<A, Team>() {
			@Override
			protected void updateItem(Team item, boolean empty) {
				super.updateItem(item, empty);
				if (empty) {
					setGraphic(null);
				} else {
					setGraphic(new CustomHyperlink(item.getName(), "name"));
				}
			}
		});
		this.getColumns().add(teamCol);
		return this;
	}

	public CustomTableView<A> addAgeColumn() {
		TableColumn<A, Number> ageCol = new TableColumn<>("Age");
		ViewUtils.tooltip(ageCol);
//    ageCol.setCellValueFactory(new PropertyValueFactory<>("yearsInTheLeague"));
		ageCol.setCellValueFactory(
				new Callbacks<A, Number>().stringCol(null, MethodMapper.age(year), null)
			);
		ageCol.setSortType(SortType.DESCENDING);
		this.getColumns().add(ageCol);
		return this;
	}

	public CustomTableView<A> addGroupColumn() {
		TableColumn<A, Number> groupCol = new TableColumn<>("Group");
		ViewUtils.tooltip(groupCol);
		groupCol.setCellValueFactory(
				new Callbacks<A, Number>().stringCol(League.get().season(year), MethodMapper.group(), null)
			);
		groupCol.setSortType(SortType.DESCENDING);
		this.getColumns().add(groupCol);
		return this;
	}

	// ============================================================================================
	// By Season Columns
	// ============================================================================================
	public CustomTableView<A> addTopPosColumn() {
		TableColumn<A, String> col = new TableColumn<>("#");
		ViewUtils.tooltip(col, "Position last season");
		col.setMinWidth(25);
		col.setCellValueFactory(
				new Callbacks<A, String>().stringCol(League.get(), MethodMapper.posOf(year - 1), null)
			);
		col.setCellFactory(c -> new TableCell<A, String>() {
			@Override
			protected void updateItem(String item, boolean empty) {
				super.updateItem(item, empty);
				if (!empty && item != "-") {
					Integer itemValue = Integer.parseInt(item.substring(4));
					if (itemValue <= 6) {
						this.setTextFill(Color.RED);
					}
					if (itemValue == 12) {
						this.setTextFill(Color.ORANGE);
					}
					if (itemValue > 12) {
						this.setTextFill(Color.LIGHTGRAY);
					}
				}
				setText(item);
			}
		});
		this.getColumns().add(col);
		return this;
	}

	public CustomTableView<A> addScoutReportColumn() {
		TableColumn<A, String> col = new TableColumn<>("Scout Report");
		ViewUtils.tooltip(col);
		col.setCellValueFactory(
				new Callbacks<A, String>().stringCol(League.get(), MethodMapper.scoutReportOf(year), null)
			);
		col.setCellFactory(c -> new TableCell<A, String>() {
			@Override
			protected void updateItem(String item, boolean empty) {
				super.updateItem(item, empty);
				if (!empty && item != "-") {
					if (item.equals("||")) {
						this.setTextFill(Color.GRAY);
						this.setFont(Font.font("Cooper Black", FontWeight.BOLD, 11));
					} else {
						this.setTextFill(Color.GREEN);
						this.setFont(Font.font("Cooper Black", FontWeight.BOLD, 14));
					}
				}
				setText(item);
			}
		});
		this.getColumns().add(col);
		return this;
	}

	public CustomTableView<A> addBySeasonStatColumn(LeagueTime time, MethodSelector sel) {
		TableColumn<A, Number> col = new TableColumn<>(sel.getName());
		ViewUtils.tooltip(col, sel.getTip());
		col.setCellValueFactory(new Callbacks<A, Number>().stringCol(League.get(),
				MethodMapper.bySeasonStat(year, time, sel), null));
		col.setSortType(SortType.DESCENDING);
		this.getColumns().add(col);
		return this;
	}
	
	public CustomTableView<A> addHiddenContractColumn() {
		TableColumn<A, Number> col = new TableColumn<>("");
		col.setVisible(false);
		col.setCellValueFactory(new Callbacks<A, Number>().stringCol(ContractsController.get(),
				MethodMapper.contractPosition(), null));
		col.setSortType(SortType.ASCENDING);
		this.getColumns().add(col);
		return this;
	}

	public CustomTableView<A> addPowersColumn(Powers p) {
		TableColumn<A, Number> col;
		if (p != null) {
			col = new TableColumn<>(p.toString());
		} else {
			col = new TableColumn<>("Grade");
		}
		ViewUtils.tooltip(col);
		col.setCellValueFactory(
				new Callbacks<A, Number>().stringCol(League.get(), MethodMapper.bySeasonPowerRelative(year, p), null));
		col.setCellFactory(c -> new TableCell<A, Number>() {
			@Override
			protected void updateItem(Number item, boolean empty) {
				super.updateItem(item, empty);
				if (!empty) {
					if (p == null) {
						setText(item.toString());
						return;
					}
					if (item.intValue() == 0) {
						this.setTextFill(Color.GRAY);
						this.setFont(Font.font("Cooper Black", FontWeight.BOLD, 11));
					} else {
						if (item.intValue() > 0) {
							this.setTextFill(Color.GREEN);
							this.setFont(Font.font("Cooper Black", FontWeight.BOLD, 14));
						} else {
							this.setTextFill(Color.RED);
							this.setFont(Font.font("Cooper Black", FontWeight.BOLD, 14));
						}
					}
					setText(Utils.powerValueToStr(item.intValue()));
				}
			}
		});
		col.setSortType(SortType.DESCENDING);
		this.getColumns().add(col);
		return this;
	}

	// ============================================================================================
	// Life Columns
	// ============================================================================================
	public CustomTableView<A> addlifeStatColumn(LeagueTime time, MethodSelector sel) {
		TableColumn<A, Number> col = new TableColumn<>(sel.getName());
		ViewUtils.tooltip(col, sel.getTip());
		col.setCellValueFactory(
				new Callbacks<A, Number>().stringCol(null, MethodMapper.lifeStat(year, time, sel), Utils.onePlaceFormat));
		col.setSortType(SortType.DESCENDING);
		this.getColumns().add(col);
		return this;
	}

	public CustomTableView<A> addPerRoundColumn(LeagueTime time, MethodSelector sel) {
		TableColumn<A, Number> col = new TableColumn<>(sel.getName());
		ViewUtils.tooltip(col, sel.getTip());
		col.setCellValueFactory(new Callbacks<A, Number>().stringCol(null,
				MethodMapper.perRoundStat(time, sel), Utils.integerFormat));

		col.setSortType(SortType.DESCENDING);
		this.getColumns().add(col);
		return this;
	}

	// ============================================================================================
	// Engine
	// ============================================================================================
	public CustomTableView<A> addHiddenColumn(String field) {
		return addSimpleColumn(field, field, field, false, null, null);
	}
	public CustomTableView<A> addSimpleColumn(String colTitle, String field) {
		return addSimpleColumn(colTitle, colTitle, field, true, null, null);
	}
	public CustomTableView<A> addSmallColumn(String colTitle, String tooltip, String field) {
		return addSimpleColumn(colTitle, tooltip, field, true, 50, 50);
	}
	public CustomTableView<A> addSimpleColumn(String colTitle, String tooltip, String field) {
		return addSimpleColumn(colTitle, tooltip, field, true, null, null);
	}
	public CustomTableView<A> addSimpleColumn(String colTitle, String tooltip, String field, Boolean visible,
																						Integer minWidth, Integer maxWidth) {
		var col = getSimpleCol(colTitle, tooltip, field);
		col.setVisible(visible);
		if(minWidth != null) col.setMinWidth(minWidth);
		if(maxWidth != null) col.setMaxWidth(maxWidth);
		this.getColumns().add(col);
		return this;
	}
	
	public void sortByColumns(Integer... cols) {
		getSortOrder().clear();
		for (Integer i : cols) {
			if(i < 0) {
				var col = getColumns().get(i * -1);
				col.setSortType(SortType.DESCENDING);
				getSortOrder().add(col);
			}else{
				getSortOrder().add(getColumns().get(i));
			}
		}
		getSortOrder().clear();
	}
	
	public void setDynamicHeight() {
		this.setPrefHeight(20+25*this.getItems().size());
	}
	
	// ============================================================================================
	// Privates
	// ============================================================================================
	private TableColumn<A, String> getSimpleCol(String colTitle, String tooltip, String field){
		TableColumn<A, String> col = new TableColumn<>(colTitle);
		ViewUtils.tooltip(col, tooltip);
		if(field.contains(".")) {
			LinkedHashMap<String, Object[]> mapping = new LinkedHashMap<>();
			Arrays.stream(field.split("\\."))
					.map(f -> {
						var shouldUpperCase = !f.substring(1, 2).toUpperCase().equals(f.substring(1, 2));
						if(shouldUpperCase){
							return "get".concat(f.substring(0, 1).toUpperCase() + f.substring(1));
						}else{
							return "get".concat(f);
						}
					})
					.forEach(f -> mapping.put(f, null));
			col.setCellValueFactory(new Callbacks<A, String>().stringCol(null, mapping, null));
		}else {
			col.setCellValueFactory(new PropertyValueFactory<>(field));
		}
		return col;
	}
	
}
