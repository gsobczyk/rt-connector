package pl.gsobczyk.rtconnector.ui;

import javax.swing.JOptionPane;

import pl.gsobczyk.rtconnector.web.AutocompleteChooser;

import com.google.common.collect.Iterables;

public class SwingAutocompleteChooser implements AutocompleteChooser {

	@Override
	public String chooseBest(Iterable<String> values, String autocompleteQuery) {
		String[] possibleValues = Iterables.toArray(values, String.class);
		if (possibleValues.length==1){
			return values.iterator().next();
		}
		String result = null;
		int tries = 0;
		while (result==null && tries++<3){
			result = (String) JOptionPane.showInputDialog(null,
					Messages.getString("SwingAutocompleteChooser.message", autocompleteQuery), 
					Messages.getString("SwingAutocompleteChooser.title"),
					JOptionPane.QUESTION_MESSAGE, null, possibleValues, possibleValues[0]);
		}
		return result;
	}

}
