package fgenejfx.controllers;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import fgenejfx.models.Contract;
import fgenejfx.models.Season;

public class NewsController implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private static NewsController controller;

    private Map<Integer, List<String>> news = new HashMap<>();
    
    public void add(Season s, String msg) {
      add(s.getYear(),msg);
    }
    public void add(Integer s, String msg) {
      List<String> list = news.getOrDefault(s, new ArrayList<>());
      list.add(msg);
      news.putIfAbsent(s, list);
    }
    
    public List<String> get(Season s) {
      return get(s.getYear());
    }
    public List<String> get(Integer s) {
      return news.get(s);
    }
    
    // ============================================================================================
    // singleton handling
    // ============================================================================================
    private NewsController() {
      NewsController.controller = this;
    }

    public static NewsController get() {
      if (controller == null) {
        new NewsController();
      }
      return controller;
    }

    public static void set(NewsController ag) {
      if (controller == null) {
        controller = ag;
      }
    }

    public static void reset() {
      new NewsController();
    }
    
    public Map<Integer, List<String>> getNews() {
      return this.news;
    }

    public void setContracts(Map<Integer, List<String>> news) {
      this.news = news;
    }
}