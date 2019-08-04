package fgenejfx.models;
import java.text.DecimalFormat;
import java.util.Random;

public enum Powers {

	AIR(2.0,-0.01),
	//DOWNFORCE(170,10),
	POWER(400,2.0),
	//SPEED(175,50),
	//SLIDING(0.6,-0.2),
	SLOWDOWN(0.1,-0.01),
	GRIP(3.0,0.01);
	
	public static final DecimalFormat format = new DecimalFormat("#.###");
	
	public Double def;
	public Double inc;
	
	private Powers(double def, double inc){
		this.def = def;
		this.inc = inc;
	}
	
	public static Powers get(){
		return Powers.values()[new Random().nextInt(Powers.values().length)];
	}
}
