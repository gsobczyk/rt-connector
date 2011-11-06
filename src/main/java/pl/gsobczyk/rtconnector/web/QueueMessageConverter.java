package pl.gsobczyk.rtconnector.web;

import static pl.gsobczyk.rtconnector.domain.QueueField.DESCRIPTION;
import static pl.gsobczyk.rtconnector.domain.QueueField.ID;
import static pl.gsobczyk.rtconnector.domain.QueueField.NAME;

import java.util.Map;

import org.springframework.stereotype.Component;

import pl.gsobczyk.rtconnector.domain.Field;
import pl.gsobczyk.rtconnector.domain.Queue;
import pl.gsobczyk.rtconnector.domain.TicketField;

import com.google.common.collect.Maps;

@Component
public class QueueMessageConverter extends RTConverter<Queue> {
	
	@Override
	protected boolean supports(Class<?> clazz) {
		return Queue.class.equals(clazz);
	}

	@Override
	protected Queue convertToEntity(Map<String, String> map) {
		Queue queue = new Queue();
		String id = map.get(TicketField.ID.getName());
		if (id==null){
			return null;
		}
		id = id.substring(id.indexOf('/')+1);
		ID.setValue(queue, Long.valueOf(id));
		DESCRIPTION.setValue(queue, map.get(DESCRIPTION.getName()));
		NAME.setValue(queue, map.get(NAME.getName()));
		return queue;
	}
	
	@Override
	protected Map<Field<?, ?>, ?> convertToMap(Queue queue){
		Map<Field<?, ?>, String> result = Maps.newHashMap();
		result.put(ID, "queue/"+ID.getValue(queue));
		result.put(DESCRIPTION, DESCRIPTION.getValue(queue));
		result.put(NAME, NAME.getValue(queue));
		return result;
	}

}
