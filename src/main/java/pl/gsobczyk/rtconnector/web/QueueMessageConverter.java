package pl.gsobczyk.rtconnector.web;

import java.util.Map;

import org.springframework.stereotype.Component;

import pl.gsobczyk.rtconnector.domain.Queue;
import pl.gsobczyk.rtconnector.domain.QueueField;
import pl.gsobczyk.rtconnector.domain.TicketField;

import com.google.common.annotations.VisibleForTesting;

@Component
public class QueueMessageConverter extends RTConverter<Queue> {
	
	@Override
	protected boolean supports(Class<?> clazz) {
		return Queue.class.equals(clazz);
	}

	@VisibleForTesting
	protected Queue convertToEntity(String response) {
		Map<String, String> map = convertToMap(response);
		Queue queue = new Queue();
		String id = map.get(TicketField.ID.getName());
		if (id==null){
			return null;
		}
		id = id.substring(id.indexOf('/')+1);
		QueueField.ID.setValue(queue, Long.valueOf(id));
		QueueField.DESCRIPTION.setValue(queue, map.get(QueueField.DESCRIPTION.getName()));
		QueueField.NAME.setValue(queue, map.get(QueueField.NAME.getName()));
		return queue;
	}
	
	protected String convertToString(Queue ticket){
		String result=null;
		return result;
	}

}
