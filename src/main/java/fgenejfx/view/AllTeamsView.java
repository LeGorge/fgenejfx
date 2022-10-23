package fgenejfx.view;

import fgenejfx.App;
import fgenejfx.view.components.CustomGridPane;
import fgenejfx.view.components.tableViews.all_teams.TeamLifeRecordsTableView;
import javafx.geometry.Pos;

public class AllTeamsView extends CustomGridPane {
	public AllTeamsView() {
		super(Pos.TOP_CENTER);
		this.minHeightProperty().bind(App.stage.heightProperty().subtract(98));

		this.add(new TeamLifeRecordsTableView(), 0, 0);
	}
}
