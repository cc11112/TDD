package edu.luc.clearing;

import static org.mockito.Mockito.*;
import static org.junit.Assert.assertEquals;

import java.io.StringReader;

import org.junit.Before;
import org.junit.Test;


public class RequestReaderTest {

	private RequestReader reader;
	private DatastoreAdapter dataStore;

	@Before
	public void setup(){
		dataStore = mock(DatastoreAdapter.class);
		reader = new RequestReader(dataStore);
	}

	@Test
	public void shouldReturnAnEmptyObjectForAnEmptyRequest() throws Exception {
		assertEquals("{}", reader.respond(new StringReader("[]")));
		//missing ',' here
		assertEquals("{}", reader.respond(new StringReader("[\"two\"\"three and 100/100\"]")));
		
		assertEquals("{}", reader.respond(new StringReader("{}")));
		
		assertEquals("{}", reader.respond(new StringReader("")));
		
		assertEquals("{}", reader.respond(new StringReader("-")));
		
		assertEquals("{}", reader.respond(new StringReader("{\"five\"}")));
	}

	@Test
	public void shouldReturnCentsForCheckValues() throws Exception {
		assertEquals("{\"one\":100}",
				reader.respond(new StringReader("[\"one\"]")));
		assertEquals("{\"seven\":700}",
				reader.respond(new StringReader("[\"seven\"]")));
	}
	
	@Test
	public void shouldReturnMultiCheckValues() throws Exception {
		assertEquals("{\"one\":100,\"two and 50/100\":250,\"three dollars\":300}",
				reader.respond(new StringReader("[\"one\",\"two and 50/100\",\"three dollars\"]")));
	}

	
	@Test
	public void shouldIgnoreMalformatAmounts() throws Exception {
		assertEquals("{}", reader.respond(new StringReader("[\"purple\"]")));
	}
	
	@Test
	public void shouldSaveAmountsInDataStore() throws Exception{
		reader.respond(new StringReader("[\"one\"]"));
		verify(dataStore).saveRow("Checks", "one");
	}

}
