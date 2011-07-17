package edu.luc.clearing;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static java.util.Arrays.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class CheckHistoryTest {

	private CheckHistory history;
	private DatastoreAdapter mockDataStore;

	@Before
	public void setUp() {
		mockDataStore = mock(DatastoreAdapter.class);
		history = new CheckHistory(mockDataStore);
	}

	@Test
	public void getRequestReturnsAllThePrevisousEncounterCheckAmount()
			throws Exception {
		String name = "Checks";

		Map<String, Object> check = new HashMap<String, Object>();
		check.put(name, "one");

		@SuppressWarnings("unchecked")
		List<Map<String, Object>> asList = asList(check);
		List<Map<String, Object>> checks = asList;
		when(mockDataStore.runQuery(name)).thenReturn(checks);

		assertEquals("[\"one\"]", history.getAoumnts(name));

	}

}
