package edu.luc.clearing;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CheckParser {
	private static final Map<String, Integer> AMOUNTS = new HashMap<String, Integer>();

	public CheckParser() {
		AMOUNTS.put("zero", 0);
		AMOUNTS.put("one", 100);
		AMOUNTS.put("two", 200);
		AMOUNTS.put("three", 300);
		AMOUNTS.put("four", 400);
		AMOUNTS.put("five", 500);
		AMOUNTS.put("six", 600);
		AMOUNTS.put("seven", 700);
		AMOUNTS.put("eight", 800);
		AMOUNTS.put("nine", 900);
		AMOUNTS.put("ten", 1000);
		AMOUNTS.put("eleven", 1100);
		AMOUNTS.put("twleve", 1200);
		AMOUNTS.put("threen", 1300);
		AMOUNTS.put("fourteen", 1400);
		AMOUNTS.put("fifteen", 1500);
		AMOUNTS.put("sixteen", 1600);
		AMOUNTS.put("seventeen", 1700);
		AMOUNTS.put("eighteen", 1800);
		AMOUNTS.put("ninteen", 1900);
		AMOUNTS.put("twenty", 2000);
		AMOUNTS.put("thirty", 3000);
		AMOUNTS.put("fourty", 4000);
		AMOUNTS.put("fifty", 5000);
		AMOUNTS.put("sixty", 6000);
		AMOUNTS.put("seventy", 7000);
		AMOUNTS.put("eighty", 8000);
		AMOUNTS.put("ninty", 9000);
	}

	public Integer parseAmount(String amount) {
		if (IsMatch("([a-z]+)\\s+dollar[s]?", amount)) {
			return parseAmountWithDollars(amount);
		} else if (IsMatch("([a-z]+)\\s([a-z]+)\\s*", amount)) {
			return parseAmountTwoDigits(amount);
		}
		return AMOUNTS.get(amount.toLowerCase().trim());
	}

	private Integer parseAmounts(String amount) {
		return AMOUNTS.get(amount.toLowerCase().trim());
	}
	
	private Integer parseAmountTwoDigits(String amount) {
		Pattern p = getPattern("\\s+");
		String[] array = p.split(amount);

		if (array.length > 0) {
			Integer total = 0;
			for(String s: array){
				total += parseAmounts(s);
			}
			return total;
		} else {
			return parseAmounts("");
		}
	}
	
	
	private Integer parseAmountWithDollars(String amount) {
		//todo: remove dollar(s) 
		
		Pattern p = getPattern("\\s+");
		String[] array = p.split(amount);

		if (array.length > 0) {
			return parseAmounts(array[0]);
		} else {
			return parseAmounts("");
		}
	}

	private Integer parseAmountWithCentces(String amount) {
		// todo:
		return 0;
	}

	private Pattern getPattern(String regex) {
		Pattern p = Pattern.compile(regex, Pattern.CASE_INSENSITIVE
				| Pattern.MULTILINE);
		return p;
	}

	private boolean IsMatch(String regex, String amount) {
		Pattern p = getPattern(regex);
		Matcher m = p.matcher(amount);
		return m.matches();
	}
}
