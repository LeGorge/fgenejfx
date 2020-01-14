package fgenejfx.models;
import java.text.DecimalFormat;
import java.util.EnumMap;
import java.util.Random;
import java.util.stream.Collectors;

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

	public Integer relativePower(Double d){
		return new Long(Math.round((d-this.def)/this.inc)).intValue();
	}
	
	public static Powers random(){
		return Powers.values()[new Random().nextInt(Powers.values().length)];
	}

	private static boolean valid(Powers p, Double value){
		return (value < (p.def + MAXPOWERCHANGES*Math.abs(p.inc)))
			&& (value > (p.def - MAXPOWERCHANGES*Math.abs(p.inc)));
	}

	public static void update(EnumMap<Powers, Double> powers, boolean improve){
		// EnumMap<Powers, Double> result = new EnumMap<>(Powers.class);
		Powers p;
		Double atual;
		do{
			p = random();
			atual = powers.get(p);
		}while(!valid(p, atual));

		if(improve){
			powers.put(p,powers.get(p)+p.inc);
		}else{
			powers.put(p,powers.get(p)-p.inc);
		}
	}
	public static Integer carPower(EnumMap<Powers, Double> powers){
		return powers.keySet().stream().collect(Collectors.summingInt(p -> {
			return p.relativePower(powers.get(p));
		}));
	}
}
