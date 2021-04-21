package fgenejfx.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import fgenejfx.controllers.ContractsController;
import fgenejfx.controllers.League;
import fgenejfx.exceptions.CopyException;
import fgenejfx.exceptions.NotValidException;
import fgenejfx.models.Pilot;
import fgenejfx.models.Season;

public class Utils {
  
  public static DecimalFormat onePlaceFormat = new DecimalFormat("#0.0", new DecimalFormatSymbols(Locale.US));
  public static DecimalFormat integerFormat = new DecimalFormat("#0", new DecimalFormatSymbols(Locale.US));
  public static DecimalFormat twoPlacesFormat = new DecimalFormat("#0.00", new DecimalFormatSymbols(Locale.US));
  
  public static Random rand = new Random();

  public static double round(double value, int places) {
    if (places < 0) throw new IllegalArgumentException();

    BigDecimal bd = BigDecimal.valueOf(value);
    bd = bd.setScale(places, RoundingMode.HALF_UP);
    return bd.doubleValue();
  }
  
	public static Double genGaussian(Double mean, Double dev) {
		return rand.nextGaussian() * dev + mean;
	}

	public static Integer genGaussian(Integer mean, Integer dev) {
		return (int) (rand.nextGaussian() * dev + mean);
	}
	
	public static String powerValueToStr(Integer p) {
	  StringBuilder b = new StringBuilder(5);
	  if(p > 0) {
	    for(int j=0; j < p; j++) {
	      b.append(">");
	    }
	  }else {
	    if(p == 0) {
	      b.append("||");
	    }else {
	      for(int j=0; j < p*-1; j++) {
	        b.append("<");
	      }
	    }
	  }
	  return b.toString();
	}

	public static <A> A copy(A obj) throws CopyException {
		try {
			ByteArrayOutputStream out1 = new ByteArrayOutputStream();
			ObjectOutputStream out = new ObjectOutputStream(out1);
			out.writeObject(obj);
			out.close();
			out1.close();
			byte[] byteData = out1.toByteArray();

			ByteArrayInputStream in1 = new ByteArrayInputStream(byteData);
			ObjectInputStream in = new ObjectInputStream(in1);
			A result = (A) in.readObject();
			in.close();
			in1.close();

			return result;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		throw new CopyException();
	}

	public static void begin() {
		League l = League.get();

		ContractsController cag = ContractsController.get();

		l.setPilots(l.createNewPilots(36));
		List<Pilot> list = new ArrayList<>(l.getPilots());
		Collections.shuffle(list);
		for (int i = 2; i < list.size(); i += 2) {
			list.get(i).setRookieYear(1 - i / 2);
			list.get(i + 1).setRookieYear(1 - i / 2);
		}

		try {
			ContractsController.get().setContracts(new HashSet<>());
			ContractsController.get().updateContracts(l.getPilots(), null);
		} catch (NotValidException e) {
			e.printStackTrace();
		}

		l.setSeason(new Season());
	}

}
