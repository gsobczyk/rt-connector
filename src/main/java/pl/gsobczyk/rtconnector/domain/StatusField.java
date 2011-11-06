package pl.gsobczyk.rtconnector.domain;

public abstract class StatusField<T> extends Field<T, RestStatus>{

	public static final StatusField<String> STATUS = new StatusField<String>("RT/"){
		@Override public String getValue(RestStatus status) {
			return null;
		}
		@Override public void setValue(RestStatus status, String value) {
			status.setStatus(value);
		}
	};
	public static final StatusField<String> MESSAGE = new StatusField<String>("#"){
		@Override public String getValue(RestStatus status) {
			return null;
		}
		@Override public void setValue(RestStatus status, String value) {
			status.setMessage(value);
		}
	};
	
	protected StatusField(String name) {
		super(name);
	}

}
