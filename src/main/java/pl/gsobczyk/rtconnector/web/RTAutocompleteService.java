package pl.gsobczyk.rtconnector.web;

public interface RTAutocompleteService {
	String findQueue(String like);
	String findClient(String like);
	String findProject(String like);
	String findClearing(String like);
}
