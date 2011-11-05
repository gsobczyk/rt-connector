package pl.gsobczyk.rtconnector.service;

public abstract class RTService {
	public abstract void addTime(String ticketQuery, int minutes);
	abstract String getName(String ticketQuery);
	abstract Long getTicketId(String ticketQuery);
	abstract String getParent(String ticketQuery);
	abstract Long getParentId(String ticketQuery);
}
