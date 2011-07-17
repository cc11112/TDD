package edu.luc.clearing;


import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gson.Gson;

public class CheckHistory {
	
	private Gson gson;
	private DatastoreAdapter storeAdapter;
	
	public CheckHistory(DatastoreAdapter storeAdapter){
		this.storeAdapter = storeAdapter;
		gson = new Gson();
	}
	
	public String getAoumnts(String name){
		
		Set<String> amounts = new HashSet<String>();
		
		List<Map<String, Object>> runQuery = storeAdapter.runQuery(name);
		
		if (runQuery != null){
			
			for (Map<String, Object> properies : runQuery) {
				amounts.add(properies.get(name).toString());
			}
		}
		
		return gson.toJson(amounts);
	}
	
}
