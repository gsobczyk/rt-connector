package pl.gsobczyk.rtconnector.service;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import pl.gsobczyk.rtconnector.configuration.AppConfig;
import pl.gsobczyk.rtconnector.domain.Queue;
import pl.gsobczyk.rtconnector.domain.Ticket;

import com.google.common.base.Splitter;
import com.google.common.collect.Maps;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={AppConfig.class})
public class SimpleRTDaoTest {
	@Autowired private RestTemplate restTemplate;
	@Autowired private Environment env;
	@Autowired private SimpleRTDao rtDao;
	
	@Before
	public void before(){
		rtDao.login();
	}

	@Test
	public void shouldLoginToRt() throws Exception {
		// given
		// when
		// then
		Assert.assertTrue(rtDao.isLogged());
	}
	
	@Test
	public void shouldLogout() throws Exception {
		// given
		// when
		rtDao.logout();

		// then
		restTemplate.getForObject(env.getProperty(SimpleRTDao.P_RT_URL)+SimpleRTDao.D_REST_CONTEXT, String.class);
	}
	
	@Test
	public void shouldFindTicketById() throws Exception {
		// given
		Long id=70670L;

		// when
		Ticket ticket = rtDao.getTicket(id);

		// then
		Assert.assertEquals(ticket.getId(), id);
	}
	
	@Test
	public void shouldFillQueueCache() throws Exception {
		// given
		rtDao.login();

		// when
		List<Queue> queues = rtDao.getAllQueues();

		// then
		Assert.assertTrue(!CollectionUtils.isEmpty(queues));
	}

	@Test
	public void shouldFindQueuesInHtml() throws Exception {
		// given
		String result = IOUtils.toString(new ClassPathResource("tools.html").getInputStream());
		Pattern p = Pattern.compile(SimpleRTDao.QUEUE_REGEX, Pattern.MULTILINE);
		// when
		Matcher m = p.matcher(result);
		boolean matches = m.find();
		// then
		Assert.assertTrue(matches);
	}
	
	@Test
	public void shouldParseQueuesInHtml() throws Exception {
		// given
		String result = IOUtils.toString(new ClassPathResource("queues.fhtml").getInputStream());
//		result = result.replaceAll("\\r?\\n", "");
		Splitter optionSplitter = Splitter.onPattern("(</option\\s*>)|(<option\\s+?value=\")|\\1\\s*\\2").trimResults().omitEmptyStrings();
		Splitter keySplitter = Splitter.onPattern("\"[^>]*>").trimResults().omitEmptyStrings();
		Map<String, String> map = Maps.newHashMap(optionSplitter.withKeyValueSeparator(keySplitter).split(result));
		System.out.println(map.size());
		// when
		// then
//		Assert.assertTrue(matches);
	}
}