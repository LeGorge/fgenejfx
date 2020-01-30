package fgenejfx.models.log;

public abstract class AiLog extends Log {
	protected static final long serialVersionUID = 1L;

	private String pilotName;
	private Integer oldAi;
	private Integer newAi;

	public AiLog() {
	}

	public AiLog(String log, String pilotName, Integer oldAi, Integer newAi) {
		super(log);
		this.pilotName = pilotName;
		this.oldAi = oldAi;
		this.newAi = newAi;
	}

	public String getPilotName() {
		return this.pilotName;
	}

	public void setPilotName(String pilotName) {
		this.pilotName = pilotName;
	}

	public Integer getOldAi() {
		return this.oldAi;
	}

	public void setOldAi(Integer oldAi) {
		this.oldAi = oldAi;
	}

	public Integer getNewAi() {
		return this.newAi;
	}

	public void setNewAi(Integer newAi) {
		this.newAi = newAi;
	}
}