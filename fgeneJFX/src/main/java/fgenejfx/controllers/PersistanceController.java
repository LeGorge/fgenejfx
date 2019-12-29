package fgenejfx.controllers;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.commons.io.FileUtils;

import fgenejfx.exceptions.PilotInactivationException;
import fgenejfx.models.ContractsAgent;
import fgenejfx.models.HistoryAgent;
import fgenejfx.models.Pilot;
import fgenejfx.models.Season;

public class PersistanceController {
	private static final String leaguePath = "saves\\league.json";
	private static final String seasonPath = "saves\\season.json";
	private static final String historyPath = "saves\\history.json";
	private static final String contractsPath = "saves\\contracts.json";

	private static void save(Object obj, String path) {
		try {
			FileUtils.writeStringToFile(new File(path), json(obj), StandardCharsets.UTF_8);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static void save(Season s) {
		save(s,seasonPath);
	}
	public static void save() {
		save(League.get(),leaguePath);
		save(League.get().getSeason(),seasonPath);
		save(HistoryAgent.get(),historyPath);
		save(ContractsAgent.get(),contractsPath);
	}

	
	// public static Object load() {
	// 	try {
	// 		FileUtils.writeStringToFile(new File("saves\\league.json"), json(obj), StandardCharsets.UTF_8);
	// 	} catch (IOException e) {
	// 		e.printStackTrace();
	// 	}
	// 	try {
	// 		FileInputStream pilotsIn = new FileInputStream("C:\\Users\\Gorge\\Desktop\\save.save");

	// 		ObjectInputStream in = new ObjectInputStream(pilotsIn);
	// 		Object o = in.readObject();
	// 		in.close();

	// 		pilotsIn.close();
	// 		return o;
	// 	} catch (IOException i) {
	// 		i.printStackTrace();
	// 		return false;
	// 	} catch (ClassNotFoundException c) {
	// 		c.printStackTrace();
	// 		return false;
	// 	}
	// }

	public static void inactivatePilotFile(Pilot p) throws PilotInactivationException {
		File fileSource = new File(System.getProperty("user.dir") + "\\Drivers\\" + p.getName() + ".drv");
		File fileTarget = new File(System.getProperty("user.dir") + "\\History\\Drivers\\" + p.getName() + ".drv");
		try {
			Files.move(fileSource.toPath(), fileTarget.toPath(), StandardCopyOption.ATOMIC_MOVE);
		} catch (IOException e) {
			throw new PilotInactivationException();
		}
	}

	public static <T> String json(T obj) {
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			String json = objectMapper.writeValueAsString(obj);
			return json;
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static Object loadJSON(String json, Class<?> c) {
		try {
			ObjectMapper objectMapper = new ObjectMapper();

			objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

			// SimpleModule module = new SimpleModule("CarDeserializer", new Version(3, 1, 8, null, null, null));
			// module.addDeserializer(Pilot.class, new PilotDeserializer(Pilot.class));
			// objectMapper.registerModule(module);

			Object obj = objectMapper.readValue(json, c);
			return obj;
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return null;
		}
	}
}
