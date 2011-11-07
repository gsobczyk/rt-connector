package pl.gsobczyk.rtconnector.service;

import java.util.List;

import pl.gsobczyk.rtconnector.domain.Ticket;

public interface TicketChooser {
	Ticket chooseBestTicket(List<Ticket> tickets, String ticketQuery);
	
	public static class FirstTicketChooser implements TicketChooser {
		@Override public Ticket chooseBestTicket(List<Ticket> tickets, String ticketQuery) {
			return tickets.iterator().next();
		}
		
	}
}
