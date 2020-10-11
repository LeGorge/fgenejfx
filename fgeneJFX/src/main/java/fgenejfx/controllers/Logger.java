package fgenejfx.controllers;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fgenejfx.models.Season;
import fgenejfx.models.log.Log;

public class Logger implements Serializable {
    private static final long serialVersionUID = 1L;

    private Map<Season, List<Log>> logs = new HashMap<>();

}