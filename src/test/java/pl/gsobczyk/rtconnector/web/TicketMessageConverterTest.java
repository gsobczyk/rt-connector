package pl.gsobczyk.rtconnector.web;

import junit.framework.Assert;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

import pl.gsobczyk.rtconnector.domain.Ticket;

public class TicketMessageConverterTest {
	TicketMessageConverter converter;
	@Before
	public void before(){
		converter = new TicketMessageConverter();
	}
	
	@Test
	public void shouldConvertToString() throws Exception {
		// given
		

		// when

		// then

	}
	
	@Test
	public void shouldConvertFromString() throws Exception {
		// given
		String response = IOUtils.toString(new ClassPathResource("sample-ticket.txt").getInputStream());
		
		// when
		Ticket ticket = converter.convertToEntity(response);

		// then
		Assert.assertEquals(ticket.getId(), Long.valueOf(64128L));
	}
}
