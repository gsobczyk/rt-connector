package pl.gsobczyk.rtconnector.ui;

import java.text.MessageFormat;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class Messages {
	private static final String BUNDLE_NAME = "messages"; //$NON-NLS-1$

	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);

	private Messages() {
	}

	public static String getString(String key, Object ... arguments) {
		try {
			return MessageFormat.format(RESOURCE_BUNDLE.getString(key), arguments);
		} catch (MissingResourceException e) {
			return '!' + key + '!';
		}
	}
}
