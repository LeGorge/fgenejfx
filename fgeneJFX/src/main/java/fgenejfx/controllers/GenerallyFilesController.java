package fgenejfx.controllers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import fgenejfx.models.Powers;
import fgenejfx.models.Team;

public class GenerallyFilesController {
	private static final String carsPath = "Cars\\cars.car";

	public void updateCarFile(){
		try {
			// File file = new File(System.getProperty("user.dir")+"\\Cars\\cars.car");
			File file = new File(GenerallyFilesController.carsPath);
			String[] equipes = {"Ferrari","Mclaren","Williams","Mercedez","Honda","Renault","Ford","Fiat","Hyunday","BMW","Kia","Toyota","Nissan",
					"Volkswagen","Chevrolet","Jaguar","Audi","Lamborghini"};
			Integer[] addresses = {5384,8,22856,16136,9416,18824,8072,6728,10760,2696,13448,20168,17480,21512,4040,12104,1352,14792};
			RandomAccessFile raf = new RandomAccessFile(file, "rw");
			
			for(int k=0;k<addresses.length;k++){
				Team e = Team.get(equipes[k]);
				//raf.read(bytes);
				//f = ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN).getFloat();
				
				raf.seek(addresses[k]);
				System.out.println(raf.getFilePointer());
				raf.skipBytes(48);
				
				raf.write(ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putFloat((float)(double)e.power(Powers.GRIP)).array());
				raf.write(ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putFloat((float)(double)e.power(Powers.GRIP)).array());
				raf.skipBytes(48);
				raf.write(ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putFloat((float)(double)e.power(Powers.SLOWDOWN)).array());
				raf.write(ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putFloat((float)(double)e.power(Powers.SLOWDOWN)).array());
				raf.skipBytes(44);
				raf.write(ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putFloat((float)(double)e.power(Powers.AIR)).array());
				raf.skipBytes(4);
				raf.write(ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putFloat((float)(double)e.power(Powers.POWER)).array());
			}
			
			raf.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
