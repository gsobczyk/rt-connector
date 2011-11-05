package pl.gsobczyk.rtconnector.web;

public enum RestAction {
	LOGIN("?user={user}&pass={pass}"),
	LOGOUT("logout"), 
	GET("ticket/{ticketId}/show"),
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
