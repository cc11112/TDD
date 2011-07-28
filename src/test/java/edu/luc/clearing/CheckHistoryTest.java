package edu.luc.clearing;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.util.ArrayList;  
import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;

import org.junit.Before;
import org.junit.Test;

public class CheckHistoryTest {

	private CheckHistory history;
	private DatastoreAdapter mockDataStore;
	private List<Map<String, Object>> checks;
	private final String ChecksName = "Checks";
	
	@Before
	public void setUp() {
		mockDataStore = mock(DatastoreAdapter.class);
		history = new CheckHistory(mockDataStore);
		
		checks = new ArrayList<Map<String, Object>>();
		
		when(mockDataStore.runQuery(ChecksName, 1)).thenReturn(checks);
		when(mockDataStore.runQuery(ChecksName, 1000)).thenReturn(checks);
		when(mockDataStore.runQuery(ChecksName, Integer.MAX_VALUE)).thenReturn(checks);
	}

	@Test
	public void getRequestReturnsAllThePrevisousEncounterCheckAmount()
			throws Exception {
		
		addCheckAmount("one");
		
		assertEquals("[\"one\"]", history.getAoumnts(ChecksName, "1000"));
	}
	
	private void addCheckAmount(String string) {
		Map<String, Object> check = new LinkedHashMap<String, Object>();
		check.put(ChecksName, string);
		checks.add(check);
	}

	@Test
	public void doesnotLimitQueryIfNullIsPassedIn() throws Exception{
		
		addCheckAmount("one");
		addCheckAmount("two");
		addCheckAmount("three");
		
		assertEquals("[\"two\",\"one\",\"three\"]", history.getAoumnts(ChecksName, null));
	}
	
	@Test
	public void canLimitNumberOfChecksReturned() throws Exception{
		addCheckAmount("one");
		addCheckAmount("two");
		addCheckAmount("three");
		
		assertEquals("[]", history.getAoumnts(ChecksName, "0"));
		
		assertEquals("[\"one\"]", history.getAoumnts(ChecksName, "1"));
	}

}
