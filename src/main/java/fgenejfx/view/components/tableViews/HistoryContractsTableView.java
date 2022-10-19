package fgenejfx.view.components.tableViews;

import java.util.Set;

import fgenejfx.models.Pilot;
import fgenejfx.view.components.CustomTableView;

public class HistoryContractsTableView extends CustomTableView<Pilot> {

	private HistoryContractsTableView(int year) {
		super(year);
	}

	public static HistoryContractsTableView build(int year, Set<Pilot> pilots){
		var table = new HistoryContractsTableView(year);
		table.addTeamColumn().addNameColumn().addHiddenContractColumn();
		table.getItems().addAll(pilots);
		table.sortByColumns(0, 2);

		return table;
	}
}
