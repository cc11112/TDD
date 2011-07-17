package edu.luc.clearing;

import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.StreamTokenizer;
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
		//missing , here
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
		//assertEquals("{\"one\":100,\"two and 50/100\":250,\"three dollars\":300}",
		//		reader.respond(new StringReader("[\"one\",\"two and 50/100\",\"three dollars\"]")));
	}

	
	@Test
	public void shouldIgnoreMalformatAmounts() throws Exception {
		assertEquals("{}", reader.respond(new StringReader("[\"purple\"]")));
	}
	
	@Test
	public void shouldTestFromFile() throws Exception{
		String fileName = "/home/crane/Projects/TestDriven";
		
		StringBuilder sb = new StringBuilder();
		try {
		    BufferedReader in = new BufferedReader(new FileReader(fileName));
		    String str;
		    while ((str = in.readLine()) != null) {
		        sb.append(str).append("\n");
		    }
		    in.close();
		} catch (IOException e) {
		}
		
		assertEquals("{}", reader.respond(new StringReader(sb.toString())));
	}

}
