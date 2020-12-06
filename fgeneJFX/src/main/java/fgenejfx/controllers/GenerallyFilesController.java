package fgenejfx.controllers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import fgenejfx.models.Pilot;
import fgenejfx.models.Powers;
import fgenejfx.models.RaceStats;
import fgenejfx.models.Team;

public class GenerallyFilesController {
	private static final String carsPath = "..\\..\\cars\\cars.car";
	private static final String pilotsPath = "..\\..\\drivers\\";
	
//	private static final String carsPath = "C:\\Users\\Gorge\\Desktop\\fgene_saves_teste\\cars.car";
//	private static final String pilotsPath = "C:\\Users\\Gorge\\Desktop\\fgene_saves_teste\\"
//	    + "drivers\\";

	public static RaceStats readDriver(String pilotName) {
		RaceStats result = new RaceStats();
//		RandomAccessFile raf = null;
		File file = new File(pilotsPath + pilotName + ".drv");
		try(RandomAccessFile raf = new RandomAccessFile(file, "r")){
			raf.seek(40);
			result.setP1st(Integer.reverseBytes(raf.readInt()));
			result.setP2nd(Integer.reverseBytes(raf.readInt()));
			result.setP3rd(Integer.reverseBytes(raf.readInt()));
			result.setP4th(Integer.reverseBytes(raf.readInt()));
			result.setP5th(Integer.reverseBytes(raf.readInt()));
			result.setP6th(Integer.reverseBytes(raf.readInt()));
//				raf.seek(84);
//				p.AI = Integer.reverseBytes(raf.readInt());

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return result;
	}

	public static void updateCarFile() {
	  String[] equipes = { "Ferrari", "Mclaren", "Williams", "Mercedez", "Honda", "Renault", "Ford",
	      "Fiat", "Hyunday", "BMW", "Kia", "Toyota", "Nissan", "Volkswagen", "Chevrolet", "Jaguar",
	      "Audi", "Lamborghini" };
	  
	  Integer[] addresses = { 5384, 8, 22856, 16136, 9416, 18824, 8072, 6728, 10760, 2696, 13448,
	      20168, 17480, 21512, 4040, 12104, 1352, 14792 };
	  File file = new File(GenerallyFilesController.carsPath);
	  
		try(RandomAccessFile raf = new RandomAccessFile(file, "rw")) {
			for (int k = 0; k < addresses.length; k++) {
				Team e = Team.get(equipes[k]);
				// raf.read(bytes);
				// f =
				// ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN).getFloat();

				raf.seek(addresses[k]);
				raf.skipBytes(48);

				raf.write(ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN)
						.putFloat((float) (double) e.power(Powers.GRIP)).array());
				raf.write(ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN)
						.putFloat((float) (double) e.power(Powers.GRIP)).array());
				raf.skipBytes(48);
				raf.write(ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN)
						.putFloat((float) (double) e.power(Powers.SLOWDOWN)).array());
				raf.write(ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN)
						.putFloat((float) (double) e.power(Powers.SLOWDOWN)).array());
				raf.skipBytes(44);
				raf.write(ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN)
						.putFloat((float) (double) e.power(Powers.AIR)).array());
				raf.skipBytes(4);
				raf.write(ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN)
						.putFloat((float) (double) e.power(Powers.POWER)).array());
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void updateDriverAI(Pilot p) {
	  File file = new File(GenerallyFilesController.pilotsPath + p.getName() + ".drv");
		try(RandomAccessFile raf = new RandomAccessFile(file, "rw")) {
			raf.seek(84);
			raf.writeByte(p.getAi());
			raf.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
