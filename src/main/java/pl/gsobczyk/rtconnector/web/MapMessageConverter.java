package pl.gsobczyk.rtconnector.web;

import java.util.Map;

import org.springframework.stereotype.Component;

import pl.gsobczyk.rtconnector.domain.Field;

@Component
public class MapMessageConverter extends RTConverter<Map<String, String>>{

	@Override
	protected Map<String, String> convertToEntity(Map<String, String> map) {
		return map;
	}

	@Override
	protected Map<Field<?, ?>, String> convertToMap(Map<String, String> entity) {
		return null;
	}

	@Override
	protected boolean supports(Class<?> clazz) {
		return Map.class.equals(clazz);
	}

}
