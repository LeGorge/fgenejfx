package fgenejfx.tests;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import fgenejfx.controllers.League;
import fgenejfx.exceptions.CopyException;
import fgenejfx.exceptions.NaoEncontradoException;
import fgenejfx.models.Contract;
import fgenejfx.models.ContractsAgent;
import fgenejfx.models.HistoryAgent;
import fgenejfx.models.Pilot;
import fgenejfx.models.Team;
import fgenejfx.models.TeamsEnum;

public class UnityTests {

	private League l;
	private HistoryAgent hag;
	private ContractsAgent cag;
	
	private Pilot p1;
	private Pilot p2;
	private Pilot p3;
	private Pilot p4;
	
	private Team t1;
	private Team t2;

	@BeforeEach
	public void beforeEach() 
			throws CopyException, NoSuchFieldException, SecurityException, IllegalArgumentException, 
			IllegalAccessException, InstantiationException, InvocationTargetException {
		resetSingletons();
		l = League.get();
		hag = HistoryAgent.get();
		cag = ContractsAgent.get();
		
		t1 = new Team(TeamsEnum.AUDI);
		t2 = new Team(TeamsEnum.BMW);
		
		p1 = Pilot.get("p1");
		hag.save(p1, p1.getStats());
		Contract c1 = new Contract(p1,t1,1);
		
		p2 = Pilot.get("p2");
		hag.save(p2, p2.getStats());
		Contract c2 = new Contract(p2,t1,2);
		
		p3 = Pilot.get("p3");
		hag.save(p3, p3.getStats());
		Contract c3 = new Contract(p3,t2,3);
		
		p4 = Pilot.get("p4");
		hag.save(p4, p4.getStats());
		Contract c4 = new Contract(p4,t2,4);
		
		Set<Contract> cts = new HashSet<>();
		cts.add(c1);
		cts.add(c2);
		cts.add(c3);
		cts.add(c4);
		
		jailBreak(cag, "contracts", cts);
	}
	
	@Test
	public void history() throws NaoEncontradoException, CopyException {
		Team t = new Team(TeamsEnum.AUDI);
		hag.save(t, t.getStats());
		
		assertEquals(t.getPowers(), hag.getPowersByYear(t, l.getYear()));
		assertNotSame(t.getPowers(), hag.getPowersByYear(t, l.getYear()));
		assertEquals(t.getStats(), hag.getStatByYear(t, l.getYear()));
		assertNotSame(t.getStats(), hag.getStatByYear(t, l.getYear()));
		
		Pilot p = Pilot.get("teste1");
		hag.save(p, p.getStats());
		assertEquals(p.getStats(), hag.getStatByYear(p, l.getYear()));
		assertNotSame(p.getStats(), hag.getStatByYear(p, l.getYear()));
	}
	
//	@Test
//	public void InternetDependantTest() throws NameGeneratorException{
//		Arrays.stream(InternetDependantUtils.getRandomNames(10)).forEach(System.out::println);
////		Arrays.stream(InternetDependantUtils.getRandomNamesByApi(10)).forEach(System.out::println);
//	}
	
	@Test
	public void UpdateContracts() 
			throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException{
		jailBreak(p1, "rookieYear", -16);
		
		Contract c1 = new Contract(p1,t1,1);
		Contract c2 = new Contract(p2,t1,1);
		Contract c3 = new Contract(p3,t2,1);
		Contract c4 = new Contract(p4,t2,4);
		Set<Contract> cts = new HashSet<>();
		cts.add(c1);
		cts.add(c2);
		cts.add(c3);
		cts.add(c4);
		jailBreak(cag, "contracts", cts);
		
		assertTrue(cag.getRemainingYearsOfContract(p2) == 1);
		assertTrue(p1.isActive());
		
		hag.save(cts);
		l.passYear();
		cag.updateContracts(4);
		
		assertFalse(p1.isActive());
		assertTrue(cag.getRemainingYearsOfContract(p4) == 3);
		assertTrue(cag.getTeamOf(p4) == t2);
		assertThrows(NoSuchElementException.class, ()->cag.getTeamOf(p1));
	}
	@Test
	public void InitAllContracts() 
			throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, 
			SecurityException, InstantiationException, InvocationTargetException{
		resetSingletons();
		l = League.get();
		hag = HistoryAgent.get();
		cag = ContractsAgent.get();
		
		cag.updateContracts();
		Set<Pilot> all = hag.getAllPilots();
		assertTrue(all.size() == 36);
	}
	
	@Test
	public void teste(){
	}
	
	private void resetSingletons() 
			throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Constructor<?> s[] = HistoryAgent.class.getDeclaredConstructors();
		s[0].setAccessible(true);
		s[0].newInstance();
		
		s = ContractsAgent.class.getDeclaredConstructors();
		s[0].setAccessible(true);
		s[0].newInstance();
		
		s = League.class.getDeclaredConstructors();
		s[0].setAccessible(true);
		s[0].newInstance();
	}
	
	private <T> Field jailBreak(T obj, String fieldName, Object value) 
			throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
		Field field = obj.getClass().getDeclaredField(fieldName);
		field.setAccessible(true);
		field.set(obj, value);
		return field;
	}
}
