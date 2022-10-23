package fgenejfx.view.components.tableViews.all_pilots;

import fgenejfx.view.components.tableViews.LifeRecordsTableView;

public class PilotLifeRecordsTableView extends LifeRecordsTableView {

	public PilotLifeRecordsTableView() {
		super();

		getItems().addAll(l.getPilots());
		sortByColumns(0);
	}

	protected void buildCols() {
		addSimpleColumn("Score", "lifeStats.pilotScore");
		pCols();
		tCols();
		addSimpleColumn("Active", "active");
		addSimpleColumn("Seasons", "lifeStats.seasons");
	}
}
