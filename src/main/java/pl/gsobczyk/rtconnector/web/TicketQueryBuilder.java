package pl.gsobczyk.rtconnector.web;

import static pl.gsobczyk.rtconnector.domain.Ticket.REGEX_DIRECT;
import static pl.gsobczyk.rtconnector.domain.Ticket.REGEX_FULL;
import static pl.gsobczyk.rtconnector.domain.Ticket.REGEX_PARENT;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TicketQueryBuilder {
	
	public TicketQuery build(String rawQuery){
		TicketQuery result = null;
		if (rawQuery.matches(REGEX_DIRECT)){
			Pattern p = Pattern.compile(REGEX_DIRECT);
			Matcher m = p.matcher(rawQuery);
			m.find();
			DirectQuery q = new DirectQuery();
			q.setId(Long.valueOf(m.group(1)));
			result = q;
		} else if (rawQuery.matches(REGEX_PARENT)){
			Pattern p = Pattern.compile(REGEX_PARENT);
			Matcher m = p.matcher(rawQuery);
			m.find();
			ParentQuery q = new ParentQuery();
			q.setParentId(Long.valueOf(m.group(1)));
			q.setName(m.group(2).trim());
			result = q;
		} else if (rawQuery.matches(REGEX_FULL)){
			Pattern p = Pattern.compile(REGEX_FULL);
			Matcher m = p.matcher(rawQuery);
			m.find();
			FullQuery q = new FullQuery();
			q.setQueue(findQueue(m.group(1)));
			q.setClient(findClient(m.group(2)));
			q.setProject(findProject(m.group(3)));
			q.setClearing(findClearing(m.group(4)));
			q.setName(m.group(5).trim());
			result = q;
		}
		return result;
	}

	protected String findClearing(String group) {
		return group.trim();
	}

	protected String findProject(String group) {
		return group.trim();
	}

	protected String findClient(String group) {
		return group.trim();
	}

	protected String findQueue(String group) {
		return group.trim();
	}
}
