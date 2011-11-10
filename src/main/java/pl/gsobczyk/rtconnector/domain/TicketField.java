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
	public static final TicketField<String> TEXT = new TicketField<String>("Text"){
		@Override public String getValue(Ticket ticket) {
			return ticket.getText();
		}
		@Override public void setValue(Ticket ticket, String value) {
			ticket.setText(value);
		}};
	public static final TicketField<String> OWNER = new TicketField<String>("Owner"){
		@Override public String getValue(Ticket ticket) {
			return ticket.getOwner();
		}
		@Override public void setValue(Ticket ticket, String value) {
			ticket.setOwner(value);
		}};
	public static final TicketField<Integer> TIME_WORKED = new TicketField<Integer>("TimeWorked"){
		@Override public Integer getValue(Ticket ticket) {
			return ticket.getTimeWorked();
		}
		@Override public void setValue(Ticket ticket, Integer value) {
			ticket.setTimeWorked(value);
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
	public static final TicketField<TicketAction> ACTION = new TicketField<TicketAction>("Action"){
		@Override public TicketAction getValue(Ticket ticket) {
			return ticket.getAction();
		}
		@Override public void setValue(Ticket ticket, TicketAction value) {
			ticket.setAction(value);
		}};
	public static final TicketField<String> REQUESTORS = new TicketField<String>("Requestor"){
		@Override public String getValue(Ticket ticket) {
			return ticket.getRequestors();
		}
		@Override public void setValue(Ticket ticket, String value) {
			ticket.setRequestors(value);
		}};
}
