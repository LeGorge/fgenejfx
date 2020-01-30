package fgenejfx.view;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import fgenejfx.controllers.League;
import fgenejfx.interfaces.StatsMonitorable;
import fgenejfx.models.Pilot;
import fgenejfx.models.RaceStats;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.util.Callback;

public class Callbacks {

  private static Object getMethodResult(Object obj, String method){
    try {
      Method m = obj.getClass().getMethod(method);
      return m.invoke(obj);
    } catch (NoSuchMethodException e) {
      e.printStackTrace();
    } catch (SecurityException e) {
      e.printStackTrace();
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    } catch (IllegalArgumentException e) {
      e.printStackTrace();
    } catch (InvocationTargetException e) {
      e.printStackTrace();
    }
    return null;
  }

  public static Callback<CellDataFeatures<StatsMonitorable, Number>, ObservableValue<Number>> stats(
      String method, int year){
    return new Callback<CellDataFeatures<StatsMonitorable, Number>, ObservableValue<Number>>() {
      @Override
      public ObservableValue<Number> call(CellDataFeatures<StatsMonitorable, Number> data) {
        StatsMonitorable sm = data.getValue();
        RaceStats stats = League.get().season(year).seasonStatsOf(sm);
        Object obj = getMethodResult(stats, method);
        return new SimpleObjectProperty(obj);
      }
    };
  }
}