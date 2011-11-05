package pl.gsobczyk.rtconnector.domain;

import org.apache.commons.lang.builder.EqualsBuilder;

public abstract class Field<T, E> {
	public static final Field<String, Void> STATUS = new Field<String,Void>("RT/"){
		@Override public String getValue(Void ticket) {
			return null;
		}
		@Override public void setValue(Void ticket, String value) {
		}
	};
	private final String name;

	protected Field(String name){
		this.name = name;
	}

	public abstract T getValue(E ticket);
	public abstract void setValue(E ticket, T value);

	public String getName() {
		return name;
	}
	
	@Override
	public String toString() {
		return name;
	}
	@Override
	public int hashCode() {
		return name.hashCode();
	}
	@Override
	public boolean equals(Object obj) {
		return EqualsBuilder.reflectionEquals(this, obj);
	}
}
