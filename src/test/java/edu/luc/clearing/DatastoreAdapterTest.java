package edu.luc.clearing;

import static org.mockito.Mockito.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.appengine.api.datastore.DatastoreService;
//import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
//import com.google.apphosting.api.ApiProxy;

//import com.google.appengine.tools.development.ApiProxyLocal;
//import com.google.appengine.tools.development.ApiProxyLocalFactory;
//import com.google.appengine.tools.development.AbstractLocalRpcService;
//import com.google.appengine.api.datastore.AsyncDatastoreService; 

public class DatastoreAdapterTest {

	// in order to avoid exception:
	// java.lang.NullPointerException: No API environment is registered for this
	// thread.
	// at
	// com.google.appengine.api.datastore.DatastoreApiHelper.getCurrentAppId(DatastoreApiHelper.java:108)
	// at
	// com.google.appengine.api.datastore.DatastoreApiHelper.getCurrentAppIdNamespace(DatastoreApiHelper.java:118)
	// at com.google.appengine.api.datastore.Query.<init>(Query.java:112)
	// at edu.luc.clearing.DatastoreAdapter.runQuery(DatastoreAdapter.java:29)
	//
	// link:
	// http://code.google.com/appengine/docs/java/tools/localunittesting.html#Introducing_the_Java_Testing_Utilities

	private final LocalServiceTestHelper helper = new LocalServiceTestHelper(
			new LocalDatastoreServiceTestConfig().setDefaultHighRepJobPolicyUnappliedJobPercentage(100));


	@Before
	public void setup(){
		helper.setUp();
		
		//ApiProxyLocal proxy = (ApiProxyLocal) ApiProxy.getDelegate();
		//proxy.setProperty(LocalDatastoreService.NO_STORAGE_PROPERTY, Boolean.TRUE.toString());
	//	ApiProxy.setEnvironmentForCurrentThread(new TestEnvironment()); 
	//	  ApiProxyLocalImpl proxy = (ApiProxyLocalImpl)
	//			  ApiProxy.getDelegate();
	//			          proxy.setProperty(LocalDatastoreService.NO_STORAGE_PROPERTY,
	//			  Boolean.TRUE.toString()); 
	}
	
    @After
    public void tearDown() {
        //helper.tearDown();
    }
    
    
	@Test
	public void canSaveAmounts() throws Exception {
		DatastoreService googleStore = mock(DatastoreService.class);
		//DatastoreService googleStore = DatastoreServiceFactory.getDatastoreService();
		
		DatastoreAdapter store = new DatastoreAdapter(googleStore);
		store.saveRow("Amount", "one");
		verify(googleStore).put((Entity) anyObject());
	}
	
}
