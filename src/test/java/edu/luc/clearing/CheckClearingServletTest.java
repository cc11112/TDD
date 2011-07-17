package edu.luc.clearing;

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
import org.junit.After;
import org.junit.Test;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;


public class CheckClearingServletTest {
	private CheckClearingServlet servlet;
	private HttpServletResponse mockResponse;
	private HttpServletRequest mockRequest;
	private CharArrayWriter writer;
	private BufferedReader reader;

	//in order to avoid exception: 
	//java.lang.NullPointerException: No API environment is registered for this thread.
	//	at com.google.appengine.api.datastore.DatastoreApiHelper.getCurrentAppId(DatastoreApiHelper.java:108)
	//	at com.google.appengine.api.datastore.DatastoreApiHelper.getCurrentAppIdNamespace(DatastoreApiHelper.java:118)
	//	at com.google.appengine.api.datastore.Query.<init>(Query.java:112)
	//	at edu.luc.clearing.DatastoreAdapter.runQuery(DatastoreAdapter.java:29)
	//
	//link: http://code.google.com/appengine/docs/java/tools/localunittesting.html#Introducing_the_Java_Testing_Utilities
	
    //private final LocalServiceTestHelper helper =
      //  new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());

	@Before
	public void setUp() throws IOException {
		
		//helper.setUp();
		
		DatastoreAdapter store = new DatastoreAdapter();

		servlet = new CheckClearingServlet(store);

		mockResponse = mock(HttpServletResponse.class);
		mockRequest = mock(HttpServletRequest.class);

		reader = new BufferedReader(new StringReader("[]"));
		when(mockRequest.getReader()).thenReturn(reader);

		writer = new CharArrayWriter();
		when(mockResponse.getWriter()).thenReturn(new PrintWriter(writer));
	}

    @After
    public void tearDown() {
        //helper.tearDown();
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
}
