package fgenejfx.models.log;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

public abstract class Log implements Serializable {
	protected static final long serialVersionUID = 1L;

	protected String id = UUID.randomUUID().toString();
	protected String log;

	public Log() {
	}

	public Log(String log) {
		this.log = log;
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLog() {
		return this.log;
	}

	public void setLog(String log) {
		this.log = log;
	}

	@Override
	public boolean equals(Object o) {
		if (o == this)
			return true;
		if (!(o instanceof Log)) {
			return false;
		}
		Log log = (Log) o;
		return Objects.equals(id, log.id);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(id);
	}

	@Override
	public String toString() {
		return getLog();
	}

}