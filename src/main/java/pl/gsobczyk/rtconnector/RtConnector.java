package pl.gsobczyk.rtconnector;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import pl.gsobczyk.rtconnector.configuration.AppConfig;

public class RtConnector {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new AnnotationConfigApplicationContext(AppConfig.class);
	}

}
