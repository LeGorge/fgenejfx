package fgenejfx.view.components.tableViews;

import fgenejfx.models.Draft;
import fgenejfx.view.components.CustomTableView;
import fgenejfx.view.engine.ViewUtils;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;

public class DraftsTableView extends CustomTableView<Draft> {

	public DraftsTableView(int year) {
		super(year);
	}
	
	public DraftsTableView addPilotColumn() {
		return (DraftsTableView)super.addNameColumn("Pilot", "pilot.name");
	}
	
	@Override
	public DraftsTableView addTeamColumn() {
		return (DraftsTableView)super.addNameColumn("Team", "team.name");
	}
	
	public DraftsTableView addYearsColumn() {
		return (DraftsTableView)super.addSimpleColumn("Contract Length", "years");
	}
}
