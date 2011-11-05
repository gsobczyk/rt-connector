package pl.gsobczyk.rtconnector.web;

import java.util.Map;

import org.springframework.stereotype.Component;

import pl.gsobczyk.rtconnector.domain.Ticket;
import pl.gsobczyk.rtconnector.domain.TicketField;

import com.google.common.annotations.VisibleForTesting;

@Component
public class TicketMessageConverter extends RTConverter<Ticket> {
	
	@Override
	protected boolean supports(Class<?> clazz) {
		return Ticket.class.equals(clazz);
	}

	@VisibleForTesting
	protected Ticket convertToEntity(String response) {
		Map<String, String> map = convertToMap(response);
		Ticket ticket=new Ticket();
		String id = map.get(TicketField.ID.getName());
		if (id==null){
			return null;
		}
		id = id.substring(id.indexOf('/')+1);
		TicketField.ID.setValue(ticket, Long.valueOf(id));
		TicketField.QUEUE.setValue(ticket, map.get(TicketField.QUEUE.getName()));
		TicketField.NAME.setValue(ticket, map.get(TicketField.NAME.getName()));
		TicketField.OWNER.setValue(ticket, map.get(TicketField.OWNER.getName()));
		TicketField.CLEARING.setValue(ticket, map.get(TicketField.CLEARING.getName()));
		TicketField.CLIENT.setValue(ticket, map.get(TicketField.CLIENT.getName()));
		TicketField.PROJECT.setValue(ticket, map.get(TicketField.PROJECT.getName()));
		return ticket;
	}
	
	protected String convertToString(Ticket ticket){
		String result=null;
		return result;
	}

}
