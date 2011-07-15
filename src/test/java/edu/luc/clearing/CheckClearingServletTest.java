package edu.luc.clearing;

import java.util.logging.Logger;

import static org.hamcrest.CoreMatchers.*;
import static org.mockito.Mockito.*;
import static org.junit.Assert.assertThat;

import java.io.BufferedReader;
import java.io.CharArrayWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Test;



public class CheckClearingServletTest {
	private CheckClearingServlet servlet;
	private HttpServletResponse mockResponse;
	private HttpServletRequest mockRequest;
	private CharArrayWriter writer;
	private BufferedReader reader;

	@Before
	public void setUp() throws IOException {
		servlet = new CheckClearingServlet();
		
		mockResponse = mock(HttpServletResponse.class);
		mockRequest = mock(HttpServletRequest.class);
		
		reader = new BufferedReader(new StringReader("[]"));
		when(mockRequest.getReader()).thenReturn(reader);
		
		writer = new CharArrayWriter();
		when(mockResponse.getWriter()).thenReturn(new PrintWriter(writer));
	}
	
	@Test
	public void setsContentTypeForTheResponse() throws Exception{
		servlet.doPost(mockRequest, mockResponse);
		
		verify(mockResponse).setContentType("application/json");
	}
	
	@Test
	public void writesAResponseObject() throws Exception{
		servlet.doPost(mockRequest, mockResponse);
		assertThat(writer.toString(), is(equalTo("{}")));
	}
	
	@Test
	public void returnsCheckAmountInAJSONArray() throws Exception{
		servlet.doGet(null, mockResponse);
		
		assertThat(writer.toString(), is(equalTo("[]")));
	}
	
	@Test
	public void getRequestReturnsAllThePreviouslyEncounteredAmount() throws Exception{
		
	}
	

}
