package pl.gsobczyk.rtconnector.web;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.util.FileCopyUtils;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public abstract class RTConverter<T> extends AbstractHttpMessageConverter<T> {
	public static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");
//	@Autowired private FormHttpMessageConverter formHttpMessageConverter;
	private boolean writeAcceptCharset = true;
	private List<Charset> availableCharsets;
	
	@PostConstruct
	public void postConstruct(){
		setSupportedMediaTypes(Lists.newArrayList(new MediaType("text", "plain", DEFAULT_CHARSET), MediaType.ALL));
		this.availableCharsets = new ArrayList<Charset>(Charset.availableCharsets().values());
	}
	
	@Override
	protected void writeInternal(T t, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
		if (writeAcceptCharset) {
			outputMessage.getHeaders().setAcceptCharset(getAcceptedCharsets());
		}
		Charset charset = getContentTypeCharset(outputMessage.getHeaders().getContentType());
		String s = convertToString(t);
		FileCopyUtils.copy(s, new OutputStreamWriter(outputMessage.getBody(), charset));
	}

	@Override
	protected T readInternal(Class<? extends T> clazz, HttpInputMessage inputMessage) throws IOException,
			HttpMessageNotReadableException {
		Charset charset = getContentTypeCharset(inputMessage.getHeaders().getContentType());
		String response = FileCopyUtils.copyToString(new InputStreamReader(inputMessage.getBody(), charset));
		return convertToEntity(response);
	}

	protected abstract T convertToEntity(String response);
	protected abstract String convertToString(T entity);
	
	protected List<Charset> getAcceptedCharsets() {
		return availableCharsets;
	}

	private Charset getContentTypeCharset(MediaType contentType) {
		if (contentType != null && contentType.getCharSet() != null) {
			return contentType.getCharSet();
		} else {
			return DEFAULT_CHARSET;
		}
	}

	public Map<String, String> convertToMap(String response) {
		Splitter keySplitter = Splitter.onPattern("(:|\\s|\\d\\.\\d\\.\\d)+").trimResults().omitEmptyStrings().limit(2);
		Iterable<String> lines = Splitter.onPattern("\\r?\\n(?![ \\t])").trimResults().omitEmptyStrings().split(response);
		Map<String, String> map = Maps.newHashMap();
		for (String line : lines) {
			Iterator<String> it = keySplitter.split(line).iterator();
			if (it.hasNext()){
				String key = it.next();
				String value = null;
				if (it.hasNext()){
					value = it.next();
				}
				map.put(key, value);
			}
		}
		return map;
	}

}
