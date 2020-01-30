package fgenejfx.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

import fgenejfx.controllers.League;
import fgenejfx.exceptions.CopyException;
import fgenejfx.exceptions.NotValidException;
import fgenejfx.models.ContractsAgent;
import fgenejfx.models.Pilot;
import fgenejfx.models.Season;

public class Utils {
  
  public static NumberFormat perFormat = new DecimalFormat("#0.0");

  public static double round(double value, int places) {
    if (places < 0) throw new IllegalArgumentException();

    BigDecimal bd = BigDecimal.valueOf(value);
    bd = bd.setScale(places, RoundingMode.HALF_UP);
    return bd.doubleValue();
  }
  
	public static Double genGaussian(Double mean, Double dev) {
		return new Random().nextGaussian() * dev + mean;
	}

	public static Integer genGaussian(Integer mean, Integer dev) {
		return (int) (new Random().nextGaussian() * dev + mean);
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

		ContractsAgent cag = ContractsAgent.get();

		l.setPilots(l.createNewPilots(36));
		List<Pilot> list = new ArrayList<>(l.getPilots());
		Collections.shuffle(list);
		for (int i = 2; i < list.size(); i += 2) {
			list.get(i).setRookieYear(1 - i / 2);
			list.get(i + 1).setRookieYear(1 - i / 2);
		}

		try {
			ContractsAgent.get().setContracts(new HashSet<>());
			ContractsAgent.get().updateContracts(l.getPilots());
		} catch (NotValidException e) {
			e.printStackTrace();
		}

		l.setSeason(new Season());
	}

}
