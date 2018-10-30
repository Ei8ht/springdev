package com.chanon.dev.response;

import com.chanon.dev.beans.ExCounterRate;

public class ResCurrentRate  extends ResponseHeader  {
	
	private ExCounterRate exCounterRate;

	public ExCounterRate getExCounterRate() {
		return exCounterRate;
	}

	public void setExCounterRate(ExCounterRate exCounterRate) {
		this.exCounterRate = exCounterRate;
	}
	
	
	

}
