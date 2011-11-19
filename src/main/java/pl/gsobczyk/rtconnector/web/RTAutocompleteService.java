package pl.gsobczyk.rtconnector.web;

import java.util.Collection;

import pl.gsobczyk.rtconnector.domain.AutocompletePosition;
import pl.gsobczyk.rtconnector.domain.Queue;

public interface RTAutocompleteService {
	Collection<Queue> findQueues(String like);
	Iterable<AutocompletePosition> findClients(String like);
	Iterable<AutocompletePosition> findProjects(String clientLike, String procjetLike);
	Iterable<AutocompletePosition> findClearings(String like);
}
