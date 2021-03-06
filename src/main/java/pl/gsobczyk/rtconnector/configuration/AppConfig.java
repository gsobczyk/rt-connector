package pl.gsobczyk.rtconnector.configuration;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.Collection;

import javax.annotation.PostConstruct;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

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
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import pl.gsobczyk.rtconnector.service.SimpleRTDao;
import pl.gsobczyk.rtconnector.service.TicketChooser;
import pl.gsobczyk.rtconnector.ui.ComponentHolder;
import pl.gsobczyk.rtconnector.ui.Messages;
import pl.gsobczyk.rtconnector.ui.ProgressBarReporter;
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
	
	@PostConstruct
	public void postConstruct() throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException{
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
	}
	
	@Bean public HttpClient httpClient() throws KeyManagementException, NoSuchAlgorithmException{
		SSLContext ctx = SSLContext.getInstance("TLS");
		TrustManager tm = new RelaxedX509TrustManager();
        ctx.init(null, new TrustManager[]{tm}, null);
        SSLSocketFactory ssf = new SSLSocketFactory(ctx, SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
        ClientConnectionManager ccm = new SingleClientConnManager();
        SchemeRegistry sr = ccm.getSchemeRegistry();
        sr.register(new Scheme("https", 443, ssf));
		DefaultHttpClient client = new DefaultHttpClient(ccm);
        return client;
	}
	
	@Bean ProgressBarReporter progressBarReporter(@Qualifier("mainWindowFrameHolder") ComponentHolder<JFrame> frameHolder){
		ProgressBarReporter progressBarReporter = new ProgressBarReporter(frameHolder.get());
		return progressBarReporter;
	}
	
	@Bean CredentialsProvider credentialsProvider(DefaultHttpClient client) {
		CredentialsProvider credendialProvider = client.getCredentialsProvider();
        credendialProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(env.getProperty(SimpleRTDao.P_USER), env.getProperty(SimpleRTDao.P_PASSWORD)));
		return credendialProvider;
	}
	
	@Bean(name="mainWindowFrameHolder") public ComponentHolder<JFrame> frame(){
		return ComponentHolder.wrap(new JFrame());
	}
	
	@Bean(name="tableHolder") public ComponentHolder<JTable> table(){
		return ComponentHolder.wrap(new JTable());
	}
	
	@Bean(name="comboBoxHolder") public ComponentHolder<JComboBox> comboBox(){
		return ComponentHolder.wrap(new JComboBox());
	}
	
	@Bean(name="commentHolder") public ComponentHolder<JTextField> commentTextField(){
		return ComponentHolder.wrap(new JTextField());
	}
		
	@Bean public TicketChooser ticketChooser(){
		return new SwingTicketChooser();
	}
	
	@Bean public AutocompleteChooser autocompleteChooser(){
		return new SwingAutocompleteChooser();
	}
	
	@Bean(name="btnReportHolder") public ComponentHolder<JButton> btnReportHolder(){
		return ComponentHolder.wrap(new JButton(Messages.getString("MainWindow.report"))); //$NON-NLS-1$
	}
	
	@Bean public RestTemplate restTemplate(HttpClient httpClient, Collection<RTConverter<?>> converters){
		HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
		RestTemplate restTemplate = new RestTemplate(requestFactory);
		restTemplate.getMessageConverters().addAll(0, converters);
		return restTemplate;
	}
	
}
