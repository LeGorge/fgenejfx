package fgenejfx.tests;

public class UnityTests {

//	private League l;
//	private HistoryAgent hag;
//	private ContractsAgent cag;
//	
//	private Pilot p1;
//	private Pilot p2;
//	private Pilot p3;
//	private Pilot p4;
//	
//	private Team t1;
//	private Team t2;
//
//	@BeforeEach
//	public void beforeEach() 
//			throws CopyException, NoSuchFieldException, SecurityException, IllegalArgumentException, 
//			IllegalAccessException, InstantiationException, InvocationTargetException {
//		resetSingletons();
//		l = League.get();
//		hag = HistoryAgent.get();
//		cag = ContractsAgent.get();
//		
//		t1 = new Team(TeamsEnum.AUDI);
//		t2 = new Team(TeamsEnum.BMW);
//		
//		p1 = new Pilot("p1");
//		hag.getPilotHistory(p1).saveStat(l.getYear(),p1.getStats());
//		Contract c1 = new Contract(p1,t1,1);
//		
//		p2 = new Pilot("p2");
//		hag.getPilotHistory(p2).saveStat(l.getYear(), p2.getStats());
//		Contract c2 = new Contract(p2,t1,2);
//		
//		p3 = new Pilot("p3");
//		hag.getPilotHistory(p3).saveStat(l.getYear(), p3.getStats());
//		Contract c3 = new Contract(p3,t2,3);
//		
//		p4 = new Pilot("p4");
//		hag.getPilotHistory(p4).saveStat(l.getYear(), p4.getStats());
//		Contract c4 = new Contract(p4,t2,4);
//		
//		Set<Contract> cts = new HashSet<>();
//		cts.add(c1);
//		cts.add(c2);
//		cts.add(c3);
//		cts.add(c4);
//		
//		jailBreak(cag, "contracts", cts);
//	}
//	
//	@Test
//	public void history() throws NaoEncontradoException, CopyException {
//		Team t = new Team(TeamsEnum.AUDI);
//		hag.getTeamHistory(t).saveStat(l.getYear(), t.getStats());
//		
//		assertEquals(t.getPowers(), hag.getTeamHistory(t).getPowersByYear(l.getYear()));
//		assertNotSame(t.getPowers(), hag.getTeamHistory(t).getPowersByYear(l.getYear()));
//		assertEquals(t.getStats(), hag.getTeamHistory(t).getStatByYear(l.getYear()));
//		assertNotSame(t.getStats(), hag.getTeamHistory(t).getStatByYear(l.getYear()));
//		
//		Pilot p = Pilot.get("teste1");
//		hag.getPilotHistory(p).saveStat(l.getYear(), p.getStats());
//		assertEquals(p.getStats(), hag.getPilotHistory(p).getStatByYear(l.getYear()));
//		assertNotSame(p.getStats(), hag.getPilotHistory(p).getStatByYear(l.getYear()));
//	}
//	
////	@Test
////	public void InternetDependantTest() throws NameGeneratorException{
////		Arrays.stream(InternetDependantUtils.getRandomNames(10)).forEach(System.out::println);
//////		Arrays.stream(InternetDependantUtils.getRandomNamesByApi(10)).forEach(System.out::println);
////	}
//	
//	@Test
//	public void UpdateContracts() 
//			throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException{
//		jailBreak(p1, "rookieYear", -16);
//		
//		Contract c1 = new Contract(p1,t1,1);
//		Contract c2 = new Contract(p2,t1,1);
//		Contract c3 = new Contract(p3,t2,1);
//		Contract c4 = new Contract(p4,t2,4);
//		Set<Contract> cts = new HashSet<>();
//		cts.add(c1);
//		cts.add(c2);
//		cts.add(c3);
//		cts.add(c4);
//		jailBreak(cag, "contracts", cts);
//		
//		assertTrue(cag.getRemainingYearsOfContract(p2) == 1);
//		assertTrue(p1.isActive());
//		
//		hag.getContractsHistory(l.getYear()).save(cts);
//		l.passYear();
//		cag.updateContracts(4);
//		
//		assertFalse(p1.isActive());
//		assertTrue(cag.getRemainingYearsOfContract(p4) == 3);
//		assertTrue(cag.getTeamOf(p4) == t2);
//		assertThrows(NoSuchElementException.class, ()->cag.getTeamOf(p1));
//	}
//	@Test
//	public void Init() 
//			throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, 
//			SecurityException, InstantiationException, InvocationTargetException{
//		resetSingletons();
//		l = League.get();
//		hag = HistoryAgent.get();
//		cag = ContractsAgent.get();
//		
//		cag.updateContracts();
//		Set<Pilot> all = l.getPilots();
//		assertTrue(all.size() == 36);
//	}
//	
//	@Test
//	public void teste(){
//		
////		assertTrue(cag.getRookies().size() == 36);
////		l.getTeams().stream()
////			.forEach(t->{
////				assertEquals(2,cag.getPilotsOf(t).size());
////			});
////		
////		l.newSeason();
////
////		IntStream.range(0, 6).forEach(i->{
////			assertEquals(6, hag.getSeason(1).getSeason()[i].getPilots().size());
////			
////		});
////		hag.getAllPilots().stream().forEach(p->{
////			assertNotNull(hag.getSeason(1).statsOf(p));
////		});
//	}
////	@Test
////	public void teste(){
////	}
////	
//	private void resetSingletons() 
//			throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
//		Constructor<?> s[] = HistoryAgent.class.getDeclaredConstructors();
//		s[0].setAccessible(true);
//		s[0].newInstance();
//		
//		s = ContractsAgent.class.getDeclaredConstructors();
//		s[0].setAccessible(true);
//		s[0].newInstance();
//		
//		s = League.class.getDeclaredConstructors();
//		s[0].setAccessible(true);
//		s[0].newInstance();
//	}
//	
//	private <T> Field jailBreak(T obj, String fieldName, Object value) 
//			throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
//		Field field = obj.getClass().getDeclaredField(fieldName);
//		field.setAccessible(true);
//		field.set(obj, value);
//		return field;
//	}
}
