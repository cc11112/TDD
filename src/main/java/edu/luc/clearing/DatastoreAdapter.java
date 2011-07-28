package edu.luc.clearing;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;

public class DatastoreAdapter {
	private DatastoreService datastore;

	public DatastoreAdapter() {
		this(DatastoreServiceFactory.getDatastoreService());
	}

	public DatastoreAdapter(DatastoreService datastore) {
		this.datastore = datastore;
	}

	//
	public List<Map<String, Object>> runQuery(String column) {

		ArrayList<Map<String, Object>> properties = new ArrayList<Map<String, Object>>();

		try
		{
			Query query = new Query(column);
			PreparedQuery preQuery = datastore.prepare(query);
			for (Entity e : preQuery.asIterable()) {
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
