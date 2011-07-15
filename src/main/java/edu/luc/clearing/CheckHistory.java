package edu.luc.clearing;


import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Entity;
import com.google.gson.Gson;

public class CheckHistory {
	
	private Gson gson;
	private DatastoreAdapter storeAdapter;
	public CheckHistory(DatastoreAdapter storeAdapter){
		this.storeAdapter = storeAdapter;
		gson = new Gson();
	}
	
	public String getAoumnts(){
		
		Set<String> amounts = new HashSet<String>();
		
		List<Map<String, Object>> runQuery = storeAdapter.runQuery("Checks");
		
		for(Map<String, Object> properies: runQuery){
			amounts.add(properies.get("amount").toString());
		}
		
		return gson.toJson(amounts);
	}
	
}
