package edu.luc.clearing;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CheckParser {

	private static final String MatchCents = "[a-z,0-9,\\s,-]+/(\\s)*1(\\s)*0(\\s)*0(\\s*)(dollar[s]?)?(\\s*)";
	private static final String Dollars = "dollar[s]?";
	private static final String Cents = "cent[s]?";
	private static final String MatchAnd = "and";

	private static final Map<String, Integer> AMOUNTS = new HashMap<String, Integer>();

	public CheckParser() {
		AMOUNTS.put("without", 0);
		AMOUNTS.put("no", 0);
		AMOUNTS.put("na", 0);
		AMOUNTS.put("n/a", 0);
		AMOUNTS.put("zero", 0);
		// AMOUNTS.put("penny", 1);
		// AMOUNTS.put("nickel", 5);
		// AMOUNTS.put("dime", 10);
		// AMOUNTS.put("quarter", 25);
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
		AMOUNTS.put("twelve", 1200);
		AMOUNTS.put("thirteen", 1300);
		AMOUNTS.put("fourteen", 1400);
		AMOUNTS.put("fifteen", 1500);
		AMOUNTS.put("sixteen", 1600);
		AMOUNTS.put("seventeen", 1700);
		AMOUNTS.put("eighteen", 1800);
		AMOUNTS.put("nineteen", 1900);
		AMOUNTS.put("twenty", 2000);
		AMOUNTS.put("thirty", 3000);
		AMOUNTS.put("forty", 4000);
		AMOUNTS.put("fourty", 4000); //support typo
		AMOUNTS.put("fifty", 5000);
		AMOUNTS.put("sixty", 6000);
		AMOUNTS.put("seventy", 7000);
		AMOUNTS.put("eighty", 8000);
		AMOUNTS.put("ninety", 9000);
		AMOUNTS.put("ninty", 9000); //support typo
		//AMOUNTS.put("one hundred", 10000);
		
	}

	private String ReplaceDollarSymbol(String amount) {
		if (amount != null) {
			if (amount.startsWith("usd")){
				amount = amount.replaceAll("usd", "");
			}
			
			if (amount.startsWith("$")){
				amount = amount.replace('$', ' ');
				if (!amount.contains("and")){
					amount += " dollars";
				}
			}
			else if (amount.contains("$"))
			{
				amount = amount.replaceAll("\\$", " dollars ");
			}
		}
		return amount;
	}

	private String ReplaceDotSymbol(String amount) {
		//TODO: Handle . expression
		/*
		if (amount != null) {
			if (amount.contains(".") && !amount.contains("/") && !amount.contains("cent"))
			{
				amount = amount.replaceAll("dollars", " ").replaceAll("dollar", " ");
				//TODO: Check parse .dot
				amount = amount.replaceAll("\\.", " and ") + "/100";
			}
		}
		*/
		return amount;
	}
	
	public Integer parseExpression(String amount) {

		try {

			if (amount != null) {

				amount = amount.toLowerCase().trim();
				amount = amount.replace('\\', '/').replace('&','+').replaceAll("\\+"," and ");

				amount = ReplaceDollarSymbol(amount);
				amount = ReplaceDotSymbol(amount);
				
				if (IsMatch(MatchAnd, amount)) {
					return parseAndAmount(amount);
				} else if (IsMatch("([a-z,0-9,\\s,-]+)(dollar[s]?)([a-z,0-9,/,\\s]+)cent[s]?(\\s*)", amount)
						|| IsMatch("([a-z,0-9,\\s,-]+)(dollar[s]?)([a-z,0-9,\\s]+)/(\\s)*100(\\s*)", amount)
						){
					return parseWithoutAmount(amount);
				} else if (IsMatch("([a-z,0-9,\\s]+)cent[s]?(\\s*)", amount) 
						|| IsMatch(MatchCents,amount)
						) {
					return parseCentsPart(amount);
				} else if (IsMatch("([a-z,0-9,\\s]+)(dollars[s]?)?(\\s*)", amount)) {
					return parseDollarsPart(amount);
				} else {
					return parseAmountTwoDigits(amount);
				}
			}

		} catch (Exception e) {
			System.err.println(e.getMessage());

		}
		return getInvalidAmount();
	}

	private Integer parseAndAmount(String amount) {

		try {
			Pattern p = getPattern(MatchAnd);
			String[] array = p.split(amount);

			if (array != null) {
				if (array.length == 1){
					return parseDollarsPart(array[0]);
				}
				if (array.length == 2) {
					Integer p1 = parseDollarsPart(array[0]);
					Integer p2 = null;
					if (IsMatch("([a-z,0-9,\\s]+)dollars[s]?", array[1])) {
						p2 = parseDollarsPart(array[1]);
					} else {
						p2 = parseCentsPart(array[1]);
					}
					return Summary(p1, p2);
				} 
				else if (array.length == 3) {
					Integer p1 = parseDollarsPart(array[0]);
					Integer p2 = parseDollarsPart(array[1]);
					//cents
					Integer p3 = parseCentsPart(array[2]);
					if (p1 != null && p2 != null && p3 != null){
						return Summary(Summary(p1, p2), p3);
					}
				}
			}
		} catch (Exception ex) {
			System.err.println(ex.getMessage());
		}

		return getInvalidAmount();
	}

	private Integer parseWithoutAmount(String amount){
		Pattern p = getPattern("dollars[s]?");
		String[] array = p.split(amount);
		if (array != null && array.length == 2)
		{
			Integer p1 = parseDollarsPart(array[0]);
			Integer p2 = parseCentsPart(array[1]);
			return  Summary(p1, p2);
		}
		return getInvalidAmount();
	}
	
	
	private Integer parseAmount(String amount) {
		return AMOUNTS.get(amount);
	}

	private Integer getInvalidAmount() {
		return null;
	}

	private String RemoveSpace(String string) {
		if (string != null) {
			return string.replaceAll("\\s*", "");
		}
		return string;
	}

	private Integer tryParseNumber(String string) {

		String s = RemoveSpace(string);

		Integer result = null;

		try {
			result = Integer.parseInt(s);
		} catch (Exception ex) {
			System.err.println(ex.getMessage());
		}

		return result;
	}
	
	private String RemoveDollars(String amount) {
		if (amount != null) {
			return amount.replaceAll(Dollars, "").replaceAll("-", " ");
		}
		return amount;
	}
	
	private Boolean IsNumberic(String amount){
		if (amount != null){
			return IsMatch("[\\d,\\s]+(\\s)*", amount);
		}
		return false;
	}

	private Integer parseDollarsPart(String amount) {

		// 
		// support one, two, ten, twenty nine,
		// three dollars, 13 dollars, ninety-nine
		// sixty 6 dollars 5/100 dollars
		amount = RemoveDollars(amount);

		if (IsMatch(MatchCents, amount)) {
			return parseCentsPart(amount);
		} else if (IsNumberic(amount)) {
			// parse digits
			Integer result = tryParseNumber(amount);
			if (result != null) {
				return result * 100;
			}
		} else {
			return parseAmountTwoDigits(amount);
		}

		return getInvalidAmount();
	}

	private Integer parseAmountTwoDigits(String amount) {
		
		amount = RemoveDollars(amount);

		Pattern p = getPattern("\\s+");
		String[] array = p.split(amount);

		if (array != null && array.length > 0) {
			Integer total = null;
			for (String s : array) {
				if (s == null || "".equals(s.trim())){
					continue;
				}
				Integer n ;

				if (IsNumberic(s)) {
					n = tryParseNumber(s);
					if (n != null){
						n *= 100;
					}
				} else {
					n = parseAmount(s);
				}
						
				if (n == null && total != null) {
					return getInvalidAmount();
				} else {
					if ( n != null && 
							( total == null || (total !=null && total == 0 && n == 0)) ) {
						total = 0;
					} else if (total < 2000 || n > 900) {
						return getInvalidAmount();
					}

					if (n != null && total != null) {
						total += n;
					}

				}
			}
			return total;
		} else {
			return getInvalidAmount();
		}
	}

	private Integer parseCentsPart(String amount) {

		amount = amount.replaceAll(Cents, "");

		// 
		// support these types: 75, 12/100, ten, eighty five /100, nine / one
		// hundred
		// 0/100 0,
		// 3/100 dollars
		// 7/100 dollar
		Pattern p = getPattern("/");

		String[] array = p.split(amount);

		if (array != null && array.length > 0) {
			if (IsMatch("[\\d,\\s]+(\\s)*", array[0])) {
				return tryParseNumber(array[0]);
			} else {

				Integer cents = parseNumber(RemoveDollars(array[0]));

				if (cents != null) {
					if (cents < 0){
						return getInvalidAmount();
					}
					else
					{
						cents /= 100; // it is cents
					}
				}
				
				return cents;
			}
		}

		return getInvalidAmount();

	}

	private Integer parseNumber(String amount) {

		Pattern p = getPattern("\\s+");
		
		String[] array = p.split(amount);

		if (array != null) {

			Integer total = null;

			for (String s : array) {

				Integer n = parseAmount(s);

				if (n == null && total != null) {
					return getInvalidAmount();
				} else {
					if (n != null && total == null) {
						total = 0;
					}

					if (n != null && total != null) {
						total += n;
					}
				}
			}
			
			return total;

		}

		return getInvalidAmount();
	}

	private Integer Summary(Integer p1, Integer p2) {
		if (p1 != null && p2 != null) {
			return p1 + p2;
		} else if (p1 == null && p2 != null) {
			return p2;
		} else if (p1 != null && p2 == null) {
			return p1;
		}

		return getInvalidAmount();
	}

	private Pattern getPattern(String regex) {
		Pattern p = Pattern.compile(regex, Pattern.CASE_INSENSITIVE
				| Pattern.MULTILINE);
		return p;
	}

	private boolean IsMatch(String regex, String amount) {
		if (amount != null && amount.contains(regex)){ 
			return true;
		}
		else
			{
			Pattern p = getPattern(regex);
			Matcher m = p.matcher(amount);
			return m.matches();
		}
	}
}
