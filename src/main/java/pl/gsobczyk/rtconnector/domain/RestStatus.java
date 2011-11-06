package pl.gsobczyk.rtconnector.domain;

public class RestStatus {
	private String status;
	private String message;
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String value) {
		this.status = value;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
}
