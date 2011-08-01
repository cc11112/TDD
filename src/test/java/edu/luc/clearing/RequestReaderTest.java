package edu.luc.clearing;

import static org.mockito.Mockito.*;
import static org.junit.Assert.assertEquals;

import java.io.StringReader;

import org.junit.Before;
import org.junit.Test;

public class RequestReaderTest {

	private RequestReader reader;
	private DatastoreAdapter dataStore;
	private Clock clock;

	@Before
	public void setup() {
		clock = mock(Clock.class);
		dataStore = mock(DatastoreAdapter.class);
		reader = new RequestReader(dataStore, clock);

	}

	@Test
	public void shouldReturnAnEmptyObjectForAnEmptyRequest() throws Exception {
		assertEquals("{}", reader.respond(new StringReader("[]")));
		// missing ',' here
		assertEquals("{}", reader.respond(new StringReader(
				"[\"two\"\"three and 100/100\"]")));

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
		assertEquals(
				"{\"one\":100,\"two and 50/100\":250,\"three dollars\":300}",
				reader.respond(new StringReader(
						"[\"one\",\"two and 50/100\",\"three dollars\"]")));
	}

	@Test
	public void shouldIgnoreMalformatAmounts() throws Exception {
		assertEquals("{}", reader.respond(new StringReader("[\"purple\"]")));
	}

	@Test
	public void shouldSaveAmountsInDataStore() throws Exception {
		reader.respond(new StringReader("[\"one\"]"));
		verify(dataStore).saveRow("Checks", "one");
	}

	@Test
	public void shouldShortCircuitTheResponseIfItTakesLongerThan25seconds()
			throws Exception {

		long now = clock.currentTime();

		when(clock.IsOverTime(now)).thenReturn(false, false, true);

		assertEquals("{\"one\":100,\"two\":200}",
				reader.respond(new StringReader("[\"one\",\"two\",\"three\"]")));

	}
	
	@Test
	public void shouldHandlePost() throws Exception{
		assertEquals("{\"count\":0}", reader.handle(new StringReader("{}")));
		assertEquals("{\"count\":1}", reader.handle(new StringReader("{\"one\":100}")));
		assertEquals("{\"count\":2}", reader.handle(new StringReader("{\"one\":100,\"two\":200}")));
	}

}
