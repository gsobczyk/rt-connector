package pl.gsobczyk.rtconnector.service;

import static pl.gsobczyk.rtconnector.service.FieldQuery.Operator.EQUAL;
import static pl.gsobczyk.rtconnector.service.FieldQuery.Operator.LIKE;
import static pl.gsobczyk.rtconnector.web.RestAction.COMMENT;
import static pl.gsobczyk.rtconnector.web.RestAction.CREATE;
import static pl.gsobczyk.rtconnector.web.RestAction.GET;
import static pl.gsobczyk.rtconnector.web.RestAction.LOGIN;
import static pl.gsobczyk.rtconnector.web.RestAction.LOGOUT;
import static pl.gsobczyk.rtconnector.web.RestAction.QUERY;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.commons.lang.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import pl.gsobczyk.rtconnector.domain.Queue;
import pl.gsobczyk.rtconnector.domain.RestStatus;
import pl.gsobczyk.rtconnector.domain.Ticket;
import pl.gsobczyk.rtconnector.domain.TicketAction;
import pl.gsobczyk.rtconnector.domain.TicketField;
import pl.gsobczyk.rtconnector.ui.Messages;
import pl.gsobczyk.rtconnector.web.RestAction;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

@Repository
public class SimpleRTDao implements RTDao {
	public static final String QUEUE_REGEX = "<select name=\"Queue\".*?>([\\r\\n\\w\\W]*?)</select"; //$NON-NLS-1$
	public static final Splitter HTML_OPTION_SPLITTER = Splitter.onPattern("(</option\\s*>)|(<option\\s+?value=\")|\\1\\s*\\2").trimResults().omitEmptyStrings(); //$NON-NLS-1$
	public static final Splitter HTML_KEY_VALUE_SPLITTER = Splitter.onPattern("\"[^>]*>").trimResults().omitEmptyStrings(); //$NON-NLS-1$
	public static final String D_REST_CONTEXT = "/REST/1.0/"; //$NON-NLS-1$
	public static final String P_RT_URL = "rt.url"; //$NON-NLS-1$
	public static final String P_USER = "rt.user"; //$NON-NLS-1$
	public static final String P_PASSWORD = "rt.password"; //$NON-NLS-1$
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
//		login();
	}
	
	@PreDestroy
	public void preDestroy(){
		try {
			logout();
		} catch (Exception e) {
			//doNothing
		}
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
		ticket.setClearing(clearing);
		ticket.setName(name);
		ticket.setRequestors(user);
		RestStatus status = restTemplate.postForObject(restUrl+CREATE, ticket, RestStatus.class);
		Matcher m = Pattern.compile("Ticket (\\d+) created\\.").matcher(status.getMessage()); //$NON-NLS-1$
		m.find();
		String id = m.group(1);
		ticket.setId(Long.parseLong(id));
		takeTicket(ticket);
		return ticket;
	}

	@Override
	public Ticket create(Ticket parent, String name) {
		return createTicket(parent.getQueue(), parent.getClient(), parent.getProject(), parent.getClearing(), name);
	}

	@Override
	public List<Ticket> findTickets(String queue, String client, String project, String clearing, String name, boolean onlyMyTickets) {
		List<FieldQuery> fieldQueries = Lists.newArrayList();
		fieldQueries.add(new FieldQuery(TicketField.QUEUE, EQUAL, queue));
		fieldQueries.add(new FieldQuery(TicketField.CLIENT, LIKE, client));
		fieldQueries.add(new FieldQuery(TicketField.PROJECT, LIKE, project));
		fieldQueries.add(new FieldQuery(TicketField.CLEARING, LIKE, clearing));
		fieldQueries.add(new FieldQuery(TicketField.NAME, LIKE, name));
		if (onlyMyTickets){
			fieldQueries.add(new FieldQuery(TicketField.OWNER, EQUAL, user));
		}
		List<String> queries = Lists.newArrayList();
		for (FieldQuery fieldQuery : fieldQueries) {
			String query = fieldQuery.toString();
			if (StringUtils.hasText(query)){
				queries.add(query);
			}
		}
		String queryString = StringUtils.collectionToDelimitedString(queries, " AND "); //$NON-NLS-1$
		if (StringUtils.hasText(queryString)){
			queryString+=" AND "; //$NON-NLS-1$
		}
		queryString += "( Status = 'new' OR Status = 'open' )"; //$NON-NLS-1$
		@SuppressWarnings("unchecked")
		Map<String, String> map = restTemplate.getForObject(restUrl+QUERY, Map.class, queryString);
		List<Ticket> result = Lists.newArrayList();
		for (String key : map.keySet()) {
			if (NumberUtils.isDigits(key)){
				Long id = Long.valueOf(key);
				result.add(getTicket(id));
			}
		}
		return result;
	}

	@Override
	public List<Ticket> findTickets(Ticket parent, String name, boolean onlyMyTickets) {
		return findTickets(parent.getQueue(), parent.getClient(), parent.getProject(), parent.getClearing(), name, onlyMyTickets);
	}

	@Override
	public Ticket getTicket(Long id) {
		return restTemplate.getForObject(restUrl+GET, Ticket.class, id);
	}

	@Override
	public RestStatus addTime(Ticket ticket, int minutes, String comment) {
		Ticket addTime = new Ticket();
		addTime.setId(ticket.getId());
		addTime.setTimeWorked(minutes);
		addTime.setAction(TicketAction.COMMENT);
		if (StringUtils.hasText(comment)){
			addTime.setText(comment);
		} else {
			addTime.setText(Messages.getString("SimpleRTDao.defaultComment")); //$NON-NLS-1$
		}
		RestStatus result = restTemplate.postForObject(restUrl+COMMENT, addTime, RestStatus.class, ticket.getId());
		int newMinutes = minutes;
		if (ticket.getTimeWorked()!=null){
			newMinutes+=ticket.getTimeWorked();
		}
		ticket.setTimeWorked(newMinutes);
		return result;
	}

	@Override
	public boolean takeTicket(Ticket ticket) {
		URI uri;
		try {
			uri = new URI(rtUrl+"/Ticket/Display.html?Action=Take;id="+ticket.getId());
			ClientHttpRequest request = restTemplate.getRequestFactory().createRequest(uri, HttpMethod.GET);
			request.execute();
			ticket.setOwner(user);
			return true;
		} catch (URISyntaxException e) {
			return false;
		} catch (IOException e) {
			return false;
		}
	}
	
	public boolean isLogged() {
		return restTemplate.getForObject(restUrl, String.class).contains("200 Ok"); //$NON-NLS-1$
	}

	private void fillQueueCache() {
		String html = restTemplate.getForObject(rtUrl+"/Tools/", String.class); //$NON-NLS-1$
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
