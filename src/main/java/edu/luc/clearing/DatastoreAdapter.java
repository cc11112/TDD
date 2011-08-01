package edu.luc.clearing;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.appengine.api.datastore.DatastoreFailureException;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.FetchOptions;

/*
import com.google.appengine.api.datastore.Transaction;
import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;


import static com.google.appengine.api.taskqueue.TaskOptions.Method;
import static com.google.appengine.api.taskqueue.TaskOptions.Builder.*;
*/

public class DatastoreAdapter {
	private DatastoreService datastore;

	public DatastoreAdapter() {
		this(DatastoreServiceFactory.getDatastoreService());
		
	}

	public DatastoreAdapter(DatastoreService datastore) {
		
		try {
			
			this.datastore = datastore;
			
			//Queue queue = QueueFactory.getDefaultQueue();
			
		    //Transaction txn = datastore.beginTransaction();

		    //queue.add(withUrl("/checkclearing?limit=1").method(Method.GET));

		    //txn.commit();
		} catch (DatastoreFailureException e) {
			System.err.print(e.getMessage());
		}
		
	}

	//
	public List<Map<String, Object>> runQuery(String column, int limit) {

		ArrayList<Map<String, Object>> properties = new ArrayList<Map<String, Object>>();

		if (limit < 0){
			limit = Integer.MAX_VALUE;
		}
		
		try
		{
			Query query = new Query(column);

			PreparedQuery preQuery = datastore.prepare(query);

			for (Entity e : preQuery.asList(FetchOptions.Builder.withLimit(limit))) {
				properties.add(e.getProperties());
			}
		} catch (Exception ex) {
			System.err.println(ex.getMessage());
		}
		return properties;

	}

	public boolean saveRow(String column, String value) {
		
		try {
			Entity e = new Entity(column);
			e.setProperty(column, value);
			//because put is not always is succeed!
			datastore.put(e);
			return true;
		} catch (Exception ex) {
			System.err.println(ex.getMessage());
			return false;
		}
		
		
	}

}
