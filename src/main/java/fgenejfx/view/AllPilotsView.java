package fgenejfx.view;

import fgenejfx.App;
import fgenejfx.view.components.CustomGridPane;
import fgenejfx.view.components.tableViews.all_pilots.PilotLifeRecordsTableView;
import javafx.geometry.Pos;

public class AllPilotsView extends CustomGridPane {
	public AllPilotsView() {
		super(Pos.TOP_CENTER);
		this.minHeightProperty().bind(App.stage.heightProperty().subtract(98));

		this.add(new PilotLifeRecordsTableView(), 0, 0);
	}
}
