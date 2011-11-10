package pl.gsobczyk.rtconnector.web;

import static pl.gsobczyk.rtconnector.domain.StatusField.MESSAGE;
import static pl.gsobczyk.rtconnector.domain.StatusField.STATUS;

import java.util.Map;

import org.springframework.stereotype.Component;

import pl.gsobczyk.rtconnector.domain.Field;
import pl.gsobczyk.rtconnector.domain.RestStatus;

@Component
public class RestStatusMessageConverter extends RTConverter<RestStatus> {

	@Override
	protected RestStatus convertToEntity(Map<String, String> map) {
		RestStatus status = new RestStatus();
		STATUS.setValue(status, map.get(STATUS.getName()));
		MESSAGE.setValue(status, map.get(MESSAGE.getName()));
		return status;
	}

	@Override
	protected Map<Field<?, ?>, String> convertToMap(RestStatus entity) {
		return null;
	}

	@Override
	protected boolean supports(Class<?> clazz) {
		return RestStatus.class.equals(clazz);
	}

}
