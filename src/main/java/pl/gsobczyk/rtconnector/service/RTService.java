package pl.gsobczyk.rtconnector.service;

import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import pl.gsobczyk.rtconnector.domain.Queue;
import pl.gsobczyk.rtconnector.domain.Ticket;
import pl.gsobczyk.rtconnector.web.AutocompleteChooser;
import pl.gsobczyk.rtconnector.web.RTAutocompleteService;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Function;
import com.google.common.collect.Iterables;

public class RTService {
	public static final String DIRECT_TICKET = "^.*RT#(\\d+):[^>]+$";
	public static final String PARENT_TICKET = "^.*RT#(\\d+):[^>]+>[^>]+$";
	public static final String FULL_TICKET = "^(?:.*>)?([^>]+)>([^>/]+)/([^>]+)>([^>]+)>([^>]+>)?[^>]+$";
	@Autowired private RTDao rtDao;
	@Autowired private RTAutocompleteService autocompleteService;
	
	public void addTime(String ticketQuery, int minutes, TicketChooser ticketChooser, AutocompleteChooser autocompleteChooser) throws QuerySyntaxException{
		Ticket ticket = findOrCreateTicket(ticketQuery, ticketChooser, autocompleteChooser);
		rtDao.addTime(ticket, minutes);
	}
	
	Ticket findOrCreateTicket(String ticketQuery, TicketChooser ticketChooser, AutocompleteChooser autocompleteChooser) throws QuerySyntaxException{
		if (ticketQuery.matches(DIRECT_TICKET)){
			Long id = getTicketId(ticketQuery);
			return rtDao.getTicket(id);
		} else if (ticketQuery.matches(PARENT_TICKET)){
			Long parentId = getParentId(ticketQuery);
			String name = getName(ticketQuery);
			Ticket parent = rtDao.getTicket(parentId);
			List<Ticket> tickets = rtDao.findTickets(parent, name, false);
			if (!CollectionUtils.isEmpty(tickets) && tickets.size()==1){
				return tickets.iterator().next();
			} else {
				return ticketChooser.chooseBestTicket(tickets);
			}
		} else if (ticketQuery.matches(FULL_TICKET)){
			String queue = getQueue(ticketQuery, autocompleteChooser);
			String client = getClient(ticketQuery, autocompleteChooser);
			String project = getProject(ticketQuery, autocompleteChooser);
			String name = getName(ticketQuery);
			String clearing = getClearing(ticketQuery, autocompleteChooser);
			List<Ticket> tickets = rtDao.findTickets(queue, client, project, clearing, name, false);
			if (!CollectionUtils.isEmpty(tickets) && tickets.size()==1){
				return tickets.iterator().next();
			} else {
				return ticketChooser.chooseBestTicket(tickets);
			}
		} else {
			throw new QuerySyntaxException(ticketQuery);
		}
	}
	String getClearing(String ticketQuery, AutocompleteChooser autocompleteChooser) throws QuerySyntaxException{
		Matcher m = Pattern.compile(FULL_TICKET).matcher(ticketQuery);
		if (!m.find()){
			throw new QuerySyntaxException("query isn't a directQuery");
		} else {
			Iterable<String> names = autocompleteService.findClients(m.group(4).trim());
			return autocompleteChooser.chooseBest(names);
		}
	}
	String getProject(String ticketQuery, AutocompleteChooser autocompleteChooser) throws QuerySyntaxException{
		Matcher m = Pattern.compile(FULL_TICKET).matcher(ticketQuery);
		if (!m.find()){
			throw new QuerySyntaxException("query isn't a directQuery");
		} else {
			Iterable<String> names = autocompleteService.findProjects(m.group(2).trim(), m.group(3).trim());
			return autocompleteChooser.chooseBest(names);
		}
	}
	String getClient(String ticketQuery, AutocompleteChooser autocompleteChooser) throws QuerySyntaxException{
		Matcher m = Pattern.compile(FULL_TICKET).matcher(ticketQuery);
		if (!m.find()){
			throw new QuerySyntaxException("query isn't a directQuery");
		} else {
			Iterable<String> names = autocompleteService.findClients(m.group(2).trim());
			return autocompleteChooser.chooseBest(names);
		}
	}
	String getQueue(String ticketQuery, AutocompleteChooser autocompleteChooser) throws QuerySyntaxException{
		Matcher m = Pattern.compile(FULL_TICKET).matcher(ticketQuery);
		if (!m.find()){
			throw new QuerySyntaxException("query isn't a directQuery");
		} else {
			Collection<Queue> queues = autocompleteService.findQueues(m.group(1).trim());
			Iterable<String> names = Iterables.transform(queues, new Function<Queue, String>(){
				@Override public String apply(Queue input) {
					return input.getName();
				}});
			return autocompleteChooser.chooseBest(names);
		}
	}
	String getName(String ticketQuery){
		return ticketQuery.substring(ticketQuery.lastIndexOf('>')+1).trim();
	}
	Long getTicketId(String ticketQuery) throws QuerySyntaxException{
		Matcher m = Pattern.compile(DIRECT_TICKET).matcher(ticketQuery);
		if (!m.find()){
			throw new QuerySyntaxException("query isn't a directQuery");
		} else {
			return Long.valueOf(m.group(1).trim());
		}
	}
	Long getParentId(String ticketQuery) throws QuerySyntaxException{
		Matcher m = Pattern.compile(PARENT_TICKET).matcher(ticketQuery);
		if (!m.find()){
			throw new QuerySyntaxException("query isn't a parentQuery");
		} else {
			return Long.valueOf(m.group(1).trim());
		}
	}

	@VisibleForTesting void setAutocompleteService(RTAutocompleteService autocompleteService) {
		this.autocompleteService = autocompleteService;
	}
}
