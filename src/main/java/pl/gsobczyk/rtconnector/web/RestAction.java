package pl.gsobczyk.rtconnector.web;

public enum RestAction {
	LOGIN("?user={user}&pass={pass}"),
	LOGOUT("logout"), 
	CREATE("ticket/new"),
	GET("ticket/{ticketId}/show"),
	EDIT("ticket/{ticketId}/edit"),
	QUEUE("queue/{queueId}");
	private final String action;

	private RestAction(String action){
		this.action = action;
	}

	public String getAction() {
		return action;
	}
	
	@Override
	public String toString() {
		return action;
	}
}
