package fgenejfx.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import fgenejfx.exceptions.PilotInactivationException;
import fgenejfx.models.Pilot;
import fgenejfx.models.Season;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class PersistanceController {
	private static final String savesPath = "saves\\";
	private static final String carsFolderPath = "cars\\";
	private static final String driversPath = "";
	private static final String pilotsPath = "drivers\\";

	private static final String leaguePath = "league.json";
	private static final String seasonPath = "season.json";
	private static final String historyPath = "history.json";
	private static final String contractsPath = "contracts.json";
	private static final String newsPath = "news.json";

	private static final String carsPath = "cars.car";

//	private static final String savesPath = "C:\\Users\\Gorge\\Desktop\\fgene_saves_teste\\";
//	private static final String carsFolderPath = savesPath;
//	private static final String driversPath = savesPath;
//	private static final String pilotsPath = "drivers\\";
//
//	private static final String leaguePath = "league.json";
//	private static final String seasonPath = "season.json";
//	private static final String historyPath = "history.json";
//	private static final String contractsPath = "contracts.json";
//	private static final String newsPath = "news.json";
//
//	private static final String carsPath = "cars.car";

	private static void save(Object obj, String path) {
		try {
			FileUtils.writeStringToFile(new File(path), json(obj), StandardCharsets.UTF_8);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void save(Season s) {
		save(s, seasonPath);
	}

	public static void save() {
		save(League.get(), savesPath.concat(leaguePath));
		save(League.get().getSeason(), savesPath.concat(seasonPath));
		save(HistoryController.get(), savesPath.concat(historyPath));
		save(ContractsController.get(), savesPath.concat(contractsPath));
		save(NewsController.get(), savesPath.concat(newsPath));
		saveBackup();
	}

	public static void saveBackup() {
		try {
			Season s = League.get().getSeason();
			var backupPath = savesPath.concat("backup_"+s.getYear()+"_"+s.getState()+"\\");
			new File(backupPath.concat("drivers")).mkdirs();

			Files.copy(Path.of(savesPath.concat(leaguePath)), Path.of(backupPath.concat(leaguePath)), StandardCopyOption.REPLACE_EXISTING);
			Files.copy(Path.of(savesPath.concat(seasonPath)), Path.of(backupPath.concat(seasonPath)), StandardCopyOption.REPLACE_EXISTING);
			Files.copy(Path.of(savesPath.concat(historyPath)), Path.of(backupPath.concat(historyPath)), StandardCopyOption.REPLACE_EXISTING);
			Files.copy(Path.of(savesPath.concat(contractsPath)), Path.of(backupPath.concat(contractsPath)), StandardCopyOption.REPLACE_EXISTING);
			Files.copy(Path.of(savesPath.concat(newsPath)), Path.of(backupPath.concat(newsPath)), StandardCopyOption.REPLACE_EXISTING);

			Files.copy(Path.of(carsFolderPath.concat(carsPath)), Path.of(backupPath.concat(carsPath)), StandardCopyOption.REPLACE_EXISTING);

			FileUtils.copyDirectory(new File(driversPath.concat(pilotsPath)), new File(backupPath.concat(pilotsPath)));
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static Boolean load() {
		try {
			String json = FileUtils.readFileToString(new File(savesPath.concat(leaguePath)), StandardCharsets.UTF_8);
			League.set((League) loadJSON(json, League.class));

			json = FileUtils.readFileToString(new File(savesPath.concat(seasonPath)), StandardCharsets.UTF_8);
			League.get().setSeason((Season) loadJSON(json, Season.class));

			json = FileUtils.readFileToString(new File(savesPath.concat(historyPath)), StandardCharsets.UTF_8);
			HistoryController.set((HistoryController) loadJSON(json, HistoryController.class));

			json = FileUtils.readFileToString(new File(savesPath.concat(contractsPath)), StandardCharsets.UTF_8);
			ContractsController.set((ContractsController) loadJSON(json, ContractsController.class));
			
			json = FileUtils.readFileToString(new File(savesPath.concat(newsPath)), StandardCharsets.UTF_8);
			NewsController.set((NewsController) loadJSON(json, NewsController.class));
			
			return true;
		} catch (IOException e) {
//			e.printStackTrace();
		  System.out.println("Nada no load");
		  return false;
		}
	}

	public static void inactivatePilotFile(Pilot p) throws PilotInactivationException {
		File fileSource = new File("..\\..\\drivers\\" + p.getName() + ".drv");
		File fileTarget = new File("..\\..\\history\\drivers\\" + p.getName() + ".drv");
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
			Object obj = objectMapper.readValue(json, c);
			return obj;
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return null;
		}
	}
}
