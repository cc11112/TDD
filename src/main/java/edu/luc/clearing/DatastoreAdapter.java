package edu.luc.clearing;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;

public class DatastoreAdapter {
	private DatastoreService datastore;
	
	public DatastoreAdapter(){
		datastore = DatastoreServiceFactory.getDatastoreService();
	}

	public List<Map<String, Object>> runQuery(String string) {
		
		return new ArrayList<Map<String, Object>>();
	}
	

}
