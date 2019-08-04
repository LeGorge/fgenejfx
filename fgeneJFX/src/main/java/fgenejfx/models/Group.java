package fgenejfx.models;

import java.io.Serializable;

import org.apache.commons.collections4.map.LinkedMap;

public class Group implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private LinkedMap<Pilot, RaceStats> pilots = new LinkedMap<>();
	
}
