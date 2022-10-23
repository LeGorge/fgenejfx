package fgenejfx.view.components.tableViews;

import fgenejfx.models.Contract;
import fgenejfx.view.components.CustomTableView;

public class CurrentContractsTableView extends CustomTableView<Contract> {

	public CurrentContractsTableView(int year) {
		super(year);

		addNameColumn("Team", "team.name");
		addNameColumn("Pilot", "pilot.name");
		addHiddenColumn("isFirst");
		addSmallColumn("Length", "Remaining Contract Years", "years");
		addSmallColumn("Career", "Remaining Career years", "pilot.yearsUntilRetirement");

		getItems().addAll(cag.getContracts());
		sortByColumns(0, -2);
	}
}
