package fgenejfx.models;

import java.io.Serializable;

public class Season implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private Integer year;
	
	private Group[] season = new Group[6];
	
	private Group pPlayoff;
	private Group ePlayoff;
	
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
	public Group getePlayoff() {
		return ePlayoff;
	}
	public void setePlayoff(Group ePlayoff) {
		this.ePlayoff = ePlayoff;
	}
	
}
