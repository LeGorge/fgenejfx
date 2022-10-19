package fgenejfx.view.engine;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.LinkedHashMap;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.util.Callback;

public class Callbacks<A,B> {
  private Object result = null;

  private LinkedHashMap<String, Object[]> updateMap(Object data, LinkedHashMap<String,
      Object[]> map){
    if(map != null) {
      LinkedHashMap<String, Object[]> result = new LinkedHashMap<>();
      result.putAll(map);
      for (String key : result.keySet()) {
        if(result.get(key) != null) {
          Object[] newArray = new Object[result.get(key).length];
          for (int i = 0; i < result.get(key).length; i++) {
            newArray[i] = result.get(key)[i];
            if(result.get(key)[i].equals("this")) {
              newArray[i] = data;
            }
          }
          result.put(key, newArray);
        }
      }
      return result;
    }
    return map;
  }
  
  private Class<?>[] paramTypes(Object[] params){
    if(params != null) {
      Class<?>[] paramTypes = new Class[params.length];
      for (int i = 0; i < params.length; i++) {
        paramTypes[i] = params[i].getClass();
      }
      return paramTypes;
    }
    return null;
  }
  
  private Object methodResult(Object obj, String method){
    return methodResultWithParams(obj, method, new Object[0]);
  }
  
  private Object methodResultWithParams(Object obj, String method, Object[] params){
    try {
//      System.out.println(method + " - "+ params[0]);
      Method m;
      //for collections' methods such as enumMap.get, the param type is Object and not 
      //whatever the collection contains, so we specifically try to fix such cases 
      try {
        m = obj.getClass().getMethod(method, paramTypes(params));
      }catch (NoSuchMethodException e) {
        m = obj.getClass().getMethod(method, Object.class);
      }
      return m.invoke(obj, params);
    } catch (Exception e) {
//      System.out.println("Provavelmente algo null em tabela: Callback");
//      e.printStackTrace();
//    } catch (SecurityException e) {
//      e.printStackTrace();
//    } catch (IllegalAccessException e) {
//      e.printStackTrace();
//    } catch (IllegalArgumentException e) {
//      e.printStackTrace();
//    } catch (InvocationTargetException e) {
//      e.printStackTrace();
//    } catch (NullPointerException e) {
//      e.printStackTrace();
    }
    return null;
  }

//  public Callback<CellDataFeatures<StatsMonitorable, Number>, ObservableValue<Number>> stats(
//      String method, int year){
//    return new Callback<CellDataFeatures<StatsMonitorable, Number>, ObservableValue<Number>>() {
//      @Override
//      public ObservableValue<Number> call(CellDataFeatures<StatsMonitorable, Number> data) {
//        StatsMonitorable sm = data.getValue();
//        RaceStats stats = League.get().season(year).seasonStatsOf(sm);
//        Object obj = methodResult(stats, method);
//        return new SimpleObjectProperty(obj);
//      }
//    };
//  }
  
  /**
   * <p>This creates a Callback object intended for javafx TableView usage. It maps nested properties
   * that are not available by the common means of cellValueFactory linkage. It expects an object from 
   * which it will start and a LinkedHasMap<String, Object[]> containing, in order, the names of 
   * the methods as keys and its arguments as values.
   * </p>
   * If said arguments are not known at this time, than this method is no good for you.
   * The string "this" maps the line(object of the table)
   * </p>
   * If the starting point is the line(object of the table), pass null
   * </p>
   * @param altData starting point of the mapping process; null means starting at the object-line 
   * aka one of the objects being shown in the TableView;
   * @param exec maps the path. key: name of the method, value: its params; String "this" maps 
   * object-line
   * @return Callback object intended for javafx TableView usage. The Generic types are set when
   * creating the enclosing class.
   * @since 1.0
   * @author LeGorge(MOURA, ANDERSON GUIMARÃES)
   */
  public Callback<CellDataFeatures<A, B>, ObservableValue<B>> stringCol(
		  Object altData, LinkedHashMap<String, Object[]> exec, DecimalFormat format){
	  return new Callback<CellDataFeatures<A, B>, ObservableValue<B>>() {
		  @Override
		  public ObservableValue<B> call(CellDataFeatures<A, B> data) {
			  Object o = data.getValue();
			  LinkedHashMap<String, Object[]> newExec = updateMap(o, exec);
			  
			  result = o;
			  boolean flagStart = true;
			  
			  if(altData != null && flagStart) {
				  result = altData;
				  flagStart = false;
			  }
			  
			  newExec.keySet().stream().forEachOrdered(method ->{
				  result = methodResultWithParams(result, method, newExec.get(method));
			  });
			  
			  if(result == null) {
			    result = "-";
			  }
			  
			  if(format != null) {
				  result = format.format(result);
			  }
			  
			  return new SimpleObjectProperty(result);
		  }
	  };
  }
}