package pl.gsobczyk.rtconnector.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;

//@Service
public class SimpleRTAutocompleteService implements RTAutocompleteService {
	@Autowired private RestTemplate restTemplate;

	@Override
	public String findQueue(String like) {
		return null;
	}

	@Override
	public String findClient(String like) {
		return null;
	}

	@Override
	public String findProject(String like) {
		return null;
	}

	@Override
	public String findClearing(String like) {
		return null;
	}

}
