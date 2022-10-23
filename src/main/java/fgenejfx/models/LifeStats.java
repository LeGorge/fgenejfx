package fgenejfx.models;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class LifeStats implements Serializable {
	private static final long serialVersionUID = 1L;

	private Integer seasons = 0;
	private Integer pPlayoffs = 0;
	private Integer tPlayoffs = 0;

	private Integer pGold = 0;
	private Integer pSilver = 0;
	private Integer pBronze = 0;

	private Integer tGold = 0;
	private Integer tSilver = 0;
	private Integer tBronze = 0;

	public void incrementSeasons() {
		seasons++;
	}

	public void incrementpPlayoffs() {
		pPlayoffs++;
	}

	public void incrementtPlayoffs() {
		tPlayoffs++;
	}

	public void incrementpGold() {
		pGold++;
	}

	public void incrementpSilver() {
		pSilver++;
	}

	public void incrementpBronze() {
		pBronze++;
	}

	public void incrementtGold() {
		tGold++;
	}

	public void incrementtSilver() {
		tSilver++;
	}

	public void incrementtBronze() {
		tBronze++;
	}

	public LifeStats() {
		// TODO Auto-generated constructor stub
	}

	public Integer getSeasons() {
		return seasons;
	}

	public void setSeasons(Integer seasons) {
		this.seasons = seasons;
	}

	public Integer getpPlayoffs() {
		return pPlayoffs;
	}

	public void setpPlayoffs(Integer pPlayoffs) {
		this.pPlayoffs = pPlayoffs;
	}

	public Integer gettPlayoffs() {
		return tPlayoffs;
	}

	public void settPlayoffs(Integer tPlayoffs) {
		this.tPlayoffs = tPlayoffs;
	}

	public Integer getpGold() {
		return pGold;
	}

	public void setpGold(Integer pGold) {
		this.pGold = pGold;
	}

	public Integer getpSilver() {
		return pSilver;
	}

	public void setpSilver(Integer pSilver) {
		this.pSilver = pSilver;
	}

	public Integer getpBronze() {
		return pBronze;
	}

	public void setpBronze(Integer pBronze) {
		this.pBronze = pBronze;
	}

	public Integer gettGold() {
		return tGold;
	}

	public void settGold(Integer tGold) {
		this.tGold = tGold;
	}

	public Integer gettSilver() {
		return tSilver;
	}

	public void settSilver(Integer tSilver) {
		this.tSilver = tSilver;
	}

	public Integer gettBronze() {
		return tBronze;
	}

	public void settBronze(Integer tBronze) {
		this.tBronze = tBronze;
	}

	@JsonIgnore
	public Integer getTeamScore() {
		int tScore = this.tGold * 6 + this.tSilver * 4 + this.tBronze * 2;
		int pScore = this.pGold * 3 + this.pSilver * 2 + this.pBronze * 1;
		return tScore + pScore;
	}

	public Integer getPilotScore() {
		int tScore = this.tGold * 3 + this.tSilver * 2 + this.tBronze * 1;
		int pScore = this.pGold * 6 + this.pSilver * 4 + this.pBronze * 2;
		return tScore + pScore;
	}

	@JsonIgnore
	public Float gettWinRate() {
		return this.tGold.floatValue() / this.tPlayoffs.floatValue();
	}

	@JsonIgnore
	public Float getpMedalRate() {
		float medals = this.pGold.floatValue() + this.pSilver.floatValue() + this.pBronze.floatValue();
		return medals / this.pPlayoffs.floatValue();
	}

}