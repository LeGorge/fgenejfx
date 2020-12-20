package fgenejfx.models.enums;

import java.util.Random;

public enum BackgroundSelector {

  DARKLEATHER,
  FAIRYDUST,
  GRASS,
  NATWHITE,
  VINTAGE,
  WOOD;
	
	@Override
	public String toString() {
	  return super.toString().toLowerCase();
	}
	
	public static BackgroundSelector getRandom() {
	  return BackgroundSelector.values()[new Random().nextInt(6)];
	}
  
}
