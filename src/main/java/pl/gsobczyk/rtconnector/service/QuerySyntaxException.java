package pl.gsobczyk.rtconnector.service;

public class QuerySyntaxException extends Exception {
	public QuerySyntaxException(String ticketQuery) {
		super(ticketQuery);
	}

	private static final long serialVersionUID = -7942044624764652382L;

}
