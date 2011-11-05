package pl.gsobczyk.rtconnector.domain;

public abstract class QueueField<T> extends Field<T, Queue> {
	protected QueueField(String name) {
		super(name);
	}
	public static final QueueField<Long> ID = new QueueField<Long>("id"){
		@Override public Long getValue(Queue queue) {
			return queue.getId();
		}
		@Override public void setValue(Queue queue, Long value) {
			queue.setId(value);
		}};
	public static final QueueField<String> NAME = new QueueField<String>("Name"){
		@Override public String getValue(Queue queue) {
			return queue.getName();
		}
		@Override public void setValue(Queue queue, String value) {
			queue.setName(value);
		}};
	public static final QueueField<String> DESCRIPTION = new QueueField<String>("Description"){
		@Override public String getValue(Queue queue) {
			return queue.getDescription();
		}
		@Override public void setValue(Queue queue, String value) {
			queue.setDescription(value);
		}};
}
