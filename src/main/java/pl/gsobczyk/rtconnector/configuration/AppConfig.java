package pl.gsobczyk.rtconnector.configuration;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.Collection;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;

import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.SingleClientConnManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import pl.gsobczyk.rtconnector.service.SimpleRTDao;
import pl.gsobczyk.rtconnector.service.TicketChooser;
import pl.gsobczyk.rtconnector.ui.SwingAutocompleteChooser;
import pl.gsobczyk.rtconnector.ui.SwingTicketChooser;
import pl.gsobczyk.rtconnector.web.AutocompleteChooser;
import pl.gsobczyk.rtconnector.web.RTConverter;
import pl.gsobczyk.tools.RelaxedX509TrustManager;

@Configuration
@ComponentScan({"pl.gsobczyk.rtconnector.service","pl.gsobczyk.rtconnector.web","pl.gsobczyk.rtconnector.ui"})
@PropertySource(value="classpath:/app.properties", name="appProps")
public class AppConfig {
	@Autowired private Environment env;
	
	@Bean public HttpClient httpClient() throws KeyManagementException, NoSuchAlgorithmException{
		SSLContext ctx = SSLContext.getInstance("TLS");
		TrustManager tm = new RelaxedX509TrustManager();
        ctx.init(null, new TrustManager[]{tm}, null);
        SSLSocketFactory ssf = new SSLSocketFactory(ctx, SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
        ClientConnectionManager ccm = new SingleClientConnManager();
        SchemeRegistry sr = ccm.getSchemeRegistry();
        sr.register(new Scheme("https", 443, ssf));
        DefaultHttpClient client = new DefaultHttpClient(ccm);
        CredentialsProvider credendialProvider = client.getCredentialsProvider();
        credendialProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(env.getProperty(SimpleRTDao.P_USER), env.getProperty(SimpleRTDao.P_PASSWORD)));
        return client;
	}
	
	@Bean public TicketChooser ticketChooser(){
		return new SwingTicketChooser();
	}
	
	@Bean public AutocompleteChooser autocompleteChooser(){
		return new SwingAutocompleteChooser();
	}
	
	@Bean public RestTemplate restTemplate(HttpClient httpClient, Collection<RTConverter<?>> converters){
		HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
		RestTemplate restTemplate = new RestTemplate(requestFactory);
		restTemplate.getMessageConverters().addAll(converters);
		return restTemplate;
	}
	
}
