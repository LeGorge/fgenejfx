package fgenejfx.controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import fgenejfx.exceptions.PilotInactivationException;
import fgenejfx.models.Pilot;

public class PersistanceController {

	public static void save(Object obj){
		try{
//	         FileOutputStream pilotsOut = new FileOutputStream(System.getProperty("user.dir")+"\\saves\\pilots.gene");
	         FileOutputStream pilotsOut = new FileOutputStream("C:\\Users\\Gorge\\Desktop\\save.save");
	         
	         ObjectOutputStream out = new ObjectOutputStream(pilotsOut);
	         out.writeObject(obj);
	         out.close();
	         
	         pilotsOut.close();
	      }catch(IOException i)
	      {
	          i.printStackTrace();
	      }
	}
	
	public static Object load(){
	      try{
	         FileInputStream pilotsIn = new FileInputStream("C:\\Users\\Gorge\\Desktop\\save.save");
	         
	         ObjectInputStream in = new ObjectInputStream(pilotsIn);
	         Object o = in.readObject();
	         in.close();
	         
	         pilotsIn.close();
	         return o;
	      }catch(IOException i)
	      {
	         i.printStackTrace();
	         return false;
	      }catch(ClassNotFoundException c)
	      {
	         c.printStackTrace();
	         return false;
	      }
	}
	
	public static void inactivatePilotFile(Pilot p) throws PilotInactivationException{
		File fileSource = new File(System.getProperty("user.dir")+"\\Drivers\\"+p.getName()+".drv");
		File fileTarget = new File(System.getProperty("user.dir")+"\\History\\Drivers\\"+p.getName()+".drv");
		try {
			Files.move(fileSource.toPath(), fileTarget.toPath(), StandardCopyOption.ATOMIC_MOVE);
		} catch (IOException e) {
			throw new PilotInactivationException();
		}
	}
}
