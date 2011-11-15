package pl.gsobczyk.rtconnector.ui;

import java.math.BigDecimal;
import java.math.RoundingMode;

public enum TimeUnit {
	HOURS {
		@Override public int getMinutes(BigDecimal value) {
			if (value==null){
				return 0;
			}
			return value.multiply(SIXTY).setScale(0, RoundingMode.HALF_EVEN).intValue();
		}
	},
	MINUTES {
		@Override public int getMinutes(BigDecimal value) {
			if (value==null){
				return 0;
			}
			return value.setScale(0, RoundingMode.HALF_EVEN).intValue();
		}
	};
	private static final BigDecimal SIXTY = new BigDecimal(60);
	public abstract int getMinutes(BigDecimal value);
	
	public String toString() {
		return Messages.getString("TimeUnit."+name());
	};
}
