package fgenejfx.view.components.tableViews.all_teams;

import fgenejfx.view.components.tableViews.LifeRecordsTableView;

public class TeamLifeRecordsTableView extends LifeRecordsTableView {

	public TeamLifeRecordsTableView() {
		super();

		getItems().addAll(l.getTeams());
		sortByColumns(0);
	}

	protected void buildCols() {
		addSimpleColumn("Score", "lifeStats.teamScore");
		tCols();
		pCols();
	}
}
