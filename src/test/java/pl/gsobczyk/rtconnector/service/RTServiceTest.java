package pl.gsobczyk.rtconnector.service;

import java.util.Collection;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import pl.gsobczyk.rtconnector.configuration.AppConfigTest;
import pl.gsobczyk.rtconnector.domain.Queue;
import pl.gsobczyk.rtconnector.domain.Ticket;
import pl.gsobczyk.rtconnector.web.AutocompleteChooser;
import pl.gsobczyk.rtconnector.web.RTAutocompleteService;

import com.google.common.collect.Lists;

public class RTServiceTest {
	private static final String FULL_QUERY = "queue > client/project > clearing > parent > ticket name";
	private static final String PARENT_QUERY = "test > test > test > #9997: test name > test name";
	private static final String DIRECT_QUERY = "test > test > test > #9999: test name";
	private RTService service;
	private AutocompleteChooser autocompleteChooser;
	private RTAutocompleteService autocompleteService;
	
	@Before
	public void before(){
		service = new RTService();
		autocompleteChooser = new AutocompleteChooser.FirstAutocompleteChooser();
		autocompleteService = new RTAutocompleteService() {
			@Override public Collection<Queue> findQueues(String like) {
				Queue queue = new Queue();
				queue.setName(like);
				return Lists.newArrayList(queue);
			}
			@Override public Iterable<String> findProjects(String clientLike, String projectLike) {
				return Lists.newArrayList(clientLike+"/"+projectLike);
			}
			@Override public Iterable<String> findClients(String like) {
				return Lists.newArrayList(like);
			}
			@Override public Iterable<String> findClearings(String like) {
				return Lists.newArrayList(like);
			}
		};
		service.setAutocompleteService(autocompleteService);
		service.setAutocompleteChooser(autocompleteChooser);
	}

	@Test
	public void shouldMatchDirectTicket() throws Exception {
		// given
		String name = DIRECT_QUERY;
		// when
		boolean matches = name.matches(RTService.DIRECT_TICKET);
		// then
		Assert.assertTrue(matches);
	}
	
	@Test
	public void shouldMatchParentTicket() throws Exception {
		// given
		String name = PARENT_QUERY;
		// when
		boolean matches = name.matches(RTService.PARENT_TICKET);
		// then
		Assert.assertTrue(matches);
	}	
	
	@Test
	public void shouldMatchFullTicket() throws Exception {
		// given
		String name = FULL_QUERY;
		// when
		boolean matches = name.matches(RTService.FULL_TICKET);
		// then
		Assert.assertTrue(matches);
	}
	
	@Test
	public void shouldGetName() throws Exception {
		// given
		// when
		String name = service.getName(FULL_QUERY);
		// then
		Assert.assertEquals("ticket name", name);
	}
	
	@Test
	public void shouldGetTicketId() throws Exception {
		// given
		// when
		Long id = service.getTicketId(DIRECT_QUERY);
		// then
		Assert.assertEquals(Long.valueOf(9999L), id);
	}
	
	@Test
	public void shouldGetParentTicketId() throws Exception {
		// given
		// when
		Long id = service.getParentId(PARENT_QUERY);
		// then
		Assert.assertEquals(Long.valueOf(9997L), id);
	}
	
	@Test
	public void shouldGetQueue() throws Exception {
		// given
		// when
		String queue = service.getQueue(FULL_QUERY);
		// then
		Assert.assertEquals("queue", queue);
	}
	
	@Test
	public void shouldGetClient() throws Exception {
		// given
		// when
		String client = service.getClient(FULL_QUERY);
		// then
		Assert.assertEquals("client", client);
	}
	
	@Test
	public void shouldGetProject() throws Exception {
		// given
		// when
		String project = service.getProject(FULL_QUERY);
		// then
		Assert.assertEquals("client/project", project);
	}
	
	@Test
	public void shouldGetClearing() throws Exception {
		// given
		// when
		String clearing = service.getClearing(FULL_QUERY);
		// then
		Assert.assertEquals("clearing", clearing);
	}
	
	@Test
	public void shouldAddTime() throws Exception {
		// given
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfigTest.class);
		RTService rtService = context.getBean(RTService.class);
		String query = "smietnik > contium/organizacyjne > radosław > Z#100 Testowe zadanie REST";
		// when
		Ticket t = rtService.addTime(query, 82);
		// then
		Assert.assertNotNull(t);
	}
	
}
