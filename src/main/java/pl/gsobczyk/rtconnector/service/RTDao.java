package pl.gsobczyk.rtconnector.service;

import java.util.List;

import pl.gsobczyk.rtconnector.domain.Queue;
import pl.gsobczyk.rtconnector.domain.Ticket;

public interface RTDao {
	Ticket createTicket(String queue, String client, String project, String clearing, String name);
	Ticket create(Ticket parent, String name);
	List<Ticket> findTicket(String query);
	List<Ticket> findTicket(Ticket parent, String query);
	Ticket getTicket(Long id);
	void addTime(Ticket ticket, int minutes);
	void closeTicket(Ticket ticket);
	void takeTicket(Ticket ticket);
	Queue getQueue(Long id);
	List<Queue> getAllQueues();
}
