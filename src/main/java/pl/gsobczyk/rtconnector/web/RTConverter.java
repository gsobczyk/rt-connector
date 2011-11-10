package pl.gsobczyk.rtconnector.web;

import java.io.IOException;
import java.io.InputStreamReader;
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
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.http.converter.xml.XmlAwareFormHttpMessageConverter;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import pl.gsobczyk.rtconnector.domain.Field;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.base.Predicates;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public abstract class RTConverter<T> extends AbstractHttpMessageConverter<T> {
	public static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");
	private FormHttpMessageConverter formHttpMessageConverter = new XmlAwareFormHttpMessageConverter();
	private List<Charset> availableCharsets;
	
	@PostConstruct
	public void postConstruct(){
		setSupportedMediaTypes(Lists.newArrayList(new MediaType("text", "plain", DEFAULT_CHARSET), MediaType.MULTIPART_FORM_DATA, MediaType.ALL));
		this.availableCharsets = new ArrayList<Charset>(Charset.availableCharsets().values());
		formHttpMessageConverter.setCharset(DEFAULT_CHARSET);
	}
	
	@Override
	protected void writeInternal(T t, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
		Map<Field<?, ?>, String> entityMap = convertToMap(t);
		entityMap = Maps.filterValues(entityMap, Predicates.notNull());
		entityMap = Maps.transformValues(entityMap, new Function<String, String>(){
			@Override public String apply(String input) {
				return input.replaceAll("\\n", "\n ");
			}});
		String content = Joiner.on("\n").withKeyValueSeparator(": ").join(entityMap);
		MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
		map.add("content", content);
		formHttpMessageConverter.write(map, MediaType.APPLICATION_FORM_URLENCODED, outputMessage);
	}

	@Override
	protected T readInternal(Class<? extends T> clazz, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
		Charset charset = getContentTypeCharset(inputMessage.getHeaders().getContentType());
		String response = FileCopyUtils.copyToString(new InputStreamReader(inputMessage.getBody(), charset));
		Map<String, String> map = convertToMap(response);
		return convertToEntity(map);
	}

	protected abstract T convertToEntity(Map<String, String> map);
	protected abstract Map<Field<?, ?>, String> convertToMap(T entity);
	
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

	@VisibleForTesting Map<String, String> convertToMap(String response) {
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
