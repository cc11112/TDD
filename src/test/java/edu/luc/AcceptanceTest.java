package edu.luc;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import org.junit.Before;
import org.junit.Test;

import edu.luc.clearing.CheckParser;


public class AcceptanceTest {

	private CheckParser parser;

	@Before
	public void setUp(){
		parser = new CheckParser();
	}

	@Test
	public void shouldParseWholeValuesLessThanTen() throws Exception {
		assertThat(parsedAmountOf("one"), is(equalTo(100)));
		assertThat(parsedAmountOf("two"), is(equalTo(200)));
		assertThat(parsedAmountOf("three"), is(equalTo(300)));
		assertThat(parsedAmountOf("four"), is(equalTo(400)));
		assertThat(parsedAmountOf("five"), is(equalTo(500)));
		assertThat(parsedAmountOf("six"), is(equalTo(600)));
		assertThat(parsedAmountOf("seven"), is(equalTo(700)));
		assertThat(parsedAmountOf("eight"), is(equalTo(800)));
		assertThat(parsedAmountOf("nine"), is(equalTo(900)));
		assertThat(parsedAmountOf("ten"), is(equalTo(1000)));
		assertThat(parsedAmountOf("eleven"), is(equalTo(1100)));
		assertThat(parsedAmountOf("twelve"), is(equalTo(1200)));
		assertThat(parsedAmountOf("thirteen"), is(equalTo(1300)));
		assertThat(parsedAmountOf("fourteen"), is(equalTo(1400)));
		assertThat(parsedAmountOf("fifteen"), is(equalTo(1500)));
		assertThat(parsedAmountOf("sixteen"), is(equalTo(1600)));
		assertThat(parsedAmountOf("seventeen"), is(equalTo(1700)));
		assertThat(parsedAmountOf("eighteen"), is(equalTo(1800)));
		assertThat(parsedAmountOf("nineteen"), is(equalTo(1900)));
		assertThat(parsedAmountOf("twenty"), is(equalTo(2000)));
		assertThat(parsedAmountOf("thirty"), is(equalTo(3000)));
		assertThat(parsedAmountOf("forty"), is(equalTo(4000)));
		assertThat(parsedAmountOf("fifty"), is(equalTo(5000)));
		assertThat(parsedAmountOf("sixty"), is(equalTo(6000)));
		assertThat(parsedAmountOf("seventy"), is(equalTo(7000)));
		assertThat(parsedAmountOf("eighty"), is(equalTo(8000)));
		assertThat(parsedAmountOf("ninety"), is(equalTo(9000)));
	}

	private int parsedAmountOf(String amount) {
		return parser.parseExpression(amount).intValue();
	}
	
	
}
