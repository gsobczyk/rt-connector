package pl.gsobczyk.rtconnector.service;

import static pl.gsobczyk.rtconnector.web.RestAction.CREATE;
import static pl.gsobczyk.rtconnector.web.RestAction.EDIT;
import static pl.gsobczyk.rtconnector.web.RestAction.GET;
import static pl.gsobczyk.rtconnector.web.RestAction.LOGIN;
import static pl.gsobczyk.rtconnector.web.RestAction.LOGOUT;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import pl.gsobczyk.rtconnector.domain.Queue;
import pl.gsobczyk.rtconnector.domain.RestStatus;
import pl.gsobczyk.rtconnector.domain.Ticket;
import pl.gsobczyk.rtconnector.web.RestAction;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Splitter;
import com.google.common.collect.Maps;

@Repository
public class SimpleRTDao implements RTDao {
	public static final String QUEUE_REGEX = "<select name=\"Queue\".*?>([\\r\\n\\w\\W]*?)</select";
	public static final Splitter HTML_OPTION_SPLITTER = Splitter.onPattern("(</option\\s*>)|(<option\\s+?value=\")|\\1\\s*\\2").trimResults().omitEmptyStrings();
	public static final Splitter HTML_KEY_VALUE_SPLITTER = Splitter.onPattern("\"[^>]*>").trimResults().omitEmptyStrings();
	public static final String D_REST_CONTEXT = "/REST/1.0/";
	public static final String P_RT_URL = "rt.url";
	public static final String P_USER = "rt.user";
	public static final String P_PASSWORD = "rt.password";
	@Autowired private RestTemplate restTemplate;
	@Autowired private Environment env;
	private String user;
	private String pass;
	private String restUrl;
	private String rtUrl;
	private List<Queue> queueCache = new ArrayList<Queue>();

	@PostConstruct
	public void postConstruct(){
		user = env.getRequiredProperty(P_USER);
		pass = env.getRequiredProperty(P_PASSWORD);
		rtUrl = env.getRequiredProperty(P_RT_URL);
		restUrl = rtUrl+D_REST_CONTEXT;
		login();
	}
	
	@PreDestroy
	public void preDestroy(){
		logout();
	}
	
	public void login() {
		if (!isLogged()){
			restTemplate.getForObject(restUrl+LOGIN, String.class, user, pass);
		}
	}

	public void logout() {
		if (isLogged()){
			restTemplate.getForObject(restUrl+LOGOUT, String.class);
		}
	}

	@Override
	public Ticket createTicket(String queue, String client, String project, String clearing, String name) {
		Ticket ticket = new Ticket();
		ticket.setQueue(queue);
		ticket.setClient(client);
		ticket.setProject(project);
		ticket.setName(name);
		RestStatus status = restTemplate.postForObject(restUrl+CREATE, ticket, RestStatus.class);
		Matcher m = Pattern.compile("Ticket (\\d+) created\\.").matcher(status.getMessage());
		m.find();
		String id = m.group(1);
		ticket.setId(Long.parseLong(id));
		return ticket;
	}

	@Override
	public Ticket create(Ticket parent, String name) {
		return createTicket(parent.getQueue(), parent.getClient(), parent.getProject(), parent.getClearing(), name);
	}

	@Override
	public List<Ticket> findTicket(String query) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Ticket> findTicket(Ticket parent, String query) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Ticket getTicket(Long id) {
		return restTemplate.getForObject(restUrl+GET, Ticket.class, id);
	}

	@Override
	public void addTime(Ticket ticket, int minutes) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void closeTicket(Ticket ticket) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public RestStatus takeTicket(Ticket ticket) {
		Ticket take = new Ticket();
		take.setOwner(user);
		RestStatus result = restTemplate.postForObject(restUrl+EDIT, take, RestStatus.class, ticket.getId());
		ticket.setOwner(take.getOwner());
		return result;
	}
	
	public boolean isLogged() {
		return restTemplate.getForObject(restUrl, String.class).contains("200 Ok");
	}

	private void fillQueueCache() {
		String html = restTemplate.getForObject(rtUrl+"/Tools/", String.class);
		Matcher m = Pattern.compile(QUEUE_REGEX).matcher(html);
		m.find();
		HashMap<String, String> map = Maps.newHashMap(HTML_OPTION_SPLITTER.withKeyValueSeparator(HTML_KEY_VALUE_SPLITTER).split(m.group(1)));
		for (Entry<String, String> entry : map.entrySet()) {
			Queue q = new Queue();
			q.setId(Long.valueOf(entry.getKey()));
			q.setName(entry.getValue());
			queueCache.add(q);
		}
	}

	@Override
	public Queue getQueue(Long i) {
		return restTemplate.getForObject(restUrl+RestAction.QUEUE, Queue.class, i);
	}

	@Override
	public List<Queue> getAllQueues() {
		if (CollectionUtils.isEmpty(queueCache)){
			fillQueueCache();
		}
		return queueCache;
	}

	@VisibleForTesting void setRestTemplate(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}
}
