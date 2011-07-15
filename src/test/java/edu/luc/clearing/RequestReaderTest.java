package edu.luc.clearing;

import static org.junit.Assert.assertEquals;

import java.io.StringReader;

import org.junit.Before;
import org.junit.Test;

public class RequestReaderTest {

	private RequestReader reader;
	
	@Before
	public void setup(){
		reader = new RequestReader();
	}

	@Test
	public void shouldReturnAnEmptyObjectForAnEmptyRequest() throws Exception {
		assertEquals("{}", reader.respond(new StringReader("[]")));
	}

	@Test
	public void shouldReturnCentsForCheckValues() throws Exception {
		assertEquals("{\"one\":100}",
				reader.respond(new StringReader("[\"one\"]")));
		assertEquals("{\"seven\":700}",
				reader.respond(new StringReader("[\"seven\"]")));
	}

	
	@Test
	public void shouldIgnoreMalformatAmounts() throws Exception {
		assertEquals("{}", reader.respond(new StringReader("[\"purple\"]")));
	}
}
