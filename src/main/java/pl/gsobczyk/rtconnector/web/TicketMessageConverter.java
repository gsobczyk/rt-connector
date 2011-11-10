package pl.gsobczyk.rtconnector.web;

import static pl.gsobczyk.rtconnector.domain.TicketField.ACTION;
import static pl.gsobczyk.rtconnector.domain.TicketField.CLEARING;
import static pl.gsobczyk.rtconnector.domain.TicketField.CLIENT;
import static pl.gsobczyk.rtconnector.domain.TicketField.ID;
import static pl.gsobczyk.rtconnector.domain.TicketField.NAME;
import static pl.gsobczyk.rtconnector.domain.TicketField.OWNER;
import static pl.gsobczyk.rtconnector.domain.TicketField.PROJECT;
import static pl.gsobczyk.rtconnector.domain.TicketField.QUEUE;
import static pl.gsobczyk.rtconnector.domain.TicketField.REQUESTORS;
import static pl.gsobczyk.rtconnector.domain.TicketField.TEXT;
import static pl.gsobczyk.rtconnector.domain.TicketField.TIME_WORKED;

import java.util.Map;

import org.apache.commons.lang.math.NumberUtils;
import org.springframework.stereotype.Component;

import pl.gsobczyk.rtconnector.domain.Field;
import pl.gsobczyk.rtconnector.domain.Ticket;
import pl.gsobczyk.rtconnector.domain.TicketAction;
import pl.gsobczyk.rtconnector.domain.TicketField;

import com.google.common.collect.Maps;

@Component
public class TicketMessageConverter extends RTConverter<Ticket> {
	
	@Override
	protected boolean supports(Class<?> clazz) {
		return Ticket.class.equals(clazz);
	}

	@Override
	protected Ticket convertToEntity(Map<String, String> map) {
		Ticket ticket=new Ticket();
		String id = map.get(TicketField.ID.getName());
		if (id==null){
			return null;
		}
		id = id.substring(id.indexOf('/')+1);
		ID.setValue(ticket, Long.valueOf(id));
		QUEUE.setValue(ticket, map.get(QUEUE.getName()));
		NAME.setValue(ticket, map.get(NAME.getName()));
		OWNER.setValue(ticket, map.get(OWNER.getName()));
		CLEARING.setValue(ticket, map.get(CLEARING.getName()));
		CLIENT.setValue(ticket, map.get(CLIENT.getName()));
		PROJECT.setValue(ticket, map.get(PROJECT.getName()));
		REQUESTORS.setValue(ticket, map.get(REQUESTORS.getName()));
		TEXT.setValue(ticket, map.get(TEXT.getName()));
		String timeWorked = map.get(TIME_WORKED.getName());
		timeWorked = timeWorked.split("\\s")[0];
		TIME_WORKED.setValue(ticket, NumberUtils.createInteger(timeWorked));
		return ticket;
	}
	
	@Override
	protected Map<Field<?,?>, String> convertToMap(Ticket ticket){
		Map<Field<?,?>, String> map = Maps.newLinkedHashMap();
		if (ticket.getId()==null){
			map.put(ID, "ticket/new");
		} else {
			map.put(ID, "ticket/"+ID.getValue(ticket));
		}
		map.put(NAME, NAME.getValue(ticket));
		map.put(OWNER, OWNER.getValue(ticket));
		map.put(CLEARING, CLEARING.getValue(ticket));
		map.put(CLIENT, CLIENT.getValue(ticket));
		map.put(PROJECT, PROJECT.getValue(ticket));
		map.put(REQUESTORS, REQUESTORS.getValue(ticket));
		map.put(TEXT, TEXT.getValue(ticket));
		Integer timeWorked = TIME_WORKED.getValue(ticket);
		if (timeWorked!=null){
			map.put(TIME_WORKED, Integer.toString(timeWorked));
		}
		TicketAction action = ACTION.getValue(ticket);
		if (action!=null){
			map.put(ACTION, action.toString());//must be last!
		}
		map.put(QUEUE, QUEUE.getValue(ticket));//must be last!
		return map;
	}

}
