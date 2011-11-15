package pl.gsobczyk.rtconnector.ui;

public enum TimeUnit {
	MINUTES,
	HOURS;
	
	public String toString() {
		return name().toLowerCase();
	};
}
