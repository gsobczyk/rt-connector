package pl.gsobczyk.rtconnector.web;

import java.util.Iterator;

import org.apache.commons.lang.NotImplementedException;

public class TicketQuery implements Iterator<TicketQuery>{

	protected TicketQuery() {
	}
	
	@Override
	public boolean hasNext() {
		return false;
	}
	@Override
	public TicketQuery next() {
		return null;
	}
	@Override
	public void remove() {
		throw new NotImplementedException();
	}
}
