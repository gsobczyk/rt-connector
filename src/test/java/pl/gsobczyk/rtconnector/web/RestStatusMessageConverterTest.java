package pl.gsobczyk.rtconnector.web;

import java.util.Map;

import junit.framework.Assert;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

import pl.gsobczyk.rtconnector.domain.RestStatus;

public class RestStatusMessageConverterTest {
	RestStatusMessageConverter converter;
	@Before
	public void before(){
		converter = new RestStatusMessageConverter();
	}
	
	@Test
	public void shouldConvertFromString() throws Exception {
		// given
		String response = IOUtils.toString(new ClassPathResource("create-response.txt").getInputStream());
		Map<String, String> map = converter.convertToMap(response);
		
		// when
		RestStatus status = converter.convertToEntity(map);

		// then
		Assert.assertEquals(status.getMessage(), "Ticket 70883 created.");
	}
}
