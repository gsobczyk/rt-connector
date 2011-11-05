package pl.gsobczyk.rtconnector.web;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Collection;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import pl.gsobczyk.rtconnector.configuration.AppConfig;
import pl.gsobczyk.rtconnector.domain.Queue;
import pl.gsobczyk.rtconnector.service.RTDao;

import com.google.common.collect.Lists;

public class SimpleRTAutocompleteServiceTest {
	private SimpleRTAutocompleteService autocomplete;
	private RTDao rtDao;

	@Before
	public void before() {
		autocomplete = new SimpleRTAutocompleteService();
		rtDao = mock(RTDao.class);
		autocomplete.setRtDao(rtDao);
	}
	
	@Test
	public void shouldFindQueue() throws Exception {
		// given
		Queue q1 = new Queue();
		q1.setName("abcdef");
		Queue q2 = new Queue();
		q2.setName("ghijkl");
		List<Queue> list = Lists.newArrayList(q1, q2);
		when(rtDao.getAllQueues()).thenReturn(list);

		// when
		Collection<Queue> found = autocomplete.findQueues("CD");

		// then
		Assert.assertTrue(found.contains(q1));
		Assert.assertFalse(found.contains(q2));
	}
	
	@Test
	public void shouldFindProjects() throws Exception {
		// given
		String client = "MIG";
		String project = "2008";
		AnnotationConfigApplicationContext app = new AnnotationConfigApplicationContext(AppConfig.class);
		SimpleRTAutocompleteService autocompleteService = app.getBean(SimpleRTAutocompleteService.class);
		
		// when
		Iterable<String> result = autocompleteService.findProjects(client, project);

		// then
		Assert.assertTrue(Lists.newArrayList(result).contains("MIG/2008-MIG"));
	}
}