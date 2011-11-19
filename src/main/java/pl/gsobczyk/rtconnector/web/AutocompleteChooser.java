package pl.gsobczyk.rtconnector.web;

import pl.gsobczyk.rtconnector.domain.AutocompletePosition;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;

public interface AutocompleteChooser {
	String chooseBest(Iterable<AutocompletePosition> names, String autocompleteQuery);
	public static class FirstAutocompleteChooser implements AutocompleteChooser {
		@Override public String chooseBest(Iterable<AutocompletePosition> values, String autocompleteQuery) {
			Iterable<AutocompletePosition> filtered = Iterables.filter(values, new Predicate<AutocompletePosition>(){
				@Override public boolean apply(AutocompletePosition input) {
					return !input.getValue().startsWith("(");
				}});
			return filtered.iterator().next().getValue();
		}
	}
}
