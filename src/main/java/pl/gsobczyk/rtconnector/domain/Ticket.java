package pl.gsobczyk.rtconnector.domain;

import org.apache.commons.lang.builder.EqualsBuilder;

public class Ticket {
	public static final String REGEX_DIRECT = "^.*RT#(\\d+):[^\\>]+$";
	public static final String REGEX_PARENT = "^.*RT#(\\d+):[^\\>]+\\>([^>]+)";
	public static final String REGEX_FULL = "^([^\\\\>]+)>([^\\\\>]+)\\/([^\\\\>]+)>([^\\>]+)>([^\\>]+)$";
	private Long id;
	private String name;
	private String queue;
	private String client;
	private String project;
	private String clearing;
	private String owner;
	private Integer timeWorked;
	
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
	
	@Override
	public boolean equals(Object obj) {
		return EqualsBuilder.reflectionEquals(this, obj);
	}
}
