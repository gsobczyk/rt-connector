package pl.gsobczyk.rtconnector.web;

import java.util.Map;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import pl.gsobczyk.rtconnector.domain.Queue;
import pl.gsobczyk.rtconnector.domain.QueueField;

import com.google.common.collect.ImmutableMap;

public class QueueMessageConverterTest {
	QueueMessageConverter converter;
	@Before
	public void before(){
		converter = new QueueMessageConverter();
	}
	
	@Test
	public void shouldConvertFromString() throws Exception {
		// given
		Map<String, String> map = ImmutableMap.of(QueueField.ID.getName(), "10", QueueField.NAME.getName(), "test");
		// when
		Queue queue = converter.convertToEntity(map);
		// then
		Assert.assertEquals(queue.getId(), Long.valueOf(10L));
	}
	
}
