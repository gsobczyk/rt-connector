package pl.gsobczyk.rtconnector.service;

import static pl.gsobczyk.rtconnector.web.RestAction.LOGIN;
import static pl.gsobczyk.rtconnector.web.RestAction.LOGOUT;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import pl.gsobczyk.rtconnector.domain.Queue;
import pl.gsobczyk.rtconnector.domain.Ticket;
import pl.gsobczyk.rtconnector.web.RestAction;

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
	private static final int CACHE_LIMIT = 30;
	@Autowired private RestTemplate restTemplate;
	@Autowired private Environment env;
	private String user;
	private String pass;
	private String restUrl;
	private String rtUrl;
	private List<Queue> queueCache = new ArrayList<Queue>();

	@PostConstruct
	public void postConstruct(){
		user = env.getProperty(P_USER);
		pass = env.getProperty(P_PASSWORD);
		rtUrl = env.getProperty(P_RT_URL);
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
		// TODO Auto-generated method stub
		Ticket ticket = null;
		takeTicket(ticket);
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
		return restTemplate.getForObject(restUrl+RestAction.GET, Ticket.class, id);
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
	public void takeTicket(Ticket ticket) {
		// TODO Auto-generated method stub
		//setOwner
	}
	
	public boolean isLogged() {
		return restTemplate.getForObject(restUrl, String.class).contains("200 Ok");
	}

	private void fillQueueCache() {
		String html = restTemplate.getForObject(rtUrl+"/Tools/", String.class);
		HashMap<String, String> map = Maps.newHashMap(HTML_OPTION_SPLITTER.withKeyValueSeparator(HTML_KEY_VALUE_SPLITTER).split(html));
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

	public List<Queue> getQueues() {
		if (CollectionUtils.isEmpty(queueCache)){
			fillQueueCache();
		}
		return queueCache;
	}
}
