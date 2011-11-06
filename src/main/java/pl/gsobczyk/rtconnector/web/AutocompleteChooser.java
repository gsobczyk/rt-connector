package pl.gsobczyk.rtconnector.web;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;

public interface AutocompleteChooser {
	String chooseBest(Iterable<String> values);
	public static class FirstAutocompleteChooser implements AutocompleteChooser {
		@Override public String chooseBest(Iterable<String> values) {
			Iterable<String> filtered = Iterables.filter(values, new Predicate<String>(){
				@Override public boolean apply(String input) {
					return !input.startsWith("(");
				}});
			return filtered.iterator().next();
		}
	}
}
