package fgenejfx.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Random;

import fgenejfx.exceptions.CopyException;

public class Utils {

	public static Double genGaussian(Double mean, Double dev){
		return new Random().nextGaussian()*dev+mean;
	}
	public static Integer genGaussian(Integer mean, Integer dev){
		return (int)(new Random().nextGaussian()*dev+mean);
	}
	
	public static <A> A copy(A obj) throws CopyException{
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
}
