package pl.gsobczyk.rtconnector.domain;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Assert;
import org.junit.Test;

public class TicketTest {
	@Test
	public void shouldParseDirectTicket() throws Exception {
		// given
		String test="DRIW-Adacko > MIG/MIG > #70907: Błedy w xxxxxxx";
		// when
		boolean result = test.matches(Ticket.REGEX_DIRECT);
		// then
		Assert.assertTrue(result);
	}
	
	@Test
	public void shouldNotParseDirectTicket() throws Exception {
		// given
		String test="DRIW-Adacko > MIG/MIG > #70907: Błedy w xxxxxxx > xxxxx";
		// when
		boolean result = test.matches(Ticket.REGEX_DIRECT);
		// then
		Assert.assertFalse(result);
	}

	@Test
	public void shouldNotParseParentTicket() throws Exception {
		// given
		String test="DRIW-Adacko > MIG/MIG > #70907: Błedy w xxxxxxx";
		// when
		boolean result = test.matches(Ticket.REGEX_PARENT);
		// then
		Assert.assertFalse(result);
	}
	
	@Test
	public void shouldParseParentTicket() throws Exception {
		// given
		String test="DRIW-Adacko > MIG/MIG > #70907: Błedy w xxxxxxx > xxxxx";
		// when
		boolean result = test.matches(Ticket.REGEX_PARENT);
		// then
		Assert.assertTrue(result);
	}

	@Test
	public void shouldFullTicket() throws Exception {
		// given
		String test="DRIW-Adacko > MIG/MIG > Radosław > Błedy w xxxxxxx";
		// when
		boolean result = test.matches(Ticket.REGEX_FULL);
		// then
		Assert.assertTrue(result);
	}
	
	@Test
	public void shouldExtractClearingFromFullTicket() throws Exception {
		// given
		String test="DRIW-Adacko > MIG/MIG > Radosław > Błedy w xxxxxxx";
		Pattern p = Pattern.compile(Ticket.REGEX_FULL);
		// when
		Matcher m = p.matcher(test);
		m.find();
		// then
		Assert.assertEquals(m.group(4).trim(), "Radosław");
	}
}
