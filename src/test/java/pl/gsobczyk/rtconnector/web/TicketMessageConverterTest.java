package pl.gsobczyk.rtconnector.web;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayOutputStream;
import java.net.URLDecoder;
import java.util.Map;

import junit.framework.Assert;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpOutputMessage;

import pl.gsobczyk.rtconnector.domain.Ticket;
import pl.gsobczyk.rtconnector.domain.TicketField;

import com.google.common.base.Charsets;

public class TicketMessageConverterTest {
	TicketMessageConverter converter;
	@Before
	public void before(){
		converter = new TicketMessageConverter();
	}
	
	@Test
	public void shouldConvertFromString() throws Exception {
		// given
		String response = IOUtils.toString(new ClassPathResource("sample-ticket.txt").getInputStream());
		Map<String, String> map = converter.convertToMap(response);
		// when
		Ticket ticket = converter.convertToEntity(map);
		// then
		Assert.assertEquals(ticket.getId(), Long.valueOf(64128L));
	}
	
	@Test
	public void shouldWriteTicket() throws Exception {
		// given
		Ticket t = new Ticket();
		t.setName("test");
		HttpOutputMessage outputMessage = mock(HttpOutputMessage.class);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		when(outputMessage.getBody()).thenReturn(baos);
		when(outputMessage.getHeaders()).thenReturn(new HttpHeaders());
		// when
		converter.writeInternal(t, outputMessage);
		// then
		Assert.assertTrue(baos.toString().contains(TicketField.NAME.getName()+": "+t.getName()));
	}
	
	@Test
	public void shouldEncodePolishLetters() throws Exception {
		// given
		String letters = "ąćęłńóźżĄĆĘŁŃÓŹŻ=&";
		Ticket t = new Ticket();
		t.setName(letters);
		HttpOutputMessage outputMessage = mock(HttpOutputMessage.class);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		when(outputMessage.getBody()).thenReturn(baos);
		when(outputMessage.getHeaders()).thenReturn(new HttpHeaders());
		// when
		converter.writeInternal(t, outputMessage);
		// then
		Assert.assertTrue(URLDecoder.decode(baos.toString(), Charsets.UTF_8.name()).contains(letters));
	}
}
