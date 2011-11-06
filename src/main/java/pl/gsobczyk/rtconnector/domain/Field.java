package pl.gsobczyk.rtconnector.domain;

import org.apache.commons.lang.builder.EqualsBuilder;

public abstract class Field<T, E> {
	private final String name;

	protected Field(String name){
		this.name = name;
	}

	public abstract T getValue(E entity);
	public abstract void setValue(E entity, T value);

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
