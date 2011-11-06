package pl.gsobczyk.rtconnector.service;

import org.apache.commons.lang.StringUtils;

import pl.gsobczyk.rtconnector.domain.TicketField;

public class FieldQuery {
	private TicketField<?> field;
	private Operator operator;
	private String value;
	
	public FieldQuery(TicketField<?> field, Operator operator, String value) {
		super();
		this.field = field;
		this.operator = operator;
		this.value = value;
	}
	
	public static enum Operator{
		LIKE("LIKE"), 
		EQUAL("=");
		private final String operator;
		private Operator(String operator){
			this.operator = operator;
		}
		@Override public String toString() {
			return operator;
		}
	}

	public TicketField<?> getField() {
		return field;
	}

	public Operator getOperator() {
		return operator;
	}

	public String getValue() {
		return value;
	}

	@Override
	public String toString() {
		if (StringUtils.isNotBlank(value)){
			return field.getName()+" "+operator+" '"+value+"'";
		} else {
			return "";
		}
	}
}
