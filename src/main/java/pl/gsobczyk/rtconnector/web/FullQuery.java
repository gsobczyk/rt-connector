package pl.gsobczyk.rtconnector.web;


public class FullQuery extends TicketQuery{
	private String queue;
	private String client;
	private String project;
	private String clearing;
	private String name;
	private TicketQuery next;
	
	@Override
	public boolean hasNext() {
		return false;
	}
	@Override
	public TicketQuery next() {
		return next;
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
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
