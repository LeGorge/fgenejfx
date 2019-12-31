package fgenejfx.models;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public enum Powers {
	
	AIR(2.0,-0.01),
	//DOWNFORCE(170,10),
	POWER(400,2.0),
	//SPEED(175,50),
	//SLIDING(0.6,-0.2),
	SLOWDOWN(0.1,-0.01),
	GRIP(3.0,0.01);
	
	private static final Integer MAXPOWERCHANGES = 10;
	public static final DecimalFormat format = new DecimalFormat("#.###");
	
	public Double def;
	public Double inc;
	
	private Powers(double def, double inc){
		this.def = def;
		this.inc = inc;
	}
	
	public static Powers random(){
		return Powers.values()[new Random().nextInt(Powers.values().length)];
	}

	private static boolean notOverflown(Powers p, Double value){
		return (value < (p.def + MAXPOWERCHANGES*Math.abs(p.inc)))
			&& (value > (p.def - MAXPOWERCHANGES*Math.abs(p.inc)));
	}

	public static void update(Team t, int times, boolean improve){
		List<Powers> list = Arrays.asList(Powers.values());
		Collections.shuffle(list);

		for (int i = 0; i < times; i++) {
			Powers p;
			Double atual;
			do{
				p = random();
				atual = t.getPowers().get(p);
			}while(notOverflown(p, atual));

			if(improve){
				improve(t, p);
			}else{
				worsen(t,p);
			}
		}
	}

	private static void improve(Team t, Powers p){
		t.update(p, t.power(p)+p.inc);
	}
	private static void worsen(Team t, Powers p){
		t.update(p, t.power(p)-p.inc);
	}
}
