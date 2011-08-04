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

	private static final Map<String, Long> AMOUNTS = new HashMap<String, Long>();

	public CheckParser() {
		AMOUNTS.put("without", 0L);
		AMOUNTS.put("no", 0L);
		AMOUNTS.put("na", 0L);
		AMOUNTS.put("n/a", 0L);
		AMOUNTS.put("zero", 0L);
		// AMOUNTS.put("penny", 1);
		// AMOUNTS.put("nickel", 5);
		// AMOUNTS.put("dime", 10);
		// AMOUNTS.put("quarter", 25);
		AMOUNTS.put("one", 100L);
		AMOUNTS.put("two", 200L);
		AMOUNTS.put("three", 300L);
		AMOUNTS.put("four", 400L);
		AMOUNTS.put("five", 500L);
		AMOUNTS.put("six", 600L);
		AMOUNTS.put("seven", 700L);
		AMOUNTS.put("eight", 800L);
		AMOUNTS.put("nine", 900L);
		AMOUNTS.put("ten", 1000L);
		AMOUNTS.put("eleven", 1100L);
		AMOUNTS.put("twelve", 1200L);
		AMOUNTS.put("thirteen", 1300L);
		AMOUNTS.put("fourteen", 1400L);
		AMOUNTS.put("fifteen", 1500L);
		AMOUNTS.put("sixteen", 1600L);
		AMOUNTS.put("seventeen", 1700L);
		AMOUNTS.put("eighteen", 1800L);
		AMOUNTS.put("nineteen", 1900L);
		AMOUNTS.put("twenty", 2000L);
		AMOUNTS.put("thirty", 3000L);
		AMOUNTS.put("forty", 4000L);
		AMOUNTS.put("fourty", 4000L); // support typo
		AMOUNTS.put("fifty", 5000L);
		AMOUNTS.put("sixty", 6000L);
		AMOUNTS.put("seventy", 7000L);
		AMOUNTS.put("eighty", 8000L);
		AMOUNTS.put("ninety", 9000L);
		AMOUNTS.put("ninty", 9000L); // support typo
		AMOUNTS.put("hundred", 10000L);
		AMOUNTS.put("hundreds", 10000L);
		AMOUNTS.put("thousand", 100000L);
		AMOUNTS.put("thousands", 100000L);
		AMOUNTS.put("k", 100000L);
		AMOUNTS.put("million", 100000000L);
		AMOUNTS.put("millions", 100000000L);
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
		Pattern p = getPattern("([\\d,\\,]+[,]+[\\d]+)");
		Matcher m = p.matcher(amount);
		if (m.find()){
			String s = m.group(1);
			String s1 = s.replaceAll(",", "") ;;
			amount = amount.replaceAll(s, s1);
		}
		
		if (amount.contains(",")){
			amount = amount.replaceAll(",", " and ") + " cents";
		}
		
		/*
		if (IsMatch("([\\$,\\d,\\,]+)[,]+([\\d,.])+([a-z,0-9,.,/,\\s]+)?", amount)
				|| IsMatch("([0-9,\\,]+/(\\s)*100)", amount)) {
			amount = amount.replaceAll(",", "") ;
		} else {
			amount = amount.replaceAll(",", " and ") + " cents";
		}*/

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
		
		// ,
		if (amount.contains(",")) {
			amount = ReplaceCommaSymbol(amount);
		}
		
		// $
		amount = ReplaceDollarSymbol(amount);		
		// . and
		if (amount.contains(".")){
			amount = ReplaceDotSymbol(amount);
		}

		amount = FilterOutUnnecessaryAnd(amount);
		
		return amount.trim();
	}

	private String FilterOutUnnecessaryAnd(String amount) {
		//TODO: Filter out unnecessary and
		//filter out "and" before million
		//filter out "and" before thousand
		Pattern p = getPattern("million");
		Matcher m = p.matcher(amount);
		if (m.find()){
			String[] array = p.split(amount);
			if (array.length > 0){
				array[0] = array[0].replaceAll("and", " ") + " million";
				
			}
			amount = Join(array);
		}
		
		return amount;
	}

	public Long parseExpression(String amount) {

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

	private Long parseAndAmount(String amount) {

		try {

			Pattern p = getPattern(MatchAnd);
			String[] array = p.split(amount);

			if (array != null && array.length > 0) {
				
				Long p1 = ("".equals(array[0].trim())) ? 0
						: parseDollarsPart(array[0]);
				
				boolean lastPosition = array[0].contains("dollar") || p1< 10000;
				
				for(int i=1; i<array.length; ++i){
					
					Long p2 = null;
					
					String s = array[i].trim();
					
					if (i == array.length - 1) {
						if (s.contains("dollar"))
						{
							 p = getPattern("dollar");
							 String[] ar = p.split(s);
							 if (ar.length == 1)
							 {
								 p2 = parseDollarsPart(ar[0]);
							 }
							 else if (ar.length > 1){
								 p2 = parseDollarsPart(ar[0]);
								 Long p3 = parseCentsPart(ar[1]);
								 p2 = Summary(p2, p3);
							 }
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

	private Long parseAmount(String amount) {
		return AMOUNTS.get(amount);
	}

	private Long getInvalidAmount() {
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

	private Long tryParseNumber(String string) {

		String s = RemoveSpace(string);

		Long result = null;

		try {
			result = Long.parseLong(s);
		} catch (Exception ex) {
			System.err.println(ex.getMessage());
		}

		return result;
	}

	private Long parseDollarsPart(String amount) {

		//
		// support one, two, ten, twenty nine,
		// three dollars, 13 dollars, ninety-nine
		// sixty 6 dollars 5/100 dollars
		amount = RemoveDollars(amount);

		if (IsMatch(MatchCents, amount)) {
			return parseCentsPart(amount);
		} else if (IsNumberic(amount)) {
			// parse digits
			Long result = tryParseNumber(amount);
			if (result != null) {
				return result * 100;
			}
		} else {

			return parseAmountInwords(amount);
		}

		return getInvalidAmount();
	}

	// TODO: parse million
	private Long tryParseNumberWithWord(String amount, Long multiple) {

		Long n = getInvalidAmount();

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

	private Long parseAmountInwords(String amount) {
		amount = RemoveDollars(amount);

		if ("".equals(amount.trim())) {
			return getInvalidAmount();
		}

		LinkedHashMap<String, Long> map = new LinkedHashMap<String, Long>();
		map.put("million", getInvalidAmount()); // million
		map.put("k", getInvalidAmount()); // thousand
		map.put("hundred", getInvalidAmount());
		map.put("dollars", getInvalidAmount());
		map.put("cents", getInvalidAmount());

		Long lastValue = null;
		
		Pattern p = getPattern("([a-z,0-9,\\-,\\s]+)\\s?million(s)?");
		Matcher m = p.matcher(amount);
		if (m.find()) {
			lastValue = tryParseNumberWithWord(m.group(1), 1000000L);
			map.put("million", lastValue);
			amount = amount.replaceAll("([a-z,0-9,\\-,\\s]+)\\s?million(s)?", "");
		}

		if ("".equals(amount.trim())) {
			map.put("k", 0L);
		} else {
			p = getPattern("([a-z,0-9,\\-,\\s]+)\\s?k");
			m = p.matcher(amount);
			if (m.find()) {
				lastValue = tryParseNumberWithWord(m.group(1), 1000L);
				map.put("k", lastValue);
				amount = amount.replaceAll("([a-z,0-9,\\-,\\s]+)\\s?k", "");
			} else if (lastValue != null) {
				map.put("k", 0L);
			}
		}

		if ("".equals(amount.trim())) {
			map.put("hundred", 0L);
		} else {
			p = getPattern("([a-z,0-9,\\-,\\s]+)\\s?hundred");
			m = p.matcher(amount);
			if (m.find()) {
				lastValue = tryParseNumberWithWord(m.group(1), 100L);
				map.put("hundred", lastValue);
				amount = amount.replaceAll(
						"([a-z,0-9,\\-,\\s]+)\\s?hundred(s)?", "");
			}
			else if (lastValue !=null){
				map.put("hundred", 0L);
			}
		}

		if ("".equals(amount.trim())) {
			map.put("dollars", 0L);
		} else {
			if (IsMatch("([a-z,0-9,\\-,\\s]+)dollar", amount)
					|| IsMatch("([a-z,0-9,\\-,\\s]+)(and)", amount)
					|| (!amount.contains("cent") && !amount.contains("/"))) {
				Long n = parseAmountTwoDigits(amount);
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
			map.put("cents", 0L);
		} else if (amount.contains("cent") || IsMatch(MatchCents, amount)) {
			map.put("cents", parseCentsPart(amount));
		}

		Long total = getInvalidAmount();
		for (Map.Entry<String, Long> pair : map.entrySet()) {
			Long n = pair.getValue();
			if (n != null && total == null) {
				total = 0L;
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

 
	private Long parseAmountTwoDigits(String amount) {

		amount = RemoveDollars(amount);

		amount = RemoveMiddleSpaceInNumber(amount);

		Pattern p = getPattern("\\s+");
		String[] array = p.split(amount);

		if (array != null && array.length > 0) {
			Long total = null;
			for (String s : array) {
				if (s == null || "".equals(s.trim())) {
					continue;
				}
				Long n;

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
						total = 0L;
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

	private Long parseCentsPart(String amount) {

		//if (amount.contains("cent") && IsFound("/(\\s)*100", amount)) {
		//	return getInvalidAmount();
		//}

		amount = amount.replaceAll(Cents, "");

		amount = RemoveMiddleSpaceInNumber(amount);

		if ("".equals(amount)){
			return getInvalidAmount();
		}
		
		Pattern p = getPattern("/");

		String[] array = p.split(amount);

		if (array != null && array.length > 0) {
			if (IsMatch("[\\d,\\s]+(\\s)*", array[0])) {
				return tryParseNumber(array[0]);
			} else {

				Long cents = parseNumberInWords(RemoveDollars(array[0]));

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

	private Long parseNumberInWords(String amount) {

		LinkedHashMap<String, Long> map = new LinkedHashMap<String, Long>();
		map.put("million", getInvalidAmount()); // million
		map.put("k", getInvalidAmount()); // thousand
		map.put("hundred", getInvalidAmount());
		map.put("dollars", getInvalidAmount());

		Pattern p = getPattern("([a-z,0-9,\\-,\\s]+)\\s?million(s)?");
		Matcher m = p.matcher(amount);
		if (m.find()) {
			map.put("million", tryParseNumberWithWord(m.group(1), 1000000L));
			amount = amount.replaceAll("([a-z,0-9,\\-,\\s]+)\\s?million(s)?",
					"");
		}

		if ("".equals(amount.trim())) {
			map.put("k", 0L);
		} else {
			p = getPattern("([a-z,0-9,\\-,\\s]+)\\s?k");
			m = p.matcher(amount);
			if (m.find()) {
				map.put("k", tryParseNumberWithWord(m.group(1), 1000L));
				amount = amount.replaceAll("([a-z,0-9,\\-,\\s]+)\\s?k", "");
			}else{
				map.put("k", 0L);
			}
		}

		if ("".equals(amount.trim())) {
			map.put("hundred", 0L);
		} else {
			p = getPattern("([a-z,0-9,\\-,\\s]+)\\s?hundred");
			m = p.matcher(amount);
			if (m.find()) {
				map.put("hundred", tryParseNumberWithWord(m.group(1), 100L));
				amount = amount.replaceAll(
						"([a-z,0-9,\\-,\\s]+)\\s?hundred(s)?", "");
			}else{
				map.put("hundred", 0L);
			}
		}

		if ("".equals(amount.trim())) {
			map.put("dollars", 0L);
		} else {
			map.put("dollars", parseAmountTwoDigits(amount));
		}

		Long total = getInvalidAmount();
		for (Map.Entry<String, Long> pair : map.entrySet()) {
			Long n = pair.getValue();
			if (n != null && total == null) {
				total = 0L;
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

	private Long Summary(Long p1, Long p2) {
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
	
	private String Join(String[] array) {
        
		StringBuffer buffer = new StringBuffer();
        
		for(String s : array){
            buffer.append(s);
        }
		
        return buffer.toString();
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
