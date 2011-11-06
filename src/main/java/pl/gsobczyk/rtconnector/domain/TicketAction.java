package pl.gsobczyk.rtconnector.domain;

public enum TicketAction {
	COMMENT,
	CORRESPOND;
	public String toString() {
		return name().toLowerCase();
	};
}
