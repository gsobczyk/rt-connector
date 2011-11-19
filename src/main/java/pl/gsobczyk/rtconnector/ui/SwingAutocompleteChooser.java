package pl.gsobczyk.rtconnector.ui;

import javax.swing.JOptionPane;

import pl.gsobczyk.rtconnector.domain.AutocompletePosition;
import pl.gsobczyk.rtconnector.web.AutocompleteChooser;

import com.google.common.collect.Iterables;

public class SwingAutocompleteChooser implements AutocompleteChooser {

	@Override
	public String chooseBest(Iterable<AutocompletePosition> values, String autocompleteQuery) {
		AutocompletePosition[] possibleValues = Iterables.toArray(values, AutocompletePosition.class);
		if (possibleValues.length==1){
			return values.iterator().next().getValue();
		}
		String result = null;
		int tries = 0;
		while (result==null && tries++<3){
			result = ((AutocompletePosition) JOptionPane.showInputDialog(null,
					Messages.getString("SwingAutocompleteChooser.message", autocompleteQuery), 
					Messages.getString("SwingAutocompleteChooser.title"),
					JOptionPane.QUESTION_MESSAGE, null, possibleValues, possibleValues[0])).getValue();
		}
		return result;
	}

}
