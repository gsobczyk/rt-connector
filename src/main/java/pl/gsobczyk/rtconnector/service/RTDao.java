package pl.gsobczyk.rtconnector.service;

import java.util.List;

import pl.gsobczyk.rtconnector.domain.Queue;
import pl.gsobczyk.rtconnector.domain.RestStatus;
import pl.gsobczyk.rtconnector.domain.Ticket;

public interface RTDao {
	Ticket createTicket(String queue, String client, String project, String clearing, String name);
	Ticket create(Ticket parent, String name);
	List<Ticket> findTickets(String queue, String client, String project, String clearing, String name, boolean myTickets);
	List<Ticket> findTickets(Ticket parent, String name, boolean myTickets);
	Ticket getTicket(Long id);
	RestStatus addTime(Ticket ticket, int minutes);
	RestStatus takeTicket(Ticket ticket);
	Queue getQueue(Long id);
	List<Queue> getAllQueues();
}
