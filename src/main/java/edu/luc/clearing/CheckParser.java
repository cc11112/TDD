package edu.luc.clearing;

import java.util.HashMap;
import java.util.LinkedHashMap;
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
		AMOUNTS.put("fourty", 4000); // support typo
		AMOUNTS.put("fifty", 5000);
		AMOUNTS.put("sixty", 6000);
		AMOUNTS.put("seventy", 7000);
		AMOUNTS.put("eighty", 8000);
		AMOUNTS.put("ninety", 9000);
		AMOUNTS.put("ninty", 9000); // support typo
		AMOUNTS.put("hundred", 10000);
		AMOUNTS.put("hundreds", 10000);
		AMOUNTS.put("thousand", 100000);
		AMOUNTS.put("thousands", 100000);
		AMOUNTS.put("k", 100000);
		AMOUNTS.put("million", 100000000);
		AMOUNTS.put("millions", 100000000);
	}

	private String ReplaceDollarSymbol(String amount) {

		if (amount.startsWith("usd")) {
			amount = amount.replaceAll("usd", "");
		}

		if (amount.startsWith("$")) {
			amount = amount.replace('$', ' ');
			if (!amount.contains("and")) {
				amount += " dollars";
			}
		} else if (amount.endsWith("$")) {
			amount = amount.replaceAll("\\$", "");
		} else if (amount.contains("$")) {
			amount = amount.replaceAll("\\$", " dollars ");
		}

		amount = amount.trim();

		if (!amount.endsWith("dollars") && !amount.endsWith("dollar")) {
			if (!amount.contains("and") && amount.contains("dollar")) {
				amount = amount.replaceAll("dollars", "dollar").replaceAll(
						"dollar", "dollar and");
			}
		}

		return amount;
	}

	private String ReplaceDotSymbol(String amount) {

		if (amount.startsWith(".")) {
			amount = "0" + amount;
		}

		if (amount.contains(".") && !amount.contains("/")
				&& !amount.contains("cent")) {
			amount = amount.replaceAll("dollars", " ")
					.replaceAll("dollar", " ");

			if (IsMatch("[a-z,0-9,\\s,-]+[\\.](\\d{2})(\\s*)", amount)
					|| IsMatch("[a-z,0-9,\\s,-]+[\\.](\\d{1})(\\s*)", amount)) {

				amount = amount.replaceAll("\\.", " dollars and ") + "/100";
			}
		}
		return amount;
	}

	private String ReplaceCommaSymbol(String amount) {
		if (IsMatch("([\\d,\\,]+)[,]+([\\d])+([a-z,.,/,\\s]+)?", amount)
				|| IsMatch(MatchAnd, amount)
				|| IsMatch("([0-9,\\,]+/(\\s)*100)", amount)) {
			amount = amount.replaceAll(",", "");
		} else {
			amount = amount.replaceAll(",", " and ");
		}

		return amount;
	}

	private String ReplaceConnectString(String amount) {

		// remove '*'
		amount = amount.replace('*', ' ').trim();

		// allow treat '\' as '/'
		amount = amount.replace('\\', '/');

		// treat tilde ~ as a 'and'
		amount = amount.replace('~', '+');

		// treat & as a 'and'
		amount = amount.replace('&', '+');

		// treat ';' as 'and'
		amount = amount.replace(';', '+');

		// treat ':" as and
		amount = amount.replace(':', '+');

		// treate '|" as and
		amount = amount.replace('|', '+');

		// this line will be the last
		// treat '+' & as a 'and'
		amount = amount.replaceAll("\\+", " and ");

		// treat multi '-' as "and"
		amount = amount.replaceAll("([\\-]{2,})", " and ");

		return amount.trim();
	}

	private String ReplaceThousandString(String amount) {
		return amount.replaceAll("thousands", " k ")
				.replaceAll("thousand", "k");
	}

	private String FormatAmount(String amount) {

		amount = amount.toLowerCase().trim();

		// replace thousand string, because it contains "and"
		amount = ReplaceThousandString(amount);

		// connect string
		amount = ReplaceConnectString(amount);

		// . and
		amount = ReplaceDotSymbol(amount);

		// ,
		amount = ReplaceCommaSymbol(amount);

		// $
		amount = ReplaceDollarSymbol(amount);

		return amount.trim();
	}

	public Integer parseExpression(String amount) {

		try {

			if (amount != null && !"".equals(amount.trim())) {
				// format pattern
				amount = FormatAmount(amount);

				if (IsMatch(MatchAnd, amount)) {
					return parseAndAmount(amount);
				} else if (IsMatch("([a-z,0-9,\\s]+)cent[s]?(\\s*)", amount)
						|| IsMatch(MatchCents, amount)) {
					return parseCentsPart(amount);
				} else {
					return parseAmountInwords(amount);
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

			if (array != null && array.length > 0) {
				
				Integer p1 = ("".equals(array[0].trim())) ? 0
						: parseDollarsPart(array[0]);
				
				boolean lastPosition = array[0].contains("dollar") || p1< 10000;
				
				for(int i=1; i<array.length; ++i){
					
					Integer p2 = null;
					
					String s = array[i].trim();
					
					if (i == array.length - 1) {
						if (s.contains("dollar"))
						{
							p2 = parseDollarsPart(s);
						}
						else if (lastPosition
								|| IsMatch("([a-z,0-9,\\s]+)cent[s]?(\\s*)", s)
								|| IsMatch(MatchCents, s)) {
							p2 = parseCentsPart(s);
						} else {
							p2 = parseDollarsPart(s);
							lastPosition = s.contains("dollar") || (p2 !=null && p2< 10000);
						}
					} else {
						p2 = parseDollarsPart(s);
					}
					
					
					if (p1 == null || p2 == null) {
						return getInvalidAmount();
					}
					
					p1 = Summary(p1, p2);
				}
				
				return p1;
			}
		} catch (Exception ex) {
			System.err.println(ex.getMessage());
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

	private String RemoveDollars(String amount) {
		if (amount != null) {

			amount = amount.replaceAll(Dollars, "");

			if (!amount.startsWith("-")) {
				amount = amount.replaceAll("-", " ");
			}
		}
		return amount;
	}

	private Boolean IsNumberic(String amount) {
		if (amount != null) {
			return IsMatch("[\\d,\\s]+(\\s)*", amount);
		}
		return false;
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

			return parseAmountInwords(amount);
		}

		return getInvalidAmount();
	}

	// TODO: parse million
	private Integer tryParseNumberWithWord(String amount, Integer multiple) {

		Integer n = getInvalidAmount();

		if (amount != null) {
			amount = amount.trim();

			if (IsNumberic(amount)) {
				n = tryParseNumber(amount);
				if (n != null) {
					n *= 100;
				}
			} else {
				n = parseNumberInWords(amount);
			}

			if (n != null) {
				n *= multiple;
			}
		}

		return n;
	}

	private Integer parseAmountInwords(String amount) {
		amount = RemoveDollars(amount);

		if ("".equals(amount.trim())) {
			return getInvalidAmount();
		}

		LinkedHashMap<String, Integer> map = new LinkedHashMap<String, Integer>();
		map.put("million", getInvalidAmount()); // million
		map.put("k", getInvalidAmount()); // thousand
		map.put("hundred", getInvalidAmount());
		map.put("dollars", getInvalidAmount());
		map.put("cents", getInvalidAmount());

		Integer lastValue = null;
		
		Pattern p = getPattern("([a-z,0-9,\\-,\\s]+)\\s?million(s)?");
		Matcher m = p.matcher(amount);
		if (m.find()) {
			lastValue = tryParseNumberWithWord(m.group(1), 1000000);
			map.put("million", lastValue);
			amount = amount.replaceAll("([a-z,0-9,\\-,\\s]+)\\s?million(s)?",
					"");
		}

		if ("".equals(amount.trim())) {
			map.put("k", 0);
		} else {
			p = getPattern("([a-z,0-9,\\-,\\s]+)\\s?k");
			m = p.matcher(amount);
			if (m.find()) {
				lastValue = tryParseNumberWithWord(m.group(1), 1000);
				map.put("k", lastValue);
				amount = amount.replaceAll("([a-z,0-9,\\-,\\s]+)\\s?k", "");
			} else if (lastValue != null) {
				map.put("k", 0);
			}
		}

		if ("".equals(amount.trim())) {
			map.put("hundred", 0);
		} else {
			p = getPattern("([a-z,0-9,\\-,\\s]+)\\s?hundred");
			m = p.matcher(amount);
			if (m.find()) {
				lastValue = tryParseNumberWithWord(m.group(1), 100);
				map.put("hundred", lastValue);
				amount = amount.replaceAll(
						"([a-z,0-9,\\-,\\s]+)\\s?hundred(s)?", "");
			}
			else if (lastValue !=null){
				map.put("hundred", 0);
			}
		}

		if ("".equals(amount.trim())) {
			map.put("dollars", 0);
		} else {
			if (IsMatch("([a-z,0-9,\\-,\\s]+)dollar", amount)
					|| IsMatch("([a-z,0-9,\\-,\\s]+)(and)", amount)
					|| (!amount.contains("cent") && !amount.contains("/"))) {
				Integer n = parseAmountTwoDigits(amount);
				map.put("dollars", n);

				amount = amount
						.replaceAll("([a-z,0-9,\\-,\\s]+)dollar(s)?", "")
						.replaceAll("([a-z,0-9,\\-,\\s]+)(and)", "");
				if (!amount.contains("cent") && !IsMatch("/(\\s)*100", amount)) {
					if (n != null) {
						amount = "";
					}
				}
			}

		}

		if ("".equals(amount.trim())) {
			map.put("cents", 0);
		} else if (amount.contains("cent") || IsMatch(MatchCents, amount)) {
			map.put("cents", parseCentsPart(amount));
		}

		Integer total = getInvalidAmount();
		for (Map.Entry<String, Integer> pair : map.entrySet()) {
			Integer n = pair.getValue();
			if (n != null && total == null) {
				total = 0;
			}

			if (n != null && total != null) {
				total += n;
			}

			if (n == null && total != null) {
				return getInvalidAmount();
			}
		}

		return total;

	}

	// obsolete
	private Integer parseAmountTwoDigits(String amount) {

		amount = RemoveDollars(amount);

		amount = RemoveMiddleSpaceInNumber(amount);

		Pattern p = getPattern("\\s+");
		String[] array = p.split(amount);

		if (array != null && array.length > 0) {
			Integer total = null;
			for (String s : array) {
				if (s == null || "".equals(s.trim())) {
					continue;
				}
				Integer n;

				if (IsNumberic(s)) {
					n = tryParseNumber(s);
					if (n != null) {
						n *= 100;
					}
				} else {
					n = parseAmount(s);
				}

				if (n == null && total != null) {
					return getInvalidAmount();
				} else {
					if (n != null
							&& (total == null || (total != null && total == 0 && n == 0))) {
						total = 0;
					} else if ((total != null && total < 2000)
							|| (n != null && n > 900)) {
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

		//if (amount.contains("cent") && IsFound("/(\\s)*100", amount)) {
		//	return getInvalidAmount();
		//}

		amount = amount.replaceAll(Cents, "");

		amount = RemoveMiddleSpaceInNumber(amount);

		Pattern p = getPattern("/");

		String[] array = p.split(amount);

		if (array != null && array.length > 0) {
			if (IsMatch("[\\d,\\s]+(\\s)*", array[0])) {
				return tryParseNumber(array[0]);
			} else {

				Integer cents = parseNumberInWords(RemoveDollars(array[0]));

				if (cents != null) {
					if (cents < 0) {
						return getInvalidAmount();
					} else {
						cents /= 100; // it is cents
					}
				}

				return cents;
			}
		}

		return getInvalidAmount();

	}

	private String RemoveMiddleSpaceInNumber(String amount) {
		if (IsMatch("^[0-9,\\s,/]+$", amount)) {
			amount = amount.replaceAll(" ", "");
		}
		return amount;
	}

	private Integer parseNumberInWords(String amount) {

		LinkedHashMap<String, Integer> map = new LinkedHashMap<String, Integer>();
		map.put("million", getInvalidAmount()); // million
		map.put("k", getInvalidAmount()); // thousand
		map.put("hundred", getInvalidAmount());
		map.put("dollars", getInvalidAmount());

		Pattern p = getPattern("([a-z,0-9,\\-,\\s]+)\\s?million(s)?");
		Matcher m = p.matcher(amount);
		if (m.find()) {
			map.put("million", tryParseNumberWithWord(m.group(1), 1000000));
			amount = amount.replaceAll("([a-z,0-9,\\-,\\s]+)\\s?million(s)?",
					"");
		}

		if ("".equals(amount.trim())) {
			map.put("k", 0);
		} else {
			p = getPattern("([a-z,0-9,\\-,\\s]+)\\s?k");
			m = p.matcher(amount);
			if (m.find()) {
				map.put("k", tryParseNumberWithWord(m.group(1), 1000));
				amount = amount.replaceAll("([a-z,0-9,\\-,\\s]+)\\s?k", "");
			}
		}

		if ("".equals(amount.trim())) {
			map.put("hundred", 0);
		} else {
			p = getPattern("([a-z,0-9,\\-,\\s]+)\\s?hundred");
			m = p.matcher(amount);
			if (m.find()) {
				map.put("hundred", tryParseNumberWithWord(m.group(1), 100));
				amount = amount.replaceAll(
						"([a-z,0-9,\\-,\\s]+)\\s?hundred(s)?", "");
			}
		}

		if ("".equals(amount.trim())) {
			map.put("dollars", 0);
		} else {
			map.put("dollars", parseAmountTwoDigits(amount));
		}

		Integer total = getInvalidAmount();
		for (Map.Entry<String, Integer> pair : map.entrySet()) {
			Integer n = pair.getValue();
			if (n != null && total == null) {
				total = 0;
			}

			if (n != null && total != null) {
				total += n;
			}

			if (n == null && total != null) {
				return getInvalidAmount();
			}
		}

		return total;
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
		if (amount != null && amount.contains(regex)) {
			return true;
		} else {
			Pattern p = getPattern(regex);
			Matcher m = p.matcher(amount);
			return m.matches();
		}
	}
/*
	private boolean IsFound(String regex, String amount) {
		if (amount != null && amount.contains(regex)) {
			return true;
		} else {
			Pattern p = getPattern(regex);
			Matcher m = p.matcher(amount);
			return m.find();
		}
	}
*/	
}
