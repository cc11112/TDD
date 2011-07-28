package edu.luc.clearing;

import java.lang.Integer;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gson.Gson;

public class CheckHistory {

	private Gson gson;
	private DatastoreAdapter storeAdapter;

	public CheckHistory(DatastoreAdapter storeAdapter) {
		this.storeAdapter = storeAdapter;
		gson = new Gson();
	}

	private int parseLimit(String limit) {

		if (limit != null) {

			try {
				int l = Integer.parseInt(limit);
				if (l >= 0) {
					return l;
				}
			} catch (Exception e) {
				System.err.println(e.getMessage());
			}
		}

		return Integer.MAX_VALUE;
	}

	public String getAoumnts(String name, String limit) {

		int l = parseLimit(limit);

		Set<String> amounts = new HashSet<String>();

		List<Map<String, Object>> runQuery = storeAdapter.runQuery(name, l);

		if (runQuery != null) {
			for (Map<String, Object> properies : runQuery) {
				if (amounts.size() < l) {
					amounts.add(properies.get(name).toString());
				}
			}

		}

		return gson.toJson(amounts);
	}

}
