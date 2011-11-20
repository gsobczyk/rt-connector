package pl.gsobczyk.rtconnector.ui;

import java.math.BigDecimal;

public class TicketEntry {
	private String ticket;
	private BigDecimal interval;
	
	public TicketEntry(String ticket, BigDecimal interval) {
		this.ticket = ticket;
		this.interval = interval;
	}
	
	public String getTicket() {
		return ticket;
	}
	public void setTicket(String ticket) {
		this.ticket = ticket;
	}
	public BigDecimal getInterval() {
		return interval;
	}
	public void setInterval(BigDecimal interval) {
		this.interval = interval;
	}
}
