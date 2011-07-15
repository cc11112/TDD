package edu.luc.clearing;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import static org.mockito.Mockito.*;

import java.util.HashMap;
import static java.util.Arrays.*;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;


public class CheckHistoryTest {
	
	private CheckHistory history;
	private DatastoreAdapter mockDataStore ;
	
	@Before
	public void setUp(){
		mockDataStore = mock(DatastoreAdapter.class);
		history = new CheckHistory(mockDataStore);
	}
	
	@Test
	public void getRequestReturnsAllThePrevisousEncounterCheckAmount() throws Exception{
				
		Map<String,Object> check = new HashMap<String, Object>();
		check.put("amount","one");
		
		List<Map<String,Object>> checks = asList(check);
		when(mockDataStore.runQuery("Checks")).thenReturn(checks);
		
		assertEquals("[\"one\"]", history.getAoumnts());
		
	}

}
