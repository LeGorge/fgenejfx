package fgenejfx.view.components.tableViews;

import fgenejfx.controllers.ContractsController;
import fgenejfx.models.Contract;
import fgenejfx.view.components.CustomTableView;

public class CurrentContractsTableView extends CustomTableView<Contract> {

	private CurrentContractsTableView(int year) {
		super(year);
	}
	
	public static CurrentContractsTableView build(int year){
		var table = new CurrentContractsTableView(year);
		table.addNameColumn("Team", "team.name")
				 .addNameColumn("Pilot", "pilot.name")
				 .addHiddenColumn("isFirst")
				 .addSmallColumn("Length", "Remaining Contract Years", "years")
				 .addSmallColumn("Career", "Remaining Career years", "pilot.yearsUntilRetirement");
		table.getItems().addAll(ContractsController.get().getContracts());
		table.sortByColumns(0, -2);

		return table;
	}
}
