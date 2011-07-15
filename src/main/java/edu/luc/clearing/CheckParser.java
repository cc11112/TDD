package edu.luc.clearing;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CheckParser {

	private static final String MatchCents = "[\\d,\\s]+/(\\s)*1(\\s)*0(\\s)*0(\\s*)";
	private static final String Dollars = "d(\\s)*o(\\s)*l(\\s)*l(\\s)*a(\\s)*r(\\s)*[s]?";
	private static final String MatchAnd = "a(\\s)*n(\\s)*d";
	
	private static final Map<String, Integer> AMOUNTS = new HashMap<String, Integer>();

	public CheckParser() {
		AMOUNTS.put("zero", 0);
		//AMOUNTS.put("penny", 1);
		//AMOUNTS.put("nickel", 5);
		//AMOUNTS.put("dime", 10);
		//AMOUNTS.put("quarter", 25);
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
		AMOUNTS.put("fifty", 5000);
		AMOUNTS.put("sixty", 6000);
		AMOUNTS.put("seventy", 7000);
		AMOUNTS.put("eighty", 8000);
		AMOUNTS.put("ninety", 9000);
	}

	public Integer parseExpression(String amount) {

		try {

			if (amount != null) {

				amount = amount.toLowerCase().trim();
				amount = amount.replace('\\', '/').replaceAll(Dollars, " "); // .replaceAll("-","")

				if (IsMatch(
						"([a-z\\s,\\-]+)([\\s]*)+((a(\\s)*n(\\s)*d)?(\\s?)[\\d,\\s]+/(\\s)*1(\\s)*0(\\s)*0(\\s*))",
						amount)) {
					return parseAmountWithCents(amount);
				} else if (IsMatch("([a-z\\s,\\-]+)(\\s*)", amount)) {
					return parseAmountWithDollars(amount);
				} else if (IsMatch("([a-z\\s,\\-]+)(\\s*)([a-z]+)", amount)) {
					return parseAmountTwoDigits(amount);
				} else if (IsMatch(MatchCents, amount) || 
						IsMatch(MatchCents + Dollars, amount)) {
					return parseCents(amount);
				}

				return parseAmount(amount);
			}

		} catch (Exception e) {
			System.err.println(e.getMessage());

		}
		return getInvalidAmount();
	}

	private Integer parseAmount(String amount) {
		return AMOUNTS.get(amount);
	}

	private Integer getInvalidAmount() {
		return null;
	}

	private Integer parseFromArrayFirstElement(String[] array) {
		if (array.length > 0) {
			return parseAmountTwoDigits(array[0]);
		} else {
			return getInvalidAmount();
		}
	}

	private String RemoveMiddleSpace(String amount){
		if (amount != null){
			return amount.replaceAll("-", " ").replaceAll(Dollars, " dollar ");
		}
		return amount;
	}
	
	private Integer parseAmountTwoDigits(String amount) {
		
		amount = RemoveMiddleSpace(amount);
		amount = amount.replaceAll(Dollars, " ").replaceAll(MatchAnd, " ");
		
		Pattern p = getPattern("\\s+");
		String[] array = p.split(amount);

		if (array.length > 0) {
			Integer total = null;
			for (String  s : array) {
				Integer n = parseAmount(s);
				if (n == null && total != null){
					return getInvalidAmount();
				}
				else
				{
					if (total == null || (total == 0 && n == 0)) {
						total = 0;
					} else if (total < 2000 || n > 900) {
						return getInvalidAmount();
					}
					total += n;
				}
			}
			return total;
		} else {
			return getInvalidAmount();
		}
	}

	private Integer parseAmountWithDollars(String amount) {
		amount = RemoveMiddleSpace(amount).replaceAll(Dollars, "");
		return parseAmountTwoDigits(amount);
	}

	private Integer parseCents(String amount) {
		amount = amount.replaceAll(Dollars, "").replaceAll("\\s*", "");
		Pattern p = getPattern("/");
		String[] array = p.split(amount);
		if (array != null && array.length > 0) {
			Integer i = Integer.parseInt(array[0].trim());
			if ( i == null || i > 100 || i < 0) {
				return getInvalidAmount();
			} else {
				return i;
			}

		}
		return getInvalidAmount();
	}

	private Integer Summary(Integer p1, Integer p2)
	{
		if (p1 != null && p2 != null) {
			return p1 + p2;
		} else if (p1 == null && p2 != null) {
			return p2;
		} else if (p1 != null && p2 == null) {
			return p1;
		}
		
		return getInvalidAmount();
	}
	
	private Integer parseAmountWithCents(String amount) {
		
		try
		{
			amount = amount.replaceAll(MatchAnd, " and ");
			Pattern p = getPattern("and");
			String[] array = p.split(amount);

			if (array != null) {
				if (array.length == 2) {
					Integer p1 = parseAmountTwoDigits(array[0]);
					Integer p2 = parseCents(array[1]);
					return Summary(p1, p2);
				} else if (array.length == 3) {
					Integer p1 = parseAmountTwoDigits(array[0] + " and "
							+ array[1]);
					Integer p2 = parseCents(array[2]);
					return Summary(p1, p2);
				}
			}
		} catch (Exception ex) {
			System.err.println(ex.getMessage());
		}

		return getInvalidAmount();
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
