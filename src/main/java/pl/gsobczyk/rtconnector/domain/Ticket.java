package pl.gsobczyk.rtconnector.domain;

import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.springframework.util.StringUtils;

import com.google.common.collect.Lists;

public class Ticket {
	public static final String REGEX_DIRECT = "^.*#(\\d+):[^\\>]+$";
	public static final String REGEX_PARENT = "^.*#(\\d+):[^\\>]+\\>([^>]+)";
	public static final String REGEX_FULL = "^([^\\\\>]+)>([^\\\\>]+)\\/([^\\\\>]+)>([^\\>]+)>([^\\>]+)$";
	private Long id;
	private String name;
	private String queue;
	private String client;
	private String project;
	private String clearing;
	private String owner;
	private Integer timeWorked;
	private String text;
	private TicketAction action;
	private String requestors;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getQueue() {
		return queue;
	}
	public void setQueue(String queue) {
		this.queue = queue;
	}
	public String getClient() {
		return client;
	}
	public void setClient(String client) {
		this.client = client;
	}
	public String getProject() {
		return project;
	}
	public void setProject(String project) {
		this.project = project;
	}
	public String getClearing() {
		return clearing;
	}
	public void setClearing(String clearing) {
		this.clearing = clearing;
	}
	public String getOwner() {
		return owner;
	}
	public void setOwner(String owner) {
		this.owner = owner;
	}
	public Integer getTimeWorked() {
		return timeWorked;
	}
	public void setTimeWorked(Integer timeWorked) {
		this.timeWorked = timeWorked;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public TicketAction getAction() {
		return action;
	}
	public void setAction(TicketAction action) {
		this.action = action;
	}
	public String getRequestors() {
		return requestors;
	}
	public void setRequestors(String requestors) {
		this.requestors = requestors;
	}
	
	@Override
	public boolean equals(Object obj) {
		return EqualsBuilder.reflectionEquals(this, obj);
	}
	@Override
	public String toString() {
		List<String> list = Lists.newArrayList();
		if (StringUtils.hasText(queue)){
			list.add(queue);
		}
		if (StringUtils.hasText(project)){
			list.add(project);
		}
		if (StringUtils.hasText(clearing)){
			list.add(clearing);
		}
		String name="";
		if (id!=null){
			name = "#"+id+":";
		}
		if (StringUtils.hasText(this.name)){
			name+=" "+this.name;
			list.add(name);
		}
		return StringUtils.collectionToDelimitedString(list, " > ");
	}
}
