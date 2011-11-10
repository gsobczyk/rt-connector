package pl.gsobczyk.rtconnector.web;

import javax.swing.JOptionPane;

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
			result = (String) JOptionPane.showInputDialog(null, "Wybierz wartość pasującą do: \n\n"+autocompleteQuery+"\n ", "Wybierz wartość", JOptionPane.QUESTION_MESSAGE, null, possibleValues, possibleValues[0]);
		}
		return result;
	}

}
