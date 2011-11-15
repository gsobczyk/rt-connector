package pl.gsobczyk.rtconnector.service;

import static org.mockito.Mockito.mock;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import pl.gsobczyk.rtconnector.configuration.AppConfigTest;
import pl.gsobczyk.rtconnector.domain.Queue;
import pl.gsobczyk.rtconnector.domain.RestStatus;
import pl.gsobczyk.rtconnector.domain.Ticket;

import com.google.common.base.Splitter;
import com.google.common.collect.Maps;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={AppConfigTest.class})
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
	public void shouldAddTime() throws Exception {
		// given
		Long id=70670L;
		Ticket ticket = rtDao.getTicket(id);
		Integer minutes=10+ticket.getTimeWorked();

		// when
		rtDao.addTime(ticket, 10, null);

		// then
		Assert.assertEquals(rtDao.getTicket(id).getTimeWorked(), minutes);
	}
	
	@Test
	public void shouldCreateTicket() throws Exception {
		// given
		SimpleRTDao dao = new SimpleRTDao();
		RestTemplate template = mock(RestTemplate.class);
		dao.setRestTemplate(template);
		RestStatus status = new RestStatus();
		status.setMessage("Ticket 70883 created.");
		Mockito.when(template.postForObject(Mockito.anyString(), Mockito.any(Ticket.class), Mockito.eq(RestStatus.class))).thenReturn(status);
		// when
		Ticket result = dao.createTicket("AAleSmietnik", null, null, null, "test rest");
		// then
		Assert.assertNotNull(result.getId());
	}
	
	@Test
	public void shouldFillQueueCache() throws Exception {
		// given
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
		Splitter optionSplitter = Splitter.onPattern("(</option\\s*>)|(<option\\s+?value=\")|\\1\\s*\\2").trimResults().omitEmptyStrings();
		Splitter keySplitter = Splitter.onPattern("\"[^>]*>").trimResults().omitEmptyStrings();
		// when
		Map<String, String> map = Maps.newHashMap(optionSplitter.withKeyValueSeparator(keySplitter).split(result));
		// then
		Assert.assertTrue(map.size()==39);
	}
	
	@Test
	public void shouldFindTestTickets() throws Exception {
		// given

		// when
		List<Ticket> tickets = rtDao.findTickets("AAleSmietnik", "", "", "Radosław Przychoda", "", false);

		// then
		Assert.assertFalse(tickets.isEmpty());
	}
	
	@Test
	public void shouldTakeTicket() throws Exception {
		// given
		Ticket ticket = new Ticket();
		ticket.setId(70670L);

		// when
		RestStatus result = rtDao.takeTicket(ticket);

		// then
		result.getStatus().startsWith("200");
	}

}
