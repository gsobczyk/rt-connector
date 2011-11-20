package pl.gsobczyk.rtconnector.service;

import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import pl.gsobczyk.rtconnector.domain.AutocompletePosition;
import pl.gsobczyk.rtconnector.domain.Queue;
import pl.gsobczyk.rtconnector.domain.Ticket;
import pl.gsobczyk.rtconnector.ui.Messages;
import pl.gsobczyk.rtconnector.web.AutocompleteChooser;
import pl.gsobczyk.rtconnector.web.RTAutocompleteService;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Function;
import com.google.common.collect.Iterables;

@Service
public class RTService {
	public static final String DIRECT_TICKET = "^.*#(\\d+):[^>]+$"; //$NON-NLS-1$
	public static final String PARENT_TICKET = "^.*#(\\d+):[^>]+>[^>]+$"; //$NON-NLS-1$
	public static final String FULL_TICKET = "^(?:.*>)?([^>]+)>([^>/]+)/([^>]+)>([^>]+)>([^>]+>)?[^>]+$"; //$NON-NLS-1$
	public static final String ONLY_QUEUE_TICKET = "^([^>]+)>(?:[^>]+>)?[^>]+$"; //$NON-NLS-1$
	@Autowired private RTDao rtDao;
	@Autowired private RTAutocompleteService autocompleteService;
	@Autowired private TicketChooser ticketChooser;
	@Autowired private AutocompleteChooser autocompleteChooser;

	public void login(){
		rtDao.login();
	}
	
	public void logout(){
		rtDao.logout();
	}
	
	public Ticket addTime(String ticketQuery, int minutes, String comment) throws QuerySyntaxException{
		Ticket ticket = findOrCreateTicket(ticketQuery);
		rtDao.addTime(ticket, minutes, comment);
		return ticket;
	}
	
	Ticket findOrCreateTicket(String ticketQuery) throws QuerySyntaxException{
		if (ticketQuery.matches(DIRECT_TICKET)){
			Long id = getTicketId(ticketQuery);
			return rtDao.getTicket(id);
		} else if (ticketQuery.matches(PARENT_TICKET)){
			Long parentId = getParentId(ticketQuery);
			String name = getName(ticketQuery);
			Ticket parent = rtDao.getTicket(parentId);
			List<Ticket> tickets = rtDao.findTickets(parent, name, true);
			if (CollectionUtils.isEmpty(tickets)){
				tickets = rtDao.findTickets(parent, name, false);
			}
			if (!CollectionUtils.isEmpty(tickets)){
				if (tickets.size()==1){
					return tickets.iterator().next();
				} else {
					return ticketChooser.chooseBestTicket(tickets, ticketQuery);
				}
			}
			return rtDao.createTicket(parent.getQueue(), parent.getClient(), parent.getProject(), parent.getClearing(), name);
		} else if (ticketQuery.matches(FULL_TICKET)){
			String queue = getQueue(ticketQuery);
			String client = getClient(ticketQuery);
			String project = getProject(client, ticketQuery);
			String name = getName(ticketQuery);
			String clearing = getClearing(ticketQuery);
			List<Ticket> tickets = rtDao.findTickets(queue, client, project, clearing, name, true);
			if (CollectionUtils.isEmpty(tickets)){
				tickets = rtDao.findTickets(queue, client, project, clearing, name, false);
			}
			if (!CollectionUtils.isEmpty(tickets)){
				if (tickets.size()==1){
					return tickets.iterator().next();
				} else {
					return ticketChooser.chooseBestTicket(tickets, ticketQuery);
				}
			}
			return rtDao.createTicket(queue, client, project, clearing, name);
		} else if (ticketQuery.matches(ONLY_QUEUE_TICKET)){
			String name = getName(ticketQuery);
			String queue = getQueue(ticketQuery);
			List<Ticket> tickets = rtDao.findTickets(queue, null, null, null, name, true);
			if (CollectionUtils.isEmpty(tickets)){
				tickets = rtDao.findTickets(queue, null, null, null, name, false);
			}
			if (!CollectionUtils.isEmpty(tickets)){
				if (tickets.size()==1){
					return tickets.iterator().next();
				} else {
					return ticketChooser.chooseBestTicket(tickets, ticketQuery);
				}
			}
			return rtDao.createTicket(queue, null, null, null, name);
		} else {
			String name = getName(ticketQuery);
			List<Ticket> tickets = rtDao.findTickets(null, null, null, null, name, true);
			if (CollectionUtils.isEmpty(tickets)){
				tickets = rtDao.findTickets(null, null, null, null, name, false);
			}
			if (!CollectionUtils.isEmpty(tickets)){
				if (tickets.size()==1){
					return tickets.iterator().next();
				} else {
					return ticketChooser.chooseBestTicket(tickets, ticketQuery);
				}
			}
			throw new QuerySyntaxException(ticketQuery);
		}
	}
	String getClearing(String ticketQuery) throws QuerySyntaxException{
		Matcher m = Pattern.compile(FULL_TICKET).matcher(ticketQuery);
		if (!m.find()){
			throw new QuerySyntaxException(Messages.getString("RTService.directQueryError")); //$NON-NLS-1$
		} else {
			String clearing = m.group(4).trim();
			Iterable<AutocompletePosition> names = autocompleteService.findClearings(clearing);
			return autocompleteChooser.chooseBest(names, clearing);
		}
	}
	String getProject(String client, String ticketQuery) throws QuerySyntaxException{
		Matcher m = Pattern.compile(FULL_TICKET).matcher(ticketQuery);
		if (!m.find()){
			throw new QuerySyntaxException(Messages.getString("RTService.directQueryError")); //$NON-NLS-1$
		} else {
			String project = m.group(3).trim();
			Iterable<AutocompletePosition> names = autocompleteService.findProjects(client, project);
			return autocompleteChooser.chooseBest(names, client+"/"+project); //$NON-NLS-1$
		}
	}
	String getClient(String ticketQuery) throws QuerySyntaxException{
		Matcher m = Pattern.compile(FULL_TICKET).matcher(ticketQuery);
		if (!m.find()){
			throw new QuerySyntaxException(Messages.getString("RTService.directQueryError")); //$NON-NLS-1$
		} else {
			String client = m.group(2).trim();
			Iterable<AutocompletePosition> names = autocompleteService.findClients(client);
			return autocompleteChooser.chooseBest(names, client);
		}
	}
	String getQueue(String ticketQuery) throws QuerySyntaxException{
		Matcher m = Pattern.compile(FULL_TICKET).matcher(ticketQuery);
		if (!m.find()){
			throw new QuerySyntaxException(Messages.getString("RTService.directQueryError")); //$NON-NLS-1$
		} else {
			String queue = m.group(1).trim();
			Collection<Queue> queues = autocompleteService.findQueues(queue);
			Iterable<AutocompletePosition> names = Iterables.transform(queues, new Function<Queue, AutocompletePosition>(){
				@Override public AutocompletePosition apply(Queue input) {
					AutocompletePosition pos = new AutocompletePosition();
					pos.setValue(input.getName());
					pos.setLabel(input.getName());
					return pos;
				}});
			return autocompleteChooser.chooseBest(names, queue);
		}
	}
	String getName(String ticketQuery){
		return ticketQuery.substring(ticketQuery.lastIndexOf('>')+1).trim();
	}
	Long getTicketId(String ticketQuery) throws QuerySyntaxException{
		Matcher m = Pattern.compile(DIRECT_TICKET).matcher(ticketQuery);
		if (!m.find()){
			throw new QuerySyntaxException(Messages.getString("RTService.directQueryError")); //$NON-NLS-1$
		} else {
			return Long.valueOf(m.group(1).trim());
		}
	}
	Long getParentId(String ticketQuery) throws QuerySyntaxException{
		Matcher m = Pattern.compile(PARENT_TICKET).matcher(ticketQuery);
		if (!m.find()){
			throw new QuerySyntaxException(Messages.getString("RTService.parentQueryError")); //$NON-NLS-1$
		} else {
			return Long.valueOf(m.group(1).trim());
		}
	}

	@VisibleForTesting void setAutocompleteService(RTAutocompleteService autocompleteService) {
		this.autocompleteService = autocompleteService;
	}
	@VisibleForTesting void setTicketChooser(TicketChooser ticketChooser) {
		this.ticketChooser = ticketChooser;
	}
	@VisibleForTesting void setAutocompleteChooser(AutocompleteChooser autocompleteChooser) {
		this.autocompleteChooser = autocompleteChooser;
	}
}
