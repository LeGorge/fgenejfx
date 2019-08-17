package fgenejfx.models;

import java.io.Serializable;

public class Season implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private Integer year;
	
	private Group[] season = new Group[6];
	
	private Group pPlayoff;
	private Group tPlayoff;
	
	public Integer getYear() {
		return year;
	}
	public void setYear(Integer year) {
		this.year = year;
	}
	public Group[] getSeason() {
		return season;
	}
	public void setSeason(Group[] season) {
		this.season = season;
	}
	public Group getpPlayoff() {
		return pPlayoff;
	}
	public void setpPlayoff(Group pPlayoff) {
		this.pPlayoff = pPlayoff;
	}
	public Group gettPlayoff() {
		return tPlayoff;
	}
	public void settPlayoff(Group tPlayoff) {
		this.tPlayoff = tPlayoff;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((year == null) ? 0 : year.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Season other = (Season) obj;
		if (year == null) {
			if (other.year != null)
				return false;
		} else if (!year.equals(other.year))
			return false;
		return true;
	}
	
}
