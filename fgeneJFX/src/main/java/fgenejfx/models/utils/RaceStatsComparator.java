package fgenejfx.models.utils;

import fgenejfx.interfaces.StatsMonitorable;
import fgenejfx.models.Group;
import fgenejfx.models.Pilot;
import fgenejfx.models.RaceStats;
import fgenejfx.models.Team;
import fgenejfx.models.enums.LeagueTime;

import java.util.Comparator;

public class RaceStatsComparator implements Comparator<StatsMonitorable> {

    public static RaceStatsComparator LIFESEASON = new RaceStatsComparator(LeagueTime.SEASON);
    public static RaceStatsComparator LIFEPPLAYOFF = new RaceStatsComparator(LeagueTime.PPLAYOFF);
    public static RaceStatsComparator LIFETPLAYOFF = new RaceStatsComparator(LeagueTime.TPLAYOFF);

    LeagueTime time;
    Group group;
    Integer year;

    private RaceStatsComparator(LeagueTime time){
        this.time = time;
    }

    public RaceStatsComparator(Group g){
        this.group = g;
    }

    public RaceStatsComparator(Group g, Integer year){
        this.group = g;
        this.year = year;
    }

    private RaceStats getStats(StatsMonitorable p){
        if(this.time != null)
            switch (time) {
                case SEASON:
                    return p.getStats().getSeason();
                case PPLAYOFF:
                    return p.getStats().getpPlayoff();
                case TPLAYOFF:
                    return p.getStats().gettPlayoff();
            }
        if(this.group != null)
            if(this.year != null)
                return this.group.statsOf((Team) p, this.year);
            else
                return this.group.statsOf((Pilot) p);
        return null;
    }

    @Override
    public int compare(StatsMonitorable p1, StatsMonitorable p2) {
        return this.getStats(p2).compareTo(getStats(p1));
    }
}
