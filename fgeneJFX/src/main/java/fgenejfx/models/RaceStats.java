package fgenejfx.models;

import java.io.Serializable;
import java.text.DecimalFormat;

import com.fasterxml.jackson.annotation.JsonIgnore;

import fgenejfx.models.enums.OpEnum;
import fgenejfx.utils.Utils;

public class RaceStats implements Serializable, Comparable<RaceStats> {

	protected static final long serialVersionUID = 1L;

	protected Integer p1st = 0;
	protected Integer p2nd = 0;
	protected Integer p3rd = 0;
	protected Integer p4th = 0;
	protected Integer p5th = 0;
	protected Integer p6th = 0;
	protected Double per = 0.0;

	// pts
	// WinRate
	// PtRate

	public RaceStats() {
		// TODO Auto-generated constructor stub
	}

	public RaceStats(Integer p1st, Integer p2nd, Integer p3rd, Integer p4th, Integer p5th,
			Integer p6th) {
		super();
		this.p1st = p1st;
		this.p2nd = p2nd;
		this.p3rd = p3rd;
		this.p4th = p4th;
		this.p5th = p5th;
		this.p6th = p6th;
	}

	@JsonIgnore
	public Integer getTotalRaces() {
		return p1st + p2nd + p3rd + p4th + p5th + p6th;
	}

	@JsonIgnore
	public Integer getPts() {
		return p1st * 8 + p2nd * 5 + p3rd * 3 + p4th * 2 + p5th * 1 + p6th * 0;
	}

	@JsonIgnore
	public Double getWinRate() {
		return getTotalRaces() != 0 ? Double.valueOf(p1st) / Double.valueOf(getTotalRaces()) : 0.0d;
	}

	@JsonIgnore
	public Double getPtRate() {
		return getTotalRaces() != 0 ? Double.valueOf(getPts()) / Double.valueOf(getTotalRaces()) / 8.0d : 0.0d;
	}
	
	@JsonIgnore
	public boolean isEmpty() {
		if ((p1st == 0) && (p2nd == 0) && (p3rd == 0) && (p4th == 0) && (p5th == 0) && (p6th == 0)) {
			return true;
		} else {
			return false;
		}
	}

	// ============================================================================================
	// static methods
	// ============================================================================================
	public static RaceStats sum(RaceStats st1, RaceStats st2) {
		return joinStats(st1, st2, OpEnum.SUM);
	}
	public static RaceStats subtract(RaceStats st1, RaceStats st2) {
		return joinStats(st1, st2, OpEnum.SUBTRACT);
	}
	private static RaceStats joinStats(RaceStats st1, RaceStats st2, OpEnum op) {
		RaceStats res = new RaceStats();
		if (OpEnum.isSum(op)) {
			res.p1st = st1.p1st + st2.p1st;
			res.p2nd = st1.p2nd + st2.p2nd;
			res.p3rd = st1.p3rd + st2.p3rd;
			res.p4th = st1.p4th + st2.p4th;
			res.p5th = st1.p5th + st2.p5th;
			res.p6th = st1.p6th + st2.p6th;
			res.per = st1.per + st2.per;
		} else {
			res.p1st = st1.p1st - st2.p1st;
			res.p2nd = st1.p2nd - st2.p2nd;
			res.p3rd = st1.p3rd - st2.p3rd;
			res.p4th = st1.p4th - st2.p4th;
			res.p5th = st1.p5th - st2.p5th;
			res.p6th = st1.p6th - st2.p6th;
		}
		return res;
	}

	// ============================================================================================
	// getters & setters
	// ============================================================================================
	public Integer getP1st() {
		return p1st;
	}

	public void setP1st(Integer p1st) {
		this.p1st = p1st;
	}

	public Integer getP2nd() {
		return p2nd;
	}

	public void setP2nd(Integer p2nd) {
		this.p2nd = p2nd;
	}

	public Integer getP3rd() {
		return p3rd;
	}

	public void setP3rd(Integer p3rd) {
		this.p3rd = p3rd;
	}

	public Integer getP4th() {
		return p4th;
	}

	public void setP4th(Integer p4th) {
		this.p4th = p4th;
	}

	public Integer getP5th() {
		return p5th;
	}

	public void setP5th(Integer p5th) {
		this.p5th = p5th;
	}

	public Integer getP6th() {
		return p6th;
	}

	public void setP6th(Integer p6th) {
		this.p6th = p6th;
	}

	public Double getPer() {
    return per;
  }

  public void setPer(Double per) {
    this.per = per;
  }

  public int compareTo(RaceStats o) {
		int result = 0;
		result = this.getPts().compareTo(o.getPts());
		if (result == 0) {
			result = p1st.compareTo(o.p1st);
		}
		if (result == 0) {
			result = p2nd.compareTo(o.p2nd);
		}
		if (result == 0) {
			result = p3rd.compareTo(o.p3rd);
		}
		if (result == 0) {
			result = p4th.compareTo(o.p4th);
		}
		if (result == 0) {
			result = p5th.compareTo(o.p5th);
		}
		if (result == 0) {
			result = p6th.compareTo(o.p6th);
		}
		return result;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((p1st == null) ? 0 : p1st.hashCode());
		result = prime * result + ((p2nd == null) ? 0 : p2nd.hashCode());
		result = prime * result + ((p3rd == null) ? 0 : p3rd.hashCode());
		result = prime * result + ((p4th == null) ? 0 : p4th.hashCode());
		result = prime * result + ((p5th == null) ? 0 : p5th.hashCode());
		result = prime * result + ((p6th == null) ? 0 : p6th.hashCode());
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
		RaceStats other = (RaceStats) obj;
		if (p1st == null) {
			if (other.p1st != null)
				return false;
		} else if (!p1st.equals(other.p1st))
			return false;
		if (p2nd == null) {
			if (other.p2nd != null)
				return false;
		} else if (!p2nd.equals(other.p2nd))
			return false;
		if (p3rd == null) {
			if (other.p3rd != null)
				return false;
		} else if (!p3rd.equals(other.p3rd))
			return false;
		if (p4th == null) {
			if (other.p4th != null)
				return false;
		} else if (!p4th.equals(other.p4th))
			return false;
		if (p5th == null) {
			if (other.p5th != null)
				return false;
		} else if (!p5th.equals(other.p5th))
			return false;
		if (p6th == null) {
			if (other.p6th != null)
				return false;
		} else if (!p6th.equals(other.p6th))
			return false;
		return true;
	}
}
