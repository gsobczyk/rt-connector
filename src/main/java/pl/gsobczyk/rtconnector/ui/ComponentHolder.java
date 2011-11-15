package pl.gsobczyk.rtconnector.ui;

import java.awt.Component;

public class ComponentHolder<E extends Component> {
	private E component;

	private ComponentHolder(E component) {
		super();
		this.component = component;
	}

	public E get() {
		return component;
	}

	public static <E extends Component> ComponentHolder<E> wrap(E component){
		return new ComponentHolder<E>(component);
	}
}
