package edu.luc.clearing;

import java.io.Reader;
import java.lang.reflect.Type;
import java.util.LinkedHashMap;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class RequestReader {
	
	private CheckParser checkParser;
	
	public RequestReader(){
		checkParser = getCheckParser();
	}
	
	private CheckParser getCheckParser() {
		if (checkParser == null) {
			checkParser = new CheckParser();
		}
		return checkParser;
	}
	
	public String respond(Reader requestData) {
		Gson gson = new Gson();
		LinkedHashMap<String, Integer> map = new LinkedHashMap<String, Integer>();
		List<String> checks = gson.fromJson(requestData, requestType());
		for (String amount : checks) {
			map.put(amount, checkParser.parseExpression(amount));
		}

		return new Gson().toJson(map);
	}

	private Type requestType() {
		return new TypeToken<List<String>>() {
		}.getType();
	}
}
