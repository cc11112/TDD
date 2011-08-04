package edu.luc.clearing;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class CheckParserTest {
	private CheckParser parser;
	
	@Before
	public void setup(){
		parser = new CheckParser();
	}

	@Test
	public void shouldIgnoreCase() throws Exception {
		assertEquals(100, parser.parseExpression("one").intValue());
		assertEquals(200, parser.parseExpression("TWO").intValue());
		assertEquals(300, parser.parseExpression("Three").intValue());
		assertEquals(1000, parser.parseExpression("TEN").intValue());
		assertEquals(5000, parser.parseExpression("fifty").intValue());
		assertEquals(9000, parser.parseExpression("*** ninETY ***").intValue());
		assertEquals(1000, parser.parseExpression("10").intValue());
		assertEquals(9900, parser.parseExpression("99").intValue());
		assertEquals(10000, parser.parseExpression("100").intValue());
		assertEquals(10100, parser.parseExpression("101").intValue());
		assertEquals(99900, parser.parseExpression("999").intValue());
		assertEquals(99900, parser.parseExpression("999 dollar").intValue());
		assertEquals(100000, parser.parseExpression("1000").intValue());
		assertEquals(1000000, parser.parseExpression("10000").intValue());
		assertEquals(1000000, parser.parseExpression("10,000").intValue());
		assertEquals(100000, parser.parseExpression("$1,000").intValue());
		assertEquals(900000, parser.parseExpression("$ 9,000").intValue());
		assertEquals(899900, parser.parseExpression(" 8,999$ ").intValue());
		assertEquals(2000000, parser.parseExpression("20 000").intValue());
		assertEquals(8031900, parser.parseExpression("80 319 dollar").intValue());
		assertEquals(8700, parser.parseExpression("8 7 dollars").intValue());
		assertEquals(0, parser.parseExpression(" no dollars ").intValue());
		assertEquals(0, parser.parseExpression(" without dollars ").intValue());
		assertEquals(0, parser.parseExpression(" without cents ").intValue());
		//assertEquals(1, parser.parseExpression("penny").intValue());
		//assertEquals(5, parser.parseExpression("nickel").intValue());
		//assertEquals(25, parser.parseExpression("Quarter").intValue());
		//assertEquals(50, parser.parseExpression("Two Quarter").intValue());
		
	}

	@Test
	public void shouldHandleContainHunderd() throws Exception{
		assertEquals(10000, parser.parseExpression("one hundred dollar").intValue());
		assertEquals(890000, parser.parseExpression("89 hundreds").intValue());
		assertEquals(890000, parser.parseExpression("89 hundreds dollar").intValue());
		assertEquals(850000, parser.parseExpression("80 five hundreds").intValue());
		assertEquals(210000, parser.parseExpression("twenty one hundreds dollar").intValue());
		assertEquals(520000, parser.parseExpression("fifty-two hundreds dollar").intValue());
		assertEquals(1000000, parser.parseExpression("100 hundreds dollars").intValue());
		assertEquals(10000000, parser.parseExpression("1000 hundreds dollars").intValue());
		assertEquals(1000000, parser.parseExpression("one hundred hundreds dollars").intValue());
		assertEquals(991000, parser.parseExpression("ninety nine hundreds ten dollar").intValue());
		assertEquals(910000, parser.parseExpression("ninety one hundreds and 0/100").intValue());
		assertEquals(991100, parser.parseExpression("ninety nine hundreds ten dollar and 100/100").intValue());
		assertEquals(991289, parser.parseExpression("ninety nine hundreds twelve dollar and 89 cent").intValue());
		assertEquals(991300, parser.parseExpression("ninety nine hundreds thirteen and no cents").intValue());
		assertEquals(991300, parser.parseExpression("ninety nine hundreds thirteen and zero cents").intValue());
		assertEquals(991300, parser.parseExpression("ninety nine hundreds thirteen and 0/100").intValue());
		assertEquals(991301, parser.parseExpression("ninety nine hundreds thirteen and one cents").intValue());
		assertEquals(991312, parser.parseExpression("ninety nine hundreds thirteen and 12/100").intValue());
		assertEquals(991399, parser.parseExpression("ninety nine hundreds thirteen dollar 99/100").intValue());
		assertEquals(9913, parser.parseExpression("ninety nine hundreds thirteen cents").intValue());
		assertEquals(990013, parser.parseExpression("ninety nine hundreds dollars thirteen cents").intValue());
		assertEquals(990113, parser.parseExpression("ninety nine hundreds and one dollar and thirteen cents").intValue());
		assertEquals(9999, parser.parseExpression("ninety nine anD 99/100").intValue());
		assertEquals(99999, parser.parseExpression("nine hundreds and ninety nine AND 99/100").intValue());
		assertEquals(999999, parser.parseExpression("ninety nine hundreds and ninety nine AND 99/100").intValue());
		assertEquals(999999, parser.parseExpression("ninety nine hundreds and ninety nine DOLLARS AND 99/100").intValue());
		assertEquals(91000, parser.parseExpression("910 ,  0/100").intValue());
		assertEquals(91076, parser.parseExpression("910 , 76 cents").intValue());
		assertEquals(91076, parser.parseExpression("910 , 76/100 cents").intValue());
		assertEquals(91076, parser.parseExpression("910 , 76 ").intValue());
		assertEquals(91076, parser.parseExpression("910 dollars, 76 ").intValue());
		assertEquals(90076, parser.parseExpression("nine hundreds , 76 ").intValue());
		assertEquals(90076, parser.parseExpression("nine hundreds and  76 cents ").intValue());
		assertEquals(91076, parser.parseExpression("nine hundreds and  ten dollars 76/100 ").intValue());
		assertEquals(91000, parser.parseExpression("nine hundreds and  ten dollars no cents ").intValue());
		assertEquals(91000, parser.parseExpression("nine hundreds and  ten dollars and no cents ").intValue());
		assertEquals(91076, parser.parseExpression("nine hundreds and ten dollars  -- 76 cents ").intValue());
		assertEquals(91076, parser.parseExpression("nine hundreds and ten dollars  and 76 cents ").intValue());
		assertEquals(91076, parser.parseExpression("nine hundreds and ten dollars  , 76 cents ").intValue());
		assertEquals(64342, parser.parseExpression("643 + 42 cents ").intValue());
		assertEquals(999999, parser.parseExpression("$9,999 AND 99/100").intValue());
		assertEquals(86835, parser.parseExpression("868 & 35 Cents").intValue());
		assertEquals(33854, parser.parseExpression("338 Dollars + 54 Cent").intValue());
		assertEquals(34805, parser.parseExpression("348 And 5 Cent").intValue());
		assertEquals(36297, parser.parseExpression("362 Dollar + 97/100").intValue());
		assertEquals(72348, parser.parseExpression("$723 + forty eight cent").intValue());
		assertEquals(47947, parser.parseExpression("$479 ~ 47 Cents").intValue());
		assertEquals(46909, parser.parseExpression("$469 , 9 Cent").intValue());
		assertEquals(36297, parser.parseExpression("362 Dollar + 97/100").intValue());
		assertEquals(97306, parser.parseExpression("973 ~ 6/100").intValue());
		assertEquals(91744, parser.parseExpression("nine hundred seventeen , 44/100").intValue());

	}
	
	@Test
	public void shouldHandleContainThousand() throws Exception{
		assertEquals(100000, parser.parseExpression("one thousAND dollar").intValue());
		assertEquals(8900000, parser.parseExpression("89 thousAND").intValue());
		assertEquals(8900000, parser.parseExpression("89 thousAND dollar").intValue());
		assertEquals(8500000, parser.parseExpression("80 five thousAND").intValue());
		assertEquals(2100000, parser.parseExpression("twenty one thousAND dollar").intValue());
		assertEquals(5200000, parser.parseExpression("fifty-two thousAND dollar").intValue());
		assertEquals(5200100, parser.parseExpression("fifty two thousAND and one dollar").intValue());
		assertEquals(4890100, parser.parseExpression("forty eight thousAND nine hundred and one dollar").intValue());
		assertEquals(5251000, parser.parseExpression("fifty two thousAND five hundred ten dollar").intValue());
		assertEquals(9901000, parser.parseExpression("ninety nine thousAND ten dollar").intValue());
		assertEquals(10000000, parser.parseExpression("one hundreds thousAND dollars").intValue());
		assertEquals(90000000, parser.parseExpression("9 hundreds thousAND dollars").intValue());
		assertEquals(900000000, parser.parseExpression("9 thousAND thousAND dollars").intValue());
		assertEquals(9100000, parser.parseExpression("ninety one thousAND and 0/100").intValue());
		assertEquals(9901100, parser.parseExpression("ninety nine thousAND ten dollar and 100/100").intValue());
		assertEquals(9901289, parser.parseExpression("ninety nine thousAND twelve dollar and 89 cent").intValue());
		assertEquals(9901300, parser.parseExpression("ninety nine thousAND thirteen and no cents").intValue());
		assertEquals(9901300, parser.parseExpression("ninety nine thousAND thirteen and zero cents").intValue());
		assertEquals(9901300, parser.parseExpression("ninety nine thousAND thirteen and 0/100").intValue());
		assertEquals(9911001, parser.parseExpression("ninety nine thousAND  one hundred and ten and one cents").intValue());
		assertEquals(9911001, parser.parseExpression("ninety nine thousAND and one hundred and ten and one cents").intValue());
		assertEquals(9901312, parser.parseExpression("ninety nine thousAND thirteen and 12/100").intValue());
		assertEquals(9901399, parser.parseExpression("ninety nine thousAND thirteen dollar 99/100").intValue());
		assertEquals(99013, parser.parseExpression("ninety nine thousAND thirteen cents").intValue());
	}
	
	@Test
	public void shouldHandleContainMillion() throws Exception{
		assertEquals(100000000L, parser.parseExpression("one Millions dollar").longValue());
		assertEquals(900000000L, parser.parseExpression("nine Million dollar").longValue());
		assertEquals(8900000000L, parser.parseExpression("89 Million").longValue());
		assertEquals(100000000100L, parser.parseExpression("1000 Million and one dollar").longValue());
		assertEquals(10000000100L, parser.parseExpression("one hundred Million and one dollar").longValue());
		assertEquals(10000000101L, parser.parseExpression("one hundred Million and one dollar and 1/100").longValue());
		assertEquals(10000000200L, parser.parseExpression("one hundred Million and one dollar and 100/100").longValue());
		assertEquals(100000000000L, parser.parseExpression("one thousand Million").longValue());
		assertEquals(1000000000000L, parser.parseExpression("ten thousand Million").longValue());
		assertEquals(1080000000000L, parser.parseExpression("ten thousand and eight hundred Million").longValue());
		assertEquals(1000900000000L, parser.parseExpression("ten thousand nine Million").longValue());
	}
	
	@Test
	public void shouldHandleZeroAmount() throws Exception {
		assertEquals(0, parser.parseExpression(" no cents ").intValue());
		assertEquals(0, parser.parseExpression("zero").intValue());
		assertEquals(0, parser.parseExpression("zero and ZERO").intValue());
		assertEquals(0, parser.parseExpression("ZERO zero ").intValue());
		assertEquals(0, parser.parseExpression("$no ").intValue());
		assertEquals(0, parser.parseExpression("na cents ").intValue());
		assertEquals(9000, parser.parseExpression("ninety zero").intValue());
		assertEquals(7000, parser.parseExpression("seventy - zero").intValue());
	}

	@Test 
	public void shouldReturnNullFoInvalidString() throws Exception{
		assertEquals(null, parser.parseExpression("red"));
		assertEquals(null, parser.parseExpression(""));
		assertEquals(null, parser.parseExpression("-"));
		assertEquals(null, parser.parseExpression("|"));
		assertEquals(null, parser.parseExpression(","));
		assertEquals(null, parser.parseExpression("."));
		assertEquals(null, parser.parseExpression("~"));
		assertEquals(null, parser.parseExpression("[]"));
		assertEquals(null, parser.parseExpression("[-]"));
		assertEquals(null, parser.parseExpression("[*]"));
		assertEquals(null, parser.parseExpression(" four-teen "));
		assertEquals(null, parser.parseExpression(" fi/ve "));
		assertEquals(null, parser.parseExpression(" thir*teen "));
		assertEquals(null, parser.parseExpression(" SEVENTY-THREE D-O-L-L-A-RS"));
		assertEquals(null, parser.parseExpression(" SEVENTY-THREE DO L LAR S"));
		assertEquals(null, parser.parseExpression("six a n d 81/100"));
	}
	
	@Test
	public void shouldIgnoreSpace() throws Exception {
		assertEquals(800, parser.parseExpression(" eight ").intValue());
		assertEquals(400, parser.parseExpression("Four ").intValue());
		assertEquals(1400, parser.parseExpression(" fourteen ").intValue());
		assertEquals(10000, parser.parseExpression(" Hundred ").intValue());
		assertEquals(10000, parser.parseExpression(" Hundreds ").intValue());
		assertEquals(100000, parser.parseExpression(" THOUsand ").intValue());
		assertEquals(100000, parser.parseExpression(" THOUsands ").intValue());
	}
	
	
	@Test
	public void shouldMatchWithDollars() throws Exception {
		assertEquals(100, parser.parseExpression("One dollar").intValue());
		assertEquals(200, parser.parseExpression("two dollars").intValue());
		assertEquals(300, parser.parseExpression("three  dollars").intValue());
		assertEquals(8000, parser.parseExpression(" Eighty  dollars  ").intValue());
		assertEquals(7600, parser.parseExpression(" SEVENTY SIX  dollars  ").intValue());
		assertEquals(1000, parser.parseExpression(" ten - dollar ").intValue());
		assertEquals(9500, parser.parseExpression(" ninety-five - dollar ").intValue());
		assertEquals(10000, parser.parseExpression(" 100 dollars ").intValue());
		assertEquals(700, parser.parseExpression(" 7 dollars ").intValue());
	}
	
	@Test
	public void shouldReturnNullOnlyDollar() throws Exception{
		assertEquals(null, parser.parseExpression("usd"));
		assertEquals(null, parser.parseExpression("USD"));
		assertEquals(null, parser.parseExpression("$"));
		assertEquals(null, parser.parseExpression("dollar"));
		assertEquals(null, parser.parseExpression("dollars"));
		assertEquals(null, parser.parseExpression("+"));
		assertEquals(null, parser.parseExpression("&"));
		assertEquals(null, parser.parseExpression("."));
		assertEquals(null, parser.parseExpression("-"));
		assertEquals(null, parser.parseExpression("--"));
		assertEquals(null, parser.parseExpression("---"));
		assertEquals(null, parser.parseExpression("/"));
		assertEquals(null, parser.parseExpression("\\"));
		assertEquals(null, parser.parseExpression("1/9 dollars"));
	}
	
	@Test
	public void shouldMatchWithTwoDigits() throws Exception{
		assertEquals(2600, parser.parseExpression("20 six").intValue());
		assertEquals(2100, parser.parseExpression("twenty one").intValue());
		assertEquals(7200, parser.parseExpression("seventy two").intValue());
		assertEquals(8700, parser.parseExpression("eighty 7").intValue());
		assertEquals(5300, parser.parseExpression("  fifty   three ").intValue());
		assertEquals(1300, parser.parseExpression("thirteen").intValue());
		assertEquals(5000, parser.parseExpression("fifty").intValue());
		assertEquals(8000, parser.parseExpression("eighty").intValue());
		assertEquals(7400, parser.parseExpression("seventy four").intValue());
		assertEquals(6900, parser.parseExpression("sixty nine").intValue());
		assertEquals(4700, parser.parseExpression(" forty  SEVEN  ").intValue());
		assertEquals(4700, parser.parseExpression(" forty  SEVEN  dollar").intValue());
		assertEquals(9100, parser.parseExpression(" NINeTY-one").intValue());
		assertEquals(9900, parser.parseExpression(" NINeTY-nine dollars").intValue());
		assertEquals(7300, parser.parseExpression(" SEVENTY-THREE DOLLARS and").intValue());
		assertEquals(7302, parser.parseExpression(" SEVENTY-THREE DOLLARS and two").intValue());
		assertEquals(7301, parser.parseExpression(" SEVENTY-THREE DOLLARS and one cent").intValue());
		assertEquals(7400, parser.parseExpression(" SEVENTY-THREE DOLLARS and 100 cents ").intValue());
		assertEquals(3005, parser.parseExpression(" thirty DOLLARS and five").intValue());
		assertEquals(3700, parser.parseExpression(" thirty DOLLARS and seven DOLLARS").intValue());
		assertEquals(6900, parser.parseExpression(" sixty DOLLARS and 9 DOLLARS").intValue());
		assertEquals(8000, parser.parseExpression(" seventy-one DOLLARS and 9 DOLLARS").intValue());
		assertEquals(4007, parser.parseExpression(" forty  and DOLLARS seven").intValue());
		assertEquals(8200, parser.parseExpression(" eighty - two").intValue());
		assertEquals(8300, parser.parseExpression(" eighty- three").intValue());
		//assertEquals(8005, parser.parseExpression(" eighty-DOLLARS-five cent").intValue()); 
	}

	@Test
	public void shouldReturnNullIfTwoDigitsReverse() throws Exception{
		assertEquals(null, parser.parseExpression("one two three"));
		assertEquals(null, parser.parseExpression("ten ten"));
		assertEquals(null, parser.parseExpression("five twenty"));
		assertEquals(null, parser.parseExpression("seventy eighty"));
		assertEquals(null, parser.parseExpression("nine five"));
		assertEquals(null, parser.parseExpression("seven six"));
		assertEquals(null, parser.parseExpression("fourteen seven"));
		assertEquals(null, parser.parseExpression("nineteen one"));
		//assertEquals(null, parser.parseExpression("seventy-three dollars/100"));
	}
	
	@Test
	public void shouldMatchOnlyCents()  throws Exception{
		assertEquals(87, parser.parseExpression("87/100").intValue());
		assertEquals(12, parser.parseExpression("12/   100").intValue());
		assertEquals(45, parser.parseExpression("45 /100").intValue());
		assertEquals(0, parser.parseExpression("0/100").intValue());
		assertEquals(1, parser.parseExpression("1\\100").intValue());
		assertEquals(76, parser.parseExpression("76 / 100").intValue());
		assertEquals(20, parser.parseExpression("20/ 100").intValue());
		assertEquals(94, parser.parseExpression("94 \\ 100").intValue());
		assertEquals(18, parser.parseExpression("18/100 dollars").intValue());
		assertEquals(39, parser.parseExpression("thirty-nine/100 dollars").intValue());
		assertEquals(5, parser.parseExpression("5/100 dollar").intValue());
		assertEquals(39, parser.parseExpression(" 39 / 1 0 0").intValue());
		assertEquals(0, parser.parseExpression("zero/100").intValue());
		assertEquals(1000, parser.parseExpression("1,000/100").intValue());
		assertEquals(10000, parser.parseExpression("10 000/100").intValue());
		assertEquals(30, parser.parseExpression("thirty/100").intValue());
		assertEquals(31, parser.parseExpression("thirty one/100").intValue());
		assertEquals(92, parser.parseExpression("ninety two/100").intValue());
		assertEquals(85, parser.parseExpression("eighty-five/100").intValue());
		assertEquals(73, parser.parseExpression("seventy-three/100 dollars").intValue());
		assertEquals(73, parser.parseExpression("seventy-three/100 dollar").intValue());
		assertEquals(7300, parser.parseExpression("seventy-three dollars 0/100").intValue());
		assertEquals(7300, parser.parseExpression("seventy-three dollars zero cent").intValue());
		assertEquals(7300, parser.parseExpression("seventy-three dollars zero ").intValue());
		assertEquals(7300, parser.parseExpression("seventy-three dollars zero/100 ").intValue());
		assertEquals(7400, parser.parseExpression("seventy-three dollars 100/100 ").intValue());
		//cents
		assertEquals(23, parser.parseExpression("23 cents").intValue());
		assertEquals(1, parser.parseExpression("1 cent").intValue());
		assertEquals(0, parser.parseExpression("  0 cent  ").intValue());
		assertEquals(100, parser.parseExpression("  100 cent  ").intValue());
		assertEquals(7890, parser.parseExpression("  7890 cent  ").intValue());
		assertEquals(100000, parser.parseExpression("  100000 cents  ").intValue());
	}
	
	@Test
	public void shouReturnNullForInvalidCents() throws Exception {
		assertEquals(null, parser.parseExpression("  /100"));
		assertEquals(null, parser.parseExpression("/100"));
		assertEquals(null, parser.parseExpression("7.6/100"));
		assertEquals(null, parser.parseExpression("99.00/100"));
		assertEquals(null, parser.parseExpression("99/100.00"));
		assertEquals(null, parser.parseExpression("/"));
		assertEquals(null, parser.parseExpression("//"));
		assertEquals(null, parser.parseExpression("\\"));
		assertEquals(null, parser.parseExpression("/0"));
		assertEquals(null, parser.parseExpression("45a461/100/1b00"));
		assertEquals(null, parser.parseExpression("-1/100"));
		assertEquals(null, parser.parseExpression("abcd/100"));
		assertEquals(null, parser.parseExpression("!!&/*100"));
		assertEquals(null, parser.parseExpression("43?/100"));		
		assertEquals(null, parser.parseExpression("(*)/(100)"));
		assertEquals(null, parser.parseExpression("(*)/(99)"));
		assertEquals(null, parser.parseExpression("100/99"));
		assertEquals(null, parser.parseExpression("100/101"));
		
	}
	
	@Test
	public void shouldMatchWithAndCents()  throws Exception{
		// cents and /100
		assertEquals(15, parser.parseExpression("15/100 cents").intValue());
		assertEquals(300, parser.parseExpression("Three dollars and 0/100 cents").intValue());
		assertEquals(7317, parser.parseExpression(" SEVENTY-THREE DOLLARS and 17/100").intValue());
		assertEquals(0, parser.parseExpression("zero and 0/100").intValue());
		assertEquals(100, parser.parseExpression("zero and 100/100").intValue());
		assertEquals(699, parser.parseExpression("six and 99\\100").intValue());
		assertEquals(326, parser.parseExpression("three and 26 / 100 dollars").intValue());
		assertEquals(100, parser.parseExpression("100/100").intValue());
		assertEquals(100, parser.parseExpression("100/100 dollars").intValue());
		assertEquals(199, parser.parseExpression("one and 99/100 dollar ").intValue());
		assertEquals(200, parser.parseExpression("one and 100/100").intValue());
		assertEquals(250, parser.parseExpression("two and 50/100").intValue());
		assertEquals(284, parser.parseExpression("2 And 84 Cents").intValue());
		assertEquals(400, parser.parseExpression("4 and 0/100").intValue());
		assertEquals(5612, parser.parseExpression("50 six and 12/100").intValue());
		assertEquals(444, parser.parseExpression("four and44/100").intValue());
		assertEquals(1037, parser.parseExpression("ten and 37/100").intValue());
		assertEquals(900, parser.parseExpression("nine and 0/100").intValue());
		assertEquals(1159, parser.parseExpression("eleven and 59/100").intValue());
		assertEquals(1861, parser.parseExpression("eighteen dollars and 61/100").intValue());
		assertEquals(1782, parser.parseExpression("seventeen and82/100").intValue());
		assertEquals(9099, parser.parseExpression("ninety and 99/100").intValue());
		assertEquals(9999, parser.parseExpression("ninety nine anD 99/100").intValue());
		assertEquals(7343, parser.parseExpression(" SEVENTY-THREE DOLLAR AND 43 / 100").intValue());
		assertEquals(8000, parser.parseExpression(" SEVENTY-NINE DOLLARS AND 100 / 100").intValue());
		assertEquals(4700, parser.parseExpression(" fourty seven dollars And zero cents ").intValue());
		assertEquals(4700, parser.parseExpression(" fourty seven dollars aNd without cents ").intValue());
	}
	
	@Test 
	public void shouldMatchDollarsWithoutAnd() throws Exception{
		assertEquals(6924, parser.parseExpression(" sixty nine dollars twenty four cents").intValue());
		assertEquals(5900, parser.parseExpression(" 50 eight dollars 100/100 ").intValue());
		assertEquals(5900, parser.parseExpression(" 50 eight and 100/100 dollars").intValue());
		assertEquals(2100, parser.parseExpression(" twenty one dollars 0 cents").intValue());
		assertEquals(7300, parser.parseExpression("seventy-three dollars 0/100").intValue());
		assertEquals(4700, parser.parseExpression(" fourty seven dollars 0 ").intValue());
		assertEquals(4700, parser.parseExpression(" fourty seven dollars no cents ").intValue());
		assertEquals(4700, parser.parseExpression(" fourty seven dollars zero cents ").intValue());
		assertEquals(4700, parser.parseExpression(" fourty seven dollars without cents ").intValue());
		assertEquals(1176, parser.parseExpression("$11  76cents").intValue());
	}
	
	@Test
	public void shouldReturnNullWithoutAnd()  throws Exception{
		assertEquals(null, parser.parseExpression("thirty 99/100"));
	}
	
	@Test
	public void shouldHandleMultiAnd() throws Exception{
		assertEquals(3754, parser.parseExpression(" thirty DOLLARS and seven DOLLARS and 54/100").intValue());
		assertEquals(3175, parser.parseExpression("thirty and one dollars and 75/100").intValue());
		assertEquals(5963, parser.parseExpression("50 and nine dollars + 63 cents").intValue());
		assertEquals(9372, parser.parseExpression("ninety and 3 dollars & 72/100 dollar").intValue());
		assertEquals(2999, parser.parseExpression("$20+ 9 & 99/100 ").intValue());
		assertEquals(4999, parser.parseExpression("$forty aNd 9 + 99 cents ").intValue());
	}
	
	@Test
	public void shouldTreatPlusAsAnd() throws Exception{
		assertEquals(7917, parser.parseExpression(" SEVENTY-nine DOLLARS + 17/100").intValue());
		assertEquals(0, parser.parseExpression("zero + 0/100").intValue());
		assertEquals(100, parser.parseExpression("zero + 100/100").intValue());
		assertEquals(699, parser.parseExpression("six + 99\\100").intValue());
		assertEquals(326, parser.parseExpression("three + 26 / 100 dollars").intValue());
		assertEquals(100, parser.parseExpression("+100/100").intValue());
		assertEquals(2996, parser.parseExpression("Twenty Nine Dollar + 96 Cents").intValue());
		assertEquals(200, parser.parseExpression("one + 100/100").intValue());
		assertEquals(250, parser.parseExpression("two + 50 cent").intValue());
		assertEquals(1000, parser.parseExpression("ten + 0/100").intValue());
		assertEquals(5612, parser.parseExpression("50 six + 12/100 dollars").intValue());
		assertEquals(444, parser.parseExpression("four +44/100").intValue());
		assertEquals(1037, parser.parseExpression("ten + 37/100").intValue());
		assertEquals(900, parser.parseExpression("nine + 0/100").intValue());
		assertEquals(1159, parser.parseExpression("eleven + 59/100").intValue());
		assertEquals(1861, parser.parseExpression("eighteen dollars + 61/100").intValue());
		assertEquals(1782, parser.parseExpression("seventeen+82/100").intValue());
		assertEquals(9099, parser.parseExpression("ninety + 99/100 ").intValue());
		assertEquals(9999, parser.parseExpression("ninety nine + 99/100  ").intValue());
		assertEquals(7343, parser.parseExpression(" SEVENTY-THREE DOLLAR + 43 / 100").intValue());
		assertEquals(8000, parser.parseExpression(" SEVENTY-NINE DOLLARS + 100 / 100").intValue());
		assertEquals(4700, parser.parseExpression(" fourty seven dollars + zero cents ").intValue());
		assertEquals(6700, parser.parseExpression(" sixty seven dollars + without cents ").intValue());
	}
	
	@Test
	public void shouldTreatAmpConnectAsAnd() throws Exception{
		assertEquals(7917, parser.parseExpression(" SEVENTY-nine DOLLARS & 17/100").intValue());
		assertEquals(0, parser.parseExpression("zero & 0/100").intValue());
		assertEquals(100, parser.parseExpression("zero & 100/100").intValue());
		assertEquals(699, parser.parseExpression("six &99\\100").intValue());
		assertEquals(326, parser.parseExpression("three& 26 / 100 dollars").intValue());
		assertEquals(100, parser.parseExpression("&100/100").intValue());
		assertEquals(200, parser.parseExpression("one & 100/100").intValue());
		assertEquals(250, parser.parseExpression("2 & 50 cent").intValue());
		assertEquals(1000, parser.parseExpression("ten & 0/100").intValue());
		assertEquals(5612, parser.parseExpression("50 six & 12/100 dollars").intValue());
		assertEquals(444, parser.parseExpression("four &44/100").intValue());
		assertEquals(1037, parser.parseExpression("ten & 37/100").intValue());
		assertEquals(900, parser.parseExpression("nine & 0/100").intValue());
		assertEquals(1159, parser.parseExpression("eleven & 59/100").intValue());
		assertEquals(1861, parser.parseExpression("eighteen dollars & 61/100").intValue());
		assertEquals(1782, parser.parseExpression("seventeen&82/100").intValue());
		assertEquals(9099, parser.parseExpression("ninety & 99/100 ").intValue());
		assertEquals(9999, parser.parseExpression("ninety nine & 99/100  ").intValue());
		assertEquals(7343, parser.parseExpression(" SEVENTY-THREE DOLLAR & 43 / 100").intValue());
		assertEquals(8000, parser.parseExpression(" SEVENTY-NINE DOLLARS &100 / 100").intValue());
		assertEquals(4700, parser.parseExpression(" fourty seven dollars & zero cents ").intValue());
		assertEquals(6700, parser.parseExpression(" sixty seven dollars & without cents ").intValue());
		assertEquals(5049, parser.parseExpression("50$ & 49 cents").intValue());
	}

	@Test
	public void shouldTreatDollarsSymbol()throws Exception{
		assertEquals(755, parser.parseExpression("$7 + 55 Cent").intValue());
		assertEquals(1039, parser.parseExpression("$10 and 39/100").intValue());
		assertEquals(1116, parser.parseExpression("$eleven + 16/100").intValue());
		assertEquals(916, parser.parseExpression("$nine and 16 cents").intValue());
		assertEquals(9616, parser.parseExpression("90 six $ and 16 cents").intValue());
		assertEquals(5400, parser.parseExpression("54$").intValue());
		assertEquals(9900, parser.parseExpression(" $99 ").intValue());
		assertEquals(10000, parser.parseExpression("$100 ").intValue());
		assertEquals(100, parser.parseExpression("$100/100 ").intValue());
		assertEquals(100, parser.parseExpression("100/100 $").intValue());
		assertEquals(9101, parser.parseExpression("$91 AND 1 CENT").intValue());
		assertEquals(1704, parser.parseExpression("17$ AND FOUR CENTS").intValue());
		assertEquals(1404, parser.parseExpression("14 AND FOUR CENTS$").intValue());
		//usd
		assertEquals(1013, parser.parseExpression("USD10 and 13/100").intValue());
		assertEquals(1176, parser.parseExpression("USD$11 and 76 / 100").intValue());
	}
	
	@Test
	public void shouldTreatDotAsAnd() throws Exception{
		assertEquals(345, parser.parseExpression("3.45").intValue());
		assertEquals(789, parser.parseExpression("7.89 dollars").intValue());
		assertEquals(1, parser.parseExpression(" .01 dollar ").intValue());
		assertEquals(100, parser.parseExpression("1.00 dollar").intValue());
		assertEquals(310, parser.parseExpression("3.10 dollar").intValue());
		assertEquals(920, parser.parseExpression("9.20 dollar").intValue());
		assertEquals(4700, parser.parseExpression(" fourty-seven.0 dollar ").intValue());
		assertEquals(123400, parser.parseExpression("1,234.00 dollar").intValue());
		assertEquals(57839801, parser.parseExpression("0,578,398.01 dollar").intValue());
		assertEquals(407839899, parser.parseExpression("4,078,398.99").intValue());
		assertEquals(407839899, parser.parseExpression("$4,078,398.99").intValue());
		assertEquals(407839899, parser.parseExpression("4,078,398.99 dollar").intValue());
	}
	
	@Test
	public void shouldReturnNullIfDotCents() throws Exception{
		assertEquals(null, parser.parseExpression("9.20 cent"));
		assertEquals(null, parser.parseExpression("1.001 dollar"));
		assertEquals(null, parser.parseExpression("1100.0103 "));
		assertEquals(null, parser.parseExpression(" fourty-seven.zero dollar "));		
		assertEquals(null, parser.parseExpression("&+ .01 dollar "));
		assertEquals(null, parser.parseExpression("9.20/100"));
		assertEquals(null, parser.parseExpression("ten.99/100"));
		assertEquals(null, parser.parseExpression("ten dollar anD .20/100"));
		
	}
	
	@Test 
	public void shouldTreateCommaAsAnd() throws Exception{
		assertEquals(7917, parser.parseExpression(" SEVENTY-nine DOLLARS, 17/100").intValue());
		assertEquals(0, parser.parseExpression("zero , 0/100").intValue());
		assertEquals(100, parser.parseExpression("zero , 100/100").intValue());
		assertEquals(699, parser.parseExpression("six , 99\\100").intValue());
		assertEquals(326, parser.parseExpression("three , 26 / 100 dollars").intValue());
		assertEquals(431200, parser.parseExpression("4,312").intValue());
		assertEquals(439900, parser.parseExpression("4,399 dollar").intValue());
		assertEquals(100000, parser.parseExpression("1,000 dollars").intValue());
		assertEquals(512439900, parser.parseExpression("5,124,399 dollar").intValue());
		assertEquals(4315, parser.parseExpression("4,315 cent").intValue());
		assertEquals(4312, parser.parseExpression("43, 12").intValue());
		assertEquals(78, parser.parseExpression(", 78").intValue());
		assertEquals(100, parser.parseExpression(", 100/100").intValue());
		assertEquals(2996, parser.parseExpression("Twenty Nine Dollar , 96 Cents").intValue());
		assertEquals(200, parser.parseExpression("one ,100/100").intValue());
		assertEquals(250, parser.parseExpression("two, 50 cent").intValue());
		assertEquals(1000, parser.parseExpression("ten , 0/100").intValue());
		assertEquals(5612, parser.parseExpression("50 six , 12/100 dollars").intValue());
		assertEquals(444, parser.parseExpression("four ,44/100").intValue());
		assertEquals(9854, parser.parseExpression("98 , 54/100").intValue());
		assertEquals(1037, parser.parseExpression("ten ,37/100").intValue());
		assertEquals(900, parser.parseExpression("nine , 0/100").intValue());
		assertEquals(1159, parser.parseExpression("eleven , 59/100").intValue());
		assertEquals(1861, parser.parseExpression("eighteen dollars , 61/100").intValue());
		assertEquals(1782, parser.parseExpression("seventeen, 82/100").intValue());
		assertEquals(9099, parser.parseExpression("ninety , 99/100 ").intValue());
		assertEquals(9999, parser.parseExpression("ninety nine , 99/100  ").intValue());
		assertEquals(7343, parser.parseExpression(" SEVENTY-THREE DOLLAR ,43 / 100").intValue());
		assertEquals(8000, parser.parseExpression(" SEVENTY-NINE DOLLARS , 100 / 100").intValue());
		assertEquals(4700, parser.parseExpression(" fourty seven dollars , zero cents ").intValue());
		assertEquals(6700, parser.parseExpression(" sixty seven dollars , without cents ").intValue());
	}
	
	@Test 
	public void shouldHandleMultiDashConnect() throws Exception{
		assertEquals(7917, parser.parseExpression(" SEVENTY-nine DOLLARS -- 17/100").intValue());
		assertEquals(0, parser.parseExpression("zero --- 0/100").intValue());
		assertEquals(100, parser.parseExpression("zero ---- 100/100").intValue());
		assertEquals(699, parser.parseExpression("six --- 99\\100").intValue());
		assertEquals(326, parser.parseExpression("three ----- 26 / 100 dollars").intValue());
		assertEquals(100, parser.parseExpression("------- 100/100").intValue());
		assertEquals(2996, parser.parseExpression("Twenty Nine Dollar --- 96 Cents").intValue());
		assertEquals(200, parser.parseExpression("one ----- 100/100").intValue());
		assertEquals(250, parser.parseExpression("two ----- 50 cent").intValue());
		assertEquals(1000, parser.parseExpression("ten ----- 0/100").intValue());
		assertEquals(5612, parser.parseExpression("50 six ------ 12/100 dollars").intValue());
		assertEquals(947, parser.parseExpression("9 --- 47 cent").intValue());
		assertEquals(444, parser.parseExpression("four ---44/100").intValue());
		assertEquals(1037, parser.parseExpression("ten --- 37/100").intValue());
		assertEquals(900, parser.parseExpression("nine ------ 0/100").intValue());
		assertEquals(1159, parser.parseExpression("eleven ----- 59/100").intValue());
		assertEquals(1861, parser.parseExpression("eighteen dollars --- 61/100").intValue());
		assertEquals(1782, parser.parseExpression("seventeen---82/100").intValue());
		assertEquals(9099, parser.parseExpression("ninety --- 99/100 ").intValue());
		assertEquals(9999, parser.parseExpression("ninety nine --- 99/100  ").intValue());
		assertEquals(7343, parser.parseExpression(" SEVENTY-THREE DOLLAR --- 43 / 100").intValue());
		assertEquals(8000, parser.parseExpression(" SEVENTY-NINE DOLLARS --- 100 / 100").intValue());
		assertEquals(4700, parser.parseExpression(" fourty seven dollars --- zero cents ").intValue());
		assertEquals(6700, parser.parseExpression(" sixty seven dollars --- without cents ").intValue());
	}
	
	@Test 
	public void shouldTreatTildeAsAnd() throws Exception{
		assertEquals(7917, parser.parseExpression(" SEVENTY-nine DOLLARS ~ 17/100").intValue());
		assertEquals(0, parser.parseExpression("zero ~ 0/100").intValue());
		assertEquals(100, parser.parseExpression("zero ~ 100/100").intValue());
		assertEquals(699, parser.parseExpression("six ~ 99\\100").intValue());
		assertEquals(326, parser.parseExpression("three ~ 26 / 100 dollars").intValue());
		assertEquals(100, parser.parseExpression("~ 100/100").intValue());
		assertEquals(2996, parser.parseExpression("Twenty Nine Dollar ~ 96 Cents").intValue());
		assertEquals(200, parser.parseExpression("one ~100/100").intValue());
		assertEquals(250, parser.parseExpression("two ~ 50 cent").intValue());
		assertEquals(1000, parser.parseExpression("ten ~ 0/100").intValue());
		assertEquals(5612, parser.parseExpression("50 six ~ 12/100 dollars").intValue());
		assertEquals(444, parser.parseExpression("four ~44/100").intValue());
		assertEquals(9854, parser.parseExpression("98 ~ 54/100").intValue());
		assertEquals(1037, parser.parseExpression("ten ~ 37/100").intValue());
		assertEquals(900, parser.parseExpression("nine ~ 0/100").intValue());
		assertEquals(1159, parser.parseExpression("eleven ~ 59/100").intValue());
		assertEquals(1861, parser.parseExpression("eighteen dollars ~ 61/100").intValue());
		assertEquals(1782, parser.parseExpression("seventeen~ 82/100").intValue());
		assertEquals(9099, parser.parseExpression("ninety ~ 99/100 ").intValue());
		assertEquals(9999, parser.parseExpression("ninety nine ~ 99/100  ").intValue());
		assertEquals(7343, parser.parseExpression(" SEVENTY-THREE DOLLAR ~ 43 / 100").intValue());
		assertEquals(8000, parser.parseExpression(" SEVENTY-NINE DOLLARS ~ 100 / 100").intValue());
		assertEquals(4700, parser.parseExpression(" fourty seven dollars ~ zero cents ").intValue());
		assertEquals(6700, parser.parseExpression(" sixty seven dollars ~ without cents ").intValue());
	}
	

	
	@Test
	public void shouldTreatColonAsAnd() throws Exception{
		assertEquals(345, parser.parseExpression("3:45 cents").intValue());
		assertEquals(6700, parser.parseExpression("67 :").intValue());
		assertEquals(1800, parser.parseExpression("18:").intValue());
		assertEquals(1011, parser.parseExpression("10:11").intValue());
		assertEquals(4336, parser.parseExpression("43:36").intValue());
		assertEquals(4336, parser.parseExpression("43 : 36 cents").intValue());
		assertEquals(98, parser.parseExpression(":98").intValue());
		assertEquals(75, parser.parseExpression(":75").intValue());
		assertEquals(15, parser.parseExpression(":15 cent ").intValue());
		assertEquals(1211, parser.parseExpression("$12 : 11 cent ").intValue());
		assertEquals(9999, parser.parseExpression("$99 : 99/100 ").intValue());
		assertEquals(1398, parser.parseExpression("Thirteen Dollars : 98 Cents ").intValue());
		assertEquals(487, parser.parseExpression("Four : 87/100").intValue());
		assertEquals(9455, parser.parseExpression("94 Dollar : 55 Cent").intValue());
		assertEquals(8785, parser.parseExpression("87 dollar : eighty five cents").intValue());
		assertEquals(7658, parser.parseExpression(" 76 Dollar : Fifty Eight Cents ").intValue());
	}

	@Test
	public void shouldTreatSemicolonAsAnd() throws Exception{
		assertEquals(597, parser.parseExpression("5;97 cents").intValue());
		assertEquals(6700, parser.parseExpression("67 ;").intValue());
		assertEquals(1800, parser.parseExpression("18;").intValue());
		assertEquals(1011, parser.parseExpression("10:11").intValue());
		assertEquals(4336, parser.parseExpression("43:36").intValue());
		assertEquals(4336, parser.parseExpression("43 : 36 cents").intValue());
		assertEquals(98, parser.parseExpression(";98").intValue());
		assertEquals(75, parser.parseExpression(";75").intValue());
		assertEquals(15, parser.parseExpression(";15 cent ").intValue());
		assertEquals(1211, parser.parseExpression("$12 ; 11 cent ").intValue());
		assertEquals(9999, parser.parseExpression("$99 ; 99/100 ").intValue());
		assertEquals(1398, parser.parseExpression("Thirteen Dollars ; 98 Cents ").intValue());
		assertEquals(487, parser.parseExpression("Four ; 87/100").intValue());
		assertEquals(9455, parser.parseExpression("94 Dollar ; 55 Cent").intValue());
		assertEquals(8785, parser.parseExpression("87 dollar ; eighty five cents").intValue());
		assertEquals(7658, parser.parseExpression(" 76 Dollar ; Fifty Eight Cents ").intValue());
	}
	
	@Test
	public void shouldTreatSeperateAsAnd() throws Exception{
		assertEquals(114, parser.parseExpression("1|14 cents").intValue());
		assertEquals(6700, parser.parseExpression("67 |").intValue());
		assertEquals(1800, parser.parseExpression("18|").intValue());
		assertEquals(1011, parser.parseExpression("10|11").intValue());
		assertEquals(4336, parser.parseExpression("43|36").intValue());
		assertEquals(4336, parser.parseExpression("43 | 36 cents").intValue());
		assertEquals(98, parser.parseExpression("|98").intValue());
		assertEquals(75, parser.parseExpression("|75").intValue());
		assertEquals(15, parser.parseExpression("|15 cent ").intValue());
		assertEquals(1211, parser.parseExpression("$12 | 11 cent ").intValue());
		assertEquals(9999, parser.parseExpression("$99 | 99/100 ").intValue());
		assertEquals(1398, parser.parseExpression("Thirteen Dollars | 98 Cents ").intValue());
		assertEquals(487, parser.parseExpression("Four | 87/100").intValue());
		assertEquals(9455, parser.parseExpression("94 Dollar | 55 Cent").intValue());
		assertEquals(8785, parser.parseExpression("87 dollar | eighty five cents").intValue());
		assertEquals(7658, parser.parseExpression(" 76 Dollar | Fifty Eight Cents ").intValue());
	}
	
	@Test
	public void shouldHandleStarSymbol()  throws Exception{
		assertEquals(7317, parser.parseExpression(" ** SEVENTY-THREE DOLLARS and 17/100 *").intValue());
		assertEquals(0, parser.parseExpression("*** zero and 0/100 **").intValue());
		assertEquals(100, parser.parseExpression("*zero and 100/100****").intValue());
		assertEquals(699, parser.parseExpression("*****six*and*99\\100******").intValue());
		assertEquals(326, parser.parseExpression("*three**and*26**/**100**dollars****").intValue());
		assertEquals(100, parser.parseExpression("*$100/100*").intValue());
		assertEquals(100, parser.parseExpression("*100/100 dollars*").intValue());
		assertEquals(199, parser.parseExpression("*one and 99/100 dollar** ").intValue());
		assertEquals(200, parser.parseExpression("*one and *100/100").intValue());
		assertEquals(250, parser.parseExpression("*two **and *50/100").intValue());
		assertEquals(284, parser.parseExpression("*2 And **84 Cents").intValue());
		assertEquals(400, parser.parseExpression("*4** and** 0/100").intValue());
		assertEquals(5612, parser.parseExpression("*50** six** and** 12/100").intValue());
		assertEquals(444, parser.parseExpression("**four** and44/100").intValue());
		assertEquals(1037, parser.parseExpression("***ten **and 37/100").intValue());
		assertEquals(900, parser.parseExpression("***nine** and** 0/100").intValue());
		assertEquals(1159, parser.parseExpression("***eleven*** and*** 59/100").intValue());
		assertEquals(1861, parser.parseExpression("***eighteen*** dollars** and** 61/100").intValue());
		assertEquals(1782, parser.parseExpression("seventeenand82/100").intValue());
		assertEquals(9099, parser.parseExpression("ninety and 99/100").intValue());
		assertEquals(9999, parser.parseExpression("ninety nine anD 99/100").intValue());
		assertEquals(7343, parser.parseExpression(" SEVENTY-THREE DOLLAR AND 43 / 100").intValue());
		assertEquals(8000, parser.parseExpression(" SEVENTY-NINE DOLLARS AND 100 / 100").intValue());
		assertEquals(4700, parser.parseExpression(" fourty seven dollars And zero cents ").intValue());
		assertEquals(4700, parser.parseExpression(" fourty seven dollars aNd without cents ").intValue());
	}
}
