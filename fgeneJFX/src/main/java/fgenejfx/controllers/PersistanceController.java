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
import fgenejfx.models.Pilot;
import fgenejfx.models.Season;

public class PersistanceController {
//	private static final String leaguePath = "..\\..\\saves\\league.json";
//	private static final String seasonPath = "..\\..\\saves\\season.json";
//	private static final String historyPath = "..\\..\\saves\\history.json";
//	private static final String contractsPath = "..\\..\\saves\\contracts.json";
//	private static final String newsPath = "..\\..\\saves\\news.json";
	
	private static final String leaguePath = "C:\\Users\\Gorge\\Desktop\\fgene_saves_teste\\league.json";
	private static final String seasonPath = "C:\\Users\\Gorge\\Desktop\\fgene_saves_teste\\season.json";
	private static final String historyPath = "C:\\Users\\Gorge\\Desktop\\fgene_saves_teste\\history.json";
	private static final String contractsPath = "C:\\Users\\Gorge\\Desktop\\fgene_saves_teste\\contracts.json";
	private static final String newsPath = "C:\\Users\\Gorge\\Desktop\\fgene_saves_teste\\news.json";

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
		save(League.get(), leaguePath);
		save(League.get().getSeason(), seasonPath);
		save(HistoryController.get(), historyPath);
		save(ContractsController.get(), contractsPath);
		save(NewsController.get(), newsPath);
	}

	public static Boolean load() {
		try {
			String json = FileUtils.readFileToString(new File(leaguePath), StandardCharsets.UTF_8);
			League.set((League) loadJSON(json, League.class));

			json = FileUtils.readFileToString(new File(seasonPath), StandardCharsets.UTF_8);
			League.get().setSeason((Season) loadJSON(json, Season.class));

			json = FileUtils.readFileToString(new File(historyPath), StandardCharsets.UTF_8);
			HistoryController.set((HistoryController) loadJSON(json, HistoryController.class));

			json = FileUtils.readFileToString(new File(contractsPath), StandardCharsets.UTF_8);
			ContractsController.set((ContractsController) loadJSON(json, ContractsController.class));
			
			json = FileUtils.readFileToString(new File(newsPath), StandardCharsets.UTF_8);
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
