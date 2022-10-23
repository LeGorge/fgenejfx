package fgenejfx.view.components.tableViews;

import fgenejfx.interfaces.StatsMonitorable;
import fgenejfx.view.components.CustomTableView;

public abstract class LifeRecordsTableView extends CustomTableView<StatsMonitorable> {

	protected LifeRecordsTableView() {
		super();
		addNameColumn();
		buildCols();
	}

	protected abstract void buildCols();

	protected void tCols() {
		addSimpleColumn("T Playoffs", "lifeStats.tPlayoffs");
		addSimpleColumn("T Gold", "lifeStats.tGold");
		addSimpleColumn("T Silver", "lifeStats.tSilver");
		addSimpleColumn("T Bronze", "lifeStats.tBronze");
		addSimpleFloatColumn("Win Rate", "lifeStats.tWinRate");
	}

	protected void pCols() {
		addSimpleColumn("P Playoffs", "lifeStats.pPlayoffs");
		addSimpleColumn("P Gold", "lifeStats.pGold");
		addSimpleColumn("P Silver", "lifeStats.pSilver");
		addSimpleColumn("P Bronze", "lifeStats.pBronze");
		addSimpleFloatColumn("Medal Rate", "lifeStats.pMedalRate");
	} 
}
