package pl.gsobczyk.rtconnector.web;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import pl.gsobczyk.rtconnector.domain.AutocompletePosition;
import pl.gsobczyk.rtconnector.domain.Queue;
import pl.gsobczyk.rtconnector.service.RTDao;
import pl.gsobczyk.rtconnector.service.SimpleRTDao;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;

@Service
public class SimpleRTAutocompleteService implements RTAutocompleteService {
	private static final String AUTOCOMPLETE_QUERY="/Helpers/Autocomplete/CustomFieldValues?Object---CustomField-{fieldId}-Value&term={value}";
	private static final int CLIENT_FIELD=16;
	private static final int PROJECT_FIELD=21;
	private static final int CLEARING_FIELD=17;
	@Autowired private RestTemplate restTemplate;
	@Autowired private RTDao rtDao;
	@Autowired private Environment env;
	private String rtUrl;
	
	@PostConstruct
	public void postContruct() {
		rtUrl = env.getRequiredProperty(SimpleRTDao.P_RT_URL);
	}

	@Override
	public Collection<Queue> findQueues(final String nameFragment) {
		List<Queue> queues = rtDao.getAllQueues();
		Predicate<? super Queue> predicate = new Predicate<Queue>() {
			@Override public boolean apply(Queue input) {
				return input.getName().toLowerCase().contains(nameFragment.toLowerCase());
			}
		};
		return Collections2.filter(queues, predicate);
	}

	@Override
	public Iterable<AutocompletePosition> findClients(String clientLike) {
		return restTemplate.getForObject(rtUrl+AUTOCOMPLETE_QUERY, AutocompleteList.class, CLIENT_FIELD, clientLike);
	}

	@Override
	public Iterable<AutocompletePosition> findProjects(String clientLike, String procjetLike) {
		return restTemplate.getForObject(rtUrl+AUTOCOMPLETE_QUERY, AutocompleteList.class, PROJECT_FIELD, "%"+clientLike+"%/%"+procjetLike);
	}

	@Override
	public Iterable<AutocompletePosition> findClearings(String like) {
		return restTemplate.getForObject(rtUrl+AUTOCOMPLETE_QUERY, AutocompleteList.class, CLEARING_FIELD, like);
	}

	@VisibleForTesting void setRtDao(RTDao rtDao) {
		this.rtDao = rtDao;
	}
	
	public static class AutocompleteList extends ArrayList<AutocompletePosition>{
		private static final long serialVersionUID = -2371484585066378425L;
	};

}
