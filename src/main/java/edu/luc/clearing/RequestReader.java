package edu.luc.clearing;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.logging.Logger;



import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class RequestReader {
	private static final Logger log = Logger.getLogger(RequestReader.class
			.getName());
	private CheckParser checkParser;

	public RequestReader() {
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
		List<String> checks = null;
		
		try
		{
			checks = gson.fromJson( GetPostData(requestData), requestType());
		}
		catch(Exception ex)
		{
			System.err.println(ex.getMessage());
		}

		if (checks != null) {
			for (String amount : checks) {
				Integer parsedValue = checkParser.parseExpression(amount);
				if (parsedValue == null) {
					System.err.println("could not parse amount " + amount);
				} else {
					map.put(amount, parsedValue);
				}
			}
		}

		return new Gson().toJson(map);
	}

	private String LogRequestData(String s) {
		if (s != null) {
			log.info(s);
			//System.out.println(s);
		}
		return s;
	}

	private String GetPostData(Reader sr) {

		StringBuilder sb = new StringBuilder();

		if (sr != null) {

			try {

				BufferedReader reader = new BufferedReader(sr);

				reader.mark(10000);

				String line;
				do {
					line = reader.readLine();

					if (line != null) {
						sb.append(line).append("\n");
					}
				} while (line != null);

				reader.reset();
			} catch (IOException e) {

			}
		}

		return LogRequestData(sb.toString());
	}

	private Type requestType() {
		return new TypeToken<List<String>>() {
		}.getType();
	}
}
