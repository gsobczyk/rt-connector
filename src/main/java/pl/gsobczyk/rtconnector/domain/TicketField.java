package pl.gsobczyk.rtconnector.domain;


public abstract class TicketField<T> extends Field<T, Ticket> {
	protected TicketField(String name) {
		super(name);
	}
	
	public static final TicketField<Long> ID = new TicketField<Long>("id"){
		@Override public Long getValue(Ticket ticket) {
			return ticket.getId();
		}
		@Override public void setValue(Ticket ticket, Long value) {
			ticket.setId(value);
		}};
	public static final TicketField<String> QUEUE = new TicketField<String>("Queue"){
		@Override public String getValue(Ticket ticket) {
			return ticket.getQueue();
		}
		@Override public void setValue(Ticket ticket, String value) {
			ticket.setQueue(value);
		}};
	public static final TicketField<String> NAME = new TicketField<String>("Subject"){
		@Override public String getValue(Ticket ticket) {
			return ticket.getName();
		}
		@Override public void setValue(Ticket ticket, String value) {
			ticket.setName(value);
		}};
	public static final TicketField<String> OWNER = new TicketField<String>("Owner"){
		@Override public String getValue(Ticket ticket) {
			return ticket.getOwner();
		}
		@Override public void setValue(Ticket ticket, String value) {
			ticket.setOwner(value);
		}};
	public static final TicketField<String> CLEARING = new TicketField<String>("CF.{Rozliczajacy}"){
		@Override public String getValue(Ticket ticket) {
			return ticket.getClearing();
		}
		@Override public void setValue(Ticket ticket, String value) {
			ticket.setClearing(value);
		}};
	public static final TicketField<String> CLIENT = new TicketField<String>("CF.{Klient}"){
		@Override public String getValue(Ticket ticket) {
			return ticket.getClient();
		}
		@Override public void setValue(Ticket ticket, String value) {
			ticket.setClient(value);
		}};
	public static final TicketField<String> PROJECT = new TicketField<String>("CF.{Projekt}"){
		@Override public String getValue(Ticket ticket) {
			return ticket.getProject();
		}
		@Override public void setValue(Ticket ticket, String value) {
			ticket.setProject(value);
		}};

}
