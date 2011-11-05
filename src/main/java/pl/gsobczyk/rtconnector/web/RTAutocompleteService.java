package pl.gsobczyk.rtconnector.web;

import java.util.Collection;

import pl.gsobczyk.rtconnector.domain.Queue;

public interface RTAutocompleteService {
	Collection<Queue> findQueues(String like);
	Iterable<String> findClients(String like);
	Iterable<String> findProjects(String clientLike, String procjetLike);
	Iterable<String> findClearings(String like);
}
