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
		//assertEquals(1, parser.parseExpression("penny").intValue());
		//assertEquals(5, parser.parseExpression("nickel").intValue());
		//assertEquals(25, parser.parseExpression("Quarter").intValue());
		//assertEquals(50, parser.parseExpression("Two Quarter").intValue());
		
	}

	@Test
	public void shouldHandleZeroAmount() throws Exception {
		assertEquals(0, parser.parseExpression("zero").intValue());
		assertEquals(0, parser.parseExpression("zero and ZERO").intValue());
		assertEquals(0, parser.parseExpression("ZERO zero ").intValue());
		assertEquals(9000, parser.parseExpression("ninety zero").intValue());
		assertEquals(7000, parser.parseExpression("seventy - zero").intValue());
	}

	@Test 
	public void shouldReturnNullFoInvalidString() throws Exception{
		assertEquals(null, parser.parseExpression("red"));
		assertEquals(null, parser.parseExpression(""));
		assertEquals(null, parser.parseExpression("-"));
		assertEquals(null, parser.parseExpression("|"));
		assertEquals(null, parser.parseExpression("[]"));
		assertEquals(null, parser.parseExpression("[-]"));
		assertEquals(null, parser.parseExpression("[*]"));
		assertEquals(null, parser.parseExpression(" four-teen "));
		assertEquals(null, parser.parseExpression(" fi/ve "));
		assertEquals(null, parser.parseExpression(" thir*teen "));
	}
	
	@Test
	public void shouldIgnoreSpace() throws Exception {
		assertEquals(800, parser.parseExpression(" eight ").intValue());
		assertEquals(400, parser.parseExpression("Four ").intValue());
		assertEquals(1400, parser.parseExpression(" fourteen ").intValue());
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
	}
	
	@Test
	public void shouldReturnNullOnlyDollar() throws Exception{
		assertEquals(null, parser.parseExpression("dollar"));
		assertEquals(null, parser.parseExpression("8 dollars"));
		assertEquals(null, parser.parseExpression("18 dollars"));
	}
	
	@Test
	public void shouldMatchWithTwoDigits() throws Exception{
		assertEquals(2100, parser.parseExpression("twenty one").intValue());
		assertEquals(7200, parser.parseExpression("seventy two").intValue());
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
		assertEquals(7500, parser.parseExpression(" SEVENTY-THREE DOLLARS and two").intValue());
		assertEquals(7300, parser.parseExpression(" SEVENTY-THREE D-O-L-L-A-RS").intValue());
		assertEquals(7300, parser.parseExpression(" SEVENTY-THREE DO L LAR S").intValue());
		assertEquals(4700, parser.parseExpression(" forty  and DOLLARS seven").intValue());
		assertEquals(3700, parser.parseExpression(" thirty DOLLARS and seven").intValue());
		assertEquals(3700, parser.parseExpression(" thirty DOLLARS and seven DOLLARS").intValue());
		assertEquals(8200, parser.parseExpression(" eighty - two").intValue());
		assertEquals(8300, parser.parseExpression(" eighty- three").intValue());
		assertEquals(8500, parser.parseExpression(" eighty-DOLLARS-five").intValue());
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
		assertEquals(null, parser.parseExpression("one hundred"));
		
	}
	
	@Test
	public void shouldMatchOnlyCents()  throws Exception{
		assertEquals(87, parser.parseExpression("87/100").intValue());
		assertEquals(12, parser.parseExpression("12/   100").intValue());
		assertEquals(45, parser.parseExpression("45 /100").intValue());
		assertEquals(76, parser.parseExpression("7 6 / 1 00").intValue());
		assertEquals(20, parser.parseExpression("20/ 10 0").intValue());
		assertEquals(39, parser.parseExpression(" 3  9 / 1 0 0").intValue());
		assertEquals(0, parser.parseExpression("0/100").intValue());
		assertEquals(1, parser.parseExpression("1\\100").intValue());
		assertEquals(94, parser.parseExpression("9 4 \\ 100").intValue());
		assertEquals(19, parser.parseExpression("19/100 dollars").intValue());
		assertEquals(5, parser.parseExpression("5/100 dollar").intValue());
	}
	
	@Test
	public void shouReturnNullForInvalidCents() throws Exception {
		
		assertEquals(null, parser.parseExpression("  /100"));
		assertEquals(null, parser.parseExpression("/100"));
		assertEquals(null, parser.parseExpression("/"));
		assertEquals(null, parser.parseExpression("//"));
		assertEquals(null, parser.parseExpression("\\"));
		assertEquals(null, parser.parseExpression("/0"));
		assertEquals(null, parser.parseExpression("10"));
		assertEquals(null, parser.parseExpression("101/100"));
		assertEquals(null, parser.parseExpression("45a461/100/1b00"));
		assertEquals(null, parser.parseExpression("-1/100"));
		assertEquals(null, parser.parseExpression("5-/100"));
		assertEquals(null, parser.parseExpression("abcd/100"));
		assertEquals(null, parser.parseExpression("&/*100"));
		assertEquals(null, parser.parseExpression("43?/100"));		
		assertEquals(null, parser.parseExpression("(*)/(100)"));
		assertEquals(null, parser.parseExpression("(*)/(99)"));
	}
	
	@Test
	public void shouldMatchWithAndCents()  throws Exception{
		assertEquals(7517, parser.parseExpression(" SEVENTY-THREE DOLLARS and two and 17/100").intValue());
		assertEquals(0, parser.parseExpression("zero and 0/100").intValue());
		assertEquals(100, parser.parseExpression("zero and 100/100").intValue());
		assertEquals(699, parser.parseExpression("six and 99\\100").intValue());
		assertEquals(326, parser.parseExpression("three and 26 / 100 dollars").intValue());
		assertEquals(100, parser.parseExpression("100/100").intValue());
		assertEquals(200, parser.parseExpression("one and 100/100").intValue());
		assertEquals(250, parser.parseExpression("two and 50/100").intValue());
		assertEquals(444, parser.parseExpression("four and44/100").intValue());
		assertEquals(681, parser.parseExpression("six a n d 81/100").intValue());
		assertEquals(1037, parser.parseExpression("ten and 37/100").intValue());
		assertEquals(900, parser.parseExpression("nine and 0/100").intValue());
		assertEquals(1159, parser.parseExpression("eleven and 59/100").intValue());
		assertEquals(1861, parser.parseExpression("eighteen dollars and 61/100").intValue());
		assertEquals(3754, parser.parseExpression(" thirty DOLLARS and seven DOLLARS and 54/100").intValue());
		assertEquals(1861, parser.parseExpression("eighteen d-o-l-l-a-r s and 61/100").intValue());
		assertEquals(3175, parser.parseExpression("thirty one d-o-l-l-a-r-s and 75/100").intValue());
		assertEquals(1782, parser.parseExpression("seventeenand82/100").intValue());
		assertEquals(9099, parser.parseExpression("ninety and 99/100").intValue());
		assertEquals(9999, parser.parseExpression("ninety nine and 99/100").intValue());
		assertEquals(7343, parser.parseExpression(" SEVENTY-THREE DOLLARS AND 43 / 100").intValue());
		assertEquals(8000, parser.parseExpression(" SEVENTY-NINE DOLLARS AND 100 / 100").intValue());
		assertEquals(3175, parser.parseExpression("thirty and one dollars and 75/100").intValue());
	}
	
	@Test
	public void shouldReturnNullWithoutAnd()  throws Exception{
		assertEquals(null, parser.parseExpression("thirty 99/100"));
		assertEquals(null, parser.parseExpression("thirty and99and/100"));
		assertEquals(null, parser.parseExpression("thirty and99and/100and"));
		assertEquals(null, parser.parseExpression("thirty/100"));
	}
	

}
